package lion.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * 系统当前时间，用了时间缓存，虽然不准
 * 
 * @author hexuhui
 * 
 */
public class SystemTimer {

	private static final ScheduledExecutorService executor;

	private static final long tickUnit = Long.parseLong(System.getProperty("notify.systimer.tick", "100"));
	private static volatile long time = System.currentTimeMillis();

	private static class TimerTicker implements Runnable {
		public void run() {
			time = System.currentTimeMillis();
		}
	}

	public static long currentTimeMillis() {
		return time;
	}

	static {
		BasicThreadFactory factory = new BasicThreadFactory.Builder().namingPattern("system-timer").daemon(true).priority(Thread.MAX_PRIORITY).build();
		executor = Executors.newSingleThreadScheduledExecutor(factory);
		executor.scheduleAtFixedRate(new TimerTicker(), tickUnit, tickUnit, TimeUnit.MILLISECONDS);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				executor.shutdownNow();
				try {
					executor.awaitTermination(1, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					//ignore
				}
			}
		});
	}

	public static void stop() {
		executor.shutdown();
	}
}
