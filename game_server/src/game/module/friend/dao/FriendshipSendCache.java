package game.module.friend.dao;

import game.module.friend.bean.FriendHeartSend;
import lion.common.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FriendshipSendCache {

    private static Logger logger = LoggerFactory.getLogger(FriendshipSendCache.class);

    static class SingletonHolder {
        static FriendshipSendCache instance = new FriendshipSendCache();
    }

    public static FriendshipSendCache getInstance() {
        return SingletonHolder.instance;
    }

    private final Map<String, FriendHeartSend.HeartSendItem> friendHeartSendCache = new ConcurrentHashMap<>();

    public void loadFromDb() {
        logger.info("friend heart send loadFromDb!");
        FriendHeartSend friendHeartSend = FriendDao.getInstance().getFriendHeartSend();
        if (friendHeartSend != null) {
            friendHeartSendCache.putAll(friendHeartSend.getHearSendInfo());
        }
    }

    public void saveToDb() {
        logger.info("friend heart send saveToDb!");
        FriendHeartSend friendHeartSend = new FriendHeartSend();
        friendHeartSend.setHearSendInfo(friendHeartSendCache);
        FriendDao.getInstance().updateFriendHeartSend(friendHeartSend);
    }

    public FriendHeartSend.HeartSendItem getFriendHeartSend(int playerId, int friendId) {
        String mykey = playerId + StringConstants.SEPARATOR_HENG + friendId;
        return friendHeartSendCache.get(mykey);
    }

    public void addFriendHeartSend(int playerId, int friendId) {
        String mykey = playerId + StringConstants.SEPARATOR_HENG + friendId;
        FriendHeartSend.HeartSendItem sendItem = new FriendHeartSend.HeartSendItem();
        sendItem.setGet(false);
        sendItem.setSendTime(new Date());
        friendHeartSendCache.put(mykey, sendItem);
    }

    public void removeFriendHeartSend(int playerId, int friendId) {
        String mykey = playerId + StringConstants.SEPARATOR_HENG + friendId;
        friendHeartSendCache.remove(mykey);
    }

}
