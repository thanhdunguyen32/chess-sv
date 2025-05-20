package game.module.mission.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.dao.NoviceTrainTemplateCache;
import game.module.mission.logic.MissionManager;
import game.module.template.NoviceTrainTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageMission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMission.C2SGetNoviceTrainAward.id, accessLimit = 200)
public class NoviceTrainAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(NoviceTrainAwardProcessor.class);

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
        WsMessageMission.C2SGetNoviceTrainAward reqmsg = WsMessageMission.C2SGetNoviceTrainAward.parse(request);
        int playerId = playingRole.getId();
        logger.info("novice train award!player={},req={}", playerId, reqmsg);
        String exid = reqmsg.exid;
        //param check
        NoviceTrainTemplate noviceTrainTemplate = NoviceTrainTemplateCache.getInstance().getNoviceTrainTemplate(exid);
        if (noviceTrainTemplate == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CGetNoviceTrainAward.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //already get
        Integer getmark = noviceTrainTemplate.getGETMARK();
        if (ItemManager.getInstance().getCount(playingRole.getPlayerBean(), getmark) > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CGetNoviceTrainAward.msgCode, 1402);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not finish
        boolean isMissionFinish = MissionManager.getInstance().checkMissionFinish(playingRole.getPlayerBean(), noviceTrainTemplate.getPMARK(),
                noviceTrainTemplate.getCNUM());
        if (!isMissionFinish) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CGetNoviceTrainAward.msgCode, 1403);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        AwardUtils.changeRes(playingRole, getmark, 1, LogConstants.MODULE_ACHIEVE_MISSION);
        //award
        List<WsMessageBase.AwardItem> respAwardList = new ArrayList<>();
        List<RewardTemplateSimple> rewards = noviceTrainTemplate.getREWARD();
        for (RewardTemplateSimple rewardTemplateConfig : rewards) {
            AwardUtils.changeRes(playingRole, rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT(), LogConstants.MODULE_ACHIEVE_MISSION);
            respAwardList.add(new WsMessageBase.AwardItem(rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT()));
        }
        //ret
        WsMessageMission.S2CGetNoviceTrainAward respmsg = new WsMessageMission.S2CGetNoviceTrainAward();
        respmsg.rewards = respAwardList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
