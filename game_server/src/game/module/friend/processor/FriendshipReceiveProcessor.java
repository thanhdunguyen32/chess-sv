package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendHeartSend;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendshipSendCache;
import game.module.friend.logic.FriendConstants;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageFriend;
import ws.WsMessageHall;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendshipReceive.id, accessLimit = 200)
public class FriendshipReceiveProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendshipReceiveProcessor.class);

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
        WsMessageFriend.C2SFriendshipReceive reqmsg = WsMessageFriend.C2SFriendshipReceive.parse(request);
        int playerId = playingRole.getId();
        logger.info("friendship receive!player={},req={}", playerId, reqmsg);
        //是否已经为好友
        int friend_id = reqmsg.role_id;
        Map<Integer, FriendBean> friendsCache = FriendCache.getInstance().getFriends(playerId);
        if (!friendsCache.containsKey(friend_id)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendshipReceive.msgCode, 1404);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //is get friendship
        Date now = new Date();
        FriendHeartSend.HeartSendItem friendHeartSend = FriendshipSendCache.getInstance().getFriendHeartSend(friend_id, playerId);
        if (friendHeartSend == null || !DateUtils.isSameDay(friendHeartSend.getSendTime(), now) || friendHeartSend.getGet()) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendshipReceive.msgCode, 1405);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //do
        friendHeartSend.setGet(true);
        //award
        AwardUtils.changeRes(playingRole, ItemConstants.FRIENDSHIP_ICON, 1, LogConstants.MODULE_FRIEND);
        //ret
        WsMessageFriend.S2CFriendshipReceive respmsg = new WsMessageFriend.S2CFriendshipReceive();
        respmsg.rewards = Collections.singletonList(new WsMessageBase.IORewardItem(ItemConstants.FRIENDSHIP_ICON, 1));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
