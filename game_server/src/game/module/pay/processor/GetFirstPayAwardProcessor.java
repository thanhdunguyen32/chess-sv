package game.module.pay.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.pay.bean.ChargeEntity;
import game.module.pay.dao.ChargeCache;
import game.module.pay.dao.FirstChargeTemplateCache;
import game.module.pay.logic.PaymentConstants;
import game.module.template.FirstChargeTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageHall.S2CGetFirstPayAward;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageHall.C2SGetFirstPayAward.id, accessLimit = 200)
public class GetFirstPayAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetFirstPayAwardProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageHall.C2SGetFirstPayAward reqmsg = WsMessageHall.C2SGetFirstPayAward.parse(request);
        logger.info("领取首冲奖励!player={},req={}", playerId, reqmsg);
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        if (chargeEntity.getFirstPayTime() == null) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // do
        List<WsMessageBase.RewardInfo> rewardInfoList = new ArrayList<>();
        Date now = new Date();
        switch (reqmsg.extype) {
            case 0://英雄3选1
                //已经选择
                int getHeroCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), 93310);
                if (getHeroCount >= 1) {
                    S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 1458);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                //do
                int[] firstRechargeHero = PaymentConstants.FIRST_RECHARGE_HERO[reqmsg.markid - 1];
                AwardUtils.changeRes(playingRole, firstRechargeHero[0], firstRechargeHero[1], LogConstants.MODULE_RECHARGE);
                rewardInfoList.add(new WsMessageBase.RewardInfo(firstRechargeHero[0], firstRechargeHero[1]));
                //add mark
                AwardUtils.changeRes(playingRole, 93310, 1, LogConstants.MODULE_RECHARGE);
                break;
            case 6://6元3日奖励
                //不到6元
                int chargeYuan = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), 90222);
                if (chargeYuan < 6) {
                    S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 1459);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                //进度
                int myProgress = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), 93311);
                if (myProgress >= 3) {
                    S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 1460);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                Date firstPayTime = chargeEntity.getFirstPayTime();
                if (DateUtils.truncate(now, Calendar.DATE).before(DateUtils.truncate(DateUtils.addDays(firstPayTime, myProgress), Calendar.DATE))) {
                    S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 1461);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                //do
                FirstChargeTemplate firstChargeTemplate = FirstChargeTemplateCache.getInstance().get6FirstChargeTemplate(myProgress);
                for (RewardTemplateSimple rewardTemplateSimple : firstChargeTemplate.getITEMS()) {
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_RECHARGE);
                    rewardInfoList.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                //add mark
                AwardUtils.changeRes(playingRole, 93311, 1, LogConstants.MODULE_RECHARGE);
                break;
            case 100:
                //不到100元
                chargeYuan = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), 90222);
                if (chargeYuan < 6) {
                    S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 1459);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                //进度
                myProgress = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), 93312);
                if (myProgress >= 3) {
                    S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 1460);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                firstPayTime = chargeEntity.getFirstPayTime();
                if (DateUtils.truncate(now, Calendar.DATE).before(DateUtils.truncate(DateUtils.addDays(firstPayTime, myProgress), Calendar.DATE))) {
                    S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CGetFirstPayAward.msgCode, 1461);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                //do
                firstChargeTemplate = FirstChargeTemplateCache.getInstance().get100FirstChargeTemplate(myProgress);
                for (RewardTemplateSimple rewardTemplateSimple : firstChargeTemplate.getITEMS()) {
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_RECHARGE);
                    rewardInfoList.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                //add mark
                AwardUtils.changeRes(playingRole, 93312, 1, LogConstants.MODULE_RECHARGE);
                break;
        }
        // ret
        S2CGetFirstPayAward respMsg = new S2CGetFirstPayAward();
        respMsg.rewards = rewardInfoList;
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
