package game.module.spin.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.spin.dao.SpinTemplateCache;
import game.module.spin.logic.SpinConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageSpin;

import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageSpin.C2SSpinBuy.id, accessLimit = 200)
public class SpinBuyProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpinBuyProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageSpin.C2SSpinBuy reqMsg = WsMessageSpin.C2SSpinBuy.parse(request);
        int count = reqMsg.count;
        int playerId = playingRole.getId();
        logger.info("spin buy,player={},req={}", playerId, reqMsg);
        if(count <= 0){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBuy.msgCode, 1366);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //yb enough
        int costYb = count * SpinConstants.SPIN_BUY_COST_YB;
        if (playingRole.getPlayerBean().getMoney() < costYb) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBuy.msgCode, 1366);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -costYb, LogConstants.SPIN);
        //reward
        Map<String, Object> spinTemplate = SpinTemplateCache.getInstance().getLookStarNormal();
        Map<String, Object> spin1Config = (Map) spinTemplate.get(String.valueOf(1));
        int gsid = (Integer) spin1Config.get("GSID");
        AwardUtils.changeRes(playingRole, gsid, count, LogConstants.SPIN);
        //ret
        WsMessageSpin.S2CSpinBuy respmsg = new WsMessageSpin.S2CSpinBuy();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
