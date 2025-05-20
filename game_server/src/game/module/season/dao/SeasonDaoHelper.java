package game.module.season.dao;

import game.GameServer;
import game.module.season.bean.BattleSeason;

import java.util.Date;

public class SeasonDaoHelper {

    public static void asyncInsertBattleSeason(final BattleSeason battleSeasonBean) {
        GameServer.executorService.execute(() -> SeasonDao.getInstance().addBattleSeason(battleSeasonBean));
    }

    public static void asyncUpdateBattleSeason(final BattleSeason battleSeasonBean) {
        GameServer.executorService.execute(() -> SeasonDao.getInstance().updateBattleSeason(battleSeasonBean));
    }

    public static void asyncRemoveBattleSeason() {
        GameServer.executorService.execute(() -> SeasonDao.getInstance().removeBattleSeason());
    }

    public static void asyncUpdateMonthEndTime(Date monthEndTime) {
        GameServer.executorService.execute(() -> SeasonDao.getInstance().updateMonthETime(monthEndTime));
    }
}
