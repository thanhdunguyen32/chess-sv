package game.module.worldboss.logic;

import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.logic.LegionManager;
import game.module.mail.logic.MailManager;
import game.module.season.bean.BattleSeason;
import game.module.season.dao.SeasonCache;
import game.module.template.LegionPracticeTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.worldboss.bean.WorldBoss;
import game.module.worldboss.dao.LegionPracticeTemplateCache;
import game.module.worldboss.dao.WorldBossCache;
import game.module.worldboss.dao.WorldBossDaoHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WorldBossManager {

    private static Logger logger = LoggerFactory.getLogger(WorldBossManager.class);

    static class SingletonHolder {
        static WorldBossManager instance = new WorldBossManager();
    }

    public static WorldBossManager getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void addHurtVal(int playerId, long legionId, long damageSum) {
        logger.info("legion world boss add damage:player={},legion={},damage={}",playerId,legionId,damageSum);
        WorldBoss worldBoss = WorldBossCache.getInstance().getWorldBoss();
        //player
        Map<Integer, Long> playerDamageSum = worldBoss.getPlayerDamageSum();
        if (!playerDamageSum.containsKey(playerId)) {
            playerDamageSum.put(playerId, damageSum);
            WorldBossCache.getInstance().addPlayerRank(playerId);
        } else {
            playerDamageSum.put(playerId, playerDamageSum.get(playerId) + damageSum);
        }
        //legion
        Map<Long, Long> legionDamageSum = worldBoss.getLegionDamageSum();
        if (!legionDamageSum.containsKey(legionId)) {
            legionDamageSum.put(legionId, damageSum);
            WorldBossCache.getInstance().addLegionRank(legionId);
        } else {
            legionDamageSum.put(legionId, legionDamageSum.get(legionId) + damageSum);
        }
        //last damage save
        Map<Integer, Long> playerLastDamage = worldBoss.getPlayerLastDamage();
        playerLastDamage.put(playerId, damageSum);
        //rank
        WorldBossCache.getInstance().sortPlayerRank();
        WorldBossCache.getInstance().sortLegionRank();
        //update db
        WorldBossDaoHelper.asyncUpdateWorldBoss(worldBoss);
    }

    private WorldBoss createWorldBoss() {
        WorldBoss worldBoss = new WorldBoss();
        worldBoss.setPlayerLastDamage(new HashMap<>());
        worldBoss.setPlayerDamageSum(new HashMap<>());
        worldBoss.setLegionDamageSum(new HashMap<>());
        worldBoss.setReward(false);
        return worldBoss;
    }

    public void resetWorldBoss() {
        WorldBoss worldBoss = WorldBossCache.getInstance().getWorldBoss();
        if (worldBoss == null) {
            return;
        }
        if(!worldBoss.getReward()){
            sendRewards();
        }
        worldBoss.getPlayerLastDamage().clear();
        worldBoss.getPlayerDamageSum().clear();
        worldBoss.getLegionDamageSum().clear();
        WorldBossCache.getInstance().clearRank();
        worldBoss.setReward(false);
        WorldBossDaoHelper.asyncUpdateWorldBoss(worldBoss);
    }

    public synchronized void checkFinish() {
        WorldBoss worldBoss = WorldBossCache.getInstance().getWorldBoss();
        Date now = new Date();
        if (worldBoss == null) {
            //有效时间内
            BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
            Date seasonEndTime = battleSeason.getEtime();
            Date bossEndTime = DateUtils.addDays(seasonEndTime, -1);
            if (now.before(bossEndTime)) {
                worldBoss = createWorldBoss();
                WorldBossCache.getInstance().addWorldBoss(worldBoss);
                WorldBossDaoHelper.asyncInsertWorldBoss(worldBoss);
            }
        } else {
            //赛季最后一天
            BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
            Date seasonEndTime = battleSeason.getEtime();
            Date bossEndTime = DateUtils.addDays(seasonEndTime, -1);
            if (now.after(bossEndTime) && !worldBoss.getReward()) {
                sendRewards();
                worldBoss.setReward(true);
                WorldBossDaoHelper.asyncUpdateWorldBoss(worldBoss);
            }
        }
    }

    private void sendRewards() {
        logger.info("world boss week rank rewards!");
        List<Long> legionRank = WorldBossCache.getInstance().getLegionRank();
        List<LegionPracticeTemplate.LegionWorldRewardTemplate> legionWorldReward = LegionPracticeTemplateCache.getInstance().getLegionWorldReward();
        int rewardIndex = 0;
        LegionPracticeTemplate.LegionWorldRewardTemplate legionWorldRewardTemplate = legionWorldReward.get(rewardIndex);
        String mailTitle = "Phần thưởng hoạt động thử thách quân đội"; //"神将试炼军团周排名奖励"
        String mailContent = "Chúc mừng quân đội của bạn đã đạt được vị trí thứ %1$s trong hoạt động thử thách quân đội, bạn đã nhận được các phần thưởng sau: Hãy tiếp tục cố gắng!"; //"恭喜您的军团在本轮神将试炼中取得第%1$s名的成绩，获得了以下奖励：愿你再接再厉！";
        int i = 1;
        for (Long aLegionId : legionRank) {
            if (i > legionWorldRewardTemplate.getMAX()) {
                rewardIndex++;
                legionWorldRewardTemplate = legionWorldReward.get(rewardIndex);
            }
            LegionBean legionBean = LegionCache.getInstance().getLegionBean(aLegionId);
            if (legionBean != null) {
                logger.info("rewrads!legion={},rank={},rewards={}", legionBean.getName(), i, legionWorldRewardTemplate.getITEMS());
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : legionWorldRewardTemplate.getITEMS()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                //do
                String myMailContent = String.format(mailContent, i);
                LegionPlayer.DbLegionPlayers dbLegionPlayers = legionBean.getDbLegionPlayers();
                if (dbLegionPlayers != null && dbLegionPlayers.getMembers() != null) {
                    for (int aPlayerId : dbLegionPlayers.getMembers().keySet()) {
                        MailManager.getInstance().sendSysMailToSingle(aPlayerId, mailTitle, myMailContent, mailAtt);
                    }
                }
            }
            i++;
        }
        //军团伤害排第一
        WorldBoss worldBoss = WorldBossCache.getInstance().getWorldBoss();
        Map<Long, List<Map.Entry<Integer, Long>>> damageMap = new HashMap<>();
        if (worldBoss != null) {
            Map<Integer, Long> playerDamageSum = worldBoss.getPlayerDamageSum();
            for (Map.Entry<Integer, Long> aEntry : playerDamageSum.entrySet()) {
                int aPlayerId = aEntry.getKey();
                long legionId = LegionManager.getInstance().getLegionId(aPlayerId);
                if (legionId > 0) {
                    if (!damageMap.containsKey(legionId)) {
                        List<Map.Entry<Integer, Long>> damagelist = new ArrayList<>();
                        damageMap.put(legionId, damagelist);
                    }
                    damageMap.get(legionId).add(aEntry);
                }
            }
        }
        //遍历
        mailTitle = "Phần thưởng hoạt động thử thách quân đội"; //"军团神将试炼周排名本军团第一奖励"
        mailContent = "Chúc mừng bạn đã đạt được vị trí thứ nhất trong hoạt động thử thách quân đội, bạn đã nhận được các phần thưởng sau: Hãy tiếp tục cố gắng!"; //"恭喜您在军团神将试炼战斗中，本军团伤害排名第一,获得如下额外奖励：愿你再接再厉！";
        List<RewardTemplateSimple> legionHarmFirst = LegionPracticeTemplateCache.getInstance().getLegionHarmFirst();
        for (Map.Entry<Long, List<Map.Entry<Integer, Long>>> aEntry : damageMap.entrySet()) {
            long legionId = aEntry.getKey();
            List<Map.Entry<Integer, Long>> damagelist = aEntry.getValue();
            damagelist.sort((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
            Map.Entry<Integer, Long> damage1Player = damagelist.get(0);
            int damage1PlayerId = damage1Player.getKey();
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : legionHarmFirst) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            //
            MailManager.getInstance().sendSysMailToSingle(damage1PlayerId, mailTitle, mailContent, mailAtt);
        }
    }

}
