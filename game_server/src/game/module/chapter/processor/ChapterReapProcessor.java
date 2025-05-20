package game.module.chapter.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityWeekManager;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.dao.ChapterTemplateCache;
import game.module.chapter.dao.MyChapterTemplateCache;
import game.module.chapter.logic.ChapterDaoHelper;
import game.module.chapter.logic.ChapterManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.season.logic.SeasonManager;
import game.module.template.ChapterTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.template.VipTemplate;
import game.module.vip.dao.VipTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SChapterReap.id, accessLimit = 200)
public class ChapterReapProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChapterReapProcessor.class);

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
        logger.info("chapter reap!player={}", playerId);
        ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playerId);
        //时间太短
        Date now = new Date();
        if (chapterBean != null) {
            Date lastReapTime = chapterBean.getLastGainTime();
            int diffSeconds = (int) ((now.getTime() - lastReapTime.getTime()) / 1000);
            if (diffSeconds < 5) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CChapterReap.msgCode, 1402);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //award
        Integer maxMapId = 0;
        if (chapterBean != null) {
            maxMapId = chapterBean.getMaxMapId();
        } else {
            maxMapId = ChapterManager.getInstance().getInitMapId();
        }
        //exp,coin reward
        int hangSeconds = 300;
        if (chapterBean != null) {
            Date lastReapTime = chapterBean.getLastGainTime();
            hangSeconds = (int) ((now.getTime() - lastReapTime.getTime()) / 1000);
        }
        if (hangSeconds > ChapterManager.MAX_HANG_SECONDS) {
            hangSeconds = ChapterManager.MAX_HANG_SECONDS;
        }
        VipTemplate vipTemplate = VipTemplateCache.getInstance().getVipTemplate(playingRole.getPlayerBean().getVipLevel());
        List<WsMessageBase.IORewardItem> ioRewards = new ArrayList<>();
        ChapterTemplate chapterTemplate = ChapterTemplateCache.getInstance().getChapterTemplateById(maxMapId);
        List<RewardTemplateSimple> hangRewards = chapterTemplate.getHANG();
        int seasonAddon = SeasonManager.getInstance().getHangAddon();
        for (RewardTemplateSimple rewardTemplateSimple : hangRewards) {
            int gsid = rewardTemplateSimple.getGSID();
            float seasonAddonMy = (gsid == GameConfig.PLAYER.GOLD || gsid == 30001) ? seasonAddon / 100f : 0f;
            int count = (int) (rewardTemplateSimple.getCOUNT() * hangSeconds / 5f * (1 + vipTemplate.getRIGHT().getHANG_GOLD() / 1000f + seasonAddonMy));
            AwardUtils.changeRes(playingRole, gsid, count, LogConstants.MODULE_CHAPTER);
            ioRewards.add(new WsMessageBase.IORewardItem(gsid, count));
        }
        //drops
        int hangMinutes = hangSeconds / 60;
        for (RewardTemplateSimple rewardTemplateSimple : chapterTemplate.getDROPS()) {
            Integer gsid = rewardTemplateSimple.getGSID();
            Integer itemDropRate = MyChapterTemplateCache.getInstance().getItemDropRate(gsid);
            if (itemDropRate == null) {
                logger.error("drop rate not exist!{}", gsid);
                continue;
            }
            addReward(hangMinutes, gsid, itemDropRate, playingRole, ioRewards);
        }
        //activity week reward
        int[] mjbgHangReward = ActivityWeekManager.getInstance().mjbgHangReward();
        if (mjbgHangReward != null) {
            addReward(hangMinutes, mjbgHangReward[0], mjbgHangReward[1], playingRole, ioRewards);
        }
        int[] gjdlHangReward = ActivityWeekManager.getInstance().gjdlHangReward();
        if (gjdlHangReward != null) {
            addReward(hangMinutes, gjdlHangReward[0], gjdlHangReward[1], playingRole, ioRewards);
        }
        //save bean
        if (chapterBean == null) {
            chapterBean = ChapterManager.getInstance().createChapterBean(playerId, maxMapId, now);
            ChapterDaoHelper.asyncInsertChapterBean(chapterBean);
            ChapterCache.getInstance().addChapterBean(chapterBean);
        } else {
            chapterBean.setLastGainTime(now);
            ChapterDaoHelper.asyncUpdateChapterBean(chapterBean);
        }
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.CHAPTER_REAP_PMARK, 1, LogConstants.MODULE_CHAPTER);
        //push
        WsMessageHall.PushPropChange pushPropChange = new WsMessageHall.PushPropChange(100009, now.getTime());
        playingRole.write(pushPropChange.build(playingRole.alloc()));
        //ret
        WsMessageBattle.S2CChapterReap respmsg = new WsMessageBattle.S2CChapterReap();
        respmsg.rewards = ioRewards;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void addReward(int hangMinutes, int gsid, Integer itemDropRate, PlayingRole playingRole, List<WsMessageBase.IORewardItem> ioRewards) {
        int dropCount = hangMinutes / itemDropRate;
        int diffMinutes = hangMinutes % itemDropRate;
        int dropCount1 = RandomUtils.nextInt(0, itemDropRate) < diffMinutes ? 1 : 0;
        int sumCount = dropCount + dropCount1;
        if (sumCount > 0) {
            //-20%----+20%随机
            float diffrate = 0.2f;
            int lowCount = (int) Math.floor(sumCount * (1 - diffrate));
            int highCount = (int) Math.ceil(sumCount * (1 + diffrate));
            sumCount = RandomUtils.nextInt(lowCount, highCount + 1);
            if (sumCount > 0) {
                AwardUtils.changeRes(playingRole, gsid, sumCount, LogConstants.MODULE_CHAPTER);
                ioRewards.add(new WsMessageBase.IORewardItem(gsid, sumCount));
            }
        }
    }

}
