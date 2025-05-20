package game.module.item.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.dao.*;
import game.module.item.logic.BagManager;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBag;
import ws.WsMessageHall;

@MsgCodeAnn(msgcode = WsMessageBag.C2SBagExpand.id, accessLimit = 200)
public class BagExpandProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BagExpandProcessor.class);

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
        logger.info("bag expand,player={}", playerId);
        //是否上限
        int maxSize = GeneralBagTemplateCache.getInstance().getSize();
        int bagBuyCount = PlayerManager.getInstance().getOtherCount(playerId, ItemConstants.GENERAL_BAG_BUY_COUNT);
        if(bagBuyCount >= maxSize){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CBagExpand.msgCode, 3000);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //yb是否够
        int moneyCost = GeneralBagTemplateCache.getInstance().getMoneyCost(bagBuyCount);
        if (playingRole.getPlayerBean().getMoney() < moneyCost) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CBagExpand.msgCode, 132);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        AwardUtils.changeRes(playingRole,ItemConstants.GENERAL_BAG_BUY_COUNT, 1, LogConstants.MODULE_BAG);
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -moneyCost, LogConstants.MODULE_BAG);
        int bagSize = BagManager.getInstance().getGeneralBagSize(playerId, playingRole.getPlayerBean().getVipLevel());
        WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(ItemConstants.BAG_SPACE,bagSize);
        playingRole.write(pushmsg.build(playingRole.alloc()));
        //ret
        WsMessageBag.S2CBagExpand respmsg = new WsMessageBag.S2CBagExpand();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
