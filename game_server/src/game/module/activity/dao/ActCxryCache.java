package game.module.activity.dao;

import game.module.activity.bean.ActCxry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class ActCxryCache {

    private static Logger logger = LoggerFactory.getLogger(ActCxryCache.class);

    static class SingletonHolder {

        static ActCxryCache instance = new ActCxryCache();
    }
    public static ActCxryCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, ActCxry> actCxryCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        ActCxry myTaskAll = actCxryCache.get(playerId);
        if (myTaskAll == null) {
            ActCxry actCxryBean = ActCxryDao.getInstance().getActCxry(playerId);
            if (actCxryBean != null) {
                actCxryCache.put(playerId, actCxryBean);
            }
        }
        logger.info("Player ID: {}'s ActCxry cache data is loaded successfully", playerId);
    }

    public void addActCxry(ActCxry actCxry) {
        actCxryCache.put(actCxry.getPlayerId(),actCxry);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public ActCxry getActCxry(int playerId) {
        return actCxryCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeActCxry(int playerId) {
        actCxryCache.remove(playerId);
    }

    public void clearActCxry(){
        actCxryCache.clear();
    }

}
