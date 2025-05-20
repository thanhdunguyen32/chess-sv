package game.module.rank.dao;

import game.module.rank.bean.DbRankLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RankLikeCache {

	private static Logger logger = LoggerFactory.getLogger(RankLikeCache.class);

	static class SingletonHolder {
		static RankLikeCache instance = new RankLikeCache();
	}

	public static RankLikeCache getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Integer, Integer> playerBeRankLikeCache = new ConcurrentHashMap<>();

	private Map<Integer, DbRankLike.DbLikePlayerInfo> myRankLikeCache = new ConcurrentHashMap<>();

	public void loadFromDb() {
		logger.info("rank like info loadFromDb!");
		DbRankLike dbChatVisit = RankDao.getInstance().getDBRankLike();
		if (dbChatVisit == null) {
			dbChatVisit = new DbRankLike();
			if (!RankDao.getInstance().isSystemBlobExist()) {
				RankDao.getInstance().insertDBRankLike(dbChatVisit);
			}
		} else {
			playerBeRankLikeCache.putAll(dbChatVisit.getBeLikeInfo());
			myRankLikeCache.putAll(dbChatVisit.getMyLikeInfo());
		}
	}

	public void saveToDb() {
		logger.info("rank like info saveToDb!");
		DbRankLike rankLike = new DbRankLike();
		rankLike.setBeLikeInfo(new HashMap<>());
		rankLike.getBeLikeInfo().putAll(playerBeRankLikeCache);
		rankLike.setMyLikeInfo(new HashMap<>());
		rankLike.getMyLikeInfo().putAll(myRankLikeCache);
		RankDao.getInstance().updateDBRankLike(rankLike);
	}

	public int getPlayerBeLikeCount(int playerId) {
		Integer beLikeCount = playerBeRankLikeCache.get(playerId);
		if(beLikeCount == null) {
			beLikeCount =0;
		}
		return beLikeCount;
	}

	public void updatePlayerBeLike(int playerId) {
		Integer beLikeCount = playerBeRankLikeCache.get(playerId);
		if(beLikeCount == null) {
			playerBeRankLikeCache.put(playerId, 1);
		}else {
			playerBeRankLikeCache.put(playerId, ++beLikeCount);
		}
	}
	
	public DbRankLike.DbLikePlayerInfo getMyRankLikeInfo(int playerId) {
		return myRankLikeCache.get(playerId);
	}
	
	public void updateMyRankLike(int playerId, DbRankLike.DbLikePlayerInfo newRankInfo) {
		myRankLikeCache.put(playerId, newRankInfo);
	}

}
