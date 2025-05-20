package test.timer;

import game.module.countdown.CountDownTimer;
import game.util.timer.Timeout;
import game.util.timer.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountDownTimerTest {

	private static Logger logger = LoggerFactory.getLogger(CountDownTimerTest.class);

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		CountDownTimer countDownTimer = new CountDownTimer(10);
		logger.info("start the timer");
		countDownTimer.start();
		countDownTimer.newJob(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				Thread.sleep(500);
				logger.info("task1 execute!");
			}
		});
		Thread.sleep(2100);
		countDownTimer.newJob(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				logger.info("task2 execute!");
			}
		});
//		Thread.sleep(1000);
//		for (int i = 3; i < 200; i++) {
//			final int index = i;
//			countDownTimer.newJob(new TimerTask() {
//				@Override
//				public void run(Timeout timeout) throws Exception {
//					logger.info("task" + index + " execute!");
//				}
//			});
//		}
		System.out.println(System.currentTimeMillis());
		Thread.sleep(13000);
		logger.info("stop the timer");
		countDownTimer.stop();
	}

}
