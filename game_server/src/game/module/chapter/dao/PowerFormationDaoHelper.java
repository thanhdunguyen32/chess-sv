package game.module.chapter.dao;

import game.GameServer;
import game.module.chapter.bean.PowerFormation;

public class PowerFormationDaoHelper {

    public static void asyncInsertPowerFormation(final PowerFormation powerFormation) {
        GameServer.executorService.execute(() -> PowerFormationDao.getInstance().addPowerFormation(powerFormation));
    }

    public static void asyncUpdatePowerFormation(final PowerFormation powerFormation) {
        GameServer.executorService.execute(() -> PowerFormationDao.getInstance().updatePowerFormation(powerFormation));
    }

    public static void asyncRemovePowerFormation(int manorId) {
        GameServer.executorService.execute(() -> PowerFormationDao.getInstance().removePowerFormation(manorId));
    }

}
