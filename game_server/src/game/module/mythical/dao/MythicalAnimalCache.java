package game.module.mythical.dao;

import game.module.mythical.bean.MythicalAnimal;
import game.module.offline.logic.PlayerOfflineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class MythicalAnimalCache {

    private static Logger logger = LoggerFactory.getLogger(MythicalAnimalCache.class);

    static class SingletonHolder {
        static MythicalAnimalCache instance = new MythicalAnimalCache();
    }

    public static MythicalAnimalCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, Map<Integer,MythicalAnimal>> playerTaskCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        Map<Integer,MythicalAnimal> myTaskAll = playerTaskCache.get(playerId);
        if (myTaskAll == null) {
            List<MythicalAnimal> mythicalAnimals = MythicalAnimalDao.getInstance().getPlayerMythicalAnimal(playerId);
            if (mythicalAnimals != null) {
                Map<Integer,MythicalAnimal> mythicalAnimalMap = new HashMap<>();
                for (MythicalAnimal mythicalAnimal : mythicalAnimals){
                    mythicalAnimalMap.put(mythicalAnimal.getTemplateId(),mythicalAnimal);
                }
                playerTaskCache.put(playerId, mythicalAnimalMap);
                PlayerOfflineManager.getInstance().updateMythical(playerId, mythicalAnimalMap);
            }
        }
        logger.info("Player ID: {}'s MythicalAnimal cache data is loaded successfully", playerId);
    }

    public void addMythicalAnimal(MythicalAnimal mythicalAnimal) {
        Map<Integer,MythicalAnimal> mythicalAnimals = playerTaskCache.get(mythicalAnimal.getPlayerId());
        if (mythicalAnimals == null) {
            mythicalAnimals = new HashMap<>();
            playerTaskCache.put(mythicalAnimal.getPlayerId(), mythicalAnimals);
        }
        mythicalAnimals.put(mythicalAnimal.getTemplateId(), mythicalAnimal);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public Map<Integer,MythicalAnimal> getPlayerMythicalAnimals(int playerId) {
        Map<Integer,MythicalAnimal> mythicalAnimals = playerTaskCache.get(playerId);
        return mythicalAnimals;
    }

    public MythicalAnimal getPlayerMythicalAnimal(int playerId, int templateId) {
        Map<Integer,MythicalAnimal> mythicalAnimals = playerTaskCache.get(playerId);
        if (mythicalAnimals == null) {
            return null;
        }
        return mythicalAnimals.get(templateId);
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
