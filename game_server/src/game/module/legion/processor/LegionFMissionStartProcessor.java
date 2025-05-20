package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionMission;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.LegionMissionTemplateCache;
import game.module.legion.logic.LegionManager;
import game.module.template.LegionMissionTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.Date;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionFactoryMissionStart.id, accessLimit = 200)
public class LegionFMissionStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionFMissionStartProcessor.class);

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
        WsMessageLegion.C2SLegionFactoryMissionStart reqmsg = WsMessageLegion.C2SLegionFactoryMissionStart.parse(request);
        logger.info("legion factory mission start!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionStart.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //mission not exist
        long missionKey = reqmsg.key;
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        if (legionPlayer.getMissions() == null || !legionPlayer.getMissions().containsKey(missionKey)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionStart.msgCode, 1537);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        LegionMission legionMission = legionPlayer.getMissions().get(missionKey);
        //already start
        if (legionMission.getEndTime() != null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionStart.msgCode, 1540);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        Date now = new Date();
        legionMission.setStartTime(now);
        LegionMissionTemplate legionMissionTemplate = LegionMissionTemplateCache.getInstance().getLegionMissionTemplate(legionMission.getId());
        legionMission.setEndTime(DateUtils.addMinutes(now, legionMissionTemplate.getTIMEMIN()));
        //update bean
        LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        //ret
        WsMessageLegion.S2CLegionFactoryMissionStart respmsg = new WsMessageLegion.S2CLegionFactoryMissionStart();
        respmsg.stime = now.getTime();
        respmsg.etime = legionMission.getEndTime().getTime();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
