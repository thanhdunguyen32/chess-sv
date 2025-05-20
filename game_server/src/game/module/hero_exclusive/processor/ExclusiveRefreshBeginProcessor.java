package game.module.hero_exclusive.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExclusive;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero_exclusive.dao.ExclusiveResetTemplateCache;
import game.module.hero_exclusive.dao.ExclusiveSkillTemplateCache;
import game.module.hero_exclusive.dao.ExclusiveTemplateCache;
import game.module.hero_exclusive.dao.ExclusiveWordTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.ExclusiveResetTemplate;
import game.module.template.ExclusiveTemplate;
import game.module.template.KVTemplate;
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

@MsgCodeAnn(msgcode = WsMessageHero.C2SExclusiveRefreshBegin.id, accessLimit = 200)
public class ExclusiveRefreshBeginProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ExclusiveRefreshBeginProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SExclusiveRefreshBegin reqmsg = WsMessageHero.C2SExclusiveRefreshBegin.parse(request);
        int playerId = playingRole.getId();
        logger.info("general exclusive refresh begin,player={},req={}", playerId, reqmsg);
        long general_uuid = reqmsg.general_uuid;
        int lock_type = reqmsg.lock_type;
        //is exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveRefreshBegin.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is unlock
        GeneralBean generalBean = heroCache.get(general_uuid);
        GeneralExclusive generalExclusive = generalBean.getExclusive();
        if (generalExclusive.getLevel() == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveRefreshBegin.msgCode, 1394);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        int exclusiveLevel = generalExclusive.getLevel();
        ExclusiveResetTemplate exclusiveResetTemplate = ExclusiveResetTemplateCache.getInstance().getExclusiveResetTemplate(exclusiveLevel);
        for (RewardTemplateSimple rewardTemplateSimple : exclusiveResetTemplate.getITEMS()) {
            int doubleRate = lock_type > 0 ? 2 : 1;
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(),
                    rewardTemplateSimple.getCOUNT() * doubleRate)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveRefreshBegin.msgCode, 1395);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        WsMessageHero.S2CExclusiveRefreshBegin respmsg = new WsMessageHero.S2CExclusiveRefreshBegin();
        //property
        ExclusiveTemplate exclusiveTemplate = ExclusiveTemplateCache.getInstance().getExclusiveTemplate(exclusiveLevel);
        List<Integer> parr = exclusiveTemplate.getPARR();
        if (lock_type != 1) {
            Map<String, Integer> propertyMap = new HashMap<>();
            for (int attributeId : parr) {
                List<KVTemplate> wordTemplates = ExclusiveWordTemplateCache.getInstance().getWordTemplates(attributeId);
                int randIndex = RandomUtils.nextInt(0, wordTemplates.size());
                KVTemplate aWordTemplate = wordTemplates.get(randIndex);
                propertyMap.put(aWordTemplate.getKEY(), aWordTemplate.getVAL());
            }
            generalExclusive.setPropertyPending(propertyMap);
            respmsg.property = new ArrayList<>(propertyMap.size());
            for (Map.Entry<String, Integer> aEntry : propertyMap.entrySet()) {
                respmsg.property.add(new WsMessageBase.KvStringPair(aEntry.getKey(), aEntry.getValue()));
            }
            generalExclusive.setPropertyPending(propertyMap);
        } else {
            generalExclusive.setPropertyPending(generalExclusive.getProperty());
            respmsg.property = new ArrayList<>(generalExclusive.getProperty().size());
            for (Map.Entry<String, Integer> aEntry : generalExclusive.getProperty().entrySet()) {
                respmsg.property.add(new WsMessageBase.KvStringPair(aEntry.getKey(), aEntry.getValue()));
            }
        }
        //skill
        List<Integer> sarr = exclusiveTemplate.getSARR();
        if (lock_type != 2) {
            List<Integer> skillList = new ArrayList<>();
            for (int aSkillId : sarr) {
                List<Integer> skillIds = ExclusiveSkillTemplateCache.getInstance().getSkillIds(aSkillId);
                int randIndex = RandomUtils.nextInt(0, skillIds.size());
                int aSkill = skillIds.get(randIndex);
                skillList.add(aSkill);
            }
            respmsg.skill = skillList;
            generalExclusive.setSkillPending(skillList);
        } else {
            generalExclusive.setSkillPending(generalExclusive.getSkill());
            respmsg.skill = generalExclusive.getSkill();
        }
        //save bean
        GeneralDaoHelper.asyncUpdateHero(generalBean);
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : exclusiveResetTemplate.getITEMS()) {
            int doubleRate = lock_type > 0 ? 2 : 1;
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT() * doubleRate,
                    LogConstants.MODULE_GENERAL_EXCLUSIVE);
        }
        //ret
        respmsg.general_uuid = general_uuid;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
