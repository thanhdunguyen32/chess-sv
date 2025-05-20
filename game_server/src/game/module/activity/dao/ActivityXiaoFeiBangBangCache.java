package game.module.activity.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class ActivityXiaoFeiBangBangCache {

	private static Logger logger = LoggerFactory.getLogger(ActivityXiaoFeiBangBangCache.class);

	static class SingletonHolder {
		static ActivityXiaoFeiBangBangCache instance = new ActivityXiaoFeiBangBangCache();
	}

	public static ActivityXiaoFeiBangBangCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<Integer, DbChongZhiBangPlayer> playerJoinInfo = new ConcurrentHashMap<>();
	
	private volatile List<DbChongZhiBangPlayer> sortedList = new ArrayList<>();

	public void loadFromDb() {
		logger.info("加载消费榜活动参与信息");
		DbChongZhiBang dbQiZhenYiBaoPlayers = ActivityDao.getInstance().getDBActivityXiaoFeiBang();
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

	public void doXiaoFei(int playerId, int val) {
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
	
	public List<DbChongZhiBangPlayer> getRankList(){
		return sortedList;
	}

	public void save2Db() {
		logger.info("保存消费榜参与信息");
		DbChongZhiBang dbQiZhenYiBaoPlayers = new DbChongZhiBang();
		List<DbChongZhiBangPlayer> playerBuyInfo = new ArrayList<>();
		playerBuyInfo.addAll(playerJoinInfo.values());
		dbQiZhenYiBaoPlayers.setPlayerInfoList(playerBuyInfo);
		ActivityDao.getInstance().updateDBActivityXiaoFeiBang(dbQiZhenYiBaoPlayers);
	}

	public void resetRank() {
		playerJoinInfo.clear();
		sortedList.clear();
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
