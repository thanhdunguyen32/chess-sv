package game.module.user.dao;

import game.module.user.bean.PlayerHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class PlayerHeadCache {

    private static Logger logger = LoggerFactory.getLogger(PlayerHeadCache.class);

    static class SingletonHolder {

        static PlayerHeadCache instance = new PlayerHeadCache();
    }
    public static PlayerHeadCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, PlayerHead> playerHeadCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        PlayerHead myTaskAll = playerHeadCache.get(playerId);
        if (myTaskAll == null) {
            PlayerHead playerHeadBean = PlayerHeadDao.getInstance().getPlayerHead(playerId);
            if (playerHeadBean != null) {
                playerHeadCache.put(playerId, playerHeadBean);
            }
        }
        logger.info("Player ID: {}'s PlayerHead cache data is loaded successfully", playerId);
    }

    public void addPlayerHead(PlayerHead playerHead) {
        playerHeadCache.put(playerHead.getPlayerId(),playerHead);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public PlayerHead getPlayerHead(int playerId) {
        return playerHeadCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removePlayerHead(int playerId) {
        playerHeadCache.remove(playerId);
    }

}
