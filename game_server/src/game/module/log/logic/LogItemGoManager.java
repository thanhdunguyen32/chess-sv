package game.module.log.logic;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.GameServer;
import game.module.log.bean.LogItemGo;
import game.module.log.dao.LogItemGoCache;
import game.module.log.dao.LogItemGoDao;

/**
 * 日志逻辑---货币消耗
 * 
 * @author zhangning
 * 
 * @Date 2015年6月2日 下午5:27:17
 */
public class LogItemGoManager {

	private static Logger logger = LoggerFactory.getLogger(LogItemGoManager.class);

	private Map<Integer, ScheduledFuture<?>> playerScheduledMap = new ConcurrentHashMap<Integer, ScheduledFuture<?>>();

	LogItemGoDao logCostDao = LogItemGoDao.getInstance();

	static class SingletonHolder {
		static LogItemGoManager instance = new LogItemGoManager();
	}

	public static LogItemGoManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 定时更新消费日志 <br/>
	 * 每5分钟执行一次
	 */
	public void scheduleLogItemGo(final int playerId) {
		ScheduledFuture<?> safr = playerScheduledMap.get(playerId);
		if (safr == null) {
			safr = GameServer.executorService.scheduleAtFixedRate(() -> {
				// 更新消耗日志
				List<LogItemGo> logCostDb = LogItemGoCache.getInstance().getinsertLogCostsAndClear(playerId);
				if (logCostDb != null && !logCostDb.isEmpty()) {
					int addCnt = logCostDao.addLogCostList(logCostDb);
					if (addCnt == logCostDb.size()) {
						logger.info("logItemGo1: count={},playerId={}", logCostDb.size(),playerId);
					} else {
						logger.error("logItemGo2: todo={}, real={},playerId={}", logCostDb.size(), addCnt, playerId);
					}
				}
			}, 1, 5, TimeUnit.MINUTES);
		}
		playerScheduledMap.put(playerId, safr);
	}

	/**
	 * 关闭定时器
	 * 
	 * @param playerId
	 */
	public void cancleLogScheduled(final int playerId) {
		ScheduledFuture<?> safr = playerScheduledMap.get(playerId);
		if (safr != null) {
			safr.cancel(false);
			playerScheduledMap.remove(playerId);
		}
	}

	/**
	 * 货币消耗
	 * 
	 * @param playerId
	 * @param moduleType
	 *            : 参考LogConstants
	 * @param value
	 */
	public void addLog(int playerId, int moduleType, int coinType, int value) {
		LogItemGo logCost = new LogItemGo();
		logCost.setPlayerId(playerId);
		logCost.setModuleType(moduleType);
		logCost.setItemId(coinType);
		logCost.setChangeValue(value);
		logCost.setCreateTime(new Date());
		LogItemGoCache.getInstance().addLogCost(playerId, logCost);
	}

}
