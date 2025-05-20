package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionMission;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.LegionMissionTemplateCache;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.template.LegionMissionTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageLegion;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionFactoryMissionUp.id, accessLimit = 200)
public class LegionFMissionUpProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionFMissionUpProcessor.class);

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
        WsMessageLegion.C2SLegionFactoryMissionUp reqmsg = WsMessageLegion.C2SLegionFactoryMissionUp.parse(request);
        logger.info("legion factory mission up!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionUp.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //mission not exist
        long missionKey = reqmsg.key;
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        if (legionPlayer.getMissions() == null || !legionPlayer.getMissions().containsKey(missionKey)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionUp.msgCode, 1537);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        LegionMission legionMission = legionPlayer.getMissions().get(missionKey);
        Integer missionId = legionMission.getId();
        if (missionId >= LegionMissionTemplateCache.getInstance().getMaxId()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionUp.msgCode, 1538);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost yb
        LegionMissionTemplate legionMissionTemplate = LegionMissionTemplateCache.getInstance().getLegionMissionTemplate(missionId);
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.YB, legionMissionTemplate.getYB())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionUp.msgCode, 1539);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        missionId++;
        legionMission.setId(missionId);
        //update legion
        LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -legionMissionTemplate.getYB(), LogConstants.MODULE_LEGION);
        //ret
        WsMessageLegion.S2CLegionFactoryMissionUp respmsg = new WsMessageLegion.S2CLegionFactoryMissionUp(missionId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
