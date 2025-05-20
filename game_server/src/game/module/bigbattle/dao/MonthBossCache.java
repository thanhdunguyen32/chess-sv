package game.module.bigbattle.dao;

import game.module.bigbattle.bean.MonthBoss;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class MonthBossCache {

    private static final Logger logger = LoggerFactory.getLogger(MonthBossCache.class);

    static class SingletonHolder {

        static MonthBossCache instance = new MonthBossCache();
    }

    public static MonthBossCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, MonthBoss> monthBossCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        MonthBoss myTaskAll = monthBossCache.get(playerId);
        if (myTaskAll == null) {
            MonthBoss monthBossBean = MonthBossDao.getInstance().getMonthBoss(playerId);
            if (monthBossBean != null) {
                monthBossCache.put(playerId, monthBossBean);
            }
        }
        logger.info("MonthBoss cache data for player ID: {} is loaded successfully", playerId);
    }

    public void addMonthBoss(MonthBoss monthBoss) {
        monthBossCache.put(monthBoss.getPlayerId(), monthBoss);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public MonthBoss getMonthBoss(int playerId) {
        return monthBossCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeMonthBoss(int playerId) {
        monthBossCache.remove(playerId);
    }

}
