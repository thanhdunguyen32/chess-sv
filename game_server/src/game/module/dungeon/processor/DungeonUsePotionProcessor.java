package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.logic.DungeonConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageDungeon;
import ws.WsMessageHall;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonUsePotion.id, accessLimit = 200)
public class DungeonUsePotionProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DungeonUsePotionProcessor.class);

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
        WsMessageDungeon.C2SDungeonUsePotion reqmsg = WsMessageDungeon.C2SDungeonUsePotion.parse(request);
        logger.info("dungeon use potion,player={},req={}", playerId, reqmsg);
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean == null || dungeonBean.getOnlineGenerals() == null || !dungeonBean.getOnlineGenerals().containsKey(reqmsg.guid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonUsePotion.msgCode, 1442);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //potion enough
        if (dungeonBean.getPotions() == null || !dungeonBean.getPotions().containsKey(DungeonConstants.REWARD_POTION_ID)
                || dungeonBean.getPotions().get(DungeonConstants.REWARD_POTION_ID) <= 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonUsePotion.msgCode, 1446);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        Integer generalHpPercent = dungeonBean.getOnlineGenerals().get(reqmsg.guid);
        generalHpPercent += 5000;
        if (generalHpPercent > 10000) {
            generalHpPercent = 10000;
        }
        dungeonBean.getOnlineGenerals().put(reqmsg.guid, generalHpPercent);
        //sub
        dungeonBean.getPotions().put(DungeonConstants.REWARD_POTION_ID, dungeonBean.getPotions().get(DungeonConstants.REWARD_POTION_ID) - 1);
        //save
        DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
        //ret
        WsMessageDungeon.S2CDungeonUsePotion respmsg = new WsMessageDungeon.S2CDungeonUsePotion(reqmsg.guid, generalHpPercent);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
