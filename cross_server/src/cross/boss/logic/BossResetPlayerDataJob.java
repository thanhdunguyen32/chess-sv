package cross.boss.logic;

import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossPlayer.CrossPlayerId;
import cross.boss.bean.CrossBossPlayerJoinInfo;

public class BossResetPlayerDataJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(BossResetPlayerDataJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("boss reset player data job start!");
		Map<CrossPlayerId, CrossBossPlayerJoinInfo> playerJoinInfoMap = BossPlayerManager.getInstance()
				.getPlayerJoins();
		for (CrossBossPlayerJoinInfo crossBossPlayerJoinInfo : playerJoinInfoMap.values()) {
			Set<Integer> finishRoomIds = crossBossPlayerJoinInfo.getFinishRoomIds();
			if (finishRoomIds != null) {
				finishRoomIds.clear();
			}
			crossBossPlayerJoinInfo.getRoomIdMap().clear();
		}
		playerJoinInfoMap.clear();
		logger.info("boss reset player data job end!");
	}

}
