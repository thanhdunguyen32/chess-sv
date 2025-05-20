package game.module.treasure.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2STreasureTakeon.id, accessLimit = 200)
public class TreasureTakeonProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TreasureTakeonProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2STreasureTakeon reqmsg = WsMessageHero.C2STreasureTakeon.parse(request);
        int playerId = playingRole.getId();
        logger.info("general takeon treasure,player={},req={}", playerId, reqmsg);
        long general_uuid = reqmsg.guid;
        int treasure_id = reqmsg.treasure_id;
        //general exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureTakeon.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //treasure exist
        if (treasure_id > 0 && !ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), treasure_id, 1)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureTakeon.msgCode, 1370);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        GeneralBean generalBean = heroCache.get(general_uuid);
        if (treasure_id == 0) {//脱宝物
            int oldTreasureId = generalBean.getTreasure();
            generalBean.setTreasure(0);
            GeneralManager.getInstance().updatePropertyAndPower(generalBean);
            GeneralDaoHelper.asyncUpdateHero(generalBean);
            //back
            if (oldTreasureId > 0) {
                AwardUtils.changeRes(playingRole, oldTreasureId, 1, LogConstants.MODULE_TREASURE);
            }
        } else {//穿宝物
            int oldTreasureId = generalBean.getTreasure();
            generalBean.setTreasure(treasure_id);
            //替换
            if(oldTreasureId >0){
                AwardUtils.changeRes(playingRole, oldTreasureId, 1, LogConstants.MODULE_TREASURE);
            }
            GeneralManager.getInstance().updatePropertyAndPower(generalBean);
            GeneralDaoHelper.asyncUpdateHero(generalBean);
            //cost
            AwardUtils.changeRes(playingRole, treasure_id, -1, LogConstants.MODULE_TREASURE);
        }
        //ret
        WsMessageHero.S2CTreasureTakeon respmsg = new WsMessageHero.S2CTreasureTakeon();
        respmsg.guid = general_uuid;
        respmsg.treasure_id = treasure_id;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
