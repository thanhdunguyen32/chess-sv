package game.module.legion.logic;

import game.module.award.bean.GameConfig;
import game.module.legion.bean.DbLegionBoss;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionMission;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.*;
import game.module.mail.logic.MailManager;
import game.module.template.LegionBossTemplate;
import game.module.template.LegionTechTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.ScrollAnnoManager;
import game.session.SessionManager;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author HeXuhui
 */
public class LegionManager {

    private static Logger logger = LoggerFactory.getLogger(LegionManager.class);

    public LegionBean createLegion(int playerId, int myPower, String name, String notice, int minlv, boolean ispass) {
        LegionBean legionBean = new LegionBean();
        legionBean.setUuid(SessionManager.getInstance().generateSessionId());
        legionBean.setLevel(1);
        legionBean.setCeoId(playerId);
        legionBean.setName(name);
        legionBean.setNotice(notice);
        legionBean.setMinLevel(minlv);
        legionBean.setPass(ispass);
        legionBean.setExp(0);
        legionBean.setFlevel(1);
        legionBean.setFexp(0);
        legionBean.setPos(0);
        legionBean.setKceo(false);
        legionBean.setPower(myPower);
        //add member
        LegionPlayer.DbLegionPlayers dbLegionPlayers = new LegionPlayer.DbLegionPlayers();
        legionBean.setDbLegionPlayers(dbLegionPlayers);
        dbLegionPlayers.setMembers(new HashMap<>());
        LegionPlayer legionMember = getLegionMember(playerId, 2);
        dbLegionPlayers.getMembers().put(playerId, legionMember);
        return legionBean;
    }

    public LegionPlayer getLegionMember(int playerId, int officalPos) {
        LegionPlayer legionPlayer = new LegionPlayer();
        legionPlayer.setDonateSum(0);
        Date now = new Date();
        legionPlayer.setLastDonateTime(now);
        legionPlayer.setPos(officalPos);//0平民，1官员，2ceo
        legionPlayer.setScore(0);
        legionPlayer.setAddTime(now);
        legionPlayer.setPlayerId(playerId);
        return legionPlayer;
    }

    public int getMyLegionPos(int playerId, long legionId) {
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        if (legionBean == null) {
            return 0;
        }
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        return legionPlayer.getPos();
    }

    public void addExp(long legionId, Integer donateExp) {
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        if (legionBean == null) {
            return;
        }
        synchronized (legionBean) {
            Integer legionLevel = legionBean.getLevel();
            int newExp = legionBean.getExp() + donateExp;
            Integer maxexp = MyLegionTemplateCache.getInstance().getLegionLevelConfig(legionLevel).getExp();
            if (newExp >= maxexp && legionLevel < MyLegionTemplateCache.getInstance().getMaxLevel()) {
                legionBean.setLevel(++legionLevel);
                legionBean.setExp(newExp - maxexp);
                //
                ScrollAnnoManager.getInstance().legionLevelup(legionBean.getName(), legionLevel);
            } else {
                legionBean.setExp(newExp);
            }
            LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        }
    }

    public DbLegionBoss createLegionBoss() {
        DbLegionBoss dbLegionBoss = new DbLegionBoss();
        int chapter_index = 0;
        dbLegionBoss.setChapterIndex(chapter_index);
        LegionBossTemplate.LegionBoss1 bossTemplate = LegionBossTemplateCache.getInstance().getBossTemplate(chapter_index);
        dbLegionBoss.setNowhp(bossTemplate.getBOSSHP());
        return dbLegionBoss;
    }

    public void killBossRewards(long legionId, int chapter_index, Map<Integer, Long> damageList) {
        logger.info("legion boss kill rewards!legion={},chapter_index={},damagelist={}", legionId, chapter_index, damageList);
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        List<Integer> damageRankList = new ArrayList<>(damageList.keySet());
        damageRankList.sort((r1, r2) -> (int) (damageList.get(r2) - damageList.get(r1)));
        int myRank = 1;
        List<LegionBossTemplate.LegionBossRank> rankTemplates = LegionBossTemplateCache.getInstance().getRankTemplate();
        LegionBossTemplate.LegionBoss1 bossTemplate = LegionBossTemplateCache.getInstance().getBossTemplate(chapter_index);
        int templateIndex = 0;
        LegionBossTemplate.LegionBossRank legionBossRank = rankTemplates.get(templateIndex);
        String mailTitle = "Phần thưởng hoạt động BOSS quân đội"; //"军团BOSS击杀奖励"
        String mailContent = "Bạn đã chiến đấu trong hoạt động BOSS quân đội, giết chết BOSS %1$d, gây %2$d sát thương, đứng thứ %3$d, phần thưởng như sau:"; //"您在军团boss战中英勇奋战，击杀了%1$d号BOSS，造成%2$d伤害，排名第%3$d,以下是您获得的击杀奖励：";
        for (int otherPlayerId : damageRankList) {
            //is player exist
            if (!legionBean.getDbLegionPlayers().getMembers().containsKey(otherPlayerId)) {
                continue;
            }
            if (myRank > legionBossRank.getMIN()) {
                templateIndex++;
                legionBossRank = rankTemplates.get(templateIndex);
            }
            Integer randPercent = legionBossRank.getRAND();
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : bossTemplate.getKREWARD()) {
                int gsid = rewardTemplateSimple.getGSID();
                int itemCount = (int) (rewardTemplateSimple.getCOUNT() * 1f * randPercent / 100f);
                mailAtt.put(gsid, itemCount);
            }
            //send mail
            String formatMailContent = String.format(mailContent, chapter_index + 1, damageList.get(otherPlayerId), myRank);
            MailManager.getInstance().sendSysMailToSingle(otherPlayerId, mailTitle, formatMailContent, mailAtt);
            //
            myRank++;
        }
    }

    static class SingletonHolder {

        static LegionManager instance = new LegionManager();

    }

    public static LegionManager getInstance() {
        return SingletonHolder.instance;
    }

    public long getLegionId(int playerId) {
        long ret = 0;
        Collection<LegionBean> legionAll = LegionCache.getInstance().getLegionAll();
        for (LegionBean legionBean : legionAll) {
            if (legionBean.getDbLegionPlayers() != null && legionBean.getDbLegionPlayers().getMembers() != null
                    && legionBean.getDbLegionPlayers().getMembers().containsKey(playerId)) {
                ret = legionBean.getUuid();
                break;
            }
        }
        return ret;
    }

    public Map<Integer, Integer> getLegionTech(int playerId) {
        Map<Integer, Integer> ret = null;
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            return ret;
        }
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        return legionPlayer.getTech();
    }

    public LegionMission createLegionMission() {
        LegionMission legionMission = new LegionMission();
        int randMissionId = RandomUtils.nextInt(1, 3);
        legionMission.setId(randMissionId);
        return legionMission;
    }

    public boolean isLegionBossFinish(LegionBean legionBean) {
        if (legionBean == null || legionBean.getLegionBoss() == null) {
            return false;
        }
        DbLegionBoss legionBoss = legionBean.getLegionBoss();
        Integer chapterIndex = legionBoss.getChapterIndex();
        if (chapterIndex == null || chapterIndex < LegionBossTemplateCache.getInstance().getSize()) {
            return false;
        }
        return true;
    }

    public Map<Integer,Integer> techResetAllBack(LegionPlayer legionPlayer){
        Map<Integer, Integer> backItemMap = new HashMap<>();
        if(legionPlayer == null || legionPlayer.getTech() == null){
            return backItemMap;
        }
        for (Map.Entry<Integer, Integer> techPair : legionPlayer.getTech().entrySet()) {
            int techId = techPair.getKey();
            int techLevel = techPair.getValue();
            LegionTechTemplate legionTechTemplate = LegionTechTemplateCache.getInstance().getLegionTechTemplate(techId);
            for (int i = 0; i < techLevel; i++) {
                Integer costGold = legionTechTemplate.getCOST().getGoldCoin().get(i);
                Integer costLegionCoin = legionTechTemplate.getCOST().getLegionCoin().get(i);
                putBackItems(backItemMap, GameConfig.PLAYER.GOLD, costGold);
                putBackItems(backItemMap, LegionConstants.LEGION_COIN, costLegionCoin);
            }
        }
        return backItemMap;
    }

    private void putBackItems(Map<Integer, Integer> backItemMap, int gsid, Integer count) {
        if (backItemMap.containsKey(gsid)) {
            backItemMap.put(gsid, backItemMap.get(gsid) + count);
        } else {
            backItemMap.put(gsid, count);
        }
    }

}
