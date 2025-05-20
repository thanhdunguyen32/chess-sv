package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.pay.bean.LibaoBuy;
import game.module.pay.dao.LibaoBuyCache;
import game.module.pay.logic.PayConstants;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdXsdhTemplate;
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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SXsdhList.id, accessLimit = 200)
public class XsdhListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(XsdhListProcessor.class);

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
        logger.info("get xsdh list,playerId={}", playerId);
        //
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        WsMessageActivity.S2CXsdhList respmsg = new WsMessageActivity.S2CXsdhList();
        respmsg.list = new ArrayList<>();
        List<ZhdXsdhTemplate> czlbTemplates = ActivityWeekTemplateCache.getInstance().getXsdhTemplates();
        int idx = 0;
        for (ZhdXsdhTemplate zhdXsdhTemplate : czlbTemplates) {
            WsMessageBase.IOXsdh1 ioXsdh = new WsMessageBase.IOXsdh1();
            ioXsdh.grid = zhdXsdhTemplate.getGrid();
            ioXsdh.grch = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdXsdhTemplate.getGrch()) {
                ioXsdh.grch.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            ioXsdh.consume = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdXsdhTemplate.getConsume()) {
                ioXsdh.consume.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            //购买记录
            int myBuyCount = 0;
            if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(PayConstants.PRODUCT_ID_XSDH + idx)) {
                myBuyCount = libaoBuy.getBuyCount().get(PayConstants.PRODUCT_ID_XSDH + idx);
            }
            ioXsdh.buytime = zhdXsdhTemplate.getBuytime() - myBuyCount;
            respmsg.list.add(ioXsdh);
            idx++;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
