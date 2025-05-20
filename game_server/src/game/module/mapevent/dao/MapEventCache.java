package game.module.mapevent.dao;

import game.module.mapevent.bean.PlayerMapEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class MapEventCache {

    private static Logger logger = LoggerFactory.getLogger(MapEventCache.class);

    static class SingletonHolder {

        static MapEventCache instance = new MapEventCache();
    }

    public static MapEventCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, PlayerMapEvent> mapEventCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        PlayerMapEvent myTaskAll = mapEventCache.get(playerId);
        if (myTaskAll == null) {
            PlayerMapEvent playerMapEventBean = MapEventDao.getInstance().getMapEvent(playerId);
            if (playerMapEventBean != null) {
                mapEventCache.put(playerId, playerMapEventBean);
            }
        }
        logger.info("Player ID: {}'s MapEvent cache data is loaded successfully", playerId);
    }

    public void addMapEvent(PlayerMapEvent playerMapEvent) {
        mapEventCache.put(playerMapEvent.getPlayerId(), playerMapEvent);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public PlayerMapEvent getMapEvent(int playerId) {
        return mapEventCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeMapEvent(int playerId) {
        mapEventCache.remove(playerId);
    }

}
