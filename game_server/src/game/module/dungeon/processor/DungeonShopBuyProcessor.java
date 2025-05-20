package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.dao.DungeonShopTemplateCache;
import game.module.log.constants.LogConstants;
import game.module.template.MyDungeonShopTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageDungeon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonShopBuy.id, accessLimit = 200)
public class DungeonShopBuyProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DungeonShopBuyProcessor.class);

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
        WsMessageDungeon.C2SDungeonShopBuy reqmsg = WsMessageDungeon.C2SDungeonShopBuy.parse(request);
        logger.info("dungeon shop buy,player={},req={}", playerId, reqmsg);
        int chapter = reqmsg.chapter;
        int node = reqmsg.node;
        MyDungeonShopTemplate dungeonShopTemplate = null;
        List<MyDungeonShopTemplate> myDungeonShopTemplates = DungeonShopTemplateCache.getInstance().getMyDungeonShopTemplates();
        for (MyDungeonShopTemplate myDungeonShopTemplate : myDungeonShopTemplates) {
            if (myDungeonShopTemplate.getChapter().equals(chapter) && myDungeonShopTemplate.getNode().equals(node)) {
                dungeonShopTemplate = myDungeonShopTemplate;
                break;
            }
        }
        //do
        if (dungeonShopTemplate != null) {
            for (RewardTemplateSimple aitem : dungeonShopTemplate.getItem()) {
                AwardUtils.changeRes(playingRole, aitem.getGSID(), aitem.getCOUNT(), LogConstants.MODULE_DUNGEON);
            }
            for (RewardTemplateSimple aitem : dungeonShopTemplate.getConsume()) {
                AwardUtils.changeRes(playingRole, aitem.getGSID(), -aitem.getCOUNT(), LogConstants.MODULE_DUNGEON);
            }
        }
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        Set<Integer> shopBuy = dungeonBean.getShopBuy();
        if (shopBuy == null) {
            shopBuy = new HashSet<>();
            dungeonBean.setShopBuy(shopBuy);
        }
        shopBuy.add(chapter * 100 + node);
        //save bean
        DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
        //ret
        WsMessageDungeon.S2CDungeonShopBuy respmsg = new WsMessageDungeon.S2CDungeonShopBuy();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
