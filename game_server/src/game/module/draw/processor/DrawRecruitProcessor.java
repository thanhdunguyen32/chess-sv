package game.module.draw.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityWeekManager;
import game.module.activity.logic.ActivityXiangouManager;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.draw.bean.PubDraw;
import game.module.draw.dao.DrawTemplateCache;
import game.module.draw.dao.PubDrawCache;
import game.module.draw.dao.PubDrawDaoHelper;
import game.module.draw.logic.PubDrawConstants;
import game.module.draw.logic.PubDrawManager;
import game.module.guide.logic.GuideConstants;
import game.module.hero.dao.GeneralChipTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.item.dao.RBoxTemplateCache;
import game.module.item.logic.BagManager;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.template.DrawTemplate;
import game.module.template.GeneralChipTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.*;

@MsgCodeAnn(msgcode = WsMessageHall.C2SDrawRecruit.id, accessLimit = 200)
public class DrawRecruitProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DrawRecruitProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHall.C2SDrawRecruit reqMsg = WsMessageHall.C2SDrawRecruit.parse(request);
        final int buy_type = reqMsg.buy_type;
        int times = reqMsg.times;
        int playerId = playingRole.getId();
        logger.info("draw recruit,player={},req={}", playerId, reqMsg);
        //params check
        if (buy_type < 1 || buy_type > 4) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CDrawRecruit.msgCode, 1350);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (times != 1 && times != 10) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CDrawRecruit.msgCode, 1350);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //check is guide
        int guideProgress = PlayerManager.getInstance().getOtherCount(playerId, GuideConstants.GUIDE_PROGRESS_MARK);
        if (guideProgress == 1 || guideProgress == 2 || guideProgress == 6) {
            guideDraw(playingRole, guideProgress, buy_type, times);
            return;
        }
        //check is free
        boolean isFree = false;
        Date now = new Date();
        PubDraw pubDraw = PubDrawCache.getInstance().getPubDraw(playerId);
        switch (buy_type) {
            case 1:
                if (pubDraw == null || pubDraw.getLastNormalTime() == null || DateUtils.addHours(pubDraw.getLastNormalTime(),
                        PubDrawConstants.DRAW_NORMAL_HOURS).before(now)) {
                    isFree = true;
                }
                break;
            case 3:
                if (pubDraw == null || pubDraw.getLastAdvanceTime() == null || DateUtils.addHours(pubDraw.getLastAdvanceTime(),
                        PubDrawConstants.DRAW_ADVANCE_HOURS).before(now)) {
                    isFree = true;
                }
                break;
        }
        //check bag full
        boolean checkBagFull = BagManager.getInstance().checkBagFull(playerId, times, playingRole.getPlayerBean().getVipLevel());
        if (checkBagFull) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CDrawRecruit.msgCode, 1358);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //material enough
        Map<Integer, DrawTemplate> drawTemplates = DrawTemplateCache.getInstance().getDrawTemplate(buy_type);
        DrawTemplate drawTemplate = drawTemplates.get(times);
        boolean useMoney = false;
        if (!isFree) {
            boolean is1Match = false;
            for (RewardTemplateSimple rewardTemplateSimple : drawTemplate.getCOST()) {
                boolean cond1Match = ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(),
                        rewardTemplateSimple.getCOUNT());
                if (cond1Match) {
                    is1Match = true;
                    if(rewardTemplateSimple.getGSID().equals(GameConfig.PLAYER.YB)){
                        useMoney = true;
                    }
                    break;
                }
            }
            if (!is1Match) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CDrawRecruit.msgCode, 1351);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //award
        List<WsMessageBase.RewardInfo> rewardInfos = new ArrayList<>();
        RandomDispatcher<Integer> rd1 = null;
        int[][] rate_list = null;
        if (buy_type == 1) {
            rate_list = PubDrawConstants.Draw1Rate;
        } else if (buy_type == 2) {
            rate_list = PubDrawConstants.Draw2Rate;
        } else if (buy_type == 3) {
            if(useMoney) {
                rate_list = PubDrawConstants.Draw3Rate;
            }else{
                rate_list = PubDrawConstants.Draw3RateItem;
            }
        }
        if (rate_list != null) {
            rd1 = new RandomDispatcher<>();
            for (int[] aPair : rate_list) {
                rd1.put(aPair[1], aPair[0]);
            }
        }
        //随机n次
        for (int i = 0; i < times; i++) {
            int targetStar = 5;
            if (rd1 != null) {
                targetStar = rd1.random();
            }
            int generalTemplateId = 0;
            if (targetStar < 5) {
                List<GeneralChipTemplate> generalTemplates = GeneralChipTemplateCache.getInstance().getGeneralTemplates(targetStar);
                int randIndex = RandomUtils.nextInt(0, generalTemplates.size());
                GeneralChipTemplate generalTemplate = generalTemplates.get(randIndex);
                generalTemplateId = (Integer) generalTemplate.getGCOND();
            } else {
                //5星将领区分为普通，精英，神话，7.78%，1.3%，0.21%
                RandomDispatcher<Integer> rd = new RandomDispatcher<>();
                for(int aChipGsid : RBoxTemplateCache.getInstance().get5StarNormalChips()){
                    rd.put(PubDrawConstants.Star5Rates[0], aChipGsid);
                }
                for(int aChipGsid : RBoxTemplateCache.getInstance().get5StarEliteChips()){
                    rd.put(PubDrawConstants.Star5Rates[1], aChipGsid);
                }
                for(int aChipGsid : RBoxTemplateCache.getInstance().get5StarLegendChips()){
                    rd.put(PubDrawConstants.Star5Rates[2], aChipGsid);
                }
                //
                ActivityWeekManager.getInstance().cxryAddRate(rd,playerId);
                int chipTemplateId = rd.random();
                GeneralChipTemplate generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(chipTemplateId);
                generalTemplateId = (Integer) generalChipTemplate.getGCOND();
            }
            rewardInfos.add(new WsMessageBase.RewardInfo(generalTemplateId, 1));
            AwardUtils.changeRes(playingRole, generalTemplateId, 1, LogConstants.MODULE_PUB);
            //
            int gstar = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId).getSTAR();
            ActivityXiangouManager.getInstance().gstarXiangou(playerId,gstar);
            ActivityWeekManager.getInstance().pubDrawAddGeneral(playerId,generalTemplateId);
        }
        //cost
        if (!isFree) {
            for (RewardTemplateSimple rewardTemplateSimple : drawTemplate.getCOST()) {
                boolean cond1Match = ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(),
                        rewardTemplateSimple.getCOUNT());
                if (cond1Match) {
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_PUB);
                    break;
                }
            }
        }
        //add score
        if (drawTemplate.getSCORE() != null) {
            AwardUtils.changeRes(playingRole, 30007, drawTemplate.getSCORE(), LogConstants.MODULE_PUB);
        }
        //save bean
        if (pubDraw == null) {
            pubDraw = PubDrawManager.getInstance().createPubDraw(playingRole, isFree, buy_type);
            PubDrawDaoHelper.asyncInsertPubDraw(pubDraw);
            PubDrawCache.getInstance().addPubDraw(pubDraw);
        } else if (isFree) {
            if (buy_type == 1) {
                pubDraw.setLastNormalTime(now);
                //push
                WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(100001,
                        DateUtils.addHours(now, PubDrawConstants.DRAW_NORMAL_HOURS).getTime());
                playingRole.write(pushmsg.build(playingRole.alloc()));
            } else {
                pubDraw.setLastAdvanceTime(now);
                WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(100002,
                        DateUtils.addHours(now, PubDrawConstants.DRAW_ADVANCE_HOURS).getTime());
                playingRole.write(pushmsg.build(playingRole.alloc()));
            }
            PubDrawDaoHelper.asyncUpdatePubDraw(pubDraw);
        }
        //ret
        sendRet(playingRole, buy_type, times, rewardInfos);
    }

    private void sendRet(PlayingRole playingRole, int buy_type, int times, List<WsMessageBase.RewardInfo> rewardInfos) {
        //update mission progress
        switch (buy_type) {
            case 1:
                AwardUtils.changeRes(playingRole, MissionConstants.RECRUIT_NORMAL_PMARK, times, LogConstants.MODULE_PUB);
                AwardUtils.changeRes(playingRole, MissionConstants.RECRUIT_NORMAL, times, LogConstants.MODULE_PUB);
                break;
            case 2:
                AwardUtils.changeRes(playingRole, MissionConstants.RECRUIT_FRIEND, times, LogConstants.MODULE_PUB);
                break;
            case 3:
                AwardUtils.changeRes(playingRole, MissionConstants.RECRUIT_ADVANCE_PMARK, times, LogConstants.MODULE_PUB);
                AwardUtils.changeRes(playingRole, MissionConstants.PUB_ADVANCE_RECRUIT, times, LogConstants.MODULE_PUB);
                ActivityWeekManager.getInstance().zmjfActivity(playingRole,times);
                break;
        }
        //ret
        WsMessageHall.S2CDrawRecruit respmsg = new WsMessageHall.S2CDrawRecruit();
        respmsg.buy_type = buy_type;
        respmsg.times = times;
        respmsg.rewards = rewardInfos;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void guideDraw(PlayingRole playingRole, int guideProgress, int buy_type, int times) {
        int rewardGsid = 0;
        switch (guideProgress) {
            case 1:
                rewardGsid = 12281;
                break;
            case 2:
                rewardGsid = 12181;
                break;
            case 6:
                rewardGsid = 12241;
                break;
        }
        AwardUtils.changeRes(playingRole, rewardGsid, 1, LogConstants.MODULE_PUB);
        //ret
        sendRet(playingRole, buy_type, times, Collections.singletonList(new WsMessageBase.RewardInfo(rewardGsid, 1)));
    }

}
