package game.module.hero.dao;

import game.module.hero.bean.GeneralBean;
import game.module.offline.logic.PlayerOfflineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 * 
 * @author zhangning
 *
 */
public class GeneralCache {

	private static Logger logger = LoggerFactory.getLogger(GeneralCache.class);

	static class SingletonHolder {
		static GeneralCache instance = new GeneralCache();
	}

	public static GeneralCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 任务进度缓存<br/>
	 * Key：玩家唯一ID<br/>
	 * Value：每个玩家的任务缓存
	 * 
	 */
	private final Map<Integer, Map<Long, GeneralBean>> heroCache = new ConcurrentHashMap<>();

	/**
	 * 初始化数据到缓存中
	 * 
	 * @param playerId
	 */
	public void loadFromDb(int playerId) {
		if (heroCache.containsKey(playerId)) {
			return;
		}
		Map<Long, GeneralBean> generalAll = heroCache.get(playerId);
		if (generalAll == null) {
			generalAll = new HashMap<>();
			List<GeneralBean> taskList = GeneralDao.getInstance().getHeros(playerId);
			for (GeneralBean taskEntity : taskList) {
				generalAll.put(taskEntity.getUuid(), taskEntity);
			}
			heroCache.put(playerId, generalAll);
			PlayerOfflineManager.getInstance().updateGenerals(playerId, generalAll);
		}
	}

	/**
	 * 获取玩家自身的任务缓存
	 * 
	 * @param playerId
	 * @return
	 */
	public Map<Long, GeneralBean> getHeros(int playerId) {
		return heroCache.get(playerId);
	}

	/**
	 * 下线删除缓存
	 * 
	 * @param playerId
	 */
	public void remove(int playerId) {
		heroCache.remove(playerId);
	}

}
