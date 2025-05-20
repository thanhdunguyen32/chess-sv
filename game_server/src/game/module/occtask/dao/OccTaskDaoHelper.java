package game.module.occtask.dao;

import game.GameServer;
import game.module.occtask.bean.OccTask;

public class OccTaskDaoHelper {

    public static void asyncInsertOccTask(final OccTask playerOccTasks) {
        GameServer.executorService.execute(() -> OccTaskDao.getInstance().addOccTask(playerOccTasks));
    }

    public static void asyncUpdateOccTask(final OccTask playerOccTasks) {
        GameServer.executorService.execute(() -> OccTaskDao.getInstance().updateOccTask(playerOccTasks));
    }

    public static void asyncRemoveOccTask(int affairId) {
        GameServer.executorService.execute(() -> OccTaskDao.getInstance().removeOccTask(affairId));
    }

}
