package game.module.pvp.dao;

import game.GameServer;
import game.module.pvp.bean.PvpBean;

public class PvpDaoHelper {

    public static void asyncInsertPvpBean(final PvpBean pvpBean) {
        GameServer.executorService.execute(() -> PvpDao.getInstance().addPvpBean(pvpBean));
    }

    public static void asyncUpdatePvpBean(final PvpBean pvpBean) {
        GameServer.executorService.execute(() -> PvpDao.getInstance().updatePvpBean(pvpBean));
    }

    public static void asyncRemovePvpBean() {
        GameServer.executorService.execute(() -> PvpDao.getInstance().removePvpBean());
    }

}
