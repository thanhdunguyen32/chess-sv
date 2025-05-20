package game.module.pvp.dao;

import game.module.pvp.bean.PvpRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class PvpRecordCache {

    private static Logger logger = LoggerFactory.getLogger(PvpRecordCache.class);

    static class SingletonHolder {

        static PvpRecordCache instance = new PvpRecordCache();
    }
    public static PvpRecordCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, PvpRecord> pvpRecordCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        PvpRecord myTaskAll = pvpRecordCache.get(playerId);
        if (myTaskAll == null) {
            PvpRecord pvpRecordBean = PvpRecordDao.getInstance().getPvpRecord(playerId);
            if (pvpRecordBean != null) {
                pvpRecordCache.put(playerId, pvpRecordBean);
            }
        }
        logger.info("Player ID: {}'s PvpRecord cache data is loaded successfully", playerId);
    }

    public void addPvpRecord(PvpRecord pvpRecord) {
        pvpRecordCache.put(pvpRecord.getPlayerId(),pvpRecord);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public PvpRecord getPvpRecord(int playerId) {
        return pvpRecordCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removePvpRecord(int playerId) {
        pvpRecordCache.remove(playerId);
    }

}
