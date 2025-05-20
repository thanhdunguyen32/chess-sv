package game.module.activity.dao;

import game.module.activity.bean.ZhdBean;
import game.module.season.bean.BattleSeason;
import game.module.season.dao.SeasonCache;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件缓存数据
 *
 * @author zhangning
 * @Date 2014年12月29日 下午2:52:06
 */
public class ActivityWeekCache {

    private static Logger logger = LoggerFactory.getLogger(ActivityWeekCache.class);

    static class SingletonHolder {
        static ActivityWeekCache instance = new ActivityWeekCache();
    }

    public static ActivityWeekCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 邮件缓存<br/>
     * Key：玩家唯一ID
     */
    private Map<String, ZhdBean> cacheAll = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     */
    public void loadFromDb() {

    }

    public void initZhdTime() {
        BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
        Integer seasonId = battleSeason.getSeason();
        refreshZhdTime(battleSeason);
    }

    public void refreshZhdTime(BattleSeason battleSeason) {
        int seasonId = battleSeason.getSeason();
        Date openEndTime = battleSeason.getEtime();
        cacheAll.clear();
        Date now = new Date();
        Date openBeginTime = DateUtils.addDays(openEndTime, -7);
        Date closeBeginTime = DateUtils.addDays(now, -6);
        Date closeEndTime = DateUtils.addDays(now, -2);
        /**
         * init
         */
        cacheAll.put("CJXG3", new ZhdBean("CJXG3", closeBeginTime, closeEndTime));
        cacheAll.put("LJCZ", new ZhdBean("LJCZ", closeBeginTime, closeEndTime));
        cacheAll.put("GZLB", new ZhdBean("GZLB", closeBeginTime, closeEndTime));
        cacheAll.put("ZMJF", new ZhdBean("ZMJF", closeBeginTime, closeEndTime));
        cacheAll.put("XSDH", new ZhdBean("XSDH", closeBeginTime, closeEndTime));
        cacheAll.put("LDCF", new ZhdBean("LDCF", closeBeginTime, closeEndTime));
        cacheAll.put("ZMQY", new ZhdBean("ZMQY", closeBeginTime, closeEndTime));
        cacheAll.put("GZLBDM", new ZhdBean("GZLBDM", closeBeginTime, closeEndTime));
        cacheAll.put("GXLB", new ZhdBean("GXLB", closeBeginTime, closeEndTime));
        cacheAll.put("SZHC", new ZhdBean("SZHC", closeBeginTime, closeEndTime));
        cacheAll.put("CJXG1", new ZhdBean("CJXG1", closeBeginTime, closeEndTime));
        cacheAll.put("GXDB", new ZhdBean("GXDB", closeBeginTime, closeEndTime));
        cacheAll.put("TNQW", new ZhdBean("TNQW", closeBeginTime, closeEndTime));
        cacheAll.put("CZLB", new ZhdBean("CZLB", closeBeginTime, closeEndTime));
        cacheAll.put("CJXG2", new ZhdBean("CJXG2", closeBeginTime, closeEndTime));
        cacheAll.put("DJJF", new ZhdBean("DJJF", closeBeginTime, closeEndTime));
        cacheAll.put("DJRW", new ZhdBean("DJRW", closeBeginTime, closeEndTime));
        cacheAll.put("JFBX", new ZhdBean("JFBX", closeBeginTime, closeEndTime));
        cacheAll.put("YHZH", new ZhdBean("YHZH", closeBeginTime, closeEndTime));
        cacheAll.put("SSLX", new ZhdBean("SSLX", closeBeginTime, closeEndTime));
        cacheAll.put("LJDL", new ZhdBean("LJDL", closeBeginTime, closeEndTime));
        cacheAll.put("SWLB", new ZhdBean("SWLB", closeBeginTime, closeEndTime));
        cacheAll.put("JHLB", new ZhdBean("JHLB", closeBeginTime, closeEndTime));
        cacheAll.put("JTHL", new ZhdBean("JTHL", closeBeginTime, closeEndTime));
        cacheAll.put("HYHL", new ZhdBean("HYHL", closeBeginTime, closeEndTime));
        cacheAll.put("YHSH", new ZhdBean("YHSH", closeBeginTime, closeEndTime));
        cacheAll.put("DAILYMAIL", new ZhdBean("DAILYMAIL", closeBeginTime, closeEndTime));
        cacheAll.put("LHLJ", new ZhdBean("LHLJ", closeBeginTime, closeEndTime));
        cacheAll.put("SMBX", new ZhdBean("SMBX", closeBeginTime, closeEndTime));
        cacheAll.put("SBJL", new ZhdBean("SBJL", closeBeginTime, closeEndTime));
        cacheAll.put("GJDL", new ZhdBean("GJDL", closeBeginTime, closeEndTime));
        cacheAll.put("GJLB", new ZhdBean("GJLB", closeBeginTime, closeEndTime));
        cacheAll.put("WZQD", new ZhdBean("WZQD", closeBeginTime, closeEndTime));
        cacheAll.put("GJLBDM", new ZhdBean("GJLBDM", closeBeginTime, closeEndTime));
        cacheAll.put("ATYZM", new ZhdBean("ATYZM", closeBeginTime, closeEndTime));
        cacheAll.put("ZMFL", new ZhdBean("ZMFL", closeBeginTime, closeEndTime));
        cacheAll.put("MJBG", new ZhdBean("MJBG", closeBeginTime, closeEndTime));
        cacheAll.put("TGSL", new ZhdBean("TGSL", closeBeginTime, closeEndTime));
        cacheAll.put("DWLB", new ZhdBean("DWLB", closeBeginTime, closeEndTime));
        cacheAll.put("DWHD", new ZhdBean("DWHD", closeBeginTime, closeEndTime));
        cacheAll.put("ZMZF", new ZhdBean("ZMZF", closeBeginTime, closeEndTime));
        cacheAll.put("THLB", new ZhdBean("THLB", closeBeginTime, closeEndTime));
        cacheAll.put("DCDY", new ZhdBean("DCDY", closeBeginTime, closeEndTime));
        cacheAll.put("XYGX", new ZhdBean("XYGX", closeBeginTime, closeEndTime));
        cacheAll.put("WJCS", new ZhdBean("WJCS", closeBeginTime, closeEndTime));
        cacheAll.put("WJZH", new ZhdBean("WJZH", closeBeginTime, closeEndTime));
        cacheAll.put("CJHD", new ZhdBean("CJHD", closeBeginTime, closeEndTime));
        cacheAll.put("RLWJ", new ZhdBean("RLWJ", closeBeginTime, closeEndTime));
        cacheAll.put("JRHDDM", new ZhdBean("JRHDDM", closeBeginTime, closeEndTime));
        cacheAll.put("JRLB1", new ZhdBean("JRLB1", closeBeginTime, closeEndTime));
        cacheAll.put("JRLB2", new ZhdBean("JRLB2", closeBeginTime, closeEndTime));
        cacheAll.put("GXLBDM", new ZhdBean("GXLBDM", closeBeginTime, closeEndTime));

        switch (seasonId) {
            case 1:
                /**
                 * 观星周
                 */
                cacheAll.put("GXLB", new ZhdBean("GXLB", openBeginTime, openEndTime));
                cacheAll.put("SZHC", new ZhdBean("SZHC", openBeginTime, openEndTime));
                cacheAll.put("CJXG1", new ZhdBean("CJXG1", openBeginTime, openEndTime));
                cacheAll.put("GXDB", new ZhdBean("GXDB", openBeginTime, openEndTime));
                cacheAll.put("TNQW", new ZhdBean("TNQW", openBeginTime, openEndTime));
                cacheAll.put("CJXG2", new ZhdBean("CJXG2", openBeginTime, openEndTime));
                cacheAll.put("LJDL", new ZhdBean("LJDL", openBeginTime, openEndTime));
                cacheAll.put("HYHL", new ZhdBean("HYHL", openBeginTime, openEndTime));
                break;
            case 2:
                /**
                 * 挂机周
                 */
                cacheAll.put("CJXG3", new ZhdBean("CJXG3", openBeginTime, openEndTime));
                cacheAll.put("GJDL", new ZhdBean("GJDL", openBeginTime, openEndTime));
                cacheAll.put("GJLB", new ZhdBean("GJLB", openBeginTime, openEndTime));
                cacheAll.put("GJLBDM", new ZhdBean("GJLBDM", openBeginTime, openEndTime));
                cacheAll.put("MJBG", new ZhdBean("MJBG", openBeginTime, openEndTime));
                cacheAll.put("TJFX", new ZhdBean("TJFX", openBeginTime, openEndTime));
                cacheAll.put("DLHL", new ZhdBean("DLHL", openBeginTime, openEndTime));
                cacheAll.put("DZLB", new ZhdBean("DZLB", openBeginTime, openEndTime));
                cacheAll.put("TGSL", new ZhdBean("TGSL", openBeginTime, openEndTime));
                cacheAll.put("CJXG2", new ZhdBean("CJXG2", openBeginTime, openEndTime));
                break;
            case 3:
                /**
                 * 点将周
                 */
                cacheAll.put("CJXG3", new ZhdBean("CJXG3", openBeginTime, openEndTime));
                cacheAll.put("LJCZ", new ZhdBean("LJCZ", openBeginTime, openEndTime));
                cacheAll.put("CZLB", new ZhdBean("CZLB", openBeginTime, openEndTime));
                cacheAll.put("DJJF", new ZhdBean("DJJF", openBeginTime, openEndTime));
                cacheAll.put("DJRW", new ZhdBean("DJRW", openBeginTime, openEndTime));
                cacheAll.put("JFBX", new ZhdBean("JFBX", openBeginTime, openEndTime));
                cacheAll.put("YHZH", new ZhdBean("YHZH", openBeginTime, openEndTime));
                cacheAll.put("SSLX", new ZhdBean("SSLX", openBeginTime, openEndTime));
                cacheAll.put("LJDL", new ZhdBean("LJDL", openBeginTime, openEndTime));
                cacheAll.put("SWLB", new ZhdBean("SWLB", openBeginTime, openEndTime));
                cacheAll.put("JHLB", new ZhdBean("JHLB", openBeginTime, openEndTime));
                cacheAll.put("JTHL", new ZhdBean("JTHL", openBeginTime, openEndTime));
                cacheAll.put("HYHL", new ZhdBean("HYHL", openBeginTime, openEndTime));
                cacheAll.put("YHSH", new ZhdBean("YHSH", openBeginTime, openEndTime));
                cacheAll.put("DAILYMAIL", new ZhdBean("DAILYMAIL", openBeginTime, openEndTime));
                cacheAll.put("CJXG2", new ZhdBean("CJXG2", openBeginTime, openEndTime));
                SeasonCache.getInstance().getBattleSeason().setZhdName("djz");
                break;
            case 4:
                /**
                 * 高招周
                 */
                cacheAll.put("GZLB", new ZhdBean("GZLB", openBeginTime, openEndTime));
                cacheAll.put("ZMJF", new ZhdBean("ZMJF", openBeginTime, openEndTime));
                cacheAll.put("XSDH", new ZhdBean("XSDH", openBeginTime, openEndTime));
                cacheAll.put("LDCF", new ZhdBean("LDCF", openBeginTime, openEndTime));
                cacheAll.put("ZMQY", new ZhdBean("ZMQY", openBeginTime, openEndTime));
                cacheAll.put("GZLBDM", new ZhdBean("GZLBDM", openBeginTime, openEndTime));
                cacheAll.put("CJXG2", new ZhdBean("CJXG2", openBeginTime, openEndTime));
                SeasonCache.getInstance().getBattleSeason().setZhdName("gzz");
                break;
        }
    }

    public Map<String, ZhdBean> getZhdAll() {
        return cacheAll;
    }

    public ZhdBean getZhd(String zhdName) {
        return cacheAll.get(zhdName);
    }

}
