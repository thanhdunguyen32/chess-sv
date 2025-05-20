package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
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
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionSet.id, accessLimit = 200)
public class LegionSetProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionSetProcessor.class);

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
        WsMessageLegion.C2SLegionSet reqmsg = WsMessageLegion.C2SLegionSet.parse(request);
        logger.info("legion set!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionSet.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not ceo
        int myLegionPos = LegionManager.getInstance().getMyLegionPos(playerId, legionId);
        if (myLegionPos != 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionSet.msgCode, 1518);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        synchronized (legionBean) {
            if (StringUtils.isNotBlank(reqmsg.notice)) {
                legionBean.setNotice(reqmsg.notice);
            }
            if (reqmsg.minlv >= 0) {
                legionBean.setMinLevel(reqmsg.minlv);
            }
            if (reqmsg.ispass >= 0) {
                legionBean.setPass(reqmsg.ispass > 0);
            }
            LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        }
        //ret
        WsMessageLegion.S2CLegionSet respmsg = new WsMessageLegion.S2CLegionSet();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
