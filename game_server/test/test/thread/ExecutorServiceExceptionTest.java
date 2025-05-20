package test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lion.common.NamedThreadFactory;
import lion.common.ScheduledThreadPoolExecutorLog;

public class ExecutorServiceExceptionTest {

	public static void main(String[] args) {
		ExecutorServiceExceptionTest mainTest = new ExecutorServiceExceptionTest();
		mainTest.testSchedule();
	}

	public void testExecute() {
		ExecutorService exec = new ScheduledThreadPoolExecutorLog(Runtime.getRuntime()
				.availableProcessors(), new NamedThreadFactory("executorPool"));
		final int y = 0;
		exec.execute(new Runnable() {
			@Override
			public void run() {
				int x = 8;
				int z = x / y;
				System.out.println(z);
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exec.execute(new Runnable() {
			@Override
			public void run() {
				int x = 10;
				int z = x / y;
				System.out.println(z);
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void testSubmit() {
		ExecutorService exec = new ScheduledThreadPoolExecutorLog(Runtime.getRuntime()
				.availableProcessors(), new NamedThreadFactory("executorPool"));
		final int y = 0;
		exec.submit(new Runnable() {
			@Override
			public void run() {
				int x = 8;
				int z = x / y;
				System.out.println(z);
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exec.submit(new Runnable() {
			@Override
			public void run() {
				int x = 10;
				int z = x / y;
				System.out.println(z);
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void testSchedule() {
		ScheduledExecutorService exec = new ScheduledThreadPoolExecutorLog(Runtime.getRuntime()
				.availableProcessors(), new NamedThreadFactory("executorPool"));
		final int y = 0;
		exec.schedule(new Runnable() {
			@Override
			public void run() {
				int x = 8;
				int z = x / y;
				System.out.println(z);
			}
		}, 3, TimeUnit.SECONDS);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exec.schedule(new Runnable() {
			@Override
			public void run() {
				int x = 10;
				int z = x / y;
				System.out.println(z);
			}
		}, 3, TimeUnit.SECONDS);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
