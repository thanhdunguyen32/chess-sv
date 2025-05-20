package game.module.user.dao;

import game.module.user.bean.GoldBuy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class GoldBuyCache {

    private static Logger logger = LoggerFactory.getLogger(GoldBuyCache.class);

    static class SingletonHolder {

        static GoldBuyCache instance = new GoldBuyCache();
    }
    public static GoldBuyCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, GoldBuy> goldBuyCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        GoldBuy myTaskAll = goldBuyCache.get(playerId);
        if (myTaskAll == null) {
            GoldBuy goldBuyBean = GoldBuyDao.getInstance().getGoldBuy(playerId);
            if (goldBuyBean != null) {
                goldBuyCache.put(playerId, goldBuyBean);
            }
        }
        logger.info("Player ID: {}'s GoldBuy cache data is loaded successfully", playerId);
    }

    public void addGoldBuy(GoldBuy goldBuy) {
        goldBuyCache.put(goldBuy.getPlayerId(),goldBuy);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public GoldBuy getGoldBuy(int playerId) {
        return goldBuyCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeGoldBuy(int playerId) {
        goldBuyCache.remove(playerId);
    }

}
