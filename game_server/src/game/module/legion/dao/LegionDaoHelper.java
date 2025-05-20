package game.module.legion.dao;

import game.GameServer;
import game.module.legion.bean.LegionBean;

public class LegionDaoHelper {

    public static void asyncInsertLegionBean(final LegionBean playerLegionBeans) {
        GameServer.executorService.execute(() -> LegionDao.getInstance().addLegionBean(playerLegionBeans));
    }

    public static void asyncUpdateLegionBean(final LegionBean playerLegionBeans) {
        GameServer.executorService.execute(() -> LegionDao.getInstance().updateLegion(playerLegionBeans));
    }

    public static void asyncRemoveLegionBean(int affairId) {
        GameServer.executorService.execute(() -> LegionDao.getInstance().removeLegion(affairId));
    }

}
