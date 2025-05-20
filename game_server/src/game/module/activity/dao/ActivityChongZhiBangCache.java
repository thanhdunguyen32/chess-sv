package game.module.activity.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.db.bean.DbChongZhiBang;
import game.module.db.bean.DbChongZhiBangPlayer;

/**
 * 好友缓存数据
 * 
 * @author zhangning
 * 
 * @Date 2015年8月24日 上午10:27:09
 */
public class ActivityChongZhiBangCache {

	private static Logger logger = LoggerFactory.getLogger(ActivityChongZhiBangCache.class);

	static class SingletonHolder {
		static ActivityChongZhiBangCache instance = new ActivityChongZhiBangCache();
	}

	public static ActivityChongZhiBangCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<Integer, DbChongZhiBangPlayer> playerJoinInfo = new ConcurrentHashMap<>();
	
	private volatile List<DbChongZhiBangPlayer> sortedList = new CopyOnWriteArrayList<>();

	public void loadFromDb() {
		logger.info("加载充值榜活动参与信息");
		DbChongZhiBang dbQiZhenYiBaoPlayers = ActivityDao.getInstance().getChongZhiBang();
		if (dbQiZhenYiBaoPlayers != null) {
			List<DbChongZhiBangPlayer> playerAll = dbQiZhenYiBaoPlayers.getPlayerInfoList();
			if (playerAll != null && playerAll.size() > 0) {
				for (DbChongZhiBangPlayer dbChongZhiBangPlayer : playerAll) {
					playerJoinInfo.put(dbChongZhiBangPlayer.getPlayerId(), dbChongZhiBangPlayer);
				}
				sortedList.addAll(playerAll);
				doSort();
			}
		}
	}
	
	private void doSort(){
		sortedList.sort(new Comparator<DbChongZhiBangPlayer>() {
			@Override
			public int compare(DbChongZhiBangPlayer o1, DbChongZhiBangPlayer o2) {
				return o2.getVal()-o1.getVal();
			}
		});
	}

	public void doChongZhi(int playerId, int val) {
		DbChongZhiBangPlayer existChongZhiPlayer = playerJoinInfo.get(playerId);
		if (existChongZhiPlayer == null) {
			existChongZhiPlayer = new DbChongZhiBangPlayer(playerId, val);
			playerJoinInfo.put(playerId, existChongZhiPlayer);
			sortedList.add(existChongZhiPlayer);
		} else {
			existChongZhiPlayer.setVal(existChongZhiPlayer.getVal() + val);
		}
		doSort();
	}
	
	public synchronized List<DbChongZhiBangPlayer> getRankList(){
		return sortedList;
	}

	public void save2Db() {
		logger.info("保存充值榜参与信息");
		DbChongZhiBang dbQiZhenYiBaoPlayers = new DbChongZhiBang();
		List<DbChongZhiBangPlayer> playerBuyInfo = new ArrayList<>();
		playerBuyInfo.addAll(playerJoinInfo.values());
		dbQiZhenYiBaoPlayers.setPlayerInfoList(playerBuyInfo);
		ActivityDao.getInstance().updateDbChongZhiBang(dbQiZhenYiBaoPlayers);
	}

	public void resetRank() {
		playerJoinInfo.clear();
		sortedList.clear();
	}
	
	public static void main(String[] args) {
		List<DbChongZhiBangPlayer> sortedList = new ArrayList<>();
		sortedList.add(new DbChongZhiBangPlayer(10086, 100));
		sortedList.add(new DbChongZhiBangPlayer(20012, 50));
		sortedList.sort(new Comparator<DbChongZhiBangPlayer>() {
			@Override
			public int compare(DbChongZhiBangPlayer o1, DbChongZhiBangPlayer o2) {
				return o2.getVal()-o1.getVal();
			}
		});
		for (DbChongZhiBangPlayer dbChongZhiBangPlayer : sortedList) {
			logger.info("id={},val={}",dbChongZhiBangPlayer.getPlayerId(),dbChongZhiBangPlayer.getVal());
		}
	}

	public int getMyVal(int playerId) {
		DbChongZhiBangPlayer chongZhiBangPlayer = playerJoinInfo.get(playerId);
		int ret = 0;
		if(chongZhiBangPlayer != null){
			ret = chongZhiBangPlayer.getVal();
		}
		return ret;
	}

}
