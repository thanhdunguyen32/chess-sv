package test.timer;

import game.session.GlobalTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalTimerTest {

	private static Logger logger = LoggerFactory.getLogger(GlobalTimerTest.class);

	public static void main(String[] args) {
		GlobalTimer gt = GlobalTimer.getInstance();
		logger.info("global timer start");
		gt.newTimeout(new TimerTask() {

			@Override
			public void run(Timeout timeout) throws Exception {
				logger.info("run after 10s");
			}
		}, 10);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gt.newTimeout(new TimerTask() {

			@Override
			public void run(Timeout timeout) throws Exception {
				logger.info("run after 15s");
			}
		}, 10);
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gt.shutdown();
	}

}
