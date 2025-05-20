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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SPayGjdl.id, accessLimit = 200)
public class PayGjdlProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(PayGjdlProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageActivity.C2SPayGjdl reqmsg = WsMessageActivity.C2SPayGjdl.parse(request);
        logger.info("buy gjdl,player={},req={}", playerId, reqmsg);
        //
        int num_index = reqmsg.item_index;
        int buynum = reqmsg.buynum;
        if (buynum <= 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CPayGjdl.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        List<ZhdCjxg1Template> gjdlTemplates = ActivityWeekTemplateCache.getInstance().getGjdlTemplates();
        if (num_index < 0 || num_index >= gjdlTemplates.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CPayGjdl.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        ZhdCjxg1Template zhdSzhcTemplate = gjdlTemplates.get(num_index);
        //购买记录
        int myBuyCount = 0;
        String productId = PayConstants.PRODUCT_ID_GJDL + num_index;
        if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(productId)) {
            myBuyCount = libaoBuy.getBuyCount().get(productId);
        }
        //库存不足
        if (zhdSzhcTemplate.getBuytime() - myBuyCount - buynum < 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CPayGjdl.msgCode, 1548);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        List<RewardTemplateSimple> demandlist = zhdSzhcTemplate.getConsume();
        for (RewardTemplateSimple rewardTemplateSimple : demandlist) {
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(),
                    rewardTemplateSimple.getCOUNT() * buynum)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CPayGjdl.msgCode, 1549);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : demandlist) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT() * buynum, LogConstants.MODULE_ACTIVITY);
        }
        //award
        for (RewardTemplateSimple rewardTemplateSimple : zhdSzhcTemplate.getItems()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT() * buynum, LogConstants.MODULE_ACTIVITY);
        }
        LibaoBuyManager.getInstance().addLibaoBuy(playerId, productId, buynum);
        //ret
        WsMessageActivity.S2CPayGjdl respmsg = new WsMessageActivity.S2CPayGjdl();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
