package game.module.friend.dao;

import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendRequest;
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
public class FriendCache {

	private static Logger logger = LoggerFactory.getLogger(FriendCache.class);

	static class SingletonHolder {
		static FriendCache instance = new FriendCache();
	}

	public static FriendCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 任务进度缓存<br/>
	 * Key：玩家唯一ID<br/>
	 * Value：每个玩家的任务缓存
	 * 
	 */
	private Map<Integer, Map<Integer,FriendBean>> friendCache = new ConcurrentHashMap<>();

	private Map<Integer, Map<Integer,FriendRequest>> friendRequestCache = new ConcurrentHashMap<>();

	/**
	 * 初始化数据到缓存中
	 * 
	 * @param playerId
	 */
	public void loadFromDb(int playerId) {
		Map<Integer,FriendBean> myTaskAll = friendCache.get(playerId);
		if (myTaskAll == null) {
			myTaskAll = new HashMap<>();
			List<FriendBean> friendBeans = FriendDao.getInstance().getFriends(playerId);
			if(friendBeans != null) {
				for(FriendBean friendBean : friendBeans){
					myTaskAll.put(friendBean.getFriendId(),friendBean);
				}
				friendCache.put(playerId, myTaskAll);
			}
		}
		logger.info("FriendBean cache data of player ID: {} is loaded successfully", playerId);
		Map<Integer,FriendRequest> friendRequests = friendRequestCache.get(playerId);
		if (friendRequests == null) {
			friendRequests = new HashMap<>();
			List<FriendRequest> friendRequests1 = FriendDao.getInstance().getFriendRequests(playerId);
			if(friendRequests1 != null) {
				for (FriendRequest friendRequest : friendRequests1){
					friendRequests.put(friendRequest.getRequestPlayerId(),friendRequest);
				}
				friendRequestCache.put(playerId, friendRequests);
			}
		}
		logger.info("Player ID: {}'s FriendRequest cache data is loaded successfully", playerId);
	}

	/**
	 * 获取玩家自身的任务缓存
	 * 
	 * @param playerId
	 * @return
	 */
	public Map<Integer,FriendBean> getFriends(int playerId) {
		return friendCache.get(playerId);
	}

	/**
	 * 下线删除缓存
	 * 
	 * @param playerId
	 */
	public void removeFriends(int playerId) {
		friendCache.remove(playerId);
	}

	/**
	 * 获取玩家自身的任务缓存
	 *
	 * @param playerId
	 * @return
	 */
	public Map<Integer,FriendRequest> getFriendRequests(int playerId) {
		return friendRequestCache.get(playerId);
	}

	/**
	 * 下线删除缓存
	 *
	 * @param playerId
	 */
	public void removeFriendRequests(int playerId) {
		friendRequestCache.remove(playerId);
	}

}
