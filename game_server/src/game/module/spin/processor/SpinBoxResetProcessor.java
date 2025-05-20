package game.module.spin.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.spin.bean.SpinBean;
import game.module.spin.dao.SpinCache;
import game.module.spin.dao.SpinTemplateCache;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageSpin;

import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageSpin.C2SSpinBoxReset.id, accessLimit = 200)
public class SpinBoxResetProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpinBoxResetProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageSpin.C2SSpinBoxReset reqMsg = WsMessageSpin.C2SSpinBoxReset.parse(request);
        final int buy_type = reqMsg.type;
        int playerId = playingRole.getId();
        logger.info("spin box reset,player={},req={}", playerId, reqMsg);
        //params check
        SpinBean spinBean = SpinCache.getInstance().getSpinBean(playerId);
        if (spinBean == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBoxReset.msgCode, 1362);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (buy_type != 1 && buy_type != 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBoxReset.msgCode, 1360);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is all open
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        Map<String, Object> spinTemplate = SpinTemplateCache.getInstance().getSpinTemplate(buy_type);
        List<Map<String, Object>> scoreShopList = (List<Map<String, Object>>) spinTemplate.get("SCORE");
        for (Map<String, Object> aScoreConfig : scoreShopList) {
            int signId = (Integer) aScoreConfig.get("SIGN");
            if (!playerOthers.containsKey(signId) || playerOthers.get(signId).getCount() <= 0) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBoxReset.msgCode, 1367);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            }
        }
        //do
        //save buy info
        for (Map<String, Object> aScoreConfig : scoreShopList) {
            int signId = (Integer) aScoreConfig.get("SIGN");
            AwardUtils.setRes(playingRole, signId, 0, true);
        }
        //save bean
        Map<String, Object> spin1Config = (Map) spinTemplate.get("1");
        List<Integer> drops = (List<Integer>) spin1Config.get("DROP");
        int scoreTemplateId = drops.get(0);
        AwardUtils.setRes(playingRole, scoreTemplateId, 0, true);
        //award
        WsMessageSpin.S2CSpinBoxOpen respmsg = new WsMessageSpin.S2CSpinBoxOpen();
        respmsg.type = buy_type;
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }
}
