package game.module.affair.dao;

import game.GameServer;
import game.module.affair.bean.PlayerAffairs;

public class AffairDaoHelper {

    public static void asyncInsertAffair(final PlayerAffairs playerAffairs) {
        GameServer.executorService.execute(() -> AffairDao.getInstance().addPlayerAffairs(playerAffairs));
    }

    public static void asyncUpdateAffair(final PlayerAffairs playerAffairs) {
        GameServer.executorService.execute(() -> AffairDao.getInstance().updateAffair(playerAffairs));
    }

    public static void asyncRemoveAffair(int affairId) {
        GameServer.executorService.execute(() -> AffairDao.getInstance().removeAffair(affairId));
    }

}
