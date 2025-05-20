package game.module.treasure.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.dao.TreasureTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.TreasureTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHero;

@MsgCodeAnn(msgcode = WsMessageHero.C2STreasureSell.id, accessLimit = 200)
public class TreasureSellProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TreasureSellProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2STreasureSell reqmsg = WsMessageHero.C2STreasureSell.parse(request);
        int playerId = playingRole.getId();
        logger.info("treasure sell,player={},req={}", playerId, reqmsg);
        int treasure_id = reqmsg.treasure_id;
        int sell_count = reqmsg.count;
        if(sell_count <= 0){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureSell.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //treasure exist
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), treasure_id, sell_count)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureSell.msgCode, 1371);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        TreasureTemplate treasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(treasure_id);
        Integer price1 = treasureTemplate.getPRICE();
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, price1 * sell_count, LogConstants.MODULE_TREASURE);
        //cost
        AwardUtils.changeRes(playingRole, treasure_id, -sell_count, LogConstants.MODULE_TREASURE);
        //ret
        WsMessageHero.S2CTreasureSell respmsg = new WsMessageHero.S2CTreasureSell(treasure_id, sell_count);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
