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
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageHall;

import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SBuySzhc.id, accessLimit = 200)
public class BuySzhcProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(BuySzhcProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageActivity.C2SBuySzhc reqmsg = WsMessageActivity.C2SBuySzhc.parse(request);
        logger.info("buy Szhc,player={},req={}", playerId, reqmsg);
        //
        int num_index = reqmsg.num_index;
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        List<ZhdSzhcTemplate> szhcTemplates = ActivityWeekTemplateCache.getInstance().getSzhcTemplate();
        if (num_index < 0 || num_index >= szhcTemplates.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CBuySzhc.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        ZhdSzhcTemplate zhdSzhcTemplate = szhcTemplates.get(num_index);
        //购买记录
        int myBuyCount = 0;
        String productId = PayConstants.PRODUCT_ID_SZHC + num_index;
        if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(productId)) {
            myBuyCount = libaoBuy.getBuyCount().get(productId);
        }
        if (zhdSzhcTemplate.getBuytime() - myBuyCount <= 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CBuySzhc.msgCode, 1548);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        List<RewardTemplateSimple> demandlist = zhdSzhcTemplate.getDemand();
        for (RewardTemplateSimple rewardTemplateSimple : demandlist) {
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT())) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CBuySzhc.msgCode, 1549);
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
        for (RewardTemplateSimple rewardTemplateSimple : zhdSzhcTemplate.getConsume()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
        }
        LibaoBuyManager.getInstance().addLibaoBuy(playerId, productId);
        //ret
        WsMessageActivity.S2CBuySzhc respmsg = new WsMessageActivity.S2CBuySzhc();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
