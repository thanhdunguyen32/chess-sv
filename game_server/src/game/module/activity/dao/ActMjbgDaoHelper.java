package game.module.activity.dao;

import game.GameServer;
import game.module.activity.bean.ActMjbg;

public class ActMjbgDaoHelper {

    public static void asyncInsertActMjbg(final ActMjbg playerActMjbgs) {
        GameServer.executorService.execute(() -> ActMjbgDao.getInstance().addActMjbg(playerActMjbgs));
    }

    public static void asyncUpdateActMjbg(final ActMjbg playerActMjbgs) {
        GameServer.executorService.execute(() -> ActMjbgDao.getInstance().updateActMjbg(playerActMjbgs));
    }

    public static void asyncRemoveActMjbg(int affairId) {
        GameServer.executorService.execute(() -> ActMjbgDao.getInstance().removeActMjbg(affairId));
    }

    public static void asyncTruncateMjbg() {
        GameServer.executorService.execute(() -> ActMjbgDao.getInstance().truncateMjbg());
    }
}
