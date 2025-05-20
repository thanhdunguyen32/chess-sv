package game.module.treasure.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.item.dao.TreasureTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.logic.MissionManager;
import game.module.template.TreasureTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@MsgCodeAnn(msgcode = WsMessageHero.C2STreasureLevel.id, accessLimit = 200)
public class TreasureLevelupProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TreasureLevelupProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2STreasureLevel reqmsg = WsMessageHero.C2STreasureLevel.parse(request);
        int playerId = playingRole.getId();
        logger.info("treasure levelup,player={},req={}", playerId, reqmsg);
        long general_uuid = reqmsg.guid;
        int treasure_id = reqmsg.treasure_id;
        WsMessageBase.RewardInfo[] consumes = reqmsg.consumes;
        //item exist
        Map<Long, GeneralBean> generalCache = GeneralCache.getInstance().getHeros(playerId);
        if (general_uuid > 0 && !generalCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureLevel.msgCode, 1372);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (general_uuid == 0 && treasure_id == 0 || general_uuid == 0 && treasure_id > 0 && !ItemManager.getInstance().checkItemEnough(
                playingRole.getPlayerBean(), treasure_id, 1)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureLevel.msgCode, 1372);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //material enough
        for (WsMessageBase.RewardInfo rewardInfo : consumes) {
            //不是宝物
            if (!TreasureTemplateCache.getInstance().containsId(rewardInfo.GSID)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureLevel.msgCode, 1373);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //count lack
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardInfo.GSID, rewardInfo.COUNT)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureLevel.msgCode, 1374);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //check duplicate
        Set<Integer> consumeSet = new HashSet<>();
        for (WsMessageBase.RewardInfo rewardInfo : consumes) {
            if (consumeSet.contains(rewardInfo.GSID)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureLevel.msgCode, 1375);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            consumeSet.add(rewardInfo.GSID);
        }
        //max level
        if (general_uuid > 0) {
            treasure_id = generalCache.get(general_uuid).getTreasure();
        }
        TreasureTemplate treasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(treasure_id);
        if (treasureTemplate.getNEXT() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureLevel.msgCode, 1376);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is material enough
        Integer needExp = treasureTemplate.getNEXT().getEXP();
        int sumExp = 0;
        for (WsMessageBase.RewardInfo rewardInfo : consumes) {
            TreasureTemplate treasureTemplate1 = TreasureTemplateCache.getInstance().getTreasureTemplateById(rewardInfo.GSID);
            sumExp += treasureTemplate1.getEXP() * rewardInfo.COUNT;
        }
        if (sumExp < needExp) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CTreasureLevel.msgCode, 1377);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        WsMessageHero.S2CTreasureLevel respmsg = new WsMessageHero.S2CTreasureLevel();
        Integer nextTreasureId = treasureTemplate.getNEXT().getID();
        if (general_uuid > 0) {
            GeneralBean generalBean = generalCache.get(general_uuid);
            generalBean.setTreasure(nextTreasureId);
            GeneralManager.getInstance().updatePropertyAndPower(generalBean);
            GeneralDaoHelper.asyncUpdateHero(generalBean);
            respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        } else {
            //new treasure
            AwardUtils.changeRes(playingRole, nextTreasureId, 1, LogConstants.MODULE_TREASURE);
            //cost
            AwardUtils.changeRes(playingRole, treasure_id, -1, LogConstants.MODULE_TREASURE);
        }
        //treasure add star change mission
        MissionManager.getInstance().treasureStarChangeProgress(playingRole,nextTreasureId);
        //cost materials
        for (WsMessageBase.RewardInfo rewardInfo : consumes) {
            AwardUtils.changeRes(playingRole, rewardInfo.GSID, -rewardInfo.COUNT, LogConstants.MODULE_TREASURE);
        }
        //ret
        respmsg.guid = general_uuid;
        respmsg.treasure_id = treasure_id;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
