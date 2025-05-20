package game.module.pay.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import game.module.pay.bean.ChargeEntity;

public class ChargeCache {

	static class SingletonHolder {
		static ChargeCache instance = new ChargeCache();
	}

	public static ChargeCache getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Integer, ChargeEntity> cacheAll = new ConcurrentHashMap<Integer, ChargeEntity>();

	public void loadFromDb(int playerId) {
		if (cacheAll.containsKey(playerId)) {
			return;
		}
		ChargeEntity stagePve = ChargeDao.getInstance().getChargeEntityByPlayerId(playerId);
		if (stagePve != null) {
			cacheAll.put(playerId, stagePve);
		}
	}

	public ChargeEntity getChargeEntity(int playerId) {
		return cacheAll.get(playerId);
	}

	public void addNewEntity(ChargeEntity stagePve) {
		cacheAll.put(stagePve.getPlayerId(), stagePve);
	}

	public void remove(int playerId) {
		cacheAll.remove(playerId);
	}

}
