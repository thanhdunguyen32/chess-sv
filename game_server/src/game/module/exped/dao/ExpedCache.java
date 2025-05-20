package game.module.exped.dao;

import game.module.exped.bean.ExpedBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class ExpedCache {

    private static Logger logger = LoggerFactory.getLogger(ExpedCache.class);

    static class SingletonHolder {
        static ExpedCache instance = new ExpedCache();
    }

    public static ExpedCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, ExpedBean> affairCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        ExpedBean myTaskAll = affairCache.get(playerId);
        if (myTaskAll == null) {
            ExpedBean affairBean = ExpedDao.getInstance().getExped(playerId);
            if (affairBean != null) {
                affairCache.put(playerId, affairBean);
            }
        }
        logger.info("Player ID: {}'s ExpedBean cache data is loaded successfully", playerId);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public ExpedBean getExped(int playerId) {
        return affairCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeExped(int playerId) {
        affairCache.remove(playerId);
    }

    public void addExped(ExpedBean playerExped) {
        affairCache.put(playerExped.getPlayerId(), playerExped);
    }
}
