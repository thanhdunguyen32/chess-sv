package lion.common;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 捕获异常，并且打印
 * 
 * @author hexuhui
 * 
 */
public class ScheduledThreadPoolExecutorLog extends ScheduledThreadPoolExecutor {

	private static Logger logger = LoggerFactory.getLogger(ScheduledThreadPoolExecutorLog.class);

	public ScheduledThreadPoolExecutorLog(int corePoolSize, ThreadFactory threadFactory) {
		super(corePoolSize, threadFactory);
	}

	public ScheduledThreadPoolExecutorLog(int corePoolSize) {
		super(corePoolSize);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if (t == null && r instanceof Future<?>) {
			try {
				Future<?> ft = (Future<?>) r;
				if (ft.isDone()) {
					((Future<?>) r).get();
				}
			} catch (CancellationException ce) {
				// t = ce;
				logger.info("task cancel");
			} catch (ExecutionException ee) {
				t = ee.getCause();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt(); // ignore/reset
			}
		}
		if (t != null)
			logger.error("", t);
	}

}
