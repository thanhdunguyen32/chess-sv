/**
 * 
 */
package lion.common;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可命名的线程池工厂类
 * 
 * @author serv_dev
 * 
 */
public class NamedThreadFactory implements ThreadFactory {

	private static Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);

	static final AtomicInteger poolNumber = new AtomicInteger(1);

	final AtomicInteger threadNumber = new AtomicInteger(1);
	final ThreadGroup group;
	final String namePrefix;
	final boolean isDaemon;

	public NamedThreadFactory() {
		this("pool");
	}

	public NamedThreadFactory(String name) {
		this(name, false);
	}

	public NamedThreadFactory(String preffix, boolean daemon) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = preffix + "-" + poolNumber.getAndIncrement() + "-";
		isDaemon = daemon;
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		t.setDaemon(isDaemon);
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("Uncaught Exception", e);
			}
		});
		return t;
	}

}
