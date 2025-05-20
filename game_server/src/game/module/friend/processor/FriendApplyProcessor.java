package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendRequest;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendDaoHelper;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageFriend;
import ws.WsMessageHall;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendApply.id, accessLimit = 200)
public class FriendApplyProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendApplyProcessor.class);

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
        WsMessageFriend.C2SFriendApply reqmsg = WsMessageFriend.C2SFriendApply.parse(request);
        int playerId = playingRole.getId();
        logger.info("friend apply!player={},req={}", playerId, reqmsg);
        //是否已经为好友
        int[] friend_ids = reqmsg.role_ids;
        if (friend_ids == null || friend_ids.length == 0) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendApply.msgCode, 1401);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //
        Map<Integer, FriendBean> friendsCache = FriendCache.getInstance().getFriends(playerId);
        for (int friendId : friend_ids) {
            if (!PlayerOfflineManager.getInstance().checkExist(friendId)) {
                WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendApply.msgCode, 1402);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
            if (friendsCache.containsKey(friendId)) {
                WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendApply.msgCode, 1403);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //add friend request
        for (int friendId : friend_ids) {
            Map<Integer,FriendRequest> friendRequests = FriendCache.getInstance().getFriendRequests(friendId);
            if (friendRequests != null) {
                boolean findExist = false;
                for (FriendRequest friendRequest : friendRequests.values()) {
                    if (friendRequest.getRequestPlayerId() == playerId) {
                        findExist = true;
                    }
                }
                if (!findExist) {
                    FriendRequest friendRequest = new FriendRequest();
                    friendRequest.setPlayerId(friendId);
                    friendRequest.setRequestPlayerId(playerId);
                    friendRequests.put(playerId, friendRequest);
                    FriendDaoHelper.asyncInsertFriendRequest(friendRequest);
                }
            } else {
                FriendDaoHelper.asyncAddFriendRequestCheck(friendId, playerId);
            }
        }
        //ret
        WsMessageFriend.S2CFriendApply respmsg = new WsMessageFriend.S2CFriendApply();
        respmsg.role_ids = Arrays.asList(ArrayUtils.toObject(friend_ids));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
