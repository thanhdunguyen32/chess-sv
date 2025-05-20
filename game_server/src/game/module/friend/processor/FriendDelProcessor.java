package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendBean;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendDaoHelper;
import game.module.friend.dao.FriendshipSendCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageFriend;
import ws.WsMessageHall;

import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendDel.id, accessLimit = 200)
public class FriendDelProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendDelProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageFriend.C2SFriendDel reqmsg = WsMessageFriend.C2SFriendDel.parse(request);
        int playerId = playingRole.getId();
        logger.info("friend del!player={},req={}", playerId, reqmsg);
        //是否已经为好友
        int friend_id = reqmsg.role_id;
        Map<Integer, FriendBean> friendsCache = FriendCache.getInstance().getFriends(playerId);
        if (!friendsCache.containsKey(friend_id)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendDel.msgCode, 1404);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //do
        FriendBean friendBean = friendsCache.get(friend_id);
        friendsCache.remove(friend_id);
        FriendDaoHelper.asyncRemoveFriendBean(friendBean.getId());
        //remove other
        Map<Integer, FriendBean> targetFriendsCache = FriendCache.getInstance().getFriends(friend_id);
        if (targetFriendsCache != null) {
            targetFriendsCache.remove(playerId);
        }
        FriendDaoHelper.asyncRemoveFriendBean(friend_id, playerId);
        //remove friendship send
        FriendshipSendCache.getInstance().removeFriendHeartSend(playerId, friend_id);
        FriendshipSendCache.getInstance().removeFriendHeartSend(friend_id, playerId);
        //ret
        WsMessageFriend.S2CFriendDel respmsg = new WsMessageFriend.S2CFriendDel(friend_id);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
