package game.module.user.dao;

import game.module.user.bean.PlayerProp;
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
 */
public class PlayerServerPropCache {

    private static Logger logger = LoggerFactory.getLogger(PlayerServerPropCache.class);

    static class SingletonHolder {

        static PlayerServerPropCache instance = new PlayerServerPropCache();
    }

    public static PlayerServerPropCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, Map<Integer, PlayerProp>> playerServerPropCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        Map<Integer, PlayerProp> myTaskAll = playerServerPropCache.get(playerId);
        if (myTaskAll == null) {
            List<PlayerProp> playerPropBean = PlayerServerPropDao.getInstance().getPlayerServerProps(playerId);
            if (playerPropBean != null) {
                Map<Integer, PlayerProp> myOthers = new HashMap<>();
                for (PlayerProp playerProp : playerPropBean) {
                    myOthers.put(playerProp.getGsid(), playerProp);
                }
                playerServerPropCache.put(playerId, myOthers);
            }
        }
        logger.info("Player ID: {}'s PlayerServerProp cache data is loaded successfully", playerId);
    }

    public void addPlayerServerProp(PlayerProp playerProp) {
        int playerId = playerProp.getPlayerId();
        if (!playerServerPropCache.containsKey(playerId)) {
            Map<Integer, PlayerProp> myHiddens = new HashMap<>();
            playerServerPropCache.put(playerProp.getPlayerId(), myHiddens);
        }
        playerServerPropCache.get(playerId).put(playerProp.getGsid(), playerProp);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public Map<Integer, PlayerProp> getPlayerServerProp(int playerId) {
        return playerServerPropCache.get(playerId);
    }

    public Map<Integer, Map<Integer, PlayerProp>> getAll() {
        return playerServerPropCache;
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removePlayerServerProp(int playerId) {
        playerServerPropCache.remove(playerId);
    }

}
