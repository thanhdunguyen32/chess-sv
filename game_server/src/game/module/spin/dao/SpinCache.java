package game.module.spin.dao;

import game.module.spin.bean.SpinBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class SpinCache {

    private static final Logger logger = LoggerFactory.getLogger(SpinCache.class);

    static class SingletonHolder {

        static SpinCache instance = new SpinCache();
    }

    public static SpinCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, SpinBean> spinBeanCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        SpinBean myTaskAll = spinBeanCache.get(playerId);
        if (myTaskAll == null) {
            SpinBean spinBeanBean = SpinDao.getInstance().getSpinBean(playerId);
            if (spinBeanBean != null) {
                spinBeanCache.put(playerId, spinBeanBean);
            }
        }
        logger.info("SpinBean cache data of player ID: {} is loaded successfully", playerId);
    }

    public void addSpinBean(SpinBean spinBean) {
        spinBeanCache.put(spinBean.getPlayerId(), spinBean);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public SpinBean getSpinBean(int playerId) {
        return spinBeanCache.get(playerId);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void removeSpinBean(int playerId) {
        spinBeanCache.remove(playerId);
    }

}
