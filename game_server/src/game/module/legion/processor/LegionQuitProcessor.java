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
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionQuit.id, accessLimit = 200)
public class LegionQuitProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionQuitProcessor.class);

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
        logger.info("legion quit!player={}", playerId);
        //is in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionQuit.msgCode, 1516);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is ceo
        int myLegionPos = LegionManager.getInstance().getMyLegionPos(playerId, legionId);
        if (myLegionPos == 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionQuit.msgCode, 1518);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //back items
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        LegionPlayer legionOtherPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        Map<Integer, Integer> backItemMap = LegionManager.getInstance().techResetAllBack(legionOtherPlayer);
        if (backItemMap != null && backItemMap.size() > 0) {
            String mailTitle = "Phần thưởng hoạt động trở về quân đội"; //"退出军团技能升级返还"
            String mailContent = "100% trả lại tài nguyên như sau:"; //"100%返还资源如下：";
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, backItemMap);
        }
        //do
        synchronized (legionBean) {
            legionBean.getDbLegionPlayers().getMembers().remove(playerId);
            LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        }
        //ret
        WsMessageLegion.S2CLegionQuit respmsg = new WsMessageLegion.S2CLegionQuit();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
