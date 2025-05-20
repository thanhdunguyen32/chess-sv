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
import game.module.mission.constants.MissionConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageFriend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendshipOnekey.id, accessLimit = 200)
public class FriendshipOnkeyProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendshipOnkeyProcessor.class);

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
        int playerId = playingRole.getId();
        logger.info("friendship onkey!player={}", playerId);
        //do
        int reveiveNum = 0;
        int giveNum = 0;
        Map<Integer, FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
        if (friends != null && friends.size() > 0) {
            for (FriendBean friendBean : friends.values()) {
                Integer friendId = friendBean.getFriendId();
                //has give
                boolean hasGive = false;
                Date now = new Date();
                FriendHeartSend.HeartSendItem friendHeartSend = FriendshipSendCache.getInstance().getFriendHeartSend(playerId, friendId);
                if (friendHeartSend != null) {
                    Date sendTime = friendHeartSend.getSendTime();
                    if (DateUtils.isSameDay(sendTime, now)) {
                        hasGive = true;
                    }
                }
                //give do
                if (!hasGive) {
                    if (friendHeartSend == null) {
                        FriendshipSendCache.getInstance().addFriendHeartSend(playerId, friendId);
                    } else {
                        friendHeartSend.setSendTime(now);
                        friendHeartSend.setGet(false);
                    }
                    giveNum++;
                }
                //is get friendship
                boolean isGetFriendship = false;
                FriendHeartSend.HeartSendItem friendHeartSendOther = FriendshipSendCache.getInstance().getFriendHeartSend(friendId, playerId);
                if (friendHeartSendOther == null || !DateUtils.isSameDay(friendHeartSendOther.getSendTime(), now) || friendHeartSendOther.getGet()) {
                    isGetFriendship = true;
                }
                //do
                if (!isGetFriendship) {
                    friendHeartSendOther.setGet(true);
                    reveiveNum++;
                }
            }
        }
        //award
        List<WsMessageBase.IORewardItem> rewards = new ArrayList<>();
        if (reveiveNum > 0) {
            AwardUtils.changeRes(playingRole, ItemConstants.FRIENDSHIP_ICON, reveiveNum, LogConstants.MODULE_FRIEND);
            rewards.add(new WsMessageBase.IORewardItem(ItemConstants.FRIENDSHIP_ICON, reveiveNum));
        }
        if (giveNum > 0) {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, giveNum * FriendConstants.FRIENDSHIP_GIVE_ADD_COIN, LogConstants.MODULE_FRIEND);
            rewards.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.GOLD, giveNum * FriendConstants.FRIENDSHIP_GIVE_ADD_COIN));
            //update mission progress
            AwardUtils.changeRes(playingRole, MissionConstants.FRIENDSHIP_PMARK, giveNum, LogConstants.MODULE_FRIEND);
        }
        //ret
        WsMessageFriend.S2CFriendshipOnekey respmsg = new WsMessageFriend.S2CFriendshipOnekey();
        respmsg.rewards = rewards;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
