package game.module.mission.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.mission.dao.MissionDailyTemplateCache;
import game.module.mission.logic.MissionManager;
import game.module.template.MissionDailyTemplate;
import game.module.template.RewardTemplateConfig;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
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
@MsgCodeAnn(msgcode = WsMessageMission.C2SMissionDailyAward.id, accessLimit = 200)
public class MissionDailyAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MissionDailyAwardProcessor.class);

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
        WsMessageMission.C2SMissionDailyAward reqmsg = WsMessageMission.C2SMissionDailyAward.parse(request);
        int playerId = playingRole.getId();
        logger.info("mission daily award!player={},req={}", playerId, reqmsg);
        int mission_index = reqmsg.mission_index;
        //param check
        if (mission_index < 0 || mission_index >= MissionDailyTemplateCache.getInstance().getSize()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CMissionDailyAward.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        MissionDailyTemplate missionDailyTemplate = MissionDailyTemplateCache.getInstance().getMissionDaily(mission_index);
        //already get
        Integer getmark = missionDailyTemplate.getGETMARK();
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        if (playerOthers.containsKey(getmark) && playerOthers.get(getmark).getCount() > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CMissionDailyAward.msgCode, 1400);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not finish
        String pmark = missionDailyTemplate.getPMARK();
        if (StringUtils.isNumeric(pmark)) {
            int pmarkGsid = Integer.parseInt(pmark);
            if (!playerOthers.containsKey(pmarkGsid) || playerOthers.get(pmarkGsid).getCount() < missionDailyTemplate.getCNUM()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CMissionDailyAward.msgCode, 1401);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        } else if (pmark.equals("all")) {
            //check all
            List<MissionDailyTemplate> allMissionDaily = MissionDailyTemplateCache.getInstance().getAllMissionDaily();
            for (MissionDailyTemplate missionDailyTemplate1 : allMissionDaily) {
                String pmark1 = missionDailyTemplate1.getPMARK();
                if (StringUtils.isNumeric(pmark)) {
                    int pmarkGsid = Integer.parseInt(pmark1);
                    if (!playerOthers.containsKey(pmarkGsid) || playerOthers.get(pmarkGsid).getCount() < missionDailyTemplate.getCNUM()) {
                        WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2CMissionDailyAward.msgCode, 1401);
                        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                        return;
                    }
                }
            }
        }
        //do
        AwardUtils.changeRes(playingRole, getmark, 1, LogConstants.MODULE_DAILY_MISSION);
        //award
        List<WsMessageBase.AwardItem> respAwardList = new ArrayList<>();
        List<RewardTemplateConfig> rewards = missionDailyTemplate.getREWARD();
        for (RewardTemplateConfig rewardTemplateConfig : rewards) {
            int rewardCount = rewardTemplateConfig.getCOUNT();
            if (rewardTemplateConfig.getPARAM() != null) {
                rewardCount += rewardTemplateConfig.getPARAM() * playingRole.getPlayerBean().getLevel();
            }
            AwardUtils.changeRes(playingRole, rewardTemplateConfig.getGSID(), rewardCount, LogConstants.MODULE_DAILY_MISSION);
            respAwardList.add(new WsMessageBase.AwardItem(rewardTemplateConfig.getGSID(), rewardCount));
        }
        //ret
        WsMessageMission.S2CMissionDailyAward respmsg = new WsMessageMission.S2CMissionDailyAward();
        respmsg.rewards = respAwardList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
