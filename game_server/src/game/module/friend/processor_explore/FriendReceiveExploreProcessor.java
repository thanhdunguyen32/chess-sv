package game.module.friend.processor_explore;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.friend.bean.FriendExplore;
import game.module.friend.dao.FchapterTemplateCache;
import game.module.friend.dao.FriendExploreCache;
import game.module.friend.dao.FriendExploreDaoHelper;
import game.module.friend.logic.FriendManager;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import game.module.template.FchapterTemplate;
import game.module.template.RewardTemplateRange;
import game.module.template.RewardTemplateWeight;
import game.module.user.logic.PlayerServerPropManager;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageFriend;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendReceiveExplore.id, accessLimit = 200)
public class FriendReceiveExploreProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendReceiveExploreProcessor.class);

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
        WsMessageFriend.C2SFriendReceiveExplore reqmsg = WsMessageFriend.C2SFriendReceiveExplore.parse(request);
        int playerId = playingRole.getId();
        logger.info("friend receive explore!player={},req={}", playerId, reqmsg);
        int chapter_id = reqmsg.chapter_id;
        //is exist
        FriendExplore friendExplore = FriendExploreCache.getInstance().getFriendExplore(playerId);
        if (friendExplore == null || friendExplore.getDbFriendChapter() == null || friendExplore.getDbFriendChapter().getChapters() == null ||
                !friendExplore.getDbFriendChapter().getChapters().containsKey(chapter_id)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendReceiveExplore.msgCode, 1503);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //is finish
        FriendExplore.DbFriendChapter1 dbFriendChapter1 = friendExplore.getDbFriendChapter().getChapters().get(chapter_id);
        Date now = new Date();
        if (dbFriendChapter1.getEtime().after(now)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendReceiveExplore.msgCode, 1504);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        FchapterTemplate fchapterTemplate = FchapterTemplateCache.getInstance().getFchapterTemplates(chapter_id - 1);
        List<RewardTemplateRange> itemsReward = fchapterTemplate.getITEMS();
        for (RewardTemplateRange rewardTemplateRange : itemsReward) {
            List<Integer> countRange = rewardTemplateRange.getCOUNT();
            int randCount = RandomUtils.nextInt(countRange.get(0), countRange.get(1) + 1);
            AwardUtils.changeRes(playingRole, rewardTemplateRange.getGSID(), randCount, LogConstants.MODULE_FRIEND);
            rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateRange.getGSID(), randCount));
        }
        FchapterTemplate.FChapterDrops dropsTemplate = fchapterTemplate.getDROPS().get(0);
        RandomDispatcher<RewardTemplateWeight> rd = new RandomDispatcher<>();
        for (RewardTemplateWeight rewardTemplateWeight : dropsTemplate.getITEMS()) {
            rd.put(rewardTemplateWeight.getW(), rewardTemplateWeight);
        }
        for (int i = 0; i < dropsTemplate.getNUM(); i++) {
            RewardTemplateWeight rewardTemplateWeight = rd.randomRemove();
            AwardUtils.changeRes(playingRole, rewardTemplateWeight.getGSID(), rewardTemplateWeight.getCOUNT(), LogConstants.MODULE_FRIEND);
            rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateWeight.getGSID(), rewardTemplateWeight.getCOUNT()));
        }
        //save bean
        friendExplore.getDbFriendChapter().getChapters().remove(chapter_id);
        //generate boss
        Integer bossrate = fchapterTemplate.getBOSSRATE();
        if (RandomUtils.nextInt(0, 1000) <= bossrate) {
            FriendManager.getInstance().generateFriendExploreBoss(playerId);
        }
        FriendExploreDaoHelper.asyncUpdateFriendExplore(friendExplore);
        //ret
        WsMessageFriend.S2CFriendReceiveExplore respmsg = new WsMessageFriend.S2CFriendReceiveExplore();
        respmsg.rewards = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
