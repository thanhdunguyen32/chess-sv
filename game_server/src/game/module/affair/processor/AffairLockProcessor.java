package game.module.affair.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.affair.bean.AffairBean;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairCache;
import game.module.affair.dao.AffairDaoHelper;
import game.module.affair.logic.AffairConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAffair;
import ws.WsMessageHall;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageAffair.C2SAffairLock.id, accessLimit = 200)
public class AffairLockProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(AffairLockProcessor.class);

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
        WsMessageAffair.C2SAffairLock reqmsg = WsMessageAffair.C2SAffairLock.parse(request);
        int playerId = playingRole.getId();
        logger.info("affair lock!player={},req={}", playerId, reqmsg);
        int affair_index = reqmsg.affair_index;
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs == null || playerAffairs.getDbAffairs() == null || playerAffairs.getDbAffairs().getAffairList() == null || affair_index<0 ||
                affair_index >= playerAffairs.getDbAffairs().getAffairList().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairLock.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is new
        AffairBean affairBean = playerAffairs.getDbAffairs().getAffairList().get(affair_index);
        if (affairBean.getStatus() != AffairConstants.AFFAIR_STATUS_NEW) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairLock.msgCode, 1420);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        affairBean.setLock(affairBean.getLock() == 0 ? 1 : 0);
        AffairDaoHelper.asyncUpdateAffair(playerAffairs);
        //ret
        WsMessageAffair.S2CAffairLock respmsg = new WsMessageAffair.S2CAffairLock();
        respmsg.affair_index = affair_index;
        respmsg.item_list = AffairListProcessor.buildAffairList(playerId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
