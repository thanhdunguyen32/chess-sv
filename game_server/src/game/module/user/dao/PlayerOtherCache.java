package game.module.user.dao;

import game.module.user.bean.PlayerProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class PlayerOtherCache {

    private static Logger logger = LoggerFactory.getLogger(PlayerOtherCache.class);

    static class SingletonHolder {

        static PlayerOtherCache instance = new PlayerOtherCache();
    }

    public static PlayerOtherCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, Map<Integer, PlayerProp>> playerOtherCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        Map<Integer, PlayerProp> myTaskAll = playerOtherCache.get(playerId);
        if (myTaskAll == null) {
            List<PlayerProp> playerPropBean = PlayerOtherDao.getInstance().getPlayerOther(playerId);
            if (playerPropBean != null) {
                Map<Integer, PlayerProp> myOthers = new HashMap<>();
                for (PlayerProp playerProp : playerPropBean) {
                    myOthers.put(playerProp.getGsid(), playerProp);
                }
                playerOtherCache.put(playerId, myOthers);
            }
        }
        logger.info("PlayerOtherCache cache data for player ID: {} is loaded successfully", playerId);
    }

    public void addPlayerOther(PlayerProp playerProp) {
        int playerId = playerProp.getPlayerId();
        if (!playerOtherCache.containsKey(playerId)) {
            Map<Integer, PlayerProp> myHiddens = new HashMap<>();
            playerOtherCache.put(playerProp.getPlayerId(), myHiddens);
        }
        playerOtherCache.get(playerId).put(playerProp.getGsid(), playerProp);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public Map<Integer, PlayerProp> getPlayerOther(int playerId) {
        return playerOtherCache.get(playerId);
    }

    public Map<Integer, Map<Integer, PlayerProp>> getAll() {
        return playerOtherCache;
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removePlayerOther(int playerId) {
        playerOtherCache.remove(playerId);
    }

}
