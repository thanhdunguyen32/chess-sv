package game.module.manor.dao;

import game.module.manor.bean.ManorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class ManorCache {

    private static Logger logger = LoggerFactory.getLogger(ManorCache.class);

    static class SingletonHolder {
        static ManorCache instance = new ManorCache();
    }

    public static ManorCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, ManorBean> affairCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        ManorBean myTaskAll = affairCache.get(playerId);
        if (myTaskAll == null) {
            ManorBean affairBean = ManorDao.getInstance().getManor(playerId);
            if (affairBean != null) {
                affairCache.put(playerId, affairBean);
            }
        }
        logger.info("Player ID: {}'s ManorBean cache data is loaded successfully", playerId);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public ManorBean getManor(int playerId) {
        return affairCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeManor(int playerId) {
        affairCache.remove(playerId);
    }

    public void addManor(ManorBean playerManor) {
        affairCache.put(playerManor.getPlayerId(), playerManor);
    }
}
