package game.module.activity.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.activity.bean.ActivityPlayer;

/**
 * 个人活动缓存
 * 
 * @author zhangning
 * 
 * @Date 2015年7月23日 下午4:28:46
 */
public class ActivityPlayerCache {

	private static Logger logger = LoggerFactory.getLogger(ActivityPlayerCache.class);

	static class SingletonHolder {
		static ActivityPlayerCache instance = new ActivityPlayerCache();
	}

	public static ActivityPlayerCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 个人活动缓存<br/>
	 * Key：玩家唯一ID
	 */
	private Map<Integer, Map<Integer, ActivityPlayer>> activityPlayerCacheAll = new ConcurrentHashMap<Integer, Map<Integer, ActivityPlayer>>();

	/**
	 * 初始化数据到缓存中
	 * 
	 * @param playerId
	 */
	public void loadFromDb(int playerId) {
		if (activityPlayerCacheAll.containsKey(playerId)) {
			return;
		}

		// Load活动
		List<ActivityPlayer> activitysPlayer = ActivityPlayerDao.getInstance().getActivityPlayer(playerId);
		if (activitysPlayer != null && !activitysPlayer.isEmpty()) {

			Map<Integer, ActivityPlayer> acticityPlayerCache = activityPlayerCacheAll.get(playerId);
			if (acticityPlayerCache == null) {
				acticityPlayerCache = new HashMap<Integer, ActivityPlayer>();
				activityPlayerCacheAll.put(playerId, acticityPlayerCache);
			}

			for (ActivityPlayer activityPlayer : activitysPlayer) {
				acticityPlayerCache.put(activityPlayer.getType(), activityPlayer);
			}
		}

		logger.info("Player ID: {}'s personal activity cache data, loaded successfully", playerId);
	}

	/**
	 * 下线删除缓存
	 * 
	 * @param playerId
	 */
	public void remove(int playerId) {
		activityPlayerCacheAll.remove(playerId);
	}

	/**
	 * 个人活动
	 * 
	 * @param playerId
	 * @return
	 */
	public ActivityPlayer getActivityPlayer(int playerId, int type) {
		Map<Integer, ActivityPlayer> activitysPlayer = activityPlayerCacheAll.get(playerId);
		if (activitysPlayer != null) {
			return activitysPlayer.get(type);
		}
		return null;
	}
	
	public Collection<Map<Integer, ActivityPlayer>> getAll(){
		return activityPlayerCacheAll.values();
	}

	/**
	 * 添加个人活动信息
	 * 
	 * @param activityPlayer
	 */
	public void addActivityPlayer(ActivityPlayer activityPlayer) {
		if (activityPlayer != null) {
			Map<Integer, ActivityPlayer> activitysPlayer = activityPlayerCacheAll.get(activityPlayer.getPlayerId());
			if (activitysPlayer == null) {
				activitysPlayer = new HashMap<Integer, ActivityPlayer>();
				activityPlayerCacheAll.put(activityPlayer.getPlayerId(), activitysPlayer);
			}
			activitysPlayer.put(activityPlayer.getType(), activityPlayer);
		}
	}

	/**
	 * 删除某个活动的所有玩家进度
	 * 
	 * @param type
	 */
	public void removePlayerActivityType(int type) {
		if (activityPlayerCacheAll.values() != null) {
			for (Map<Integer, ActivityPlayer> activityPlayers : activityPlayerCacheAll.values()) {
				try {
					activityPlayers.remove(type);
				} catch (Exception e) {
					logger.error("移除玩家活动进度异常！", e);
				}
			}
		}
	}
	
	public void removePlayerActivityType(int type, int playerId) {
		Map<Integer, ActivityPlayer> activitysPlayer = activityPlayerCacheAll.get(playerId);
		if (activitysPlayer != null) {
			activitysPlayer.remove(type);
		}
	}

	/**
	 * 删除某一玩家的某种类型的活动进度
	 * 
	 * @param playerId
	 * @param type
	 */
	public void removeOnePlayerActivityType(int playerId, int type) {
		Map<Integer, ActivityPlayer> playerActivities = activityPlayerCacheAll.get(playerId);
		if (playerActivities != null) {
			playerActivities.remove(type);
		}
	}

}
