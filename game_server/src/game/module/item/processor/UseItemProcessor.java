package game.module.item.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityXiangouManager;
import game.module.affair.logic.AffairManager;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.hero.dao.GeneralChipTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.logic.GeneralConstants;
import game.module.item.bean.Item;
import game.module.item.dao.*;
import game.module.item.logic.BagManager;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.PaymentConstants;
import game.module.pay.logic.PaymentManager;
import game.module.template.*;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBag;
import ws.WsMessageBag.C2SUseItem;
import ws.WsMessageBag.S2CUseItem;
import ws.WsMessageBase;
import ws.WsMessageHall.S2CErrorCode;

import java.util.*;

@MsgCodeAnn(msgcode = C2SUseItem.id, accessLimit = 250)
public class UseItemProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(UseItemProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        C2SUseItem useItemMsg = C2SUseItem.parse(request);
        int playerId = playingRole.getId();
        logger.info("use item,playerId={},req={}", playerId, useItemMsg);
        Map<Integer, Item> itemAll = ItemCache.getInstance().getItemTemplateKey(playerId);
        // 道具不存在
        int template_id = useItemMsg.gsid;
        int itemCount = useItemMsg.count;
        if (itemCount < 0) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), template_id, itemCount)) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        int itemType = ItemManager.getInstance().getItemTypeByGsid(template_id);
        switch (itemType) {
            case GameConfig.ItemType.generalchip:
                useGeneralChip(playingRole, template_id, itemCount);
                break;
            case GameConfig.ItemType.grandomchip:
                useGeneralRandomChip(playingRole, template_id, itemCount);
                break;
            case GameConfig.ItemType.treaschip:
                useTreasureChip(playingRole, template_id, itemCount);
                break;
            case GameConfig.ItemType.trandomchip:
                useTreasureRandomChip(playingRole, template_id, itemCount);
                break;
            case GameConfig.ItemType.box:
                useBox(playingRole, template_id, itemCount);
                break;
            case GameConfig.ItemType.cbox:
                useCBox(playingRole, template_id, itemCount, useItemMsg.param);
                break;
            case GameConfig.ItemType.rbox:
                useRBox(playingRole, template_id, itemCount);
                break;
            default:
                S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1302);
                playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                break;
        }
    }

    private void useBox(PlayingRole playingRole, int template_id, int itemCount) {
        CBoxTemplate giftTemplate = GiftTemplateCache.getInstance().getGiftTemplate(template_id);
        if (giftTemplate == null) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1304);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<RewardTemplateSimple> rewardTemplateChances = giftTemplate.getITEMS();
        //ret
        WsMessageBag.S2CUseItem respmsg = new WsMessageBag.S2CUseItem();
        respmsg.status = 0;
        //充值卡
        if (rewardTemplateChances.get(0).getGSID() == ItemConstants.PAY_CARD_RMB_YUAN) {
            int rmbYuan = rewardTemplateChances.get(0).getCOUNT();
            PaymentManager.getInstance().fakePay(playingRole, rmbYuan, "money");
            int addYb = rmbYuan * PaymentConstants.RMB_2_YUANBAO;
            respmsg.reward = new ArrayList<>(1);
            respmsg.reward.add(new WsMessageBase.RewardInfo(GameConfig.PLAYER.YB, addYb * itemCount));
        } else {//道具奖励
            respmsg.reward = new ArrayList<>(rewardTemplateChances.size());
            for (RewardTemplateSimple rewardTemplateSimple : rewardTemplateChances) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT() * itemCount, LogConstants.MODULE_BAG);
                respmsg.reward.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT() * itemCount));
            }
        }
        //cost
        AwardUtils.changeRes(playingRole, template_id, -itemCount, LogConstants.MODULE_BAG);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void useCBox(PlayingRole playingRole, int template_id, int itemCount, int param) {
        CBoxTemplate cBoxTemplate = CBoxTemplateCache.getInstance().getCBoxTemplate(template_id);
        if (cBoxTemplate == null) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1304);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<RewardTemplateSimple> rewardTemplateChances = cBoxTemplate.getITEMS();
        RewardTemplateSimple rewardTemplateSimple = rewardTemplateChances.get(param);
        //award
        WsMessageBag.S2CUseItem respmsg = new WsMessageBag.S2CUseItem();
        respmsg.status = 0;
        respmsg.reward = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_BAG);
            respmsg.reward.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //cost
        AwardUtils.changeRes(playingRole, template_id, -itemCount, LogConstants.MODULE_BAG);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void useGeneralRandomChip(PlayingRole playingRole, int template_id, int itemCount) {
        //数量是否够
        GeneralChipTemplate generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(template_id);
        if (itemCount < generalChipTemplate.getCHIP()) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1303);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //是否超出背包上限
        int rewardCount = itemCount / generalChipTemplate.getCHIP();
        int playerId = playingRole.getId();
        if (BagManager.getInstance().checkBagFull(playerId, rewardCount, playingRole.getPlayerBean().getVipLevel())) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1307);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<Integer> gcond = (List<Integer>) generalChipTemplate.getGCOND();
        Collection<GeneralChipTemplate> allChips = GeneralChipTemplateCache.getInstance().getAllChips();
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        for (GeneralChipTemplate generalChipTemplate1 : allChips) {
            if (generalChipTemplate1.getCTYPE() == 1) {
                boolean isAllMatch = true;
                int generalTemplateId = (Integer) generalChipTemplate1.getGCOND();
                GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
                for (int aCondId : gcond) {
                    AffairManager.CondTypeBean condTypeBean = GeneralConstants.GENERAL_COM_CONDS.get(String.valueOf(aCondId));
                    switch (condTypeBean.getCondType()) {
                        case "star":
                            if (!generalTemplate.getSTAR().equals(condTypeBean.getCondValue())) {
                                isAllMatch = false;
                                break;
                            }
                            break;
                        case "camp":
                            if (!generalTemplate.getCAMP().equals(condTypeBean.getCondValue())) {
                                isAllMatch = false;
                                break;
                            }
                            break;
                        case "occu":
                            if (!generalTemplate.getOCCU().equals(condTypeBean.getCondValue())) {
                                isAllMatch = false;
                                break;
                            }
                            break;
                    }
                }
                if (isAllMatch) {
                    int getRate = 15;
                    Integer generalChipTemplateId = generalChipTemplate1.getId();
                    if (RBoxTemplateCache.getInstance().get5StarEliteChips().contains(generalChipTemplateId)) {
                        getRate = 6;
                    } else if (RBoxTemplateCache.getInstance().get5StarLegendChips().contains(generalChipTemplateId)) {
                        getRate = 2;
                    }
                    rd.put(getRate, generalTemplateId);
                }
            }
        }
        //rand
        WsMessageBag.S2CUseItem respmsg = new WsMessageBag.S2CUseItem();
        respmsg.reward = new ArrayList<>(rewardCount);
        for (int i = 0; i < rewardCount; i++) {
            int gsid = rd.random();
            AwardUtils.changeRes(playingRole, gsid, 1, LogConstants.MODULE_BAG);
            respmsg.reward.add(new WsMessageBase.RewardInfo(gsid, 1));
            //
            int gstar = GeneralTemplateCache.getInstance().getHeroTemplate(gsid).getSTAR();
            ActivityXiangouManager.getInstance().gstarXiangou(playerId, gstar);
        }
        //cost
        AwardUtils.changeRes(playingRole, template_id, -itemCount, LogConstants.MODULE_BAG);
        //ret
        respmsg.status = 0;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void useTreasureRandomChip(PlayingRole playingRole, int template_id, int itemCount) {
        TreasureChipTemplate treasureChipTemplate = TreasureChipTemplateCache.getInstance().getTreasureChipTemplate(template_id);
        if (itemCount < treasureChipTemplate.getCHIP()) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1303);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        WsMessageBag.S2CUseItem respmsg = new WsMessageBag.S2CUseItem();
        int rewardCount = itemCount / treasureChipTemplate.getCHIP();
        respmsg.reward = new ArrayList<>(rewardCount);
        int tcond = treasureChipTemplate.getTCOND();
        int[] legendTreasures = TreasureTemplateCache.getInstance().getLegendTreasures(tcond);
        if (legendTreasures == null) {
            List<TreasureChipTemplate> treasureChipTemplateByQualitys = TreasureChipTemplateCache.getInstance().getTreasureChipTemplateByQuality(tcond);
            for (int i = 0; i < rewardCount; i++) {
                TreasureChipTemplate randTreasureChipTemplate = treasureChipTemplateByQualitys.get(RandomUtils.nextInt(0,
                        treasureChipTemplateByQualitys.size()));
                int treasureId = randTreasureChipTemplate.getTCOND();
                AwardUtils.changeRes(playingRole, treasureId, 1, LogConstants.MODULE_BAG);
                respmsg.reward.add(new WsMessageBase.RewardInfo(treasureId, 1));
            }
        } else {
            for (int i = 0; i < rewardCount; i++) {
                int randIndex = RandomUtils.nextInt(0, legendTreasures.length);
                int treasureId = legendTreasures[randIndex];
                AwardUtils.changeRes(playingRole, treasureId, 1, LogConstants.MODULE_BAG);
                respmsg.reward.add(new WsMessageBase.RewardInfo(treasureId, 1));
            }
        }
        //cost
        AwardUtils.changeRes(playingRole, template_id, -itemCount, LogConstants.MODULE_BAG);
        //ret
        respmsg.status = 0;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void useRBox(PlayingRole playingRole, int template_id, int itemCount) {
        RBoxTemplate rBoxTemplate = RBoxTemplateCache.getInstance().getRBoxTemplateById(template_id);
        if (rBoxTemplate == null) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1304);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<RewardTemplateChance> rewardTemplateChances = rBoxTemplate.getITEMS();
        RandomDispatcher<RewardTemplateChance> rd = new RandomDispatcher<>();
        for (RewardTemplateChance rewardTemplateChance : rewardTemplateChances) {
            rd.put(rewardTemplateChance.getCHANCE(), rewardTemplateChance);
        }
        //award
        WsMessageBag.S2CUseItem respmsg = new WsMessageBag.S2CUseItem();
        respmsg.status = 0;
        respmsg.reward = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            RewardTemplateChance rewardTemplateChance = rd.random();
            AwardUtils.changeRes(playingRole, rewardTemplateChance.getGSID(), rewardTemplateChance.getCOUNT(), LogConstants.MODULE_BAG);
            respmsg.reward.add(new WsMessageBase.RewardInfo(rewardTemplateChance.getGSID(), rewardTemplateChance.getCOUNT()));
        }
        //cost
        AwardUtils.changeRes(playingRole, template_id, -itemCount, LogConstants.MODULE_BAG);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void useTreasureChip(PlayingRole playingRole, int template_id, int itemCount) {
        //数量是否够
        TreasureChipTemplate treasureChipTemplate = TreasureChipTemplateCache.getInstance().getTreasureChipTemplate(template_id);
        if (itemCount < treasureChipTemplate.getCHIP()) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1303);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        int rewardCount = itemCount / treasureChipTemplate.getCHIP();
        int gsid = treasureChipTemplate.getTCOND();
        AwardUtils.changeRes(playingRole, gsid, rewardCount, LogConstants.MODULE_BAG);
        //cost
        AwardUtils.changeRes(playingRole, template_id, -itemCount, LogConstants.MODULE_BAG);
        //ret
        WsMessageBag.S2CUseItem respmsg = new WsMessageBag.S2CUseItem();
        respmsg.status = 0;
        respmsg.reward = new ArrayList<>(Collections.singletonList(new WsMessageBase.RewardInfo(gsid, rewardCount)));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void useGeneralChip(PlayingRole playingRole, int template_id, int itemCount) {
        //数量是否够
        GeneralChipTemplate generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(template_id);
        if (itemCount < generalChipTemplate.getCHIP()) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1303);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //是否超出背包上限
        int rewardCount = itemCount / generalChipTemplate.getCHIP();
        int playerId = playingRole.getId();
        if (BagManager.getInstance().checkBagFull(playerId, rewardCount, playingRole.getPlayerBean().getVipLevel())) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CUseItem.msgCode, 1307);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        int gsid = (Integer) generalChipTemplate.getGCOND();
        AwardUtils.changeRes(playingRole, gsid, rewardCount, LogConstants.MODULE_BAG);
        //cost
        AwardUtils.changeRes(playingRole, template_id, -itemCount, LogConstants.MODULE_BAG);
        //
        int gstar = GeneralTemplateCache.getInstance().getHeroTemplate(gsid).getSTAR();
        ActivityXiangouManager.getInstance().gstarXiangou(playerId, gstar);
        //ret
        WsMessageBag.S2CUseItem respmsg = new WsMessageBag.S2CUseItem();
        respmsg.status = 0;
        respmsg.reward = new ArrayList<>(Collections.singletonList(new WsMessageBase.RewardInfo(gsid, rewardCount)));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
