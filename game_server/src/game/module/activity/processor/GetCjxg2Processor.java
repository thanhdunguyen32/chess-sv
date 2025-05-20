package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.pay.bean.LibaoBuy;
import game.module.pay.dao.LibaoBuyCache;
import game.module.pay.logic.PayConstants;
import game.module.season.dao.SeasonCache;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdCjxg2Template;
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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGetCjxg2.id, accessLimit = 200)
public class GetCjxg2Processor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetCjxg2Processor.class);

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
        logger.info("get cjxg2,playerId={}", playerId);
        //
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        WsMessageActivity.S2CGetCjxg2 respmsg = new WsMessageActivity.S2CGetCjxg2();
        respmsg.list = new ArrayList<>();
        List<List<ZhdCjxg2Template>> cjxg2TemplatesList = ActivityWeekTemplateCache.getInstance().getCjxg2Templates();
        Integer seasonId = SeasonCache.getInstance().getBattleSeason().getSeason();
        List<ZhdCjxg2Template> cjxg2Templates = cjxg2TemplatesList.get(seasonId - 1);
        int idx = 0;
        for (ZhdCjxg2Template zhdCjxg2Template : cjxg2Templates) {
            WsMessageBase.IOCjxg2 ioCjxg2 = new WsMessageBase.IOCjxg2();
            ioCjxg2.value = zhdCjxg2Template.getValue();
            ioCjxg2.price = zhdCjxg2Template.getPrice();
            List<RewardTemplateSimple> items = zhdCjxg2Template.getItems();
            ioCjxg2.items = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : items) {
                ioCjxg2.items.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            //购买记录
            int myBuyCount = 0;
            if (libaoBuy != null && libaoBuy.getBuyCount() != null && libaoBuy.getBuyCount().containsKey(PayConstants.PRODUCT_ID_CJXG2 + idx)) {
                myBuyCount = libaoBuy.getBuyCount().get(PayConstants.PRODUCT_ID_CJXG2 + idx);
            }
            ioCjxg2.buytime = zhdCjxg2Template.getBuytime() - myBuyCount;
            ioCjxg2.icon = zhdCjxg2Template.getIcon();
            List<RewardTemplateSimple> special = zhdCjxg2Template.getSpecial();
            if (special != null && special.size() > 0) {
                ioCjxg2.special = new ArrayList<>();
                for (RewardTemplateSimple rewardTemplateSimple : special) {
                    ioCjxg2.special.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
            }
            ioCjxg2.bg1 = zhdCjxg2Template.getBg1();
            ioCjxg2.bg2 = zhdCjxg2Template.getBg2();
            ioCjxg2.hero = zhdCjxg2Template.getHero();
            ioCjxg2.heroname = zhdCjxg2Template.getHeroname();
            ioCjxg2.txbig = zhdCjxg2Template.getTxbig();
            ioCjxg2.normal = zhdCjxg2Template.getNormal();
            ioCjxg2.check = zhdCjxg2Template.getCheck();
            respmsg.list.add(ioCjxg2);
            idx++;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
