package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionMission;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.LegionFLevelTemplateCache;
import game.module.legion.logic.LegionManager;
import game.module.template.LegionFLevelTemplate;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionFactoryMissionList.id, accessLimit = 200)
public class LegionFMissionListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionFMissionListProcessor.class);

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
        WsMessageLegion.C2SLegionFactoryMissionList reqmsg = WsMessageLegion.C2SLegionFactoryMissionList.parse(request);
        logger.info("legion factory mission list!player={},req={}", playerId, reqmsg);
        //ret
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionList.msgCode, 1516);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //12小时间隔刷新
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        Date now = new Date();
        if (reqmsg.isupdate && legionPlayer.getMissionGetTime() != null && DateUtils.addHours(legionPlayer.getMissionGetTime(), 12).after(now)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionList.msgCode, 1536);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        if (reqmsg.isupdate || legionPlayer.getMissions() == null) {
            legionPlayer.setMissionGetTime(now);
            Map<Long, LegionMission> missions = legionPlayer.getMissions();
            if (missions == null) {
                missions = new HashMap<>();
                legionPlayer.setMissions(missions);
            }
            //remove
            missions.entrySet().removeIf(aMissionPair -> aMissionPair.getValue().getEndTime() == null);
            //generate count
            LegionFLevelTemplate legionFLevelTemplate = LegionFLevelTemplateCache.getInstance().getLegionFLevelTemplate(legionBean.getFlevel());
            Integer mnum = legionFLevelTemplate.getMNUM();
            for (int i = 0; i < mnum; i++) {
                LegionMission legionMission = LegionManager.getInstance().createLegionMission();
                long missionKey = SessionManager.getInstance().generateSessionId();
                missions.put(missionKey, legionMission);
            }
            LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        }
        //ret
        WsMessageLegion.S2CLegionFactoryMissionList respmsg = new WsMessageLegion.S2CLegionFactoryMissionList();
        respmsg.time = legionPlayer.getMissionGetTime() != null ? legionPlayer.getMissionGetTime().getTime() : 0;
        respmsg.list = new ArrayList<>(legionPlayer.getMissions().size());
        for (Map.Entry<Long, LegionMission> aEntry : legionPlayer.getMissions().entrySet()) {
            LegionMission legionMission = aEntry.getValue();
            Long missionKey = aEntry.getKey();
            respmsg.list.add(new WsMessageBase.IOLegionFactoryMission(missionKey, legionMission.getId(), legionMission.getStartTime() != null ?
                    legionMission.getStartTime().getTime() : 0, legionMission.getEndTime() != null ? legionMission.getEndTime().getTime() : 0));
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
