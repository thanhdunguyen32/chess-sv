package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.logic.LegionManager;
import game.module.mail.logic.MailManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionDismiss.id, accessLimit = 200)
public class LegionDismissProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionDismissProcessor.class);

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
        WsMessageLegion.C2SLegionDismiss reqmsg = WsMessageLegion.C2SLegionDismiss.parse(request);
        logger.info("legion dismiss!player={},req={}", playerId, reqmsg);
        //is in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionDismiss.msgCode, 1516);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not ceo
        int myLegionPos = LegionManager.getInstance().getMyLegionPos(playerId, legionId);
        if (myLegionPos != 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionDismiss.msgCode, 1518);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //check is member
        int otherPlayerId = reqmsg.rid;
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        if (!legionBean.getDbLegionPlayers().getMembers().containsKey(otherPlayerId)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionDismiss.msgCode, 1525);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //back items
        LegionPlayer legionOtherPlayer = legionBean.getDbLegionPlayers().getMembers().get(otherPlayerId);
        Map<Integer, Integer> backItemMap = LegionManager.getInstance().techResetAllBack(legionOtherPlayer);
        if (backItemMap != null && backItemMap.size() > 0) {
            String mailTitle = "Phần thưởng hoạt động trở về quân đội"; //"退出军团技能升级返还"
            String mailContent = "100% trả lại tài nguyên như sau:"; //"100%返还资源如下：";
            MailManager.getInstance().sendSysMailToSingle(otherPlayerId, mailTitle, mailContent, backItemMap);
        }
        //do
        synchronized (legionBean) {
            legionBean.getDbLegionPlayers().getMembers().remove(otherPlayerId);
            LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        }
        //ret
        WsMessageLegion.S2CLegionDismiss respmsg = new WsMessageLegion.S2CLegionDismiss();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
