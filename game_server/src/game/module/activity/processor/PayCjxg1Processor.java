package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.pay.bean.LibaoBuy;
import game.module.pay.dao.LibaoBuyCache;
import game.module.pay.logic.LibaoBuyManager;
import game.module.pay.logic.PayConstants;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdCjxg1Template;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageHall;

import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SPayCjxg1.id, accessLimit = 200)
public class PayCjxg1Processor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(PayCjxg1Processor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageActivity.C2SPayCjxg1 reqmsg = WsMessageActivity.C2SPayCjxg1.parse(request);
        logger.info("buy cjxg1,player={},req={}", playerId, reqmsg);
        //
        int num_index = reqmsg.num_index;
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        List<ZhdCjxg1Template> cjxg1Templates = ActivityWeekTemplateCache.getInstance().getCjxg1Templates();
        if (num_index < 0 || num_index >= cjxg1Templates.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CPayCjxg1.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        ZhdCjxg1Template zhdCjxg1Template = cjxg1Templates.get(num_index);
        //购买记录
        int myBuyCount = 0;
        String productId = PayConstants.PRODUCT_ID_CJXG1 + num_index;
        if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(productId)) {
            myBuyCount = libaoBuy.getBuyCount().get(productId);
        }
        if (zhdCjxg1Template.getBuytime() - myBuyCount <= 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CPayCjxg1.msgCode, 1548);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg1Template.getConsume()) {
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT())) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CPayCjxg1.msgCode, 1549);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //vip
        if (playingRole.getPlayerBean().getVipLevel() < zhdCjxg1Template.getViple()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CPayCjxg1.msgCode, 1550);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg1Template.getConsume()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
        }
        //award
        for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg1Template.getItems()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
        }
        LibaoBuyManager.getInstance().addLibaoBuy(playerId, productId);
        //ret
        WsMessageActivity.S2CPayCjxg1 respmsg = new WsMessageActivity.S2CPayCjxg1();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
