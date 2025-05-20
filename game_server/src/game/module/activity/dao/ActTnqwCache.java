package game.module.activity.dao;

import game.module.activity.bean.ActTnqw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class ActTnqwCache {

    private static Logger logger = LoggerFactory.getLogger(ActTnqwCache.class);

    static class SingletonHolder {

        static ActTnqwCache instance = new ActTnqwCache();
    }
    public static ActTnqwCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, ActTnqw> actTnqwCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        ActTnqw myTaskAll = actTnqwCache.get(playerId);
        if (myTaskAll == null) {
            ActTnqw actTnqwBean = ActTnqwDao.getInstance().getActTnqw(playerId);
            if (actTnqwBean != null) {
                actTnqwCache.put(playerId, actTnqwBean);
            }
        }
        logger.info("Player ID: {}'s ActTnqw cache data is loaded successfully", playerId);
    }

    public void addActTnqw(ActTnqw actTnqw) {
        actTnqwCache.put(actTnqw.getPlayerId(),actTnqw);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public ActTnqw getActTnqw(int playerId) {
        return actTnqwCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeActTnqw(int playerId) {
        actTnqwCache.remove(playerId);
    }

    public void clearActTnqw(){
        actTnqwCache.clear();
    }

}
