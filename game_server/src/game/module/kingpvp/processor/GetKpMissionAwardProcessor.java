package game.module.kingpvp.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.kingpvp.dao.KingPvpTemplateCache;
import game.module.log.constants.LogConstants;
import game.module.template.KingMissionDailyTemplate;
import game.module.template.RewardTemplateConfig;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessagePvp;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessagePvp.C2SGetKpMissionAward.id, accessLimit = 250)
public class GetKpMissionAwardProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GetKpMissionAwardProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessagePvp.C2SGetKpMissionAward reqmsg = WsMessagePvp.C2SGetKpMissionAward.parse(request);
        logger.info("king pvp mission award,player={},req={}", playerId, reqmsg);
        int mission_index = reqmsg.mission_index;
        KingMissionDailyTemplate missionDailyTemplate = KingPvpTemplateCache.getInstance().getMissionDailyTemplate(mission_index);
        if (missionDailyTemplate == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessagePvp.S2CGetKpMissionAward.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Integer pmark = missionDailyTemplate.getPMARK();
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), pmark, missionDailyTemplate.getCNUM())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessagePvp.S2CGetKpMissionAward.msgCode, 1471);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (ItemManager.getInstance().getCount(playingRole.getPlayerBean(), missionDailyTemplate.getGETMARK()) > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessagePvp.S2CGetKpMissionAward.msgCode, 1472);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        for (RewardTemplateConfig rewardTemplateConfig : missionDailyTemplate.getREWARD()) {
            int rewardCount = rewardTemplateConfig.getCOUNT();
            if (rewardTemplateConfig.getPARAM() != null) {
                rewardCount += rewardTemplateConfig.getPARAM() * playingRole.getPlayerBean().getLevel();
            }
            AwardUtils.changeRes(playingRole, rewardTemplateConfig.getGSID(), rewardCount, LogConstants.MODULE_KING_PVP);
            rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateConfig.getGSID(), rewardCount));
        }
        //mark
        AwardUtils.changeRes(playingRole, missionDailyTemplate.getGETMARK(), 1, LogConstants.MODULE_KING_PVP);
        WsMessagePvp.S2CGetKpMissionAward respmsg = new WsMessagePvp.S2CGetKpMissionAward();
        respmsg.rewards = rewardItems;
        //send
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
