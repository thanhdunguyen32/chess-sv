package game.module.pay.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.template.RewardTemplateSimple;
import game.module.template.VipTemplate;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import game.module.vip.dao.VipTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHall.C2SGetVipGift;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageHall.S2CGetVipGift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = C2SGetVipGift.id, accessLimit = 200)
public class GetVipPackAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetVipPackAwardProcessor.class);

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
        C2SGetVipGift reqMsg = C2SGetVipGift.parse(request);
        int vip_level = reqMsg.vip_level;
        logger.info("VIP面板领取不同VIP等级的奖励!playerId={},vip_level={}", playerId, vip_level);
        // vip等级是否到
        if (playingRole.getPlayerBean().getVipLevel() < vip_level) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CGetVipGift.msgCode, 1603);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // 是否已经领取
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        VipTemplate vipTemplate = VipTemplateCache.getInstance().getVipTemplate(vip_level);
        Integer vipMark = vipTemplate.getMARK();
        if (playerOthers.containsKey(vipMark) && playerOthers.get(vipMark).getCount() > 0) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CGetVipGift.msgCode, 1605);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // do
        List<WsMessageBase.RewardInfo> rewards = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : vipTemplate.getREWARD()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_VIP);
            rewards.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
//            GmManager.getInstance().sendPurpleCardMarquee(rewardTemplateSimple.getGSID(), playingRole.getPlayerBean().getName(), 0);
        }
        // 保存记录
        AwardUtils.changeRes(playingRole, vipMark, 1, LogConstants.MODULE_VIP);
        // ret
        S2CGetVipGift retMsg = new S2CGetVipGift();
        retMsg.rewards = rewards;
        playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
    }

}
