package game.module.friend.dao;

import game.module.friend.bean.FriendExplore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class FriendExploreCache {

    private static Logger logger = LoggerFactory.getLogger(FriendExploreCache.class);

    static class SingletonHolder {
        static FriendExploreCache instance = new FriendExploreCache();
    }

    public static FriendExploreCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, FriendExplore> affairCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        FriendExplore myTaskAll = affairCache.get(playerId);
        if (myTaskAll == null) {
            FriendExplore affairBean = FriendExploreDao.getInstance().getFriendExplore(playerId);
            if (affairBean != null) {
                affairCache.put(playerId, affairBean);
            }
        }
        logger.info("FriendExplore cache data of player ID: {} is loaded successfully", playerId);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public FriendExplore getFriendExplore(int playerId) {
        return affairCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeFriendExplore(int playerId) {
        affairCache.remove(playerId);
    }

    public void addFriendExplore(FriendExplore playerFriendExplore) {
        affairCache.put(playerFriendExplore.getPlayerId(), playerFriendExplore);
    }

}
