package game.module.activity.dao;

import game.module.activity.bean.ActMjbg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class ActMjbgCache {

    private static Logger logger = LoggerFactory.getLogger(ActMjbgCache.class);

    static class SingletonHolder {

        static ActMjbgCache instance = new ActMjbgCache();
    }
    public static ActMjbgCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, ActMjbg> actMjbgCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        ActMjbg myTaskAll = actMjbgCache.get(playerId);
        if (myTaskAll == null) {
            ActMjbg actMjbgBean = ActMjbgDao.getInstance().getActMjbg(playerId);
            if (actMjbgBean != null) {
                actMjbgCache.put(playerId, actMjbgBean);
            }
        }
        logger.info("Player ID: {}'s ActMjbg cache data is loaded successfully", playerId);
    }

    public void addActMjbg(ActMjbg actMjbg) {
        actMjbgCache.put(actMjbg.getPlayerId(),actMjbg);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public ActMjbg getActMjbg(int playerId) {
        return actMjbgCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeActMjbg(int playerId) {
        actMjbgCache.remove(playerId);
    }

    public void clearActMjbg(){
        actMjbgCache.clear();
    }

}
