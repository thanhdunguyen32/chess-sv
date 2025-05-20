package game.module.log.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.log.bean.LogItemGo;

/**
 * 货币消耗日志
 * 
 * @author zhangning
 * 
 * @Date 2015年5月26日 下午3:28:16
 */
public class LogItemGoCache {

	private static Logger logger = LoggerFactory.getLogger(LogItemGoCache.class);

	static class SingletonHolder {
		static LogItemGoCache instance = new LogItemGoCache();
	}

	public static LogItemGoCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 需要更新的玩家信息日志
	 */
	private Map<Integer, List<LogItemGo>> insertLogCostMap = new ConcurrentHashMap<Integer, List<LogItemGo>>();

	/**
	 * 玩家同步锁
	 */
	private Map<Integer, ReadWriteLock> readWriteMap = new ConcurrentHashMap<Integer, ReadWriteLock>();

	/**
	 * 下线删除缓存
	 * 
	 * @param playerId
	 */
	public void remove(int playerId) {
		insertLogCostMap.remove(playerId);
		readWriteMap.remove(playerId);
	}

	/**
	 * 添加货币消耗日志
	 * 
	 * @param logCost
	 */
	public void addLogCost(int playerId, LogItemGo logCost) {
		if (logCost != null) {
			ReadWriteLock readWriteLock = readWriteMap.get(playerId);
			if (readWriteLock == null) {
				readWriteLock = new ReentrantReadWriteLock();
				readWriteMap.put(playerId, readWriteLock);
			}

			// 获取写锁
			List<LogItemGo> insertLogCostList = insertLogCostMap.get(playerId);
			if (insertLogCostList == null) {
				insertLogCostList = new ArrayList<LogItemGo>(1);
				insertLogCostMap.put(playerId, insertLogCostList);
			}

			readWriteLock.writeLock().lock();
			try {
				insertLogCostList.add(logCost);
			} finally {
				// 解锁
				readWriteLock.writeLock().unlock();
			}
		}
	}

	/**
	 * 获取更新到DB的日志
	 * 
	 * @return
	 */
	public List<LogItemGo> getinsertLogCostsAndClear(int playerId) {
		ReadWriteLock readWriteLock = readWriteMap.get(playerId);
		if (readWriteLock == null) {
			readWriteLock = new ReentrantReadWriteLock();
			readWriteMap.put(playerId, readWriteLock);
		}

		// 获得读锁
		List<LogItemGo> insertLogCosts = insertLogCostMap.get(playerId);
		readWriteLock.readLock().lock();
		try {
			if (insertLogCosts != null && !insertLogCosts.isEmpty()) {
				List<LogItemGo> logCostDb = new ArrayList<LogItemGo>(insertLogCosts);
				insertLogCosts.clear();
				return logCostDb;
			}

			return null;
		} finally {
			// 解锁
			readWriteLock.readLock().unlock();
		}
	}

}
