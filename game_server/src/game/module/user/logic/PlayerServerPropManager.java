package game.module.user.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.friend.logic.FriendConstants;
import game.module.item.logic.ItemConstants;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerServerPropCache;
import game.module.user.dao.PlayerServerPropDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.Map;

public class PlayerServerPropManager {

    private static Logger logger = LoggerFactory.getLogger(PlayerServerPropManager.class);

    static class SingletonHolder {

        static PlayerServerPropManager instance = new PlayerServerPropManager();
    }

    public static PlayerServerPropManager getInstance() {
        return SingletonHolder.instance;
    }

    public int getTower(int playerId) {
        Map<Integer, PlayerProp> playerServerProps = PlayerServerPropCache.getInstance().getPlayerServerProp(playerId);
        int retTower = 1;
        if (playerServerProps.containsKey(ItemConstants.TOWER_ID)) {
            retTower = playerServerProps.get(ItemConstants.TOWER_ID).getCount();
        }
        return retTower;
    }

    public void changeServerProp(PlayingRole playingRole, int gsid, int changeCount) {
        logger.info("changeServerProp,player={},gsid={},count={}", playingRole.getId(), gsid, changeCount);
        int playerId = playingRole.getId();
        Map<Integer, PlayerProp> playerServerProps = PlayerServerPropCache.getInstance().getPlayerServerProp(playerId);
        int existCount = 0;
        if (playerServerProps.containsKey(gsid)) {
            existCount = playerServerProps.get(gsid).getCount();
        }
        int nowCount = existCount + changeCount;
        nowCount = Math.max(nowCount, 0);
        if (nowCount > 0) {
            //change val
            PlayerProp playerProp = playerServerProps.get(gsid);
            if (playerProp == null) {
                playerProp = new PlayerProp();
                playerProp.setGsid(gsid);
                playerProp.setCount(nowCount);
                playerProp.setPlayerId(playerId);
                PlayerProp finalPlayerProp = playerProp;
                GameServer.executorService.execute(() -> PlayerServerPropDao.getInstance().addPlayerServerProp(finalPlayerProp));
                playerServerProps.put(gsid, playerProp);
            } else {
                playerProp.setCount(nowCount);
                PlayerProp finalPlayerProp1 = playerProp;
                GameServer.executorService.execute(() -> PlayerServerPropDao.getInstance().updatePlayerServerProp(finalPlayerProp1));
            }
        } else {
            PlayerProp playerProp = playerServerProps.get(gsid);
            if (playerProp != null) {
                playerServerProps.remove(gsid);
                GameServer.executorService.execute(() -> PlayerServerPropDao.getInstance().removePlayerServerProp(playerProp.getId()));
            }
        }
        // push item change
        WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(gsid, nowCount);
        if (gsid == ItemConstants.FRIEND_BOSS_ATTACK_COUNT_MARK) {
            pushMsg.count = FriendConstants.FRIEND_BOSS_ATTACK_COUNT - nowCount;
        }
        playingRole.write(pushMsg.build(playingRole.alloc()));
    }

    public void setServerProp(PlayingRole playingRole, int gsid, int nowCount, boolean isPush) {
        int playerId = playingRole.getId();
        logger.info("set server prop,player={},gsid={},count={}", playerId, gsid, nowCount);
        Map<Integer, PlayerProp> playerServerProps = PlayerServerPropCache.getInstance().getPlayerServerProp(playerId);
        nowCount = Math.max(nowCount, 0);
        if (nowCount > 0) {
            //change val
            PlayerProp playerProp = playerServerProps.get(gsid);
            if (playerProp == null) {
                playerProp = new PlayerProp();
                playerProp.setGsid(gsid);
                playerProp.setCount(nowCount);
                playerProp.setPlayerId(playerId);
                PlayerProp finalPlayerProp = playerProp;
                GameServer.executorService.execute(() -> PlayerServerPropDao.getInstance().addPlayerServerProp(finalPlayerProp));
                playerServerProps.put(gsid, playerProp);
            } else {
                playerProp.setCount(nowCount);
                PlayerProp finalPlayerProp1 = playerProp;
                GameServer.executorService.execute(() -> PlayerServerPropDao.getInstance().updatePlayerServerProp(finalPlayerProp1));
            }
        } else {
            PlayerProp playerProp = playerServerProps.get(gsid);
            if (playerProp != null) {
                playerProp.setCount(0);
                playerServerProps.remove(gsid);
                GameServer.executorService.execute(() -> PlayerServerPropDao.getInstance().removePlayerServerProp(playerProp.getId()));
            }
        }
        if (isPush) {
            // push item change
            WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(gsid, nowCount);
            if (gsid == ItemConstants.FRIEND_BOSS_ATTACK_COUNT_MARK) {
                pushMsg.count = FriendConstants.FRIEND_BOSS_ATTACK_COUNT - nowCount;
            }
            playingRole.write(pushMsg.build(playingRole.alloc()));
        }
    }

    public int getServerPropCount(int playerId, int gsid) {
        Map<Integer, PlayerProp> playerServerProps = PlayerServerPropCache.getInstance().getPlayerServerProp(playerId);
        PlayerProp playerProp = playerServerProps.get(gsid);
        if (playerProp != null) {
            return playerProp.getCount();
        }
        return 0;
    }

}
