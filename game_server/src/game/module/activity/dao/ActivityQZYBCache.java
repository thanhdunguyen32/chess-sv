package game.module.activity.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.db.bean.DBQZYBPlayer1;
import game.module.db.bean.DBQiZhenYiBaoPlayers;

/**
 * 好友缓存数据
 * 
 * @author zhangning
 * 
 * @Date 2015年8月24日 上午10:27:09
 */
public class ActivityQZYBCache {

	private static Logger logger = LoggerFactory.getLogger(ActivityQZYBCache.class);

	static class SingletonHolder {
		static ActivityQZYBCache instance = new ActivityQZYBCache();
	}

	public static ActivityQZYBCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile int currentJoinCount = 0;

	private volatile Map<Integer, DBQZYBPlayer1> playerJoinInfo = new ConcurrentHashMap<>();

	private volatile List<String> kaiJiangInfo = new ArrayList<>();

	public void loadFromDb() {
		logger.info("加载奇珍异宝活动参与信息");
		DBQiZhenYiBaoPlayers dbQiZhenYiBaoPlayers = ActivityDao.getInstance().getQiZhenYiBaoLog();
		if (dbQiZhenYiBaoPlayers != null) {
			if (dbQiZhenYiBaoPlayers.getCurrentCount() != null) {
				currentJoinCount = dbQiZhenYiBaoPlayers.getCurrentCount();
			}
			Map<Integer, DBQZYBPlayer1> tmpPlayerJoinInfo = new ConcurrentHashMap<>();
			if (dbQiZhenYiBaoPlayers.getPlayerJoinList() != null) {
				for (DBQZYBPlayer1 dbqzybPlayer1 : dbQiZhenYiBaoPlayers.getPlayerJoinList()) {
					tmpPlayerJoinInfo.put(dbqzybPlayer1.getPlayerId(), dbqzybPlayer1);
				}
			}
			playerJoinInfo = tmpPlayerJoinInfo;
			if (dbQiZhenYiBaoPlayers.getKaiJiangPlayerList() != null) {
				kaiJiangInfo = dbQiZhenYiBaoPlayers.getKaiJiangPlayerList();
			}
		}
	}

	public void addQZYBBuyInfo(int playerId, String playerName) {
		DBQZYBPlayer1 existOne = playerJoinInfo.get(playerId);
		int lastBuyTime = (int) (System.currentTimeMillis() / 1000);
		if (existOne == null) {
			existOne = new DBQZYBPlayer1(playerId, playerName, 1, lastBuyTime);
			playerJoinInfo.put(playerId, existOne);
		} else {
			existOne.setCount(existOne.getCount() + 1);
			existOne.setLastBuyTime(lastBuyTime);
		}
		currentJoinCount++;
	}

	public void addKaiJIang(String playerName) {
		kaiJiangInfo.add(playerName);
		currentJoinCount = 0;
		playerJoinInfo.clear();
	}

	public void save2Db() {
		logger.info("保存奇珍异宝活动参与信息");
		DBQiZhenYiBaoPlayers dbQiZhenYiBaoPlayers = new DBQiZhenYiBaoPlayers(currentJoinCount);
		List<DBQZYBPlayer1> playerBuyInfo = new ArrayList<>();
		playerBuyInfo.addAll(playerJoinInfo.values());
		dbQiZhenYiBaoPlayers.setPlayerJoinList(playerBuyInfo);
		if (kaiJiangInfo != null) {
			dbQiZhenYiBaoPlayers.setKaiJiangPlayerList(kaiJiangInfo);
		}
		dbQiZhenYiBaoPlayers.setCurrentCount(currentJoinCount);
		ActivityDao.getInstance().updateDBQiZhenYiBao(dbQiZhenYiBaoPlayers);
	}

	public int getBuyCount() {
		return currentJoinCount;
	}

	public Collection<DBQZYBPlayer1> getPlayerBuyInfo() {
		return playerJoinInfo.values();
	}

	public DBQZYBPlayer1 getMyBuyInfo(int playerId) {
		return playerJoinInfo.get(playerId);
	}

	public List<String> getKaiJiangLog() {
		return kaiJiangInfo;
	}
	
	public void resetQZYB(){
		currentJoinCount = 0;
		playerJoinInfo.clear();
		kaiJiangInfo.clear();
	}

}
