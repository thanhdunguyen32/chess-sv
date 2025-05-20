package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
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
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralTakeonEquip.id, accessLimit = 200)
public class GeneralTakeonEquipProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GeneralTakeonEquipProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralTakeonEquip reqmsg = WsMessageHero.C2SGeneralTakeonEquip.parse(request);
        int playerId = playingRole.getId();
        logger.info("general takeon equip,player={},req={}", playerId, reqmsg);
        int action_type = reqmsg.action_type;
        int equip_id = reqmsg.equip_id;
        long general_uuid = reqmsg.general_uuid;
        int pos = reqmsg.pos_index;
        //general exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralTakeonEquip.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        switch (action_type) {
            case 1:
                equip1(playingRole, action_type, general_uuid, equip_id, pos);
                break;
            case 2:
                equipAll(playingRole, action_type, general_uuid);
                break;
            case 3:
                equipOffAll(playingRole, action_type, general_uuid);
                break;
            default:
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralTakeonEquip.msgCode, 1301);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
        }
    }

    private void equipOffAll(PlayingRole playingRole, int action_type, long general_uuid) {
        int playerId = playingRole.getId();
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        GeneralBean generalBean = heroCache.get(general_uuid);
        for (int i = 0; i < generalBean.getEquip().size(); i++) {
            int aEquipId = generalBean.getEquip().get(i);
            if (aEquipId > 0) {
                generalBean.getEquip().set(i, 0);
                AwardUtils.changeRes(playingRole, aEquipId, 1, LogConstants.MODULE_GENERAL);
            }
        }
        Integer treasureId = generalBean.getTreasure();
        if(treasureId >0){
            generalBean.setTreasure(0);
            AwardUtils.changeRes(playingRole, treasureId, 1, LogConstants.MODULE_GENERAL);
        }
        GeneralManager.getInstance().updatePropertyAndPower(generalBean);
        GeneralDaoHelper.asyncUpdateHero(generalBean);
        //ret
        WsMessageHero.S2CGeneralTakeonEquip respmsg = new WsMessageHero.S2CGeneralTakeonEquip();
        respmsg.action_type = action_type;
        respmsg.general_uuid = general_uuid;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void equipAll(PlayingRole playingRole, int action_type, long general_uuid) {
        int playerId = playingRole.getId();
        //is exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        GeneralBean generalBean = heroCache.get(general_uuid);
        //auto takeon
        TreeSet<Item> equipsSorted = ItemCache.getInstance().getEquips(playerId);
        Set<Integer> costEquips = new HashSet<>();
        for (int i = 0; i < generalBean.getEquip().size(); i++) {
            int equipPos = i + 1;
            for (Item equipBean : equipsSorted) {
                Integer equipTemplateId = equipBean.getTemplateId();
                EquipTemplate equipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(equipTemplateId);
                if (equipPos == equipTemplate.getPOS()) {
                    //没有穿
                    Integer existEquipId = generalBean.getEquip().get(i);
                    if(existEquipId == 0){
                        generalBean.getEquip().set(i, equipTemplateId);
                        costEquips.add(equipTemplateId);
                    }else {
                        //替换
                        EquipTemplate existEquipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(existEquipId);
                        EquipTemplate newEquipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(equipTemplateId);
                        if (isEquipBetter(newEquipTemplate,existEquipTemplate)) {
                            generalBean.getEquip().set(i, equipTemplateId);
                            costEquips.add(equipTemplateId);
                            AwardUtils.changeRes(playingRole, existEquipId, 1, LogConstants.MODULE_GENERAL);
                        }
                    }
                    break;
                }
            }
        }
        //宝物
        int takeonTreasureId = 0;
        TreeSet<Item> treasures = ItemCache.getInstance().getTreasures(playerId);
        for (Item treasureBean : treasures) {
            Integer treasureTemplateId = treasureBean.getTemplateId();
            //没有穿
            Integer existTreasureId = generalBean.getTreasure();
            if(existTreasureId == 0){
                takeonTreasureId = treasureTemplateId;
                generalBean.setTreasure(takeonTreasureId);
            }else {
                //替换
                TreasureTemplate existTreasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(existTreasureId);
                TreasureTemplate newTreasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(treasureTemplateId);
                if (isTreasureBetter(newTreasureTemplate,existTreasureTemplate)) {
                    takeonTreasureId = treasureTemplateId;
                    generalBean.setTreasure(takeonTreasureId);
                    AwardUtils.changeRes(playingRole, existTreasureId, 1, LogConstants.MODULE_GENERAL);
                }
            }
            break;
        }
        //穿上
        if(takeonTreasureId > 0){
            AwardUtils.changeRes(playingRole, takeonTreasureId, -1, LogConstants.MODULE_GENERAL);
        }
        if (costEquips.size() > 0 || takeonTreasureId > 0) {
            for (int templateId : costEquips) {
                AwardUtils.changeRes(playingRole, templateId, -1, LogConstants.MODULE_GENERAL);
            }
            GeneralManager.getInstance().updatePropertyAndPower(generalBean);
            GeneralDaoHelper.asyncUpdateHero(generalBean);
        }
        //ret
        WsMessageHero.S2CGeneralTakeonEquip respmsg = new WsMessageHero.S2CGeneralTakeonEquip();
        respmsg.action_type = action_type;
        respmsg.general_uuid = general_uuid;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void equip1(PlayingRole playingRole, int action_type, long general_uuid, int equip_id, int pos) {
        int playerId = playingRole.getId();
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        GeneralBean generalBean = heroCache.get(general_uuid);
        Map<Integer, Item> itemCache = ItemCache.getInstance().getItemTemplateKey(playerId);
        EquipTemplate equipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(equip_id);
        if (equipTemplate != null) {//穿装备
            Integer equipPos = equipTemplate.getPOS();
            Integer existEquipId = generalBean.getEquip().get(equipPos - 1);
            if (existEquipId > 0 && existEquipId == equip_id) {//同一件装备
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralTakeonEquip.msgCode, 1309);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //equip exist
            if (!itemCache.containsKey(equip_id)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralTakeonEquip.msgCode, 1301);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //do
            generalBean.getEquip().set(equipPos - 1, equip_id);
            GeneralManager.getInstance().updatePropertyAndPower(generalBean);
            GeneralDaoHelper.asyncUpdateHero(generalBean);
            if (existEquipId > 0) {//替换装备脱下
                AwardUtils.changeRes(playingRole, existEquipId, 1, LogConstants.MODULE_GENERAL);
            }
            //cost
            AwardUtils.changeRes(playingRole, equip_id, -1, LogConstants.MODULE_GENERAL);
        } else {//脱装备
            if (pos <= 0 || pos > generalBean.getEquip().size()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralTakeonEquip.msgCode, 1301);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Integer existEquipId = generalBean.getEquip().get(pos - 1);
            generalBean.getEquip().set(pos - 1, 0);
            GeneralManager.getInstance().updatePropertyAndPower(generalBean);
            GeneralDaoHelper.asyncUpdateHero(generalBean);
            //放入背包
            if (existEquipId > 0) {
                AwardUtils.changeRes(playingRole, existEquipId, 1, LogConstants.MODULE_GENERAL);
            }
        }
        //ret
        WsMessageHero.S2CGeneralTakeonEquip respmsg = new WsMessageHero.S2CGeneralTakeonEquip();
        respmsg.action_type = action_type;
        respmsg.general_uuid = general_uuid;
        respmsg.equip_id = equip_id;
        respmsg.pos_index = pos;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private boolean isEquipBetter(EquipTemplate equipTemplate1, EquipTemplate equipTemplate2) {
        if (!equipTemplate1.getQUALITY().equals(equipTemplate2.getQUALITY())) {
            return equipTemplate1.getQUALITY() > equipTemplate2.getQUALITY();
        } else if (!equipTemplate1.getSTAR().equals(equipTemplate2.getSTAR())) {
            return equipTemplate1.getSTAR() > equipTemplate2.getSTAR();
        } else {
            return false;
        }
    }

    private boolean isTreasureBetter(TreasureTemplate equipTemplate1, TreasureTemplate equipTemplate2) {
        if (!equipTemplate1.getQUALITY().equals(equipTemplate2.getQUALITY())) {
            return equipTemplate1.getQUALITY() > equipTemplate2.getQUALITY();
        } else if (!equipTemplate1.getSTAR().equals(equipTemplate2.getSTAR())) {
            return equipTemplate1.getSTAR() > equipTemplate2.getSTAR();
        } else {
            return false;
        }
    }

}
