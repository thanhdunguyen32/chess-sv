package game.module.worldboss.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.legion.logic.LegionConstants;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.season.bean.BattleSeason;
import game.module.season.dao.SeasonCache;
import game.module.template.LegionPracticeTemplate;
import game.module.template.RewardTemplateChance;
import game.module.template.RewardTemplateRandomRate;
import game.module.template.RewardTemplateSimple;
import game.module.worldboss.bean.WorldBoss;
import game.module.worldboss.dao.LegionPracticeTemplateCache;
import game.module.worldboss.dao.WorldBossCache;
import game.module.worldboss.logic.WorldBossManager;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SWorldBossFarm.id, accessLimit = 200)
public class WorldBossFarmProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(WorldBossFarmProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("legion world boss farm!player={}", playerId);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CWorldBossFarm.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count max
        int attackCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), LegionConstants.WORLD_BOSS_ATTACK_MARK);
        List<Integer> fightCosts = LegionPracticeTemplateCache.getInstance().getFightCost();
        if (attackCount >= fightCosts.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CWorldBossFarm.msgCode, 1543);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        Integer fightCost = fightCosts.get(attackCount);
        if (fightCost > 0 && !ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.YB, fightCost)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CWorldBossFarm.msgCode, 1544);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //record exist
        WorldBoss worldBoss = WorldBossCache.getInstance().getWorldBoss();
        if (worldBoss == null || worldBoss.getPlayerLastDamage() == null || !worldBoss.getPlayerLastDamage().containsKey(playerId)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CWorldBossFarm.msgCode, 1547);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<WsMessageBase.IORewardItem> dropItems = new ArrayList<>();
        //reward
        BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
        Integer seasonId = battleSeason.getSeason();
        LegionPracticeTemplate.LegionPracticeBossTemplate legionPracticeBossTemplate =
                LegionPracticeTemplateCache.getInstance().getWorldBossTemplate(seasonId);
        for (RewardTemplateSimple rewardTemplateSimple : legionPracticeBossTemplate.getFIGHT_REWARD()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_LEGION);
            dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //random reward
        List<RewardTemplateRandomRate> random_rate = legionPracticeBossTemplate.getRANDOM_RATE();
        RandomDispatcher<Integer> rd1 = new RandomDispatcher<>();
        for (RewardTemplateRandomRate rewardTemplateRandomRate : random_rate) {
            if (rewardTemplateRandomRate.getCHANCE() > 0) {
                rd1.put(rewardTemplateRandomRate.getCHANCE(), rewardTemplateRandomRate.getNUM());
            }
        }
        Integer randCount = rd1.random();
        if (randCount > 0) {
            List<RewardTemplateChance> random_reward = legionPracticeBossTemplate.getRANDOM_REWARD();
            RandomDispatcher<RewardTemplateChance> rd2 = new RandomDispatcher<>();
            for (RewardTemplateChance rewardTemplateChance : random_reward) {
                if (rewardTemplateChance.getCHANCE() > 0) {
                    rd2.put(rewardTemplateChance.getCHANCE(), rewardTemplateChance);
                }
            }
            for (int i = randCount; i > 0; i--) {
                RewardTemplateChance rewardTemplateChance = rd2.randomRemove();
                AwardUtils.changeRes(playingRole, rewardTemplateChance.getGSID(), rewardTemplateChance.getCOUNT(), LogConstants.MODULE_LEGION);
                dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateChance.getGSID(), rewardTemplateChance.getCOUNT()));
            }
        }
        //hurt
        long hurmSum = worldBoss.getPlayerLastDamage().get(playerId);
        WorldBossManager.getInstance().addHurtVal(playerId, legionId, hurmSum);
        //mark
        AwardUtils.changeRes(playingRole, LegionConstants.WORLD_BOSS_ATTACK_MARK, 1, LogConstants.MODULE_LEGION);
        //cost
        if (fightCost > 0) {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -fightCost, LogConstants.MODULE_LEGION);
        }
        //ret
        WsMessageLegion.S2CWorldBossFarm respmsg = new WsMessageLegion.S2CWorldBossFarm();
        respmsg.rewards = dropItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
