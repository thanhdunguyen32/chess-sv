package game.module.friend.processor_explore;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendExplore;
import game.module.friend.dao.FchapterTemplateCache;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendExploreCache;
import game.module.friend.dao.FriendExploreDaoHelper;
import game.module.friend.logic.FriendConstants;
import game.module.friend.logic.FriendManager;
import game.module.log.constants.LogConstants;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.template.FchapterTemplate;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageFriend;
import ws.WsMessageHall;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendOpenExplore.id, accessLimit = 200)
public class FriendOpenExploreProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendOpenExploreProcessor.class);

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
        WsMessageFriend.C2SFriendOpenExplore reqmsg = WsMessageFriend.C2SFriendOpenExplore.parse(request);
        int playerId = playingRole.getId();
        logger.info("friend open explore!player={},req={}", playerId, reqmsg);
        //是否已经为好友
        int[] friend_ids = reqmsg.friends;
        int chapter_id = reqmsg.chapter_id;
        if (friend_ids == null || friend_ids.length == 0) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendOpenExplore.msgCode, 1401);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //friend check
        Map<Integer, FriendBean> friendsCache = FriendCache.getInstance().getFriends(playerId);
        for (int friendId : friend_ids) {
            if (!PlayerOfflineManager.getInstance().checkExist(friendId)) {
                WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendOpenExplore.msgCode, 1402);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
            if (!friendsCache.containsKey(friendId)) {
                WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendOpenExplore.msgCode, 1403);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
        }
        //count max
        int attackCount = PlayerManager.getInstance().getOtherCount(playerId, FriendConstants.FRIEND_EXPLORE_CHAPTER_COUNT_MARK);
        if (attackCount >= FriendConstants.FRIEND_EXPLORE_CHAPTER_COUNT) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendOpenExplore.msgCode, 1500);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //power not enough
        int powerSum = playingRole.getPlayerBean().getPower();
        for (int friendId : friend_ids) {
            PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(friendId);
            powerSum += poc.getPower();
        }
        FchapterTemplate fchapterTemplate = FchapterTemplateCache.getInstance().getFchapterTemplates(chapter_id - 1);
        if (powerSum < fchapterTemplate.getPOWER()) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendOpenExplore.msgCode, 1501);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //is progress
        FriendExplore friendExplore = FriendExploreCache.getInstance().getFriendExplore(playerId);
        if (friendExplore != null && friendExplore.getDbFriendChapter() != null && friendExplore.getDbFriendChapter().getChapters() != null && friendExplore.getDbFriendChapter().getChapters().containsKey(chapter_id)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendOpenExplore.msgCode, 1502);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //do
        Date now = new Date();
        Date endTime = DateUtils.addMinutes(now, fchapterTemplate.getMIN());
        if (friendExplore == null) {
            friendExplore = FriendManager.getInstance().createFriendExplore(playerId);
            FriendExplore.DbFriendChapter dbFriendChapter = new FriendExplore.DbFriendChapter();
            friendExplore.setDbFriendChapter(dbFriendChapter);
            Map<Integer, FriendExplore.DbFriendChapter1> chapters = new HashMap<>();
            dbFriendChapter.setChapters(chapters);
            FriendExplore.DbFriendChapter1 dbFriendChapter1 = new FriendExplore.DbFriendChapter1();
            dbFriendChapter1.setEtime(endTime);
            dbFriendChapter1.setFriends(Arrays.asList(ArrayUtils.toObject(friend_ids)));
            chapters.put(chapter_id, dbFriendChapter1);
            //save
            FriendExploreCache.getInstance().addFriendExplore(friendExplore);
            FriendExploreDaoHelper.asyncInsertFriendExplore(friendExplore);
        } else {
            if (friendExplore.getDbFriendChapter() == null || friendExplore.getDbFriendChapter().getChapters() == null) {
                FriendExplore.DbFriendChapter dbFriendChapter = new FriendExplore.DbFriendChapter();
                friendExplore.setDbFriendChapter(dbFriendChapter);
                Map<Integer, FriendExplore.DbFriendChapter1> chapters = new HashMap<>();
                dbFriendChapter.setChapters(chapters);
            }
            FriendExplore.DbFriendChapter1 dbFriendChapter1 = new FriendExplore.DbFriendChapter1();
            dbFriendChapter1.setEtime(endTime);
            dbFriendChapter1.setFriends(Arrays.asList(ArrayUtils.toObject(friend_ids)));
            friendExplore.getDbFriendChapter().getChapters().put(chapter_id, dbFriendChapter1);
            //save
            FriendExploreDaoHelper.asyncUpdateFriendExplore(friendExplore);
        }
        //mark save
        AwardUtils.changeRes(playingRole, FriendConstants.FRIEND_EXPLORE_CHAPTER_COUNT_MARK, 1, LogConstants.MODULE_FRIEND);
        //ret
        WsMessageFriend.S2CFriendOpenExplore respmsg = new WsMessageFriend.S2CFriendOpenExplore();
        respmsg.etime = endTime.getTime();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
