package game.module.hero.processor;

import com.google.common.primitives.Longs;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityXiangouManager;
import game.module.activity_month.logic.ActivityMonthManager;
import game.module.affair.logic.AffairManager;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralCompTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.logic.GeneralConstants;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.logic.MissionManager;
import game.module.template.GeneralCompTemplate;
import game.module.template.GeneralTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.ScrollAnnoManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.*;

/**
 * 武将觉醒
 */
@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralAddStar.id, accessLimit = 200)
public class GeneralAddStarProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GeneralAddStarProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralAddStar reqmsg = WsMessageHero.C2SGeneralAddStar.parse(request);
        int playerId = playingRole.getId();
        logger.info("general add star,player={},req={}", playerId, reqmsg);
        int target_gsid = reqmsg.target_gsid;
        long general_uuid = reqmsg.general_uuid;
        long[] cost_generals = reqmsg.cost_generals;
        //is exist
        GeneralCompTemplate generalCompTemplate = GeneralCompTemplateCache.getInstance().getGeneralCompTemplate(target_gsid);
        if (generalCompTemplate == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddStar.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        //is exist
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddStar.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //match
        Integer mainTemplateId = generalCompTemplate.getMAIN();
        GeneralBean generalBean = heroCache.get(general_uuid);
        if (!mainTemplateId.equals(generalBean.getTemplateId())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddStar.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is duplicate
        Set<Long> mySet = new HashSet<>();
        mySet.add(general_uuid);
        for (long aGeneralUuid : cost_generals) {
            if (!heroCache.containsKey(aGeneralUuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddStar.msgCode, 1396);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (mySet.contains(aGeneralUuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddStar.msgCode, 1397);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            mySet.add(aGeneralUuid);
        }
        //count match
        int generalCount = 0;
        List<GeneralCompTemplate.GeneralCompConds> cons = generalCompTemplate.getCONS();
        for (GeneralCompTemplate.GeneralCompConds aCond : cons) {
            generalCount += aCond.getCOUNT();
        }
        if (generalCount != cost_generals.length) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddStar.msgCode, 1340);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //check cond
        int pickIndex = 0;
        for (GeneralCompTemplate.GeneralCompConds aCond : cons) {
            int costGeneralNum = aCond.getCOUNT();
            Object condDesc = aCond.getCOND();
            for (int i = 0; i < costGeneralNum; i++) {
                long to_select_general_uuid = cost_generals[pickIndex];
                boolean condMatch = checkCondMatch(condDesc, heroCache.get(to_select_general_uuid));
                if (!condMatch) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddStar.msgCode, 1341);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                pickIndex++;
            }
        }
        //cost items
        List<RewardTemplateSimple> costItems = generalCompTemplate.getITEMS();
        if (costItems != null && costItems.size() > 0) {
            for (RewardTemplateSimple rewardTemplateSimple : costItems) {
                if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT())) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddStar.msgCode, 1342);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            }
        }
        //do
        //back
        Map<Integer, Integer> backMap = new HashMap<>();
        //save bean
        generalBean.setStar(generalBean.getStar() + 1);
        generalBean.setTemplateId(target_gsid);
        GeneralManager.getInstance().updatePropertyAndPower(generalBean);
        GeneralDaoHelper.asyncUpdateHero(generalBean);
        //month activity
        ActivityMonthManager.getInstance().generalCompose(playingRole, generalBean.getStar());
        if (generalBean.getStar() >= 5) {
            GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(target_gsid);
            ScrollAnnoManager.getInstance().heroAddStar(playingRole, playingRole.getPlayerBean().getName(), heroTemplate.getNAME(), generalBean.getStar());
            ActivityXiangouManager.getInstance().gstarXiangou(playerId,generalBean.getStar());
        }
        //cost
        for (long aGeneralUuid : cost_generals) {
            GeneralBean generalBeanCost = heroCache.get(aGeneralUuid);
            //general back
            GeneralManager.getInstance().generalDecompAwards(playingRole, generalBeanCost, backMap, true);
            //remove
            GeneralManager.getInstance().removeGeneral(playingRole, aGeneralUuid);
        }
        //back items
        List<WsMessageBase.RewardInfo> backItems = new ArrayList<>();
        if (backMap.size() > 0) {
            for (Map.Entry<Integer, Integer> aEntry : backMap.entrySet()) {
                AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.MODULE_GENERAL);
                backItems.add(new WsMessageBase.RewardInfo(aEntry.getKey(), aEntry.getValue()));
            }
        }
        //cost
        if (costItems != null && costItems.size() > 0) {
            for (RewardTemplateSimple rewardTemplateSimple : costItems) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_GENERAL);
            }
        }
        //update mission progress
        int newStar = generalBean.getStar();
        MissionManager.getInstance().generalStarChangeProgress(playingRole, newStar);
        //ret
        WsMessageHero.S2CGeneralAddStar respmsg = new WsMessageHero.S2CGeneralAddStar();
        respmsg.cost_generals = Longs.asList(cost_generals);
        respmsg.target_gsid = target_gsid;
        respmsg.general_uuid = general_uuid;
        respmsg.items = backItems;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private boolean checkCondMatch(Object condDesc, GeneralBean generalBean) {
        if (condDesc instanceof Integer) {
            int condGeneralTemplateId = (int) condDesc;
            return condGeneralTemplateId == generalBean.getTemplateId();
        } else {
            List<String> condList = (List<String>) condDesc;
            boolean isAllMatch = true;
            for (String aCondId : condList) {
                AffairManager.CondTypeBean condTypeBean = GeneralConstants.GENERAL_COM_CONDS.get(aCondId);
                int generalTemplateId = generalBean.getTemplateId();
                GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
                switch (condTypeBean.getCondType()) {
                    case "star":
                        if (!generalBean.getStar().equals(condTypeBean.getCondValue())) {
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
            return isAllMatch;
        }
    }

}
