package game.module.mine.dao;

import game.module.mine.bean.DBMine;
import game.module.mine.bean.DBMinePlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MineCache {

	static class SingletonHolder {
		static MineCache instance = new MineCache();
	}

	public static MineCache getInstance() {
		return SingletonHolder.instance;
	}

	private DBMine cacheEntity = null;
	
	private Map<Integer, DBMinePlayer> playerCache = new ConcurrentHashMap<>();

	public void loadFromDb() {
		cacheEntity = MineDao.getInstance().getMine();
	}

	public DBMine getDBMine() {
		return cacheEntity;
	}

	public void setMineEntity(DBMine pbb) {
		cacheEntity = pbb;
	}

	public void remove() {
		cacheEntity = null;
	}

	public void save2Db() {
		if(cacheEntity != null) {
			MineDao.getInstance().updateMine(cacheEntity);
		}
	}
	
	public DBMinePlayer getMinePlayer(int playerId) {
		return playerCache.get(playerId);
	}
	
	public void setMinePlayer(int playerId,DBMinePlayer minePlayer) {
		playerCache.put(playerId, minePlayer);
	}

	public void putMinePlayerAll(Map<Integer, DBMinePlayer> playerMap) {
		playerCache.putAll(playerMap);
	}

}
