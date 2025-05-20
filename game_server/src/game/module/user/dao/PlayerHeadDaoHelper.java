package game.module.user.dao;

import game.GameServer;
import game.module.user.bean.PlayerHead;

public class PlayerHeadDaoHelper {

    public static void asyncInsertPlayerHead(final PlayerHead playerPlayerHeads) {
        GameServer.executorService.execute(() -> PlayerHeadDao.getInstance().addPlayerHead(playerPlayerHeads));
    }

    public static void asyncUpdatePlayerHead(int playerHeadId,String headIconsStr,String headFramesStr,String headImagesStr) {
        GameServer.executorService.execute(() -> PlayerHeadDao.getInstance().updatePlayerHead(playerHeadId,headIconsStr,headFramesStr,headImagesStr));
    }

    public static void asyncRemovePlayerHead(int affairId) {
        GameServer.executorService.execute(() -> PlayerHeadDao.getInstance().removePlayerHead(affairId));
    }

}
