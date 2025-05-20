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
public class PlayerHideCache {

    private static Logger logger = LoggerFactory.getLogger(PlayerHideCache.class);

    static class SingletonHolder {

        static PlayerHideCache instance = new PlayerHideCache();
    }

    public static PlayerHideCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, Map<Integer, PlayerProp>> playerHiddenCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        Map<Integer, PlayerProp> myTaskAll = playerHiddenCache.get(playerId);
        if (myTaskAll == null) {
            List<PlayerProp> playerPropBean = PlayerHideDao.getInstance().getPlayerHidden(playerId);
            if (playerPropBean != null) {
                Map<Integer, PlayerProp> myHiddens = new HashMap<>();
                for (PlayerProp playerProp : playerPropBean) {
                    myHiddens.put(playerProp.getGsid(), playerProp);
                }
                playerHiddenCache.put(playerId, myHiddens);
            }
        }
        logger.info("PlayerHidden cache data for player ID: {} is loaded successfully", playerId);
    }

    public void addPlayerHidden(PlayerProp playerProp) {
        int playerId = playerProp.getPlayerId();
        if (!playerHiddenCache.containsKey(playerId)) {
            Map<Integer, PlayerProp> myHiddens = new HashMap<>();
            playerHiddenCache.put(playerProp.getPlayerId(), myHiddens);
        }
        playerHiddenCache.get(playerId).put(playerProp.getGsid(), playerProp);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public Map<Integer, PlayerProp> getPlayerHidden(int playerId) {
        return playerHiddenCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removePlayerHidden(int playerId) {
        playerHiddenCache.remove(playerId);
    }

}
