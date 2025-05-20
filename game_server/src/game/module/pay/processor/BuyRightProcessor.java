package game.module.pay.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.ChargeInfoManager;
import game.module.pay.logic.RightTemplateCache;
import game.module.template.RewardTemplateSimple;
import game.module.template.RightTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHall.C2SBuyRight;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SBuyRight.id, accessLimit = 200)
public class BuyRightProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(BuyRightProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        C2SBuyRight reqMsg = C2SBuyRight.parse(request);
        String pid = reqMsg.pid;
        logger.info("buy right!player={},pid={}", playerId, pid);
        RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate(pid);
        if (rightTemplate == null) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CBuyRight.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        if (!rightTemplate.getCOIN().equals("YB")) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CBuyRight.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        Integer costYb = rightTemplate.getNUM();
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.YB, costYb)) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageHall.S2CBuyRight.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -costYb, LogConstants.MODULE_PAYMENT);
        //add
        for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_PAYMENT);
        }
        //add mark
        if (rightTemplate.getPID().equals("specialzx")) {
            ChargeInfoManager.getInstance().addZxns(playingRole);
        } else if (rightTemplate.getPID().equals("speciallm")) {
            ChargeInfoManager.getInstance().addJt(playingRole);
        }
        //ret
        WsMessageHall.S2CBuyRight respmsg = new WsMessageHall.S2CBuyRight();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
