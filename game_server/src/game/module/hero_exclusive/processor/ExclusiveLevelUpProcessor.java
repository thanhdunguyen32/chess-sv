package game.module.hero_exclusive.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExclusive;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.hero_exclusive.dao.ExclusiveLvTemplateCache;
import game.module.hero_exclusive.dao.ExclusiveSkillTemplateCache;
import game.module.hero_exclusive.dao.ExclusiveTemplateCache;
import game.module.hero_exclusive.dao.ExclusiveWordTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.ExclusiveLvTemplate;
import game.module.template.ExclusiveTemplate;
import game.module.template.KVTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.ScrollAnnoManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SExclusiveLevelUp.id, accessLimit = 200)
public class ExclusiveLevelUpProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ExclusiveLevelUpProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SExclusiveLevelUp reqmsg = WsMessageHero.C2SExclusiveLevelUp.parse(request);
        int playerId = playingRole.getId();
        logger.info("general exclusive level up,player={},req={}", playerId, reqmsg);
        long general_uuid = reqmsg.general_uuid;
        boolean is_lock = reqmsg.is_lock;
        //is exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveLevelUp.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is level 30
        GeneralBean generalBean = heroCache.get(general_uuid);
        if (generalBean.getLevel() < 30) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveLevelUp.msgCode, 1391);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //max
        GeneralExclusive generalExclusive = generalBean.getExclusive();
        Integer exclusiveLevel = generalExclusive.getLevel();
        if (exclusiveLevel >= ExclusiveLvTemplateCache.getInstance().getMaxLevel()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveLevelUp.msgCode, 1392);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        ExclusiveLvTemplate exclusiveLvTemplate = ExclusiveLvTemplateCache.getInstance().getExclusiveLvTemplate(exclusiveLevel);
        if (is_lock) {
            List<RewardTemplateSimple> lockTemplate = exclusiveLvTemplate.getLOCK();
            for (RewardTemplateSimple rewardTemplateSimple : lockTemplate) {
                if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT())) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveLevelUp.msgCode, 1393);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            }
        }
        //cost
        List<RewardTemplateSimple> itemsTemplates = exclusiveLvTemplate.getITEMS();
        for (RewardTemplateSimple rewardTemplateSimple : itemsTemplates) {
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT())) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveLevelUp.msgCode, 1393);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        generalExclusive.setLevel(++exclusiveLevel);
        //cost
        if (is_lock) {
            List<RewardTemplateSimple> lockTemplate = exclusiveLvTemplate.getLOCK();
            for (RewardTemplateSimple rewardTemplateSimple : lockTemplate) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_GENERAL_EXCLUSIVE);
            }
        }
        for (RewardTemplateSimple rewardTemplateSimple : itemsTemplates) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_GENERAL_EXCLUSIVE);
        }
        //propertys
        ExclusiveTemplate exclusiveTemplate = ExclusiveTemplateCache.getInstance().getExclusiveTemplate(exclusiveLevel);
        List<Integer> parr = exclusiveTemplate.getPARR();
        Map<String, Integer> propertyMap = generalExclusive.getProperty();
        if (propertyMap == null) {
            propertyMap = new HashMap<>();
            generalExclusive.setProperty(propertyMap);
        }
        if (is_lock) {
            for (int attributeId : parr) {
                List<KVTemplate> wordTemplates = ExclusiveWordTemplateCache.getInstance().getWordTemplates(attributeId);
                boolean findExist = false;
                for (KVTemplate kvTemplate : wordTemplates) {
                    if (propertyMap.containsKey(kvTemplate.getKEY())) {
                        propertyMap.put(kvTemplate.getKEY(), kvTemplate.getVAL());
                        findExist = true;
                        break;
                    }
                }
                if (!findExist) {
                    int randIndex = RandomUtils.nextInt(0, wordTemplates.size());
                    KVTemplate aWordTemplate = wordTemplates.get(randIndex);
                    propertyMap.put(aWordTemplate.getKEY(), aWordTemplate.getVAL());
                }
            }
        } else {
            propertyMap.clear();
            for (int attributeId : parr) {
                List<KVTemplate> wordTemplates = ExclusiveWordTemplateCache.getInstance().getWordTemplates(attributeId);
                int randIndex = RandomUtils.nextInt(0, wordTemplates.size());
                KVTemplate aWordTemplate = wordTemplates.get(randIndex);
                propertyMap.put(aWordTemplate.getKEY(), aWordTemplate.getVAL());
            }
        }
        //skill
        List<Integer> skillList = generalExclusive.getSkill();
        if (skillList == null) {
            skillList = new ArrayList<>();
            generalExclusive.setSkill(skillList);
        } else {
            skillList.clear();
        }
        List<Integer> sarr = exclusiveTemplate.getSARR();
        for (int aSkillId : sarr) {
            List<Integer> skillIds = ExclusiveSkillTemplateCache.getInstance().getSkillIds(aSkillId);
            int randIndex = RandomUtils.nextInt(0, skillIds.size());
            int aSkill = skillIds.get(randIndex);
            skillList.add(aSkill);
        }
        //save bean
        GeneralManager.getInstance().updatePropertyAndPower(generalBean);
        GeneralDaoHelper.asyncUpdateHero(generalBean);
        //
        ScrollAnnoManager.getInstance().exclusiveAddStar(playingRole,generalBean.getTemplateId(),exclusiveTemplate);
        //ret
        WsMessageHero.S2CExclusiveLevelUp respmsg = new WsMessageHero.S2CExclusiveLevelUp();
        respmsg.general_uuid = general_uuid;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
