package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendBean;
import game.module.friend.dao.FriendCache;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageManor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageManor.C2SManorFriendList.id, accessLimit = 200)
public class ManorFriendListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorFriendListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("manor friend list,player={}", playerId);
        //ret
        WsMessageManor.S2CManorFriendList respmsg = new WsMessageManor.S2CManorFriendList();
        Map<Integer, FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
        respmsg.list = new ArrayList<>(friends.size());
        for (FriendBean fb : friends.values()) {
            Integer friendId = fb.getFriendId();
            PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance().getPlayerOfflineCache(friendId);
            respmsg.list.add(new WsMessageBase.IOManorFriend(playerOfflineCache.getIconid(), playerOfflineCache.getFrameid(), playerOfflineCache.getHeadid(),
                    playerOfflineCache.getName(), friendId, playerOfflineCache.getLevel(), playerOfflineCache.getPower()));
        }
        //sort
        respmsg.list.sort(Comparator.comparingInt(f -> -f.power));
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
