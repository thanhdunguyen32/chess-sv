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
import game.module.item.logic.ItemDaoHelper;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.session.PlayerOnlineCacheMng;
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
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendshipGive.id, accessLimit = 200)
public class FriendshipGiveProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendshipGiveProcessor.class);

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
        WsMessageFriend.C2SFriendshipGive reqmsg = WsMessageFriend.C2SFriendshipGive.parse(request);
        int playerId = playingRole.getId();
        logger.info("friendship give!player={},req={}", playerId, reqmsg);
        //是否已经为好友
        int friend_id = reqmsg.role_id;
        Map<Integer, FriendBean> friendsCache = FriendCache.getInstance().getFriends(playerId);
        if (!friendsCache.containsKey(friend_id)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendshipGive.msgCode, 1404);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //has give
        Date now = new Date();
        FriendHeartSend.HeartSendItem friendHeartSend = FriendshipSendCache.getInstance().getFriendHeartSend(playerId, friend_id);
        if (friendHeartSend != null) {
            Date sendTime = friendHeartSend.getSendTime();
            if (DateUtils.isSameDay(sendTime, now)) {
                WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendshipGive.msgCode, 1512);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        if (friendHeartSend == null) {
            FriendshipSendCache.getInstance().addFriendHeartSend(playerId, friend_id);
        } else {
            friendHeartSend.setSendTime(now);
            friendHeartSend.setGet(false);
        }
        //reward
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, FriendConstants.FRIENDSHIP_GIVE_ADD_COIN, LogConstants.MODULE_FRIEND);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.FRIENDSHIP_PMARK, 1, LogConstants.MODULE_FRIEND);
        //ret
        WsMessageFriend.S2CFriendshipGive respmsg = new WsMessageFriend.S2CFriendshipGive();
        respmsg.rewards = Collections.singletonList(new WsMessageBase.IORewardItem(GameConfig.PLAYER.GOLD, FriendConstants.FRIENDSHIP_GIVE_ADD_COIN));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
