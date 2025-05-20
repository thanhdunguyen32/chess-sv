package game.util.concurrent;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lion.common.NamedThreadFactory;

public class SerialExecutePool implements Runnable {

	private volatile boolean polling = false;

	private static Logger logger = LoggerFactory.getLogger(SerialExecutePool.class);

	private Thread serializeExecuteThread = null;

	private BlockingDeque<Runnable> taskList = new LinkedBlockingDeque<Runnable>();

	private ExecutorService sceneExecutorService;

	public Thread getSerializeExecuteThread() {
		return serializeExecuteThread;
	}

	public SerialExecutePool() {
		sceneExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
				new NamedThreadFactory("serialize-execute-pool"));
	}

	@Override
	public void run() {
		while (polling) {
			try {
				taskList.take().run();
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}

	public void addTask(Runnable task) {
		if (polling) {
			taskList.add(task);
		}
	}

	public void startService() {
		logger.warn("开启线程");
		polling = true;
		serializeExecuteThread = new Thread(this, getClass().getName());
		serializeExecuteThread.start();
	}

	/**
	 * 
	 */
	public void commonTimmer() {
		if (polling) {
			taskList.add(new Runnable() {
				@Override
				public void run() {
//					final long currentTime = System.currentTimeMillis();
//					// 场景更新
//					List<IScene> scenes = new ArrayList<IScene>();// 世界场景和副本场景
//					List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
//					for (final IScene scene : scenes) {
//						tasks.add(Executors.callable(new Runnable() {
//							@Override
//							public void run() {
//								scene.update(currentTime);
//							}
//						}));
//					}
//					try {
//						sceneExecutorService.invokeAll(tasks, 300, TimeUnit.MILLISECONDS);// 时间设置得过短，不容易应付异常情况，例如gc update执行被阻塞
//					} catch (InterruptedException e) {
//						logger.error("", e);
//					}
//					// TODO other interval work
				}
			});
		}
	}

	public void shutdown() throws InterruptedException {
		logger.warn("closing serail executor pool!");
		polling = false;
		sceneExecutorService.shutdown();
		while (!sceneExecutorService.isTerminated()) {
			sceneExecutorService.awaitTermination(10, TimeUnit.SECONDS);
		}
		// TODO 关机处理，存在问题，如何正确地关闭队列，等待所有job执行完成
		serializeExecuteThread.interrupt();
		logger.warn("success close serail executor pool!");
	}
}