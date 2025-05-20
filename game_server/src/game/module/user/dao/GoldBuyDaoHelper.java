package game.module.user.dao;

import game.GameServer;
import game.module.user.bean.GoldBuy;

public class GoldBuyDaoHelper {

    public static void asyncInsertGoldBuy(final GoldBuy playerGoldBuys) {
        GameServer.executorService.execute(() -> GoldBuyDao.getInstance().addGoldBuy(playerGoldBuys));
    }

    public static void asyncUpdateGoldBuy(final GoldBuy playerGoldBuys) {
        GameServer.executorService.execute(() -> GoldBuyDao.getInstance().updateGoldBuy(playerGoldBuys));
    }

    public static void asyncRemoveGoldBuy(int affairId) {
        GameServer.executorService.execute(() -> GoldBuyDao.getInstance().removeGoldBuy(affairId));
    }

}
