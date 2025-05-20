package game.module.vip.dao;

import game.module.vip.bean.VipCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class VipCountCache {

    private static Logger logger = LoggerFactory.getLogger(VipCountCache.class);

    static class SingletonHolder {

        static VipCountCache instance = new VipCountCache();
    }

    public static VipCountCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, VipCount> myCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        if (myCache.containsKey(playerId)) {
            return;
        }
        VipCount vipCount = VipCountDao.getInstance().getVipCount(playerId);
        if (vipCount != null) {
            myCache.put(playerId, vipCount);
        }
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public VipCount getVipCount(int playerId) {
        return myCache.get(playerId);
    }

    public void addVipCount(VipCount vipCount) {
        myCache.put(vipCount.getPlayerId(), vipCount);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void remove(int playerId) {
        myCache.remove(playerId);
    }

}
