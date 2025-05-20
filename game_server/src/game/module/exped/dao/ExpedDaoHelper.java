package game.module.exped.dao;

import game.GameServer;
import game.module.exped.bean.ExpedBean;

public class ExpedDaoHelper {

    public static void asyncInsertExped(final ExpedBean playerExpeds) {
        GameServer.executorService.execute(() -> ExpedDao.getInstance().addExpedBean(playerExpeds));
    }

    public static void asyncUpdateExped(final ExpedBean playerExpeds) {
        GameServer.executorService.execute(() -> ExpedDao.getInstance().updateExped(playerExpeds));
    }

    public static void asyncRemoveExped(int expedId) {
        GameServer.executorService.execute(() -> ExpedDao.getInstance().removeExped(expedId));
    }

}
