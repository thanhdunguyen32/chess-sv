package game.module.friend.dao;

import game.GameServer;
import game.module.friend.bean.FriendBoss;

public class FriendBossDaoHelper {

    public static void asyncInsertFriendBoss(final FriendBoss playerFriendBosss) {
        GameServer.executorService.execute(() -> FriendBossDao.getInstance().addFriendBoss(playerFriendBosss));
    }

    public static void asyncUpdateFriendBoss(final FriendBoss playerFriendBosss) {
        GameServer.executorService.execute(() -> FriendBossDao.getInstance().updateFriendBoss(playerFriendBosss));
    }

    public static void asyncRemoveFriendBoss(int manorId) {
        GameServer.executorService.execute(() -> FriendBossDao.getInstance().removeFriendBoss(manorId));
    }

}
