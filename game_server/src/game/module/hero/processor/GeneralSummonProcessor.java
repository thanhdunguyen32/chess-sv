package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityWeekManager;
import game.module.award.logic.AwardUtils;
import game.module.hero.dao.GeneralChipTemplateCache;
import game.module.hero.logic.GeneralConstants;
import game.module.item.dao.RBoxTemplateCache;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.template.GeneralChipTemplate;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
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

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralSummon.id, accessLimit = 200)
public class GeneralSummonProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GeneralSummonProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralSummon reqMsg = WsMessageHero.C2SGeneralSummon.parse(request);
        int playerId = playingRole.getId();
        logger.info("general summon,player={},req={}", playerId, reqMsg);
        int action_type = reqMsg.type;
        int times = reqMsg.times;
        if (action_type < 1 || action_type > 5) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralSummon.msgCode, 1350);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (times != 1 && times != 10) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralSummon.msgCode, 1350);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //点将令count
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.SUMMON_COIN, times)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralSummon.msgCode, 1350);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        AwardUtils.changeRes(playingRole, ItemConstants.SUMMON_COIN, -times, LogConstants.SUMMON);
        //award
        AwardUtils.changeRes(playingRole, ItemConstants.JIANG_HUN, RandomUtils.nextInt(15, 31)*times, LogConstants.SUMMON);
        AwardUtils.changeRes(playingRole, ItemConstants.LONG_YU, times, LogConstants.SUMMON);
        //generals
        WsMessageHero.S2CGeneralSummon respmsg = new WsMessageHero.S2CGeneralSummon();
        respmsg.rewards = new ArrayList<>();
        Map<Integer, Integer> rewardMap = new HashMap<>();
        if (action_type < 5) {//魏蜀吴群抽奖
            RandomDispatcher<Integer> rd = new RandomDispatcher<>();
            for (int i = 0; i < GeneralConstants.WEI_SHU_WU_QUN_RATE.length; i++) {
                rd.put(GeneralConstants.WEI_SHU_WU_QUN_RATE[i], i);
            }
            for (int j = 0; j < times; j++) {
                int randIndex = rd.random();
                int rewardTemplateId = 0;
                int rewardCount = 0;
                switch (randIndex) {
                    case 0://随机4星武将
                        List<GeneralChipTemplate> generalChipTemplateList = GeneralChipTemplateCache.getInstance().getStar4TemplatesByCamp(action_type);
                        int randChipIndex = RandomUtils.nextInt(0, generalChipTemplateList.size());
                        GeneralChipTemplate generalChipTemplate = generalChipTemplateList.get(randChipIndex);
                        Integer generalChipTemplateId = generalChipTemplate.getId();
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = generalChipTemplate.getCHIP();
                        break;
                    case 1://随机5星武将碎片(5-20)个
                        List<GeneralChipTemplate> generalChipTemplates = GeneralChipTemplateCache.getInstance().getGeneralTemplates(5);
                        randChipIndex = RandomUtils.nextInt(0, generalChipTemplates.size());
                        generalChipTemplate = generalChipTemplates.get(randChipIndex);
                        generalChipTemplateId = generalChipTemplate.getId();
                        int randChipCount = RandomUtils.nextInt(5, 21);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = randChipCount;
                        break;
                    case 2://指定阵营5星武将碎片(5-20)个
                        List<Integer> camp5StarNormalChips = RBoxTemplateCache.getInstance().getCamp5StarNormalChips(action_type);
                        randChipIndex = RandomUtils.nextInt(0, camp5StarNormalChips.size());
                        generalChipTemplateId = camp5StarNormalChips.get(randChipIndex);
                        randChipCount = RandomUtils.nextInt(5, 21);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = randChipCount;
                        break;
                    case 3://普通5星武将
                        camp5StarNormalChips = RBoxTemplateCache.getInstance().getCamp5StarNormalChips(action_type);
                        randChipIndex = RandomUtils.nextInt(0, camp5StarNormalChips.size());
                        generalChipTemplateId = camp5StarNormalChips.get(randChipIndex);
                        generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(generalChipTemplateId);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = generalChipTemplate.getCHIP();
                        break;
                    case 4://精英5星武将
                        camp5StarNormalChips = RBoxTemplateCache.getInstance().getCamp5StarEliteChips(action_type);
                        randChipIndex = RandomUtils.nextInt(0, camp5StarNormalChips.size());
                        generalChipTemplateId = camp5StarNormalChips.get(randChipIndex);
                        generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(generalChipTemplateId);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = generalChipTemplate.getCHIP();
                        break;
                    case 5://神话5星武将
                        camp5StarNormalChips = RBoxTemplateCache.getInstance().getCamp5StarLegendChips(action_type);
                        randChipIndex = RandomUtils.nextInt(0, camp5StarNormalChips.size());
                        generalChipTemplateId = camp5StarNormalChips.get(randChipIndex);
                        generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(generalChipTemplateId);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = generalChipTemplate.getCHIP();
                        break;
                }
                putMap(rewardMap, rewardTemplateId, rewardCount);
                respmsg.rewards.add(new WsMessageBase.RewardInfo(rewardTemplateId, rewardCount));
            }
        } else {//帝魔抽奖
            RandomDispatcher<Integer> rd = new RandomDispatcher<>();
            for (int i = 0; i < GeneralConstants.DI_MO_RATE.length; i++) {
                rd.put(GeneralConstants.DI_MO_RATE[i], i);
            }
            for (int j = 0; j < times; j++) {
                int randIndex = rd.random();
                int rewardTemplateId = 0;
                int rewardCount = 0;
                switch (randIndex) {
                    case 0://4星帝魔武将
                        List<GeneralChipTemplate> generalChipTemplateList = GeneralChipTemplateCache.getInstance().getStar4DimoTemplates();
                        int randChipIndex = RandomUtils.nextInt(0, generalChipTemplateList.size());
                        GeneralChipTemplate generalChipTemplate = generalChipTemplateList.get(randChipIndex);
                        Integer generalChipTemplateId = generalChipTemplate.getId();
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = generalChipTemplate.getCHIP();
                        break;
                    case 1://随机5星武将碎片(5-20)个
                        List<GeneralChipTemplate> generalChipTemplates = GeneralChipTemplateCache.getInstance().getGeneralTemplates(5);
                        randChipIndex = RandomUtils.nextInt(0, generalChipTemplates.size());
                        generalChipTemplate = generalChipTemplates.get(randChipIndex);
                        generalChipTemplateId = generalChipTemplate.getId();
                        int randChipCount = RandomUtils.nextInt(5, 21);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = randChipCount;
                        break;
                    case 2://帝5星武将碎片(5-20)个
                        List<GeneralChipTemplate> camp5StarChips = GeneralChipTemplateCache.getInstance().getStar5TemplatesByCamp(5);
                        randChipIndex = RandomUtils.nextInt(0, camp5StarChips.size());
                        generalChipTemplateId = camp5StarChips.get(randChipIndex).getId();
                        randChipCount = RandomUtils.nextInt(5, 21);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = randChipCount;
                        break;
                    case 3://魔5星武将碎片(5-20)个
                        camp5StarChips = GeneralChipTemplateCache.getInstance().getStar5TemplatesByCamp(6);
                        randChipIndex = RandomUtils.nextInt(0, camp5StarChips.size());
                        generalChipTemplateId = camp5StarChips.get(randChipIndex).getId();
                        randChipCount = RandomUtils.nextInt(5, 21);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = randChipCount;
                        break;
                    case 4://普通5星帝魔武将
                        List<Integer> camp5StarNormalChips = RBoxTemplateCache.getInstance().getCamp5StarNormalChips(5);
                        List<Integer> selectList = new ArrayList<>();
                        selectList.addAll(camp5StarNormalChips);
                        selectList.addAll(RBoxTemplateCache.getInstance().getCamp5StarNormalChips(6));
                        randChipIndex = RandomUtils.nextInt(0, selectList.size());
                        generalChipTemplateId = selectList.get(randChipIndex);
                        generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(generalChipTemplateId);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = generalChipTemplate.getCHIP();
                        break;
                    case 5://精英5星帝魔武将
                        camp5StarNormalChips = RBoxTemplateCache.getInstance().getCamp5StarEliteChips(5);
                        selectList = new ArrayList<>();
                        selectList.addAll(camp5StarNormalChips);
                        selectList.addAll(RBoxTemplateCache.getInstance().getCamp5StarEliteChips(6));
                        randChipIndex = RandomUtils.nextInt(0, selectList.size());
                        generalChipTemplateId = selectList.get(randChipIndex);
                        generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(generalChipTemplateId);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = generalChipTemplate.getCHIP();
                        break;
                    case 6://神话5星武将
                        camp5StarNormalChips = RBoxTemplateCache.getInstance().getCamp5StarLegendChips(5);
                        selectList = new ArrayList<>();
                        selectList.addAll(camp5StarNormalChips);
                        selectList.addAll(RBoxTemplateCache.getInstance().getCamp5StarLegendChips(6));
                        randChipIndex = RandomUtils.nextInt(0, selectList.size());
                        generalChipTemplateId = selectList.get(randChipIndex);
                        generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(generalChipTemplateId);
                        rewardTemplateId = generalChipTemplateId;
                        rewardCount = generalChipTemplate.getCHIP();
                        break;
                }
                putMap(rewardMap, rewardTemplateId, rewardCount);
                respmsg.rewards.add(new WsMessageBase.RewardInfo(rewardTemplateId, rewardCount));
            }
        }
        //reward
        for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
            AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.SUMMON);
        }
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.MARK_SUMMON, times, LogConstants.SUMMON);
        //
        ActivityWeekManager.getInstance().djjfActivity(playingRole,times);
        ActivityWeekManager.getInstance().jfbxCostDjl(playingRole,times);
        //ret
        respmsg.type = action_type;
        respmsg.times = times;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void putMap(Map<Integer, Integer> rewardMap, int generalChipTemplateId, int chipCount) {
        if (!rewardMap.containsKey(generalChipTemplateId)) {
            rewardMap.put(generalChipTemplateId, chipCount);
        } else {
            rewardMap.put(generalChipTemplateId, rewardMap.get(generalChipTemplateId) + chipCount);
        }
    }

}
