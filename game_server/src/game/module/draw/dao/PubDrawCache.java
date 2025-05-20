package game.module.draw.dao;

import game.module.draw.bean.PubDraw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class PubDrawCache {

    private static Logger logger = LoggerFactory.getLogger(PubDrawCache.class);

    static class SingletonHolder {

        static PubDrawCache instance = new PubDrawCache();
    }

    public static PubDrawCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, PubDraw> pubDrawCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        PubDraw myTaskAll = pubDrawCache.get(playerId);
        if (myTaskAll == null) {
            PubDraw pubDrawBean = PubDrawDao.getInstance().getPubDraw(playerId);
            if (pubDrawBean != null) {
                pubDrawCache.put(playerId, pubDrawBean);
            }
        }
        logger.info("Player ID: {}'s PubDraw cache data is loaded successfully", playerId);
    }

    public void addPubDraw(PubDraw pubDraw) {
        pubDrawCache.put(pubDraw.getPlayerId(), pubDraw);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public PubDraw getPubDraw(int playerId) {
        return pubDrawCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removePubDraw(int playerId) {
        pubDrawCache.remove(playerId);
    }

}
