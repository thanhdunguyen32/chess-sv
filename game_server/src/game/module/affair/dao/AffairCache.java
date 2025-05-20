package game.module.affair.dao;

import game.module.affair.bean.PlayerAffairs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class AffairCache {

    private static Logger logger = LoggerFactory.getLogger(AffairCache.class);

    static class SingletonHolder {
        static AffairCache instance = new AffairCache();
    }

    public static AffairCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, PlayerAffairs> affairCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        PlayerAffairs myTaskAll = affairCache.get(playerId);
        if (myTaskAll == null) {
            PlayerAffairs affairBean = AffairDao.getInstance().getAffairs(playerId);
            if (affairBean != null) {
                affairCache.put(playerId, affairBean);
            }
        }
        logger.info("Player ID: {}'s AffairBean cache data is loaded successfully", playerId);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public PlayerAffairs getAffairs(int playerId) {
        return affairCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeAffairs(int playerId) {
        affairCache.remove(playerId);
    }

    public void addPlayerAffair(PlayerAffairs playerAffairs) {
        affairCache.put(playerAffairs.getPlayerId(), playerAffairs);
    }
}
