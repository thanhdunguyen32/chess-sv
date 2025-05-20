package game.module.worldboss.dao;

import game.module.worldboss.bean.WorldBoss;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class WorldBossCache {

    private static Logger logger = LoggerFactory.getLogger(WorldBossCache.class);

    static class SingletonHolder {
        static WorldBossCache instance = new WorldBossCache();
    }

    public static WorldBossCache getInstance() {
        return SingletonHolder.instance;
    }

    private WorldBoss worldBoss;

    private List<Integer> playerRank = new ArrayList<>();
    private List<Long> legionRank = new ArrayList<>();

    /**
     * 初始化数据到缓存中
     */
    public void loadFromDb() {
        if (worldBoss == null) {
            worldBoss = WorldBossDao.getInstance().getWorldBoss();
            if (worldBoss != null) {
                Map<Integer, Long> playerDamages = worldBoss.getPlayerDamageSum();
                if(playerDamages == null){
                    playerDamages = new HashMap<>();
                    worldBoss.setPlayerDamageSum(playerDamages);
                }
                playerRank.clear();
                legionRank.clear();
                playerRank.addAll(playerDamages.keySet());
                sortRank(playerRank, playerDamages);
                //
                Map<Long, Long> legionDamages = worldBoss.getLegionDamageSum();
                if(legionDamages == null){
                    legionDamages = new HashMap<>();
                    worldBoss.setLegionDamageSum(legionDamages);
                }
                legionRank.addAll(legionDamages.keySet());
                sortRank(legionRank, legionDamages);
                Map<Integer, Long> playerLastDamage = worldBoss.getPlayerLastDamage();
                if(playerLastDamage == null){
                    playerLastDamage = new HashMap<>();
                    worldBoss.setPlayerLastDamage(playerLastDamage);
                }
            }
        }
    }

    public void sortPlayerRank() {
        sortRank(playerRank, worldBoss.getPlayerDamageSum());
    }

    public void addPlayerRank(int playerId){
        playerRank.add(playerId);
    }

    public void sortLegionRank() {
        sortRank(legionRank, worldBoss.getLegionDamageSum());
    }

    public void addLegionRank(long legionId){
        legionRank.add(legionId);
    }

    private <T> void sortRank(List<T> rankList, Map<T, Long> damageMap) {
        rankList.sort((t1, t2) -> {
            Long damage1 = damageMap.get(t1);
            Long damage2 = damageMap.get(t2);
            return damage2.compareTo(damage1);
        });
    }

    public void addWorldBoss(WorldBoss worldBoss){
        this.worldBoss = worldBoss;
        playerRank.clear();
        legionRank.clear();
        Map<Integer, Long> playerDamages = worldBoss.getPlayerDamageSum();
        playerRank.addAll(playerDamages.keySet());
        sortRank(playerRank, playerDamages);
        //
        Map<Long, Long> legionDamages = worldBoss.getLegionDamageSum();
        legionRank.addAll(legionDamages.keySet());
        sortRank(legionRank, legionDamages);
    }

    public void persistWorldBoss() {
        WorldBossDao.getInstance().updateWorldBoss(worldBoss);
    }

    public WorldBoss getWorldBoss() {
        return worldBoss;
    }

    public List<Integer> getPlayerRank(){
        return playerRank;
    }

    public List<Long> getLegionRank(){
        return legionRank;
    }

    public void clearRank(){
        playerRank.clear();
        legionRank.clear();
    }

}
