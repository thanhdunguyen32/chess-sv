package game.module.chapter.dao;

import game.GameServer;
import game.module.chapter.bean.BattleFormation;

public class BattleFormationDaoHelper {

    public static void asyncInsertBattleFormation(final BattleFormation playerBattleFormations) {
        GameServer.executorService.execute(() -> BattleFormationDao.getInstance().addBattleFormation(playerBattleFormations));
    }

    public static void asyncUpdateBattleFormation(final BattleFormation playerBattleFormations) {
        GameServer.executorService.execute(() -> BattleFormationDao.getInstance().updateBattleFormation(playerBattleFormations));
    }

    public static void asyncRemoveBattleFormation(int affairId) {
        GameServer.executorService.execute(() -> BattleFormationDao.getInstance().removeBattleFormation(affairId));
    }

}
