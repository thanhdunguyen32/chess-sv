package game.module.chapter.dao;

import game.module.chapter.bean.BattleFormation;
import game.module.offline.logic.PlayerOfflineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class BattleFormationCache {

    private static final Logger logger = LoggerFactory.getLogger(BattleFormationCache.class);

    static class SingletonHolder {

        static BattleFormationCache instance = new BattleFormationCache();
    }

    public static BattleFormationCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, BattleFormation> battleFormationCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        BattleFormation myTaskAll = battleFormationCache.get(playerId);
        if (myTaskAll == null) {
            BattleFormation battleFormationBean = BattleFormationDao.getInstance().getBattleFormation(playerId);
            if (battleFormationBean != null) {
                battleFormationCache.put(playerId, battleFormationBean);
                PlayerOfflineManager.getInstance().updateBattleFormation(playerId, battleFormationBean);
            }
        }
        logger.info("BattleFormation cache data for player ID: {} is loaded successfully", playerId);
    }

    public void addBattleFormation(BattleFormation battleFormation) {
        battleFormationCache.put(battleFormation.getPlayerId(), battleFormation);
        PlayerOfflineManager.getInstance().updateBattleFormation(battleFormation.getPlayerId(), battleFormation);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public BattleFormation getBattleFormation(int playerId) {
        return battleFormationCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeBattleFormation(int playerId) {
        battleFormationCache.remove(playerId);
    }

}
