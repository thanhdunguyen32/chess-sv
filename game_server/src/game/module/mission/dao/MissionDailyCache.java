package game.module.mission.dao;

import game.module.mission.bean.MissionDaily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 * 
 * @author zhangning
 *
 */
public class MissionDailyCache {

	private static Logger logger = LoggerFactory.getLogger(MissionDailyCache.class);

	static class SingletonHolder {
		static MissionDailyCache instance = new MissionDailyCache();
	}

	public static MissionDailyCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 任务进度缓存<br/>
	 * Key：玩家唯一ID<br/>
	 * Value：每个玩家的任务缓存
	 * 
	 */
	private Map<Integer, MissionDaily> playerTaskCache = new ConcurrentHashMap<>();

	/**
	 * 初始化数据到缓存中
	 * 
	 * @param playerId
	 */
	public void loadFromDb(int playerId) {
		MissionDaily myTaskAll = playerTaskCache.get(playerId);
		if (myTaskAll == null) {
			MissionDaily missionDaily = MissionDailyDao.getInstance().getPlayerMissionDaily(playerId);
			if(missionDaily != null) {
				playerTaskCache.put(playerId, missionDaily);
			}
		}
		logger.info("Player ID: {}'s MissionDaily cache data is loaded successfully", playerId);
	}

	/**
	 * 获取玩家自身的任务缓存
	 * 
	 * @param playerId
	 * @return
	 */
	public MissionDaily getPlayerMissionDaily(int playerId) {
		return playerTaskCache.get(playerId);
	}

	public void addMissionDaily(MissionDaily missionDaily){
		playerTaskCache.put(missionDaily.getPlayerId(), missionDaily);
	}

	/**
	 * 下线删除缓存
	 * 
	 * @param playerId
	 */
	public void remove(int playerId) {
		playerTaskCache.remove(playerId);
	}

}
