package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralResetTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.logic.GeneralConstants;
import game.module.hero.logic.GeneralManager;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralReborn.id, accessLimit = 100)
public class GeneralRebornProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GeneralRebornProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralReset reqMsg = WsMessageHero.C2SGeneralReset.parse(request);
        int playerId = playingRole.getId();
        logger.info("general reborn,to5star,player={},req={}", playerId, reqMsg);
        long general_uuid = reqMsg.general_uuid;
        int action_type = reqMsg.action_type;
        //是否存在
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralReset.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //star
        GeneralBean generalBean = heroCache.get(general_uuid);
        Integer generalStar = generalBean.getStar();
        if (generalStar < 6 || generalStar > 13) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralReset.msgCode, 1392);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        List<Integer> generalResetConfig = GeneralResetTemplateCache.getInstance().getGeneralResetConfig(generalStar);
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), generalResetConfig.get(0), generalResetConfig.get(1))) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralReset.msgCode, 1393);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        boolean isDo = action_type > 0;
        Map<Integer, Integer> rewardMap = new HashMap<>();
        GeneralManager.getInstance().generalRebornAwards(playingRole, generalBean, rewardMap, isDo);
        //random generals
        List<WsMessageBase.IOAwardRandomGeneral> randomGeneralList = new ArrayList<>();
        int[] randomGenerals = GeneralConstants.GENERAL_REBORN_RANDOM_GSID[generalStar - 6];
        for (int i = 0; i < randomGenerals.length; i += 2) {
            int aStar = randomGenerals[i];
            int aCount = randomGenerals[i + 1];
            randomGeneralList.add(new WsMessageBase.IOAwardRandomGeneral(aCount, aStar, 0, 0));
        }
        if (isDo) {
            //award
            for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
                AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.MODULE_GENERAL_REBORN);
            }
            //general remove
            GeneralManager.getInstance().removeGeneral(playingRole, general_uuid);
            //cost
            AwardUtils.changeRes(playingRole, generalResetConfig.get(0), -generalResetConfig.get(1), LogConstants.MODULE_GENERAL_REBORN);
            //random generals
            List<RewardTemplateSimple> randomGeneralRewards = new ArrayList<>();
            for (int i = 0; i < randomGenerals.length; i += 2) {
                int aStar = randomGenerals[i];
                int aCount = randomGenerals[i + 1];
                List<Integer> generalsByStar = GeneralTemplateCache.getInstance().getGeneralByStar(aStar);
                for (int j = 0; j < aCount; j++) {
                    int aRandIndex = RandomUtils.nextInt(0, generalsByStar.size());
                    Integer aGsid = generalsByStar.get(aRandIndex);
                    AwardUtils.changeRes(playingRole, aGsid, 1, LogConstants.MODULE_GENERAL_REBORN);
                    randomGeneralRewards.add(new RewardTemplateSimple(aGsid, 1));
                }
            }
            //ret reward
            putRewards(rewardMap,randomGeneralRewards);
        }
        //ret
        WsMessageHero.S2CGeneralReborn respmsg = new WsMessageHero.S2CGeneralReborn();
        respmsg.items = new ArrayList<>(rewardMap.size());
        for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
            respmsg.items.add(new WsMessageBase.RewardInfo(aEntry.getKey(), aEntry.getValue()));
        }
        respmsg.general = randomGeneralList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void putRewards(Map<Integer, Integer> rewardMap, List<RewardTemplateSimple> rewardConfig) {
        for (RewardTemplateSimple rewardTemplateSimple : rewardConfig) {
            Integer gsid = rewardTemplateSimple.getGSID();
            Integer count = rewardTemplateSimple.getCOUNT();
            if (rewardMap.containsKey(gsid)) {
                rewardMap.put(gsid, count + rewardMap.get(gsid));
            } else {
                rewardMap.put(gsid, count);
            }
        }
    }

}
