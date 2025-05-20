package game.module.manor.dao;

import game.module.manor.bean.SurrenderPersuade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class SurrenderPersuadeCache {

    private static Logger logger = LoggerFactory.getLogger(SurrenderPersuadeCache.class);

    static class SingletonHolder {
        static SurrenderPersuadeCache instance = new SurrenderPersuadeCache();
    }

    public static SurrenderPersuadeCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, SurrenderPersuade> affairCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        SurrenderPersuade myTaskAll = affairCache.get(playerId);
        if (myTaskAll == null) {
            SurrenderPersuade affairBean = SurrenderPersuadeDao.getInstance().getSurrenderPersuade(playerId);
            if (affairBean != null) {
                affairCache.put(playerId, affairBean);
            }
        }
        logger.info("Player ID: {}'s SurrenderPersuade cache data is loaded successfully", playerId);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public SurrenderPersuade getSurrenderPersuade(int playerId) {
        return affairCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeSurrenderPersuade(int playerId) {
        affairCache.remove(playerId);
    }

    public void addSurrenderPersuade(SurrenderPersuade playerSurrenderPersuade) {
        affairCache.put(playerSurrenderPersuade.getPlayerId(), playerSurrenderPersuade);
    }
}
