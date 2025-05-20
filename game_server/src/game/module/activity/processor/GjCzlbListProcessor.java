package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.pay.bean.LibaoBuy;
import game.module.pay.dao.LibaoBuyCache;
import game.module.pay.logic.PayConstants;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdCzlbTemplate;
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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGjCzlbList.id, accessLimit = 200)
public class GjCzlbListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GjCzlbListProcessor.class);

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
        logger.info("get gjlb,playerId={}", playerId);
        //
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        WsMessageActivity.S2CGjCzlbList respmsg = new WsMessageActivity.S2CGjCzlbList();
        respmsg.list = new ArrayList<>();
        List<ZhdCzlbTemplate> czlbTemplates = ActivityWeekTemplateCache.getInstance().getGjCzlbTemplates();
        int idx = 0;
        for (ZhdCzlbTemplate zhdCzlbTemplate : czlbTemplates) {
            WsMessageBase.IOCzlb ioCzlb = new WsMessageBase.IOCzlb();
            ioCzlb.value = zhdCzlbTemplate.getValue();
            ioCzlb.price = zhdCzlbTemplate.getPrice();
            List<RewardTemplateSimple> items = zhdCzlbTemplate.getItems();
            ioCzlb.items = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : items) {
                ioCzlb.items.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            //购买记录
            int myBuyCount = 0;
            if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(PayConstants.PRODUCT_ID_GJLB + idx)) {
                myBuyCount = libaoBuy.getBuyCount().get(PayConstants.PRODUCT_ID_GJLB + idx);
            }
            ioCzlb.buytime = zhdCzlbTemplate.getBuytime() - myBuyCount;
            ioCzlb.limit = zhdCzlbTemplate.getLimit();
            List<RewardTemplateSimple> special = zhdCzlbTemplate.getSpecial();
            if (special != null && special.size() > 0) {
                ioCzlb.special = new ArrayList<>();
                for (RewardTemplateSimple rewardTemplateSimple : special) {
                    ioCzlb.special.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
            }
            ioCzlb.exp = zhdCzlbTemplate.getExp();
            ioCzlb.path = zhdCzlbTemplate.getPath();
            respmsg.list.add(ioCzlb);
            idx++;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
