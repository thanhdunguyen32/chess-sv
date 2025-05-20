package game.module.bigbattle.logic;

import game.entity.PlayingRole;
import game.module.bigbattle.bean.MonthBoss;
import game.module.bigbattle.dao.MonthBossDaoHelper;
import game.module.bigbattle.dao.MyMonthBossTemplateCache;
import game.module.template.MyMonthBossTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Admin
 */
public class MonthBossManager {

    private static Logger logger = LoggerFactory.getLogger(MonthBossManager.class);

    static class SingletonHolder {

        static MonthBossManager instance = new MonthBossManager();
    }

    public static MonthBossManager getInstance() {
        return SingletonHolder.instance;
    }

    public MonthBoss createMonthBoss(int playerId) {
        MonthBoss monthBoss = new MonthBoss();
        monthBoss.setPlayerId(playerId);
        initMonthBoss(monthBoss);
        return monthBoss;
    }

    public void initMonthBoss(MonthBoss monthBoss) {
        monthBoss.setLevelIndex(Arrays.asList(0, 0, 0));
        monthBoss.setLastDamage(Arrays.asList(0L, 0L, 0L));
        monthBoss.setNowHp(Arrays.asList(0L, 0L, 0L));
        monthBoss.setLastVisitTime(new Date());
        List<List<MyMonthBossTemplate>> monthBossConfig = MyMonthBossTemplateCache.getInstance().getMonthBossConfig();
        int i = 0;
        for (List<MyMonthBossTemplate> myMonthBossTemplates : monthBossConfig) {
            Long maxhp = myMonthBossTemplates.get(0).getMaxhp();
            monthBoss.getNowHp().set(i, maxhp);
            monthBoss.getLastDamage().set(i, maxhp / 500);
            i++;
        }
    }

    public void resetCheck(PlayingRole playingRole, MonthBoss monthBoss) {
        Date lastVisitTime = monthBoss.getLastVisitTime();
        int nowDiffMonths = (int) ((System.currentTimeMillis() - playingRole.getPlayerBean().getCreateTime().getTime()) / 1000 / 2592e3);
        int oldDiffMonths = (int) ((lastVisitTime.getTime() - playingRole.getPlayerBean().getCreateTime().getTime()) / 1000 / 2592e3);
        if (nowDiffMonths != oldDiffMonths) {
            initMonthBoss(monthBoss);
            MonthBossDaoHelper.asyncUpdateMonthBoss(monthBoss);
        }
    }

}
