package game.module.dungeon.dao;

import game.module.dungeon.bean.DungeonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class DungeonCache {

    private static Logger logger = LoggerFactory.getLogger(DungeonCache.class);

    static class SingletonHolder {
        static DungeonCache instance = new DungeonCache();
    }

    public static DungeonCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, DungeonBean> affairCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        DungeonBean myTaskAll = affairCache.get(playerId);
        if (myTaskAll == null) {
            DungeonBean affairBean = DungeonDao.getInstance().getDungeon(playerId);
            if (affairBean != null) {
                affairCache.put(playerId, affairBean);
            }
        }
        logger.info("Player ID: {}'s DungeonBean cache data is loaded successfully", playerId);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public DungeonBean getDungeon(int playerId) {
        return affairCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeDungeon(int playerId) {
        affairCache.remove(playerId);
    }

    public void addDungeon(DungeonBean playerDungeon) {
        affairCache.put(playerDungeon.getPlayerId(), playerDungeon);
    }
}
