package game.module.shop.dao;

import game.module.shop.bean.ShopBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class ShopCache {

    private static Logger logger = LoggerFactory.getLogger(ShopCache.class);

    static class SingletonHolder {
        static ShopCache instance = new ShopCache();
    }

    public static ShopCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, ShopBean> playerTaskCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        ShopBean myTaskAll = playerTaskCache.get(playerId);
        if (myTaskAll == null) {
            ShopBean shopBean = ShopDao.getInstance().getPlayerShopBean(playerId);
            if (shopBean != null) {
                playerTaskCache.put(playerId, shopBean);
            }
        }
        logger.info("Player ID: {}'s ShopBean cache data is loaded successfully", playerId);
    }

    public void addShopBean(ShopBean shopBean) {
        playerTaskCache.put(shopBean.getPlayerId(), shopBean);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public ShopBean getPlayerShopBean(int playerId) {
        ShopBean shopBeans = playerTaskCache.get(playerId);
        return shopBeans;
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void remove(int playerId) {
        playerTaskCache.remove(playerId);
    }

}
