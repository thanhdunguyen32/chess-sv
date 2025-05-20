package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionCache;
import game.module.legion.logic.LegionManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
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
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionApplyList.id, accessLimit = 200)
public class LegionApplyListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionApplyListProcessor.class);

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
        logger.info("legion apply list!player={}", playerId);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApplyList.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not ceo
        int myLegionPos = LegionManager.getInstance().getMyLegionPos(playerId, legionId);
        if (myLegionPos < 1) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApplyList.msgCode, 1518);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //ret
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        WsMessageLegion.S2CLegionApplyList respmsg = new WsMessageLegion.S2CLegionApplyList();
        respmsg.applylist = new ArrayList<>();
        if (legionBean.getApplyPlayers() != null) {
            for (Map.Entry<Integer,Long> aEntry : legionBean.getApplyPlayers().entrySet()) {
                int legionPlayerId = aEntry.getKey();
                PlayerBaseBean playerOfflineBean = PlayerOfflineManager.getInstance().getPlayerOfflineCache(legionPlayerId);
                WsMessageBase.IOLegionMember ioLegionMember = new WsMessageBase.IOLegionMember(legionPlayerId, playerOfflineBean.getName(),
                        playerOfflineBean.getIconid(), playerOfflineBean.getHeadid(), playerOfflineBean.getFrameid(), playerOfflineBean.getLevel(),
                        0, 0, 0, playerOfflineBean.getPower(), aEntry.getValue());
                respmsg.applylist.add(ioLegionMember);
            }
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
