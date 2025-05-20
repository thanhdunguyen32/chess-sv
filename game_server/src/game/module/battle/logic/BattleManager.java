package game.module.battle.logic;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import game.module.battle.dao.BattlePlayerBase;
import game.module.mine.bean.DBMinePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BattleManager {

    private static final Logger logger = LoggerFactory.getLogger(BattleManager.class);

    static class SingletonHolder {

        static BattleManager instance = new BattleManager();
    }

    public static BattleManager getInstance() {
        return BattleManager.SingletonHolder.instance;
    }

    private final Cache<Integer, Long> battleIdCache = CacheBuilder.newBuilder().concurrencyLevel(8)
            .expireAfterAccess(1, TimeUnit.HOURS).build();

    private final Cache<Integer, DBMinePoint> robotCache = CacheBuilder.newBuilder().concurrencyLevel(8)
            .expireAfterAccess(2, TimeUnit.HOURS).build();

    private final Map<Integer,int[]> mineCache = new ConcurrentHashMap<>();

    private final Map<Integer,Integer> guozhanCache = new ConcurrentHashMap<>();

    public void saveBattleId(int playerId, long battleId) {
        battleIdCache.put(playerId, battleId);
    }

    public void tmpSaveMineCache(int playerId,int levelIndex,int pointIndex){
        mineCache.put(playerId,new int[]{levelIndex,pointIndex});
    }

    public void tmpSaveGuozhanCache(int playerId,int tmpIndex){
        guozhanCache.put(playerId, tmpIndex);
    }

    public void tmpSaveRobot(int playerId, DBMinePoint robotFormation){
        robotCache.put(playerId, robotFormation);
    }

    public boolean checkBattleId(int playerId, long battleId) {
        Long aLong = battleIdCache.getIfPresent(playerId);
        boolean ret = aLong != null && aLong.equals(battleId);
        battleIdCache.invalidate(playerId);
        return ret;
    }

    public int[] getTmpMineCache(int playerId){
        int[] ints = mineCache.get(playerId);
        mineCache.remove(playerId);
        return ints;
    }

    public int getTmpGuozhanCache(int playerId){
        int tmpIndex = guozhanCache.get(playerId);
        guozhanCache.remove(playerId);
        return tmpIndex;
    }

    public DBMinePoint getRobotFormation(int playerId,boolean isInvalidate){
        DBMinePoint robotFormation = robotCache.getIfPresent(playerId);
        if(isInvalidate) {
            robotCache.invalidate(playerId);
        }
        return robotFormation;
    }

}
