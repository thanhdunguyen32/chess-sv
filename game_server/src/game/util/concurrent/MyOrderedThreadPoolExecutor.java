package game.util.concurrent;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import game.util.timer.ConcurrentIdentityWeakKeyHashMap;

/**
 * change implementation 并发执行，但针对某个条件，需要串行执行 例如，同个角色id的任务，需要串行执行
 * 
 * @author hexuhui
 * 
 */
public class MyOrderedThreadPoolExecutor extends ThreadPoolExecutor {

	protected final ConcurrentMap<Object, Executor> childExecutors = newChildExecutorMap();

	public MyOrderedThreadPoolExecutor(int nThreads) {
		super(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	public MyOrderedThreadPoolExecutor(int nThreads, ThreadFactory threadFactory) {
		super(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
	}

	public MyOrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	protected ConcurrentMap<Object, Executor> newChildExecutorMap() {
		return new ConcurrentIdentityWeakKeyHashMap<Object, Executor>();
	}

	protected Object getChildExecutorKey(OrderedEventRunnable e) {
		return e.getIdentifyer();
	}

	public Set<Object> getChildExecutorKeySet() {
		return childExecutors.keySet();
	}

	protected boolean removeChildExecutor(Object key) {
		// FIXME: Succeed only when there is no task in the ChildExecutor's
		// queue.
		// Note that it will need locking which might slow down task submission.
		return childExecutors.remove(key) != null;
	}

	@Override
	public void execute(Runnable task) {
		if (!(task instanceof OrderedEventRunnable)) {
			doUnorderedExecute(task);
		} else {
			OrderedEventRunnable r = (OrderedEventRunnable) task;
			getChildExecutor(r).execute(task);
		}
	}

	protected Executor getChildExecutor(OrderedEventRunnable e) {
		Object key = getChildExecutorKey(e);
		Executor executor = childExecutors.get(key);
		if (executor == null) {
			executor = new ChildExecutor();
			Executor oldExecutor = childExecutors.putIfAbsent(key, executor);
			if (oldExecutor != null) {
				executor = oldExecutor;
			}
		}

		// Remove the entry when the channel closes.
		if (e.getEventType() == OrderedEventRunnable.OP_TYPE_CLOSE) {
			removeChildExecutor(key);
		}
		return executor;
	}

	void onAfterExecute(Runnable r, Throwable t) {
		afterExecute(r, t);
	}

	protected final void doUnorderedExecute(Runnable task) {
		super.execute(task);
	}

	protected final class ChildExecutor implements Executor, Runnable {
		private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<Runnable>();
		private final AtomicBoolean isRunning = new AtomicBoolean();

		public void execute(Runnable command) {
			// TODO: What todo if the add return false ?
			tasks.add(command);

			if (!isRunning.get()) {
				doUnorderedExecute(this);
			}
		}

		public void run() {
			boolean acquired = false;

			// check if its already running by using CAS. If so just return
			// here. So in the worst case the thread
			// is executed and do nothing
			if (isRunning.compareAndSet(false, true)) {
				acquired = true;
				try {
					Thread thread = Thread.currentThread();
					for (;;) {
						final Runnable task = tasks.poll();
						// if the task is null we should exit the loop
						if (task == null) {
							break;
						}

						boolean ran = false;
						beforeExecute(thread, task);
						try {
							task.run();
							ran = true;
							onAfterExecute(task, null);
						} catch (RuntimeException e) {
							if (!ran) {
								onAfterExecute(task, e);
							}
							throw e;
						}
					}
				} finally {
					// set it back to not running
					isRunning.set(false);
				}

				if (acquired && !isRunning.get() && tasks.peek() != null) {
					doUnorderedExecute(this);
				}

			}
		}
	}

}
