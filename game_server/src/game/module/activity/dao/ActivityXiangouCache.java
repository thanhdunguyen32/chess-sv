package game.module.activity.dao;

import game.module.activity.bean.ActivityXiangou;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件缓存数据
 *
 * @author zhangning
 * @Date 2014年12月29日 下午2:52:06
 */
public class ActivityXiangouCache {

    private static Logger logger = LoggerFactory.getLogger(ActivityXiangouCache.class);

    static class SingletonHolder {
        static ActivityXiangouCache instance = new ActivityXiangouCache();
    }

    public static ActivityXiangouCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 邮件缓存<br/>
     * Key：玩家唯一ID
     */
    private Map<Integer, List<ActivityXiangou>> activityXiangouCacheAll = new ConcurrentHashMap<Integer, List<ActivityXiangou>>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        if (activityXiangouCacheAll.containsKey(playerId)) {
            return;
        }
        List<ActivityXiangou> activityXiangouList = ActivityXiangouDao.getInstance().getPlayerActivityXiangouAll(playerId);
        activityXiangouCacheAll.put(playerId, activityXiangouList);
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void remove(int playerId) {
        activityXiangouCacheAll.remove(playerId);
    }

    /**
     * 获取玩家所有邮件
     *
     * @param playerId
     * @return
     */
    public List<ActivityXiangou> getActivityXiangouAll(int playerId) {
        return playerId > 0 ? activityXiangouCacheAll.get(playerId) : null;
    }

    /**
     * 添加一个邮件
     *
     * @param playerId
     * @param activityXiangou
     */
    public void addActivityXiangou(int playerId, ActivityXiangou activityXiangou) {
        if (activityXiangou != null) {
            List<ActivityXiangou> activityXiangouMap = getActivityXiangouAll(playerId);
            if (activityXiangouMap == null) {
                activityXiangouMap = new ArrayList<>();
                activityXiangouCacheAll.put(playerId, activityXiangouMap);
            }
            activityXiangouMap.add(activityXiangou);
        }
    }

}
