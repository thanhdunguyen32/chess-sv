package game.module.manor.dao;

import game.GameServer;
import game.module.manor.bean.SurrenderPersuade;

public class SurrenderPersuadeDaoHelper {

    public static void asyncInsertSurrenderPersuade(final SurrenderPersuade playerSurrenderPersuades) {
        GameServer.executorService.execute(() -> SurrenderPersuadeDao.getInstance().addSurrenderPersuade(playerSurrenderPersuades));
    }

    public static void asyncUpdateSurrenderPersuade(final SurrenderPersuade playerSurrenderPersuades) {
        GameServer.executorService.execute(() -> SurrenderPersuadeDao.getInstance().updateSurrenderPersuade(playerSurrenderPersuades));
    }

    public static void asyncRemoveSurrenderPersuade(int manorId) {
        GameServer.executorService.execute(() -> SurrenderPersuadeDao.getInstance().removeSurrenderPersuade(manorId));
    }

}
