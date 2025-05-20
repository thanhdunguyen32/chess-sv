package game.util.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lion.common.SystemTimer;

/**
 * tick duration以秒为单位，因此系统里的当前时间不需要非常精确，因此用了{@link lion.common.SystemTimer}类来获取当前时间
 * 
 * @author hexuhui
 * 
 */
public class HierarchicalWheelTimer implements Timer {

	private static Logger logger = LoggerFactory.getLogger(HashWheelTimer.class);

	final int mask;

	final int maskPerWheel;

	final Set<HashedWheelTimeout>[][] wheel;

	final int tickDuration;

	final int tickDurationInSeconds;

	final int tickShiftLength;

	final int ticksPerWheel;

	final int wheelCount;

	final int maxTicks;

	private final Worker worker = new Worker();

	final Thread workerThread;
	final AtomicInteger workerState = new AtomicInteger(); // 0 - init, 1 - started, 2 - shut down

	final ReadWriteLock lock = new ReentrantReadWriteLock();
	volatile int wheelCursor;

	final ReusableIterator<HashedWheelTimeout>[][] iterators;

	public HierarchicalWheelTimer() {
		this(1, 256, 3);
	}

	/**
	 * 
	 * @param tickDuration
	 *            以秒为单位
	 * @param ticksPerWheel
	 *            每个轮子有几个眼
	 * @param wheelCount
	 *            轮子的数量
	 */
	public HierarchicalWheelTimer(int tickDuration, int ticksPerWheel, int wheelCount) {
		if (tickDuration <= 0) {
			throw new IllegalArgumentException("tickDuration must be greater than 0: " + tickDuration);
		}
		this.ticksPerWheel = ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
		tickShiftLength = getTickShiftLength(ticksPerWheel);
		this.wheelCount = wheelCount;
		maxTicks = getMaxTicks();
		wheel = createWheel(ticksPerWheel, wheelCount);
		iterators = createIterators(wheel, wheelCount, ticksPerWheel);
		mask = maxTicks - 1;
		maskPerWheel = ticksPerWheel - 1;
		tickDurationInSeconds = tickDuration;
		this.tickDuration = tickDuration * 1000;

		workerThread = new Thread(worker, getClass().getSimpleName());
	}

	private int getTickShiftLength(int ticksPerWheel2) {
		int ret = 0;
		while ((ticksPerWheel2 >>= 1) > 0) {
			ret++;
		}
		return ret;
	}

	private int getMaxTicks() {
		int ret = 1;
		int tmpWheelCount = wheelCount;
		while (tmpWheelCount-- > 0) {
			ret <<= tickShiftLength;
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private static Set<HashedWheelTimeout>[][] createWheel(int ticksPerWheel, int wheelCount) {
		if (ticksPerWheel <= 0) {
			throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
		}
		if (ticksPerWheel > 1073741824) {
			throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
		}

		Set<HashedWheelTimeout>[][] wheel = new Set[wheelCount][ticksPerWheel];
		for (int p = 0; p < wheelCount; p++) {
			for (int i = 0; i < ticksPerWheel; i++) {
				wheel[p][i] = Collections.newSetFromMap(new ConcurrentIdentityHashMap<HashedWheelTimeout, Boolean>(16, 0.95f, 4));
			}
		}
		return wheel;
	}

	@SuppressWarnings("unchecked")
	private static ReusableIterator<HashedWheelTimeout>[][] createIterators(Set<HashedWheelTimeout>[][] wheel, int wheelCount, int ticksPerWheel) {
		ReusableIterator<HashedWheelTimeout>[][] iterators = new ReusableIterator[wheelCount][ticksPerWheel];
		for (int p = 0; p < wheelCount; p++) {
			for (int i = 0; i < ticksPerWheel; i++) {
				iterators[p][i] = (ReusableIterator<HashedWheelTimeout>) wheel[p][i].iterator();
			}
		}
		return iterators;
	}

	private static int normalizeTicksPerWheel(int ticksPerWheel) {
		int normalizedTicksPerWheel = 1;
		while (normalizedTicksPerWheel < ticksPerWheel) {
			normalizedTicksPerWheel <<= 1;
		}
		return normalizedTicksPerWheel;
	}

	public void start() {
		switch (workerState.get()) {
		case 0:
			if (workerState.compareAndSet(0, 1)) {
				workerThread.start();
			}
			break;
		case 1:
			break;
		case 2:
			logger.warn("cannot be started once stopped");
		default:
			throw new Error();
		}
	}

	@Override
	public Timeout newTimeout(TimerTask task, int delay) {
		final long currentTime = SystemTimer.currentTimeMillis();

		if (task == null) {
			throw new NullPointerException("task");
		}

		start();
		HashedWheelTimeout timeout = new HashedWheelTimeout(task, currentTime + delay * 1000);
		scheduleTimeout(timeout, delay);
		return timeout;
	}

	void scheduleTimeout(HashedWheelTimeout timeout, int delay) {
		// delay must be equal to or greater than tickDuration so that the
		// worker thread never misses the timeout.
		if (delay < tickDurationInSeconds) {
			delay = tickDurationInSeconds;
		}
		int totalTicks = delay / tickDurationInSeconds + wheelCursor;
		if (totalTicks >= maxTicks) {
			System.out.println("delay too large");
			return;
		}
		int tmpTotalTicks = totalTicks;
		int targetLevel = 0;
		int[] eachLevelIndex = new int[wheelCount];
		for (int i = 0; i < wheelCount; i++) {
			eachLevelIndex[i] = tmpTotalTicks & maskPerWheel;
			if ((tmpTotalTicks = tmpTotalTicks >> tickShiftLength) > 0) {
				totalTicks = tmpTotalTicks;
				targetLevel = i + 1;
			} else {
				break;
			}
		}
		timeout.eachLevelIndex = eachLevelIndex;
		lock.readLock().lock();
		try {
			// logger.info("add to wheel x={},y={},eachLevelIndex={}", new Object[] { targetLevel, totalTicks, eachLevelIndex });
			timeout.stopIndex[0] = targetLevel;
			timeout.stopIndex[1] = totalTicks;
			wheel[targetLevel][totalTicks].add(timeout);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<Timeout> stop() {
		if (Thread.currentThread() == workerThread) {
			throw new IllegalStateException(HashWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
		}

		if (workerState.getAndSet(2) != 1) {
			// workerState wasn't 1, so return an empty set
			return Collections.emptySet();
		}

		boolean interrupted = false;
		while (workerThread.isAlive()) {
			workerThread.interrupt();
			try {
				workerThread.join(100);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}

		Set<Timeout> unprocessedTimeouts = new HashSet<Timeout>();
		for (int i = 0; i < wheelCount; i++) {
			for (Set<HashedWheelTimeout> bucket : wheel[i]) {
				unprocessedTimeouts.addAll(bucket);
				bucket.clear();
			}
		}

		return Collections.unmodifiableSet(unprocessedTimeouts);
	}

	public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
		return newTimeout(task, (int) unit.toSeconds(delay));
	}

	private final class Worker implements Runnable {

		private long startTime;
		private long elapseTime;

		Worker() {
		}

		public long getStartTime() {
			return startTime;
		}

		public long getElapseTime() {
			return elapseTime;
		}

		public void run() {
			List<HashedWheelTimeout> expiredTimeouts = new ArrayList<HashedWheelTimeout>();

			startTime = SystemTimer.currentTimeMillis();
			elapseTime = tickDuration;

			while (workerState.get() == 1) {
				final long deadline = waitForNextTick();
				if (deadline > 0) {
					fetchExpiredTimeouts(expiredTimeouts, deadline);
					notifyExpiredTimeouts(expiredTimeouts);
				}
			}
		}

		private void fetchExpiredTimeouts(List<HashedWheelTimeout> expiredTimeouts, long deadline) {
			lock.writeLock().lock();
			try {
				int newWheelCursor = wheelCursor = wheelCursor + 1 & mask;
				// logger.info("cursor={}", newWheelCursor);
				ReusableIterator<HashedWheelTimeout> timeoutIte = iterators[0][newWheelCursor & maskPerWheel];
				fetchExpiredTimeouts(expiredTimeouts, timeoutIte, deadline, 0);
				int wheelNum = 0;
				int targetLevelIndex = 0;
				while ((newWheelCursor & maskPerWheel) == 0) {
					wheelNum++;
					newWheelCursor >>= tickShiftLength;
					targetLevelIndex = newWheelCursor & maskPerWheel;
				}
				if (wheelNum > 0) {
					// logger.info("parse x={},y={}", wheelNum, newWheelCursor);
					timeoutIte = iterators[wheelNum][targetLevelIndex];
					fetchExpiredTimeouts(expiredTimeouts, timeoutIte, deadline, wheelNum);
				}
			} finally {
				lock.writeLock().unlock();
			}
		}

		private void fetchExpiredTimeouts(List<HashedWheelTimeout> expiredTimeouts, ReusableIterator<HashedWheelTimeout> i, long deadline, int wheelIndex) {
			List<HashedWheelTimeout> slipped = null;
			i.rewind();
			if (wheelIndex <= 0) {
				while (i.hasNext()) {
					HashedWheelTimeout timeout = i.next();
					i.remove();
					if (timeout.deadline <= deadline || deadline - timeout.deadline < 200) {
						expiredTimeouts.add(timeout);
					} else {
						// Handle the case where the timeout is put into a wrong
						// place, usually one tick earlier. For now, just add
						// it to a temporary list - we will reschedule it in a
						// separate loop.
						if (slipped == null) {
							slipped = new ArrayList<HashedWheelTimeout>();
						}
						slipped.add(timeout);
					}
				}
			} else {// 放入下一级wheel
				while (i.hasNext()) {
					HashedWheelTimeout timeout = i.next();
					i.remove();
					int nextWheelIndex = wheelIndex - 1;
					int nextLevelIndex = timeout.eachLevelIndex[nextWheelIndex];
					while (nextLevelIndex == 0 && nextWheelIndex > 0) {
						nextLevelIndex = timeout.eachLevelIndex[--nextWheelIndex];
					}
					if (nextLevelIndex != 0) {
						// logger.info("add to x={},y={}", nextWheelIndex, nextLevelIndex);
						timeout.stopIndex[0] = nextWheelIndex;
						timeout.stopIndex[1] = nextLevelIndex;
						wheel[nextWheelIndex][nextLevelIndex].add(timeout);
					} else {
						expiredTimeouts.add(timeout);
					}
				}
			}

			// Reschedule the slipped timeouts.
			if (slipped != null) {
				for (HashedWheelTimeout timeout : slipped) {
					scheduleTimeout(timeout, (int) ((timeout.deadline - deadline) / 1000));
				}
			}
		}

		private void notifyExpiredTimeouts(List<HashedWheelTimeout> expiredTimeouts) {
			// Notify the expired timeouts.
			for (HashedWheelTimeout hashedWheelTimeout : expiredTimeouts) {
				hashedWheelTimeout.expire();
			}

			// Clean up the temporary list.
			expiredTimeouts.clear();
		}

		private long waitForNextTick() {
			long deadline = startTime + elapseTime;

			for (;;) {
				final long currentTime = SystemTimer.currentTimeMillis();
				long sleepTime = elapseTime - (currentTime - startTime);

				// Check if we run on windows, as if thats the case we will need
				// to round the sleepTime as workaround for a bug that only affect
				// the JVM if it runs on windows.
				//
				// See https://github.com/netty/netty/issues/356
				// if (DetectionUtil.isWindows()) {
				// sleepTime = sleepTime / 10 * 10;
				// }

				if (sleepTime <= 0) {
					break;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					if (workerState.get() != 1) {
						return -1;
					}
				}
			}

			// Increase the tick.
			elapseTime += tickDuration;
			return deadline;
		}
	}

	private final class HashedWheelTimeout implements Timeout {

		private static final int ST_INIT = 0;
		private static final int ST_CANCELLED = 1;
		private static final int ST_EXPIRED = 2;

		private final TimerTask task;
		final long deadline;
		private int[] eachLevelIndex;
		volatile int[] stopIndex = new int[] { 0, 0 };
		private final AtomicInteger state = new AtomicInteger(ST_INIT);

		HashedWheelTimeout(TimerTask task, long deadline) {
			this.task = task;
			this.deadline = deadline;
		}

		public Timer getTimer() {
			return HierarchicalWheelTimer.this;
		}

		public TimerTask getTask() {
			return task;
		}

		public void cancel() {
			if (!state.compareAndSet(ST_INIT, ST_CANCELLED)) {
				return;
			}
			wheel[stopIndex[0]][stopIndex[1]].remove(this);
		}

		public boolean isCancelled() {
			return state.get() == ST_CANCELLED;
		}

		public boolean isExpired() {
			return state.get() != ST_INIT;
		}

		public void expire() {
			if (!state.compareAndSet(ST_INIT, ST_EXPIRED)) {
				return;
			}

			try {
				task.run(this);
			} catch (Throwable t) {
				if (logger.isWarnEnabled()) {
					logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
				}
			}
		}

		@Override
		public String toString() {
			long currentTime = SystemTimer.currentTimeMillis();
			long remaining = deadline - currentTime;

			StringBuilder buf = new StringBuilder(192);
			buf.append(getClass().getSimpleName());
			buf.append('(');

			buf.append("deadline: ");
			if (remaining > 0) {
				buf.append(remaining);
				buf.append(" ms later, ");
			} else if (remaining < 0) {
				buf.append(-remaining);
				buf.append(" ms ago, ");
			} else {
				buf.append("now, ");
			}

			if (isCancelled()) {
				buf.append(", cancelled");
			}

			return buf.append(')').toString();
		}
	}

	@Override
	public String toString() {
		String retStr = "start time=%s,elapseTime=%ss,set container count=%s,remaining tasks=%s";
		long startTime = worker.getStartTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String startTimeStr = sdf.format(new Date(startTime));
		long elapseTime = worker.getElapseTime();
		int containerCount = 0;
		int remainingTasks = 0;
		for (int i = 0; i < wheelCount; i++) {
			for (Set<HashedWheelTimeout> bucket : wheel[i]) {
				remainingTasks += bucket.size();
				containerCount++;
			}
		}
		retStr = String.format(retStr, startTimeStr, elapseTime / 1000, containerCount, remainingTasks);
		return retStr;
	}
}
