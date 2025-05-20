package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonShopTemplateCache;
import game.module.template.MyDungeonShopTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonShopList.id, accessLimit = 200)
public class DungeonShopListProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DungeonShopListProcessor.class);

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
        logger.info("dungeon shop list,player={}", playerId);
        //ret
        WsMessageDungeon.S2CDungeonShopList respmsg = new WsMessageDungeon.S2CDungeonShopList();
        List<MyDungeonShopTemplate> myDungeonShopTemplates = DungeonShopTemplateCache.getInstance().getMyDungeonShopTemplates();
        respmsg.list = new ArrayList<>();
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        Set<Integer> shopBuy = dungeonBean.getShopBuy();
        for (MyDungeonShopTemplate myDungeonShopTemplate : myDungeonShopTemplates) {
            Integer chapter = myDungeonShopTemplate.getChapter();
            Integer node = myDungeonShopTemplate.getNode();
            if (shopBuy != null && shopBuy.contains(chapter * 100 + node)) {
                continue;
            }
            WsMessageBase.IODungeonShop ioDungeonShop = new WsMessageBase.IODungeonShop();
            respmsg.list.add(ioDungeonShop);
            ioDungeonShop.chapter = chapter;
            ioDungeonShop.node = myDungeonShopTemplate.getNode();
            ioDungeonShop.disc = myDungeonShopTemplate.getDisc();
            ioDungeonShop.quality = myDungeonShopTemplate.getQuality();
            ioDungeonShop.item = new ArrayList<>(myDungeonShopTemplate.getItem().size());
            for (RewardTemplateSimple rewardTemplateSimple : myDungeonShopTemplate.getItem()) {
                ioDungeonShop.item.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            ioDungeonShop.consume = new ArrayList<>(myDungeonShopTemplate.getConsume().size());
            for (RewardTemplateSimple rewardTemplateSimple : myDungeonShopTemplate.getConsume()) {
                ioDungeonShop.consume.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
