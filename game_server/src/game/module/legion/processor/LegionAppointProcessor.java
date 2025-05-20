package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.logic.LegionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageLegion;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionAppoint.id, accessLimit = 200)
public class LegionAppointProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionAppointProcessor.class);

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
        WsMessageLegion.C2SLegionAppoint reqmsg = WsMessageLegion.C2SLegionAppoint.parse(request);
        logger.info("legion appoint!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionAppoint.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not ceo
        int myLegionPos = LegionManager.getInstance().getMyLegionPos(playerId, legionId);
        if (myLegionPos != 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionAppoint.msgCode, 1518);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //check is member
        int otherPlayerId = reqmsg.rid;
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        if (!legionBean.getDbLegionPlayers().getMembers().containsKey(otherPlayerId)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionAppoint.msgCode, 1525);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //param check
        int posType = reqmsg.type;
        if (posType < 0 || posType > 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionAppoint.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        synchronized (legionBean) {
            if (posType == 2) {
                legionBean.setCeoId(otherPlayerId);
                LegionPlayer myLegionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
                myLegionPlayer.setPos(0);
            }
            LegionPlayer otherLegionPlayer = legionBean.getDbLegionPlayers().getMembers().get(otherPlayerId);
            otherLegionPlayer.setPos(posType);
            LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        }
        //ret
        WsMessageLegion.S2CLegionAppoint respmsg = new WsMessageLegion.S2CLegionAppoint();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
