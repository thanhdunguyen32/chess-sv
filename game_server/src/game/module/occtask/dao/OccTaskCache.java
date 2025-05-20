package game.module.occtask.dao;

import game.module.occtask.bean.OccTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class OccTaskCache {

    private static Logger logger = LoggerFactory.getLogger(OccTaskCache.class);

    static class SingletonHolder {
        static OccTaskCache instance = new OccTaskCache();
    }

    public static OccTaskCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, OccTask> affairCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        OccTask myTaskAll = affairCache.get(playerId);
        if (myTaskAll == null) {
            OccTask affairBean = OccTaskDao.getInstance().getOccTask(playerId);
            if (affairBean != null) {
                affairCache.put(playerId, affairBean);
            }
        }
        logger.info("Player ID: {}'s OccTaskBean cache data is loaded successfully", playerId);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public OccTask getOccTask(int playerId) {
        return affairCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeOccTask(int playerId) {
        affairCache.remove(playerId);
    }

    public void addOccTask(OccTask occTasks) {
        affairCache.put(occTasks.getPlayerId(), occTasks);
    }
}
