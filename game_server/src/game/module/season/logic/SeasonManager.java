package game.module.season.logic;

import game.GameServer;
import game.module.activity.dao.ActivityWeekCache;
import game.module.activity_month.logic.ActivityMonthManager;
import game.module.kingpvp.logic.KingPvpManager;
import game.module.season.bean.BattleSeason;
import game.module.season.dao.SeasonCache;
import game.module.worldboss.logic.WorldBossManager;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author HeXuhui
 */
public class SeasonManager {

    private static Logger logger = LoggerFactory.getLogger(SeasonManager.class);

    static class SingletonHolder {
        static SeasonManager instance = new SeasonManager();
    }

    public static SeasonManager getInstance() {
        return SingletonHolder.instance;
    }

    //    private final List<List<Integer>> SEASON_ADDONS = new ArrayList<>(Arrays.asList(Arrays.asList(50, 50, 50, 50), Arrays.asList(50, 50, 50, 20),
//            Arrays.asList(50, 50, 50, 20), Arrays.asList(50, 50, 50, 20)));
    private final List<Integer> SEASON_ADDONS = new ArrayList<>(Arrays.asList(50, 50, 50, 20));

    public BattleSeason createBattleSeason() {
        BattleSeason battleSeason = new BattleSeason();
        battleSeason.setSeason(1);
        battleSeason.setAddonVals(SEASON_ADDONS);
        battleSeason.setYear(5);
        battleSeason.setPos(generatePos());
        battleSeason.setMonthEndTime(ActivityMonthManager.getInstance().refreshMonthEndTime());
        Date etime = generateETime();
        battleSeason.setEtime(etime);
        return battleSeason;
    }

    public void updateSeason(BattleSeason battleSeason) {
        battleSeason.setPos(generatePos());
        Date etime = generateETime();
        battleSeason.setEtime(etime);
        int season = battleSeason.getSeason();
        int oldSeason = season;
        season++;
        if (season >= 5) {
            season = 1;
        }
        battleSeason.setSeason(season);
        battleSeason.setAddonVals(SEASON_ADDONS);
        //
        WorldBossManager.getInstance().resetWorldBoss();
        ActivityWeekCache.getInstance().refreshZhdTime(battleSeason);
        //
        KingPvpManager.getInstance().kpSeasonEnd(oldSeason);
        KingPvpManager.getInstance().kpSeasonStart(oldSeason);
    }

    private Date generateETime() {
        Date now = new Date();
        Date openTime = GameServer.getInstance().getServerConfig().getOpenTime();
        int diffDay = (int) ((now.getTime() - openTime.getTime()) / 24 / 3600000);
        int diffWeek = diffDay / 7;
        return DateUtils.addDays(openTime, (diffWeek + 1) * 7);
    }

    private List<Integer> generatePos() {
        int pos1 = RandomUtils.nextInt(0, 32);
        int pos2 = pos1;
        while (pos2 == pos1) {
            pos2 = RandomUtils.nextInt(0, 32);
        }
        return Arrays.asList(pos1, pos2);
    }

    public WsMessageBase.IOBattleRecordSeason buildIoBattleRecordSeason() {
        WsMessageBase.IOBattleRecordSeason ioBattleRecordSeason = new WsMessageBase.IOBattleRecordSeason();
        BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
        ioBattleRecordSeason.season = battleSeason.getSeason();
        ioBattleRecordSeason.pos = battleSeason.getPos();
        ioBattleRecordSeason.left = battleSeason.getAddonVals().get(0);
        ioBattleRecordSeason.right = battleSeason.getAddonVals().get(2);
        return ioBattleRecordSeason;
    }

    public int getBuyCoinAddon() {
        return getModuleAddon(4);
    }

    public int getHangAddon() {
        return getModuleAddon(3);
    }

    public int getBigBattleAddon() {
        return getModuleAddon(2);
    }

    public int getLegionAddon() {
        return getModuleAddon(1);
    }

    public int getModuleAddon(int targetSeason) {
        BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
        if (battleSeason == null) {
            return 0;
        }
        if (battleSeason.getSeason().equals(targetSeason)) {
            return battleSeason.getAddonVals().get(targetSeason - 1);
        }
        return 0;
    }

}
