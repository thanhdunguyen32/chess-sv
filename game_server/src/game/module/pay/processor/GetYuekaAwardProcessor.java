package game.module.pay.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.pay.bean.ChargeEntity;
import game.module.pay.dao.ChargeCache;
import game.module.pay.dao.MCardTemplateCache;
import game.module.template.MCardTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.template.VipTemplate;
import game.module.vip.dao.VipTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall.C2SGetYuekaAward;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageHall.S2CGetYuekaAward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = C2SGetYuekaAward.id, accessLimit = 200)
public class GetYuekaAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetYuekaAwardProcessor.class);

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
        C2SGetYuekaAward reqMsg = C2SGetYuekaAward.parse(request);
        int yuekaType = reqMsg.type;
        logger.info("领取月卡奖励!player={},type={}", playerId, yuekaType);
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CGetYuekaAward.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        switch (yuekaType) {
            case 1:
                // 贵族月卡
                getNormalYueka(playingRole, yuekaType);
                break;
            case 2:
                //至尊月卡
                getZzYueka(playingRole, yuekaType);
                break;
            default:
                break;
        }
    }

    private void getNormalYueka(PlayingRole playingRole, int yuekaType) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date yueKaEndTime = chargeEntity.getGzYuekaEndTime();
        Date now = new Date();
        if (yueKaEndTime == null || yueKaEndTime.before(now)) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CGetYuekaAward.msgCode, 135);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        int todayGetCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), ItemConstants.GZ_YUEKA_GET_MARK);
        if (todayGetCount > 0) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CGetYuekaAward.msgCode, 136);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // do
        List<WsMessageBase.RewardInfo> rewardInfoList = new ArrayList<>();
        MCardTemplate gzMCardTemplate = MCardTemplateCache.getInstance().getGzMCardTemplate();
        List<Map<String, Integer>> arrrew = gzMCardTemplate.getARRREW();
        for (Map<String, Integer> aPair : arrrew) {
            Integer actgsid = aPair.get("REWGSID");
            Integer actcount = aPair.get("REWCOUNT");
            AwardUtils.changeRes(playingRole, actgsid, actcount, LogConstants.MODULE_RECHARGE);
            rewardInfoList.add(new WsMessageBase.RewardInfo(actgsid, actcount));
        }
        // save
        AwardUtils.changeRes(playingRole, ItemConstants.GZ_YUEKA_GET_MARK, 1, LogConstants.MODULE_RECHARGE);
        // ret
        S2CGetYuekaAward respMsg = new S2CGetYuekaAward();
        respMsg.rewards = rewardInfoList;
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void getZzYueka(PlayingRole playingRole, int yuekaType) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date yueKaEndTime = chargeEntity.getZzYuekaEndTime();
        Date now = new Date();
        if (yueKaEndTime == null || yueKaEndTime.before(now)) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CGetYuekaAward.msgCode, 135);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        int todayGetCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), ItemConstants.ZZ_YUEKA_GET_MARK);
        if (todayGetCount > 0) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CGetYuekaAward.msgCode, 136);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // do
        List<WsMessageBase.RewardInfo> rewardInfoList = new ArrayList<>();
        MCardTemplate gzMCardTemplate = MCardTemplateCache.getInstance().getZzMCardTemplate();
        List<Map<String, Integer>> arrrew = gzMCardTemplate.getARRREW();
        for (Map<String, Integer> aPair : arrrew) {
            Integer actgsid = aPair.get("REWGSID");
            Integer actcount = aPair.get("REWCOUNT");
            AwardUtils.changeRes(playingRole, actgsid, actcount, LogConstants.MODULE_RECHARGE);
            rewardInfoList.add(new WsMessageBase.RewardInfo(actgsid, actcount));
        }
        VipTemplate vipTemplate = VipTemplateCache.getInstance().getVipTemplate(playingRole.getPlayerBean().getVipLevel());
        for (RewardTemplateSimple rewardTemplateSimple : vipTemplate.getBCARD()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_RECHARGE);
            rewardInfoList.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        // save
        AwardUtils.changeRes(playingRole, ItemConstants.ZZ_YUEKA_GET_MARK, 1, LogConstants.MODULE_RECHARGE);
        // ret
        S2CGetYuekaAward respMsg = new S2CGetYuekaAward();
        respMsg.rewards = rewardInfoList;
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
