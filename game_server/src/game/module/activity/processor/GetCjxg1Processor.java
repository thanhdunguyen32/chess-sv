package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.pay.bean.LibaoBuy;
import game.module.pay.dao.LibaoBuyCache;
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
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGetCjxg1.id, accessLimit = 200)
public class GetCjxg1Processor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetCjxg1Processor.class);

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
        logger.info("get Cjxg1,playerId={}", playerId);
        //
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        WsMessageActivity.S2CGetCjxg1 respmsg = new WsMessageActivity.S2CGetCjxg1();
        respmsg.list = new ArrayList<>();
        List<ZhdCjxg1Template> cjxg1Templates = ActivityWeekTemplateCache.getInstance().getCjxg1Templates();
        int idx = 0;
        for (ZhdCjxg1Template zhdCjxg1Template : cjxg1Templates) {
            WsMessageBase.IOCjxg1 ioCjxg1 = new WsMessageBase.IOCjxg1();
            ioCjxg1.viple = zhdCjxg1Template.getViple();
            List<RewardTemplateSimple> consumes = zhdCjxg1Template.getConsume();
            ioCjxg1.consume = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : consumes) {
                ioCjxg1.consume.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            List<RewardTemplateSimple> items = zhdCjxg1Template.getItems();
            ioCjxg1.items = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : items) {
                ioCjxg1.items.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            //购买记录
            int myBuyCount = 0;
            if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(PayConstants.PRODUCT_ID_CJXG1 + idx)) {
                myBuyCount = libaoBuy.getBuyCount().get(PayConstants.PRODUCT_ID_CJXG1 + idx);
            }
            ioCjxg1.buytime = zhdCjxg1Template.getBuytime() - myBuyCount;
            respmsg.list.add(ioCjxg1);
            idx++;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
