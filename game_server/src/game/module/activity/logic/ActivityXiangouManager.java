package game.module.activity.logic;

import game.GameServer;
import game.module.activity.bean.ActivityXiangou;
import game.module.activity.dao.ActivityXiangouCache;
import game.module.activity.dao.ActivityXiangouDao;
import game.module.activity.dao.SpPackStarTemplateCache;
import game.module.activity.dao.XiangouTemplateCache;
import game.module.template.MyXiangouTemplate;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ActivityXiangouManager {

    private static Logger logger = LoggerFactory.getLogger(ActivityXiangouManager.class);

    static class SingletonHolder {
        static ActivityXiangouManager instance = new ActivityXiangouManager();
    }

    public static ActivityXiangouManager getInstance() {
        return SingletonHolder.instance;
    }

    public void gstarXiangou(int playerId, int gstar) {
        Integer gstarXiangouLimit = SpPackStarTemplateCache.getInstance().getGstarXiangouLimit(gstar);
        if (gstarXiangouLimit == null) {
            return;
        }
        List<ActivityXiangou> activityXiangouAll = ActivityXiangouCache.getInstance().getActivityXiangouAll(playerId);
        ActivityXiangou activityXiangou = null;
        for (ActivityXiangou activityXiangou1 : activityXiangouAll) {
            if (activityXiangou1.getGstar() != null && activityXiangou1.getGstar().equals(gstar)) {
                activityXiangou = activityXiangou1;
                break;
            }
        }
        if (activityXiangou == null) {
            activityXiangou = createActivityXiangou(playerId, gstar, 0);
            activityXiangouAll.add(activityXiangou);
            ActivityXiangou finalActivityXiangou = activityXiangou;
            GameServer.executorService.execute(() -> ActivityXiangouDao.getInstance().addOneActivityXiangou(finalActivityXiangou));
        } else {
            activityXiangou.setEndTime(DateUtils.addHours(new Date(), 6));
            ActivityXiangou finalActivityXiangou = activityXiangou;
            GameServer.executorService.execute(() -> ActivityXiangouDao.getInstance().updateActivityXiangou(finalActivityXiangou));
        }
    }

    public void levelXiaogou(int playerId, int oldLevel, int newLevel) {
//        MyXiangouTemplate levelXiangouTemplate = XiangouTemplateCache.getInstance().getLevelXiangouTemplate(newLevel);
//        if (levelXiangouTemplate == null) {
//            return;
//        }
        Collection<MyXiangouTemplate> levelXiangouTemplateAll = XiangouTemplateCache.getInstance().getLevelXiangouTemplateAll();
        boolean findMatch = false;
        int targetLevel = 0;
        for (MyXiangouTemplate myXiangouTemplate : levelXiangouTemplateAll) {
            if (oldLevel < myXiangouTemplate.getLevel() && newLevel >= myXiangouTemplate.getLevel()) {
                findMatch = true;
                targetLevel = myXiangouTemplate.getLevel();
                break;
            }
        }
        if (!findMatch) {
            return;
        }
        List<ActivityXiangou> activityXiangouAll = ActivityXiangouCache.getInstance().getActivityXiangouAll(playerId);
        ActivityXiangou activityXiangou = null;
        for (ActivityXiangou activityXiangou1 : activityXiangouAll) {
            if (activityXiangou1.getLevel() != null && activityXiangou1.getLevel().equals(targetLevel)) {
                activityXiangou = activityXiangou1;
                break;
            }
        }
        if (activityXiangou == null) {
            activityXiangou = createActivityXiangou(playerId, 0, targetLevel);
            activityXiangouAll.add(activityXiangou);
            ActivityXiangou finalActivityXiangou = activityXiangou;
            GameServer.executorService.execute(() -> ActivityXiangouDao.getInstance().addOneActivityXiangou(finalActivityXiangou));
        } else {
            activityXiangou.setEndTime(DateUtils.addHours(new Date(), 6));
            ActivityXiangou finalActivityXiangou = activityXiangou;
            GameServer.executorService.execute(() -> ActivityXiangouDao.getInstance().updateActivityXiangou(finalActivityXiangou));
        }
    }

    public void levelAddBuyCount(int playerId, int targetLevel) {
        List<ActivityXiangou> activityXiangouAll = ActivityXiangouCache.getInstance().getActivityXiangouAll(playerId);
        ActivityXiangou activityXiangou = null;
        for (ActivityXiangou activityXiangou1 : activityXiangouAll) {
            if (activityXiangou1.getLevel() != null && activityXiangou1.getLevel().equals(targetLevel)) {
                activityXiangou = activityXiangou1;
                break;
            }
        }
        if (activityXiangou != null) {
            int curBuyCount = 0;
            Date now = new Date();
            if (activityXiangou.getBuyCount() != null && activityXiangou.getLastBuyTime() != null
                    && DateUtils.isSameDay(activityXiangou.getLastBuyTime(), now)) {
                curBuyCount = activityXiangou.getBuyCount();
            }
            activityXiangou.setBuyCount(curBuyCount + 1);
            activityXiangou.setLastBuyTime(now);
            ActivityXiangou finalActivityXiangou = activityXiangou;
            GameServer.executorService.execute(() -> ActivityXiangouDao.getInstance().updateActivityXiangou(finalActivityXiangou));
        }
    }

    public void gstarAddBuycount(int playerId, int gstar) {
        List<ActivityXiangou> activityXiangouAll = ActivityXiangouCache.getInstance().getActivityXiangouAll(playerId);
        ActivityXiangou activityXiangou = null;
        for (ActivityXiangou activityXiangou1 : activityXiangouAll) {
            if (activityXiangou1.getGstar() != null && activityXiangou1.getGstar().equals(gstar)) {
                activityXiangou = activityXiangou1;
                break;
            }
        }
        if (activityXiangou != null) {
            int curBuyCount = 0;
            Date now = new Date();
            if (activityXiangou.getBuyCount() != null && activityXiangou.getLastBuyTime() != null
                    && DateUtils.isSameDay(activityXiangou.getLastBuyTime(), now)) {
                curBuyCount = activityXiangou.getBuyCount();
            }
            activityXiangou.setBuyCount(curBuyCount + 1);
            activityXiangou.setLastBuyTime(now);
            ActivityXiangou finalActivityXiangou = activityXiangou;
            GameServer.executorService.execute(() -> ActivityXiangouDao.getInstance().updateActivityXiangou(finalActivityXiangou));
        }
    }

    public ActivityXiangou createActivityXiangou(int playerId, int gstar, int level) {
        ActivityXiangou activityXiangou = new ActivityXiangou();
        activityXiangou.setPlayerId(playerId);
        if (gstar > 0) {
            activityXiangou.setGstar(gstar);
        }
        if (level > 0) {
            activityXiangou.setLevel(level);
        }
        activityXiangou.setEndTime(DateUtils.addHours(new Date(), 6));
        return activityXiangou;
    }

}
