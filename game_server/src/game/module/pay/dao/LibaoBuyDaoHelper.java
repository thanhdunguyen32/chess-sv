package game.module.pay.dao;

import game.GameServer;
import game.module.pay.bean.LibaoBuy;

public class LibaoBuyDaoHelper {

    public static void asyncInsertAffair(final LibaoBuy libaoBuy) {
        GameServer.executorService.execute(() -> LibaoBuyDao.getInstance().addLibaoBuy(libaoBuy));
    }

    public static void asyncUpdateAffair(final LibaoBuy libaoBuy) {
        GameServer.executorService.execute(() -> LibaoBuyDao.getInstance().updateLibaoBuy(libaoBuy));
    }

    public static void asyncRemoveAffair(int affairId) {
        GameServer.executorService.execute(() -> LibaoBuyDao.getInstance().removeLibaoBuy(affairId));
    }

}
