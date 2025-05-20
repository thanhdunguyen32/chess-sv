package game.module.pay.logic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.pay.bean.BuyCostBean;
import game.module.pay.dao.BuyCostDao;

public class BuyCostCache {

	private static Logger logger = LoggerFactory.getLogger(BuyCostCache.class);

	static class SingletonHolder {
		static BuyCostCache instance = new BuyCostCache();
	}

	public static BuyCostCache getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Integer, BuyCostBean> cacheAll = new ConcurrentHashMap<Integer, BuyCostBean>();

	public void loadFromDb(int playerId) {
		if(cacheAll.containsKey(playerId)){
			return;
		}
		BuyCostBean pbb = BuyCostDao.getInstance().getBuyCostBean(playerId);
		if (pbb != null) {
			cacheAll.put(playerId, pbb);
		}
	}

	public BuyCostBean getBuyCostBean(int playerId) {
		return cacheAll.get(playerId);
	}

	public void addNewEntity(BuyCostBean pbb) {
		cacheAll.put(pbb.getPlayerId(), pbb);
	}

	public void remove(int playerId) {
		cacheAll.remove(playerId);
	}

}
