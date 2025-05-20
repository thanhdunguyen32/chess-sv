package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.DbLegionLog;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionCache;
import game.module.legion.logic.LegionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionLog.id, accessLimit = 200)
public class LegionLogProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionLogProcessor.class);

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
        logger.info("legion log list!player={}", playerId);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionLog.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //ret
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        WsMessageLegion.S2CLegionLog respmsg = new WsMessageLegion.S2CLegionLog();
        respmsg.list = new ArrayList<>();
        if (legionBean.getLegionLog() != null) {
            for (DbLegionLog.LegionLog legionLog : legionBean.getLegionLog().getLogs()) {
                WsMessageBase.IOLegionLog ioLegionLog = new WsMessageBase.IOLegionLog();
                respmsg.list.add(ioLegionLog);
                ioLegionLog.create = legionLog.getCeateTime().getTime();
                ioLegionLog.event = legionLog.getEvent();
                ioLegionLog.params = legionLog.getParams();
            }
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
