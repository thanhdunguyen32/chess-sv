package game.pool;

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
		sceneExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("serialize-execute-pool"));
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
		logger.warn("人物角色请求、怪物定时攻击等操作，定时轮询刷帧开始");
		polling = true;
		serializeExecuteThread = new Thread(this, getClass().getName());
		serializeExecuteThread.start();
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
