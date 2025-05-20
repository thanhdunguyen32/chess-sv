package game.module.hero.dao;

import game.module.hero.bean.GeneralExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class GeneralExchangeCache {

    private static Logger logger = LoggerFactory.getLogger(GeneralExchangeCache.class);

    static class SingletonHolder {

        static GeneralExchangeCache instance = new GeneralExchangeCache();
    }

    public static GeneralExchangeCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, GeneralExchange> generalExchangeCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        GeneralExchange myTaskAll = generalExchangeCache.get(playerId);
        if (myTaskAll == null) {
            GeneralExchange generalExchangeBean = GeneralExchangeDao.getInstance().getGeneralExchange(playerId);
            if (generalExchangeBean != null) {
                generalExchangeCache.put(playerId, generalExchangeBean);
            }
        }
        logger.info("GeneralExchange cache data for player ID: {} is loaded successfully", playerId);
    }

    public void addGeneralExchange(GeneralExchange generalExchange) {
        generalExchangeCache.put(generalExchange.getPlayerId(), generalExchange);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public GeneralExchange getGeneralExchange(int playerId) {
        return generalExchangeCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeGeneralExchange(int playerId) {
        generalExchangeCache.remove(playerId);
    }

}
