package game.module.friend.dao;

import game.GameServer;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendRequest;
import game.module.friend.logic.FriendManager;

public class FriendDaoHelper {

    public static void asyncInsertFriendBean(final FriendBean playerFriendBeans) {
        GameServer.executorService.execute(() -> FriendDao.getInstance().addFriendBean(playerFriendBeans));
    }

    public static void asyncRemoveFriendBean(int affairId) {
        GameServer.executorService.execute(() -> FriendDao.getInstance().removeFriend(affairId));
    }

    public static void asyncRemoveFriendBean(int playerId, int friendId) {
        GameServer.executorService.execute(() -> FriendDao.getInstance().removeFriend(playerId,friendId));
    }

    public static void asyncInsertFriendRequest(final FriendRequest playerFriendRequests) {
        GameServer.executorService.execute(() -> FriendDao.getInstance().addFriendRequest(playerFriendRequests));
    }

    public static void asyncRemoveFriendRequest(int friendRequestId) {
        GameServer.executorService.execute(() -> FriendDao.getInstance().removeFriendRequest(friendRequestId));
    }

    public static void asyncRemoveFriendRequest(int playerId, int friendId) {
        GameServer.executorService.execute(() -> FriendDao.getInstance().removeFriendRequest(playerId, friendId));
    }

    public static void asyncAddFriendRequestCheck(int friendId, int requestPlayerId) {
        GameServer.executorService.execute(() -> {
            boolean friendRequestExist = FriendDao.getInstance().checkFriendRequestExist(friendId, requestPlayerId);
            if(!friendRequestExist){
                FriendRequest friendRequest = new FriendRequest();
                friendRequest.setPlayerId(friendId);
                friendRequest.setRequestPlayerId(requestPlayerId);
                FriendDao.getInstance().addFriendRequest(friendRequest);
            }
        });
    }

    public static void asyncAddFriendCheck(int playerId, int friendId) {
        GameServer.executorService.execute(() -> {
            boolean friendExist = FriendDao.getInstance().checkFriendExist(friendId, friendId);
            if(!friendExist){
                FriendBean friendBean = FriendManager.getInstance().createFriendBean(playerId,friendId);
                FriendDao.getInstance().addFriendBean(friendBean);
            }
        });
    }
}
