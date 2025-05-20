package game.module.item.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import game.module.manor.logic.ManorConstants;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBag;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.Collections;

@MsgCodeAnn(msgcode = WsMessageBag.C2SBuyItem.id, accessLimit = 200)
public class ItemBuyProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ItemBuyProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBag.C2SBuyItem reqmsg = WsMessageBag.C2SBuyItem.parse(request);
        logger.info("bag expand,player={},req={}", playerId, reqmsg);
        if (reqmsg.count <= 0) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CBuyItem.msgCode, 1330);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        if (!ItemConstants.BUY_ITEM_COST.containsKey(reqmsg.gsid)) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CBuyItem.msgCode, 1332);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //yb enough
        int costYb = ItemConstants.BUY_ITEM_COST.get(reqmsg.gsid);
        if (playingRole.getPlayerBean().getMoney() < costYb * reqmsg.count) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CBuyItem.msgCode, 1331);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //daily buy count limit
        if (reqmsg.gsid == GameConfig.PLAYER.FOOD) {
            int todayBuyCount = PlayerManager.getInstance().getOtherCount(playerId, ManorConstants.MANOR_BUY_COUNT_MARK);
            if (todayBuyCount + reqmsg.count > ManorConstants.MANOR_DAILY_BUY_MAX) {
                WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CBuyItem.msgCode, 1353);
                playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        AwardUtils.changeRes(playingRole, reqmsg.gsid, reqmsg.count, LogConstants.MODULE_BAG);
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -costYb * reqmsg.count, LogConstants.MODULE_BAG);
        //save count
        if (reqmsg.gsid == GameConfig.PLAYER.FOOD) {
            AwardUtils.changeRes(playingRole, ManorConstants.MANOR_BUY_COUNT_MARK, reqmsg.count, LogConstants.MODULE_BAG);
        }
        //ret
        WsMessageBag.S2CBuyItem respmsg = new WsMessageBag.S2CBuyItem();
        respmsg.rewards = Collections.singletonList(new WsMessageBase.RewardInfo(reqmsg.gsid, reqmsg.count));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
