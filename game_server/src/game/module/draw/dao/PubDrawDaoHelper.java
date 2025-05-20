package game.module.draw.dao;

import game.GameServer;
import game.module.draw.bean.PubDraw;

public class PubDrawDaoHelper {

    public static void asyncInsertPubDraw(final PubDraw playerPubDraws) {
        GameServer.executorService.execute(() -> PubDrawDao.getInstance().addPubDraw(playerPubDraws));
    }

    public static void asyncUpdatePubDraw(final PubDraw playerPubDraws) {
        GameServer.executorService.execute(() -> PubDrawDao.getInstance().updatePubDraw(playerPubDraws));
    }

    public static void asyncRemovePubDraw(int affairId) {
        GameServer.executorService.execute(() -> PubDrawDao.getInstance().removePubDraw(affairId));
    }

}
