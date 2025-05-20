package game.module.countdown;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ConcurrentHashMultiset;

import game.util.timer.HashWheelTimer;
import game.util.timer.Timeout;
import game.util.timer.Timer;
import game.util.timer.TimerTask;
import lion.common.SystemTimer;

/**
 * 高效定时器，用来进行倒计时操作
 * 
 * @author hexuhui
 * 
 */
public class CountDownTimer implements Timer {

	private static Logger logger = LoggerFactory.getLogger(CountDownTimer.class);

	private final Thread workerThread;

	private final Worker worker = new Worker();

	final AtomicInteger workerState = new AtomicInteger();

	final int tickDuration = 1000;

	final int totalSeconds;

	int currentIndex;

	private ConcurrentHashMultiset<CountDownTimeout>[] setWheel;

	public CountDownTimer(int totalSeconds) {
		workerThread = new Thread(worker, getClass().getSimpleName());
		this.totalSeconds = totalSeconds;
		createSetWheel(totalSeconds + 1);
		currentIndex = 0;
	}

	@SuppressWarnings("unchecked")
	private void createSetWheel(int wheelSize) {
		setWheel = new ConcurrentHashMultiset[wheelSize];
		for (int i = 0; i < setWheel.length; i++) {
			setWheel[i] = ConcurrentHashMultiset.create();
		}
	}

	public int getTaskCount() {
		int totalSize = 0;
		for (int i = 0; i < setWheel.length; i++) {
			totalSize += setWheel[i].size();
		}
		return totalSize;
	}

	public CountDownTimeout newJob(TimerTask task) {
		if (task == null) {
			throw new NullPointerException("task");
		}
		start();
		int stopIndex = currentIndex - 1;
		if (stopIndex < 0) {
			stopIndex = totalSeconds;
		}
		CountDownTimeout timeout = new CountDownTimeout(task, stopIndex);
		setWheel[stopIndex].add(timeout);
		return timeout;
	}

	public CountDownTimeout newJobCustom(TimerTask task, int delaySeconds) {
		if (task == null) {
			throw new IllegalArgumentException("task is null");
		}
		if (delaySeconds < 0 || delaySeconds > totalSeconds) {
			throw new IllegalArgumentException("delaySeconds < 0 || delaySeconds > " + totalSeconds);
		}
		start();
		int stopIndex = currentIndex + delaySeconds;
		if (stopIndex > totalSeconds) {
			stopIndex = stopIndex - totalSeconds - 1;
		}
		CountDownTimeout timeout = new CountDownTimeout(task, stopIndex);
		setWheel[stopIndex].add(timeout);
		return timeout;
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
			throw new IllegalStateException("cannot be started once stopped");
		default:
			throw new Error();
		}
	}

	private final class Worker implements Runnable {

		private long startTime;
		private long elapseTime;

		@Override
		public void run() {
			startTime = SystemTimer.currentTimeMillis();
			elapseTime = tickDuration;
			while (workerState.get() == 1) {
				parseExpiredTimeouts(currentIndex);
				waitForNextTick();
				currentIndex = (currentIndex + 1) % (totalSeconds + 1);
			}
		}

		private void parseExpiredTimeouts(int currentIndex) {
			ConcurrentHashMultiset<CountDownTimeout> oneTickTimeout = setWheel[currentIndex];
			for (CountDownTimeout countDownTimeout : oneTickTimeout) {
				countDownTimeout.expire();
			}
			oneTickTimeout.clear();
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

	private final class CountDownTimeout implements Timeout {

		private int stopIndex;

		private final TimerTask task;

		private static final int ST_INIT = 0;
		private static final int ST_CANCELLED = 1;
		private static final int ST_EXPIRED = 2;

		private final AtomicInteger state = new AtomicInteger(ST_INIT);

		public CountDownTimeout(TimerTask task, int stopIndex) {
			this.task = task;
			this.stopIndex = stopIndex;
		}

		@Override
		public Timer getTimer() {
			return CountDownTimer.this;
		}

		@Override
		public TimerTask getTask() {
			return task;
		}

		@Override
		public boolean isExpired() {
			return state.get() != ST_INIT;
		}

		@Override
		public boolean isCancelled() {
			return state.get() == ST_CANCELLED;
		}

		@Override
		public void cancel() {
			if (!state.compareAndSet(ST_INIT, ST_CANCELLED)) {
				return;
			}
			setWheel[stopIndex].remove(this);
		}

		public void expire() {
			if (!state.compareAndSet(ST_INIT, ST_EXPIRED)) {
				return;
			}
			try {
				task.run(this);
			} catch (Throwable t) {
				if (logger.isWarnEnabled()) {
					logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName()
							+ '.', t);
				}
			}
		}

	}

	@Override
	public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
		return null;
	}

	@Override
	public Set<Timeout> stop() {
		if (Thread.currentThread() == workerThread) {
			throw new IllegalStateException(HashWheelTimer.class.getSimpleName()
					+ ".stop() cannot be called from " + TimerTask.class.getSimpleName());
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
		for (int i = 0; i < setWheel.length; i++) {
			ConcurrentHashMultiset<CountDownTimeout> timeoutSet = setWheel[i];
			unprocessedTimeouts.addAll(timeoutSet);
			timeoutSet.clear();
		}

		return Collections.unmodifiableSet(unprocessedTimeouts);
	}

	@Override
	public Timeout newTimeout(TimerTask task, int delay) {
		return null;
	}

}
