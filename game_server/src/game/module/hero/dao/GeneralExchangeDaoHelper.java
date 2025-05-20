package game.module.hero.dao;

import game.GameServer;
import game.module.hero.bean.GeneralExchange;

public class GeneralExchangeDaoHelper {

    public static void asyncInsertGeneralExchange(final GeneralExchange playerGeneralExchanges) {
        GameServer.executorService.execute(() -> GeneralExchangeDao.getInstance().addGeneralExchange(playerGeneralExchanges));
    }

    public static void asyncUpdateGeneralExchange(final GeneralExchange playerGeneralExchanges) {
        GameServer.executorService.execute(() -> GeneralExchangeDao.getInstance().updateGeneralExchange(playerGeneralExchanges));
    }

    public static void asyncRemoveGeneralExchange(int affairId) {
        GameServer.executorService.execute(() -> GeneralExchangeDao.getInstance().removeGeneralExchange(affairId));
    }

}
