package game.module.item.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.bean.Item;
import game.module.item.dao.EquipTemplateCache;
import game.module.item.dao.ItemCache;
import game.module.item.dao.TreasureTemplateCache;
import game.module.log.constants.LogConstants;
import game.module.template.EquipTemplate;
import game.module.template.TreasureTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBag;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBag.C2SItemSell.id, accessLimit = 200)
public class SellItemProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SellItemProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageBag.C2SItemSell reqmsg = WsMessageBag.C2SItemSell.parse(request);
        int playerId = playingRole.getId();
        logger.info("ItemSell,player={},req={}", playerId, reqmsg);
        int item_id = reqmsg.gsid;
        int num = reqmsg.count;
        if (num <= 0) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CItemSell.msgCode, 1301);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //exist
        Map<Integer, Item> itemCache = ItemCache.getInstance().getItemTemplateKey(playerId);
        if (!itemCache.containsKey(item_id) || itemCache.get(item_id).getCount() < num) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CItemSell.msgCode, 1301);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //is equip and treasure
        if (!EquipTemplateCache.getInstance().containsId(item_id) && !TreasureTemplateCache.getInstance().containsId(item_id)) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CItemSell.msgCode, 1302);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        int sumPrice = 0;
        EquipTemplate equipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(item_id);
        if (equipTemplate != null) {
            sumPrice = equipTemplate.getPRICE() * num;
        } else {
            TreasureTemplate treasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(item_id);
            sumPrice = treasureTemplate.getPRICE() * num;
        }
        //award
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, sumPrice, LogConstants.MODULE_BAG);
        //cost
        AwardUtils.changeRes(playingRole, item_id, -num, LogConstants.MODULE_BAG);
        //ret
        WsMessageBag.S2CItemSell respmsg = new WsMessageBag.S2CItemSell();
        respmsg.status = 0;
        respmsg.reward = Collections.singletonList(new WsMessageBase.RewardInfo(GameConfig.PLAYER.GOLD, sumPrice));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
