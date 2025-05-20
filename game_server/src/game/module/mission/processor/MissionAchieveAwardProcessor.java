package game.module.mission.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.mission.dao.MissionAchieveTemplateCache;
import game.module.mission.logic.MissionManager;
import game.module.template.MissionAchieveTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
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
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMission.C2SMissionAchieveAward.id, accessLimit = 200)
public class MissionAchieveAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MissionAchieveAwardProcessor.class);

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
        WsMessageMission.C2SMissionAchieveAward reqmsg = WsMessageMission.C2SMissionAchieveAward.parse(request);
        int playerId = playingRole.getId();
        logger.info("mission achieve award!player={},req={}", playerId, reqmsg);
        int mission_type = reqmsg.mission_type;
        int mission_index = reqmsg.mission_index;
        //param check
        if (mission_type <= 0 || mission_type > MissionAchieveTemplateCache.getInstance().getSize()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CMissionAchieveAward.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        MissionAchieveTemplate missionAchieveTemplate = MissionAchieveTemplateCache.getInstance().getMissionAchieveTemplate(mission_type - 1);
        //already get
        Integer getmark = missionAchieveTemplate.getGETMARK();
        MissionAchieveTemplate.MissionAchieveTemplateMission missionAchieveTemplateMission = missionAchieveTemplate.getMISSION().get(mission_index);
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        if (playerOthers.containsKey(getmark) && playerOthers.get(getmark).getCount() > mission_index) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CMissionAchieveAward.msgCode, 1402);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not finish
        String pmark = missionAchieveTemplateMission.getPMARK();
        boolean isMissionFinish = MissionManager.getInstance().checkMissionFinish(playingRole.getPlayerBean(), pmark, missionAchieveTemplateMission.getCNUM());
        if (!isMissionFinish) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CMissionAchieveAward.msgCode, 1403);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        AwardUtils.changeRes(playingRole, getmark, 1, LogConstants.MODULE_ACHIEVE_MISSION);
        //award
        List<WsMessageBase.AwardItem> respAwardList = new ArrayList<>();
        List<RewardTemplateSimple> rewards = missionAchieveTemplateMission.getREWARD();
        for (RewardTemplateSimple rewardTemplateConfig : rewards) {
            AwardUtils.changeRes(playingRole, rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT(), LogConstants.MODULE_ACHIEVE_MISSION);
            respAwardList.add(new WsMessageBase.AwardItem(rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT()));
        }
        //ret
        WsMessageMission.S2CMissionAchieveAward respmsg = new WsMessageMission.S2CMissionAchieveAward();
        respmsg.rewards = respAwardList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
