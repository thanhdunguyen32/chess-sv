package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.pay.bean.LibaoBuy;
import game.module.pay.dao.LibaoBuyCache;
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
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGetSzhc.id, accessLimit = 200)
public class GetSzhcProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetSzhcProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("get Szhc,playerId={}", playerId);
        //
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        WsMessageActivity.S2CGetSzhc respmsg = new WsMessageActivity.S2CGetSzhc();
        respmsg.list = new ArrayList<>();
        List<ZhdSzhcTemplate> szhcTemplates = ActivityWeekTemplateCache.getInstance().getSzhcTemplate();
        int idx = 0;
        for (ZhdSzhcTemplate zhdSzhcTemplate : szhcTemplates) {
            WsMessageBase.IOSzhc ioSzhc = new WsMessageBase.IOSzhc();
            ioSzhc.consume = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdSzhcTemplate.getConsume()) {
                ioSzhc.consume.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            ioSzhc.demand = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdSzhcTemplate.getDemand()) {
                ioSzhc.demand.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            //购买记录
            int myBuyCount = 0;
            if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(PayConstants.PRODUCT_ID_SZHC + idx)) {
                myBuyCount = libaoBuy.getBuyCount().get(PayConstants.PRODUCT_ID_SZHC + idx);
            }
            ioSzhc.buytime = zhdSzhcTemplate.getBuytime() - myBuyCount;
            respmsg.list.add(ioSzhc);
            idx++;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
