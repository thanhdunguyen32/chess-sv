package cross.boss.logic;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossRoom;
import game.util.concurrent.OrderedEventRunnable;

public class BossTimeoutFinishJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(BossTimeoutFinishJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("boss timeout finish job start!");
		List<CrossBossRoom> tmpList = new ArrayList<>();
		tmpList.addAll(BossBattleManager.getInstance().getAllRooms());
		for (final CrossBossRoom aRoom : tmpList) {
			BossBattleManager.getInstance().execute(new OrderedEventRunnable() {
				@Override
				public void run() {
					try {
						BossLogicManager.getInstance().finishBattleTimeout(aRoom);
					} catch (Exception e) {
						logger.error("", e);
					}
				}

				@Override
				public Object getIdentifyer() {
					return aRoom.getId();
				}

				@Override
				public byte getEventType() {
					return 0;
				}
			});
		}
		tmpList.clear();
		logger.info("boss timeout finish job end!");
	}

}
