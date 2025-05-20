package game.module.friend.dao;

import game.module.friend.bean.FriendBoss;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FriendBossCache {

    private static Logger logger = LoggerFactory.getLogger(FriendBossCache.class);

    static class SingletonHolder {
        static FriendBossCache instance = new FriendBossCache();
    }

    public static FriendBossCache getInstance() {
        return SingletonHolder.instance;
    }

    private final Map<Integer, FriendBoss> friendBossCache = new ConcurrentHashMap<>();

    public void loadFromDb() {
        logger.info("friendBoss loadFromDb!");
        List<FriendBoss> friendBosses = FriendBossDao.getInstance().getFriendBoss();
        friendBossCache.clear();
        for (FriendBoss friendBossBean : friendBosses) {
            friendBossCache.put(friendBossBean.getPlayerId(), friendBossBean);
        }
    }

    public FriendBoss getFriendBoss(int playerId) {
        return friendBossCache.get(playerId);
    }

    public void addFriendBoss(FriendBoss friendBossBean) {
        friendBossCache.put(friendBossBean.getPlayerId(), friendBossBean);
    }

    public void removeFriendBoss(int playerId) {
        friendBossCache.remove(playerId);
    }

    public Collection<FriendBoss> getFriendBossAll() {
        return friendBossCache.values();
    }

}
