package game.module.item.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.item.bean.Item;
import game.module.item.dao.ItemCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBag;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBag.C2SItemList.id, accessLimit = 250)
public class ItemListProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ItemListProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        sendResponse(playingRole);
    }

    public static void sendResponse(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        logger.info("item list,playerId={}", playerId);
        Map<Integer, Item> itemAll = ItemCache.getInstance().getItemTemplateKey(playerId);
        //ret
        WsMessageBag.S2CItemList respMsg = new WsMessageBag.S2CItemList();
        respMsg.item_list = new ArrayList<>(itemAll.size());
        for (Item aItem : itemAll.values()) {
            respMsg.item_list.add(new WsMessageBase.SimpleItemInfo(aItem.getTemplateId(), aItem.getCount()));
        }
        //send
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
