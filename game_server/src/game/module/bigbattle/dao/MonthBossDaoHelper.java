package game.module.bigbattle.dao;

import game.GameServer;
import game.module.bigbattle.bean.MonthBoss;

public class MonthBossDaoHelper {

    public static void asyncInsertMonthBoss(final MonthBoss playerMonthBosss) {
        GameServer.executorService.execute(() -> MonthBossDao.getInstance().addMonthBoss(playerMonthBosss));
    }

    public static void asyncUpdateMonthBoss(final MonthBoss playerMonthBosss) {
        GameServer.executorService.execute(() -> MonthBossDao.getInstance().updateMonthBoss(playerMonthBosss));
    }

    public static void asyncRemoveMonthBoss(int affairId) {
        GameServer.executorService.execute(() -> MonthBossDao.getInstance().removeMonthBoss(affairId));
    }

}
