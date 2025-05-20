package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendRequest;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendDaoHelper;
import game.module.friend.logic.FriendManager;
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
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendApplyHandle.id, accessLimit = 200)
public class FriendApplyHandleProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendApplyHandleProcessor.class);

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
        WsMessageFriend.C2SFriendApplyHandle reqmsg = WsMessageFriend.C2SFriendApplyHandle.parse(request);
        int playerId = playingRole.getId();
        logger.info("friend apply handle!player={},req={}", playerId, reqmsg);
        //是否已经为好友
        int[] friend_ids = reqmsg.role_ids;
        boolean is_agree = reqmsg.is_agree;
        if (friend_ids == null || friend_ids.length == 0) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendApplyHandle.msgCode, 1401);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //do
        Map<Integer, FriendRequest> friendRequests = FriendCache.getInstance().getFriendRequests(playerId);
        if (is_agree) {
            //be friend
            Map<Integer, FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
            for (int aFriendId : friend_ids) {
                //request not exist
                if (!friendRequests.containsKey(aFriendId)) {
                    continue;
                }
                //remove request
                FriendDaoHelper.asyncRemoveFriendRequest(friendRequests.get(aFriendId).getId());
                friendRequests.remove(aFriendId);
                //be friend
                if (!friends.containsKey(aFriendId)) {
                    FriendBean friendBean = FriendManager.getInstance().createFriendBean(playerId, aFriendId);
                    friends.put(aFriendId, friendBean);
                    FriendDaoHelper.asyncInsertFriendBean(friendBean);
                }
                //add other friend
                Map<Integer, FriendBean> otherFriends = FriendCache.getInstance().getFriends(aFriendId);
                if (otherFriends != null) {
                    if (!otherFriends.containsKey(playerId)) {
                        FriendBean friendBean = FriendManager.getInstance().createFriendBean(aFriendId, playerId);
                        otherFriends.put(playerId, friendBean);
                        FriendDaoHelper.asyncInsertFriendBean(friendBean);
                    }
                } else {
                    FriendDaoHelper.asyncAddFriendCheck(aFriendId, playerId);
                }
            }
        } else {
            //delete
            for (int aFriendId : friend_ids) {
                friendRequests.remove(aFriendId);
                FriendDaoHelper.asyncRemoveFriendRequest(playerId, aFriendId);
            }
        }
        //ret
        WsMessageFriend.S2CFriendApplyHandle respmsg = new WsMessageFriend.S2CFriendApplyHandle();
        respmsg.role_ids = Arrays.asList(ArrayUtils.toObject(friend_ids));
        respmsg.is_agree = is_agree;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
