package game.module.friend.dao;

import game.GameServer;
import game.module.friend.bean.FriendExplore;

public class FriendExploreDaoHelper {

    public static void asyncInsertFriendExplore(final FriendExplore playerFriendExplores) {
        GameServer.executorService.execute(() -> FriendExploreDao.getInstance().addFriendExplore(playerFriendExplores));
    }

    public static void asyncUpdateFriendExplore(final FriendExplore playerFriendExplores) {
        GameServer.executorService.execute(() -> FriendExploreDao.getInstance().updateFriendExplore(playerFriendExplores));
    }

    public static void asyncRemoveFriendExplore(int manorId) {
        GameServer.executorService.execute(() -> FriendExploreDao.getInstance().removeFriendExplore(manorId));
    }

}
