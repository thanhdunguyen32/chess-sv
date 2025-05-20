package game.module.activity.dao;

import game.GameServer;
import game.module.activity.bean.ActCxry;

public class ActCxryDaoHelper {

    public static void asyncInsertActCxry(final ActCxry playerActCxrys) {
        GameServer.executorService.execute(() -> ActCxryDao.getInstance().addActCxry(playerActCxrys));
    }

    public static void asyncUpdateActCxry(final ActCxry playerActCxrys) {
        GameServer.executorService.execute(() -> ActCxryDao.getInstance().updateActCxry(playerActCxrys));
    }

    public static void asyncRemoveActCxry(int affairId) {
        GameServer.executorService.execute(() -> ActCxryDao.getInstance().removeActCxry(affairId));
    }

    public static void asyncTruncateCxry() {
        GameServer.executorService.execute(() -> ActCxryDao.getInstance().truncateCxry());
    }
}
