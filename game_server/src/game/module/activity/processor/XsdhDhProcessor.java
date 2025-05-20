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
import game.module.template.ZhdSzhcTemplate;
import game.module.template.ZhdXsdhTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageHall;

import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SXsdhDh.id, accessLimit = 200)
public class XsdhDhProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(XsdhDhProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageActivity.C2SXsdhDh reqmsg = WsMessageActivity.C2SXsdhDh.parse(request);
        logger.info("xsdh dh,player={},req={}", playerId, reqmsg);
        //
        int num_index = reqmsg.item_index;
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        List<ZhdXsdhTemplate> xsdhTemplateList = ActivityWeekTemplateCache.getInstance().getXsdhTemplates();
        if (num_index < 0 || num_index >= xsdhTemplateList.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CXsdhDh.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        ZhdXsdhTemplate zhdXsdhTemplate = xsdhTemplateList.get(num_index);
        //购买记录
        int myBuyCount = 0;
        String productId = PayConstants.PRODUCT_ID_XSDH + num_index;
        if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(productId)) {
            myBuyCount = libaoBuy.getBuyCount().get(productId);
        }
        if (zhdXsdhTemplate.getBuytime() - myBuyCount <= 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CXsdhDh.msgCode, 1548);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        List<RewardTemplateSimple> demandlist = zhdXsdhTemplate.getConsume();
        for (RewardTemplateSimple rewardTemplateSimple : demandlist) {
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT())) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CXsdhDh.msgCode, 1549);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : demandlist) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
        }
        //award
        for (RewardTemplateSimple rewardTemplateSimple : zhdXsdhTemplate.getGrch()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
        }
        LibaoBuyManager.getInstance().addLibaoBuy(playerId, productId);
        //ret
        WsMessageActivity.S2CXsdhDh respmsg = new WsMessageActivity.S2CXsdhDh();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
