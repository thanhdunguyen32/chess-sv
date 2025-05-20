package game.module.kingpvp.dao;

import game.GameServer;
import game.module.kingpvp.bean.KingPvp;

public class KingPvpDaoHelper {

    public static void asyncInsertKingPvp(final KingPvp playerKingPvps) {
        GameServer.executorService.execute(() -> KingPvpDao.getInstance().addKingPvp(playerKingPvps));
    }

    public static void asyncUpdateKingPvp(final KingPvp playerKingPvps) {
        GameServer.executorService.execute(() -> KingPvpDao.getInstance().updateKingPvp(playerKingPvps));
    }

    public static void asyncRemoveKingPvp(int manorId) {
        GameServer.executorService.execute(() -> KingPvpDao.getInstance().removeKingPvp(manorId));
    }

}
