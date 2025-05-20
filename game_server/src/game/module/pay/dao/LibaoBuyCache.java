package game.module.pay.dao;

import game.module.pay.bean.LibaoBuy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class LibaoBuyCache {

    private static Logger logger = LoggerFactory.getLogger(LibaoBuyCache.class);

    static class SingletonHolder {
        static LibaoBuyCache instance = new LibaoBuyCache();
    }

    public static LibaoBuyCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, LibaoBuy> libaoBuyCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        LibaoBuy myTaskAll = libaoBuyCache.get(playerId);
        if (myTaskAll == null) {
            LibaoBuy libaoBuyBean = LibaoBuyDao.getInstance().getLibaoBuy(playerId);
            if (libaoBuyBean != null) {
                libaoBuyCache.put(playerId, libaoBuyBean);
            }
        }
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public LibaoBuy getLibaoBuy(int playerId) {
        return libaoBuyCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeLibaoBuy(int playerId) {
        libaoBuyCache.remove(playerId);
    }

    public void addLibaoBuy(LibaoBuy libaoBuy) {
        libaoBuyCache.put(libaoBuy.getPlayerId(), libaoBuy);
    }

    public Map<Integer, LibaoBuy> getLibaoBuyCache(){
        return libaoBuyCache;
    }
}
