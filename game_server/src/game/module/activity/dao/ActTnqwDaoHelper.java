package game.module.activity.dao;

import game.GameServer;
import game.module.activity.bean.ActTnqw;

public class ActTnqwDaoHelper {

    public static void asyncInsertActTnqw(final ActTnqw playerActTnqws) {
        GameServer.executorService.execute(() -> ActTnqwDao.getInstance().addActTnqw(playerActTnqws));
    }

    public static void asyncUpdateActTnqw(final ActTnqw playerActTnqws) {
        GameServer.executorService.execute(() -> ActTnqwDao.getInstance().updateActTnqw(playerActTnqws));
    }

    public static void asyncRemoveActTnqw(int affairId) {
        GameServer.executorService.execute(() -> ActTnqwDao.getInstance().removeActTnqw(affairId));
    }

    public static void asyncTruncateTnqw() {
        GameServer.executorService.execute(() -> ActTnqwDao.getInstance().truncateTnqw());
    }
}
