package game.module.kingpvp.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.bean.DbBattleset;
import game.module.chapter.logic.BattleFormationManager;
import game.module.exped.logic.FormationRobotManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.kingpvp.bean.KingPvp;
import game.module.kingpvp.bean.KingPvpPlayer;
import game.module.kingpvp.dao.KingPvpCache;
import game.module.kingpvp.dao.KingPvpDao;
import game.module.kingpvp.dao.KingPvpTemplateCache;
import game.module.mail.logic.MailManager;
import game.module.mythical.bean.MythicalAnimal;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.rank.logic.RankManager;
import game.module.template.GeneralTemplate;
import game.module.template.KingLineupNumTemplate;
import game.module.template.KingStageTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.PlayerHeadManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author HeXuhui
 */
public class KingPvpManager {

    private static Logger logger = LoggerFactory.getLogger(KingPvpManager.class);

    static class SingletonHolder {
        static KingPvpManager instance = new KingPvpManager();
    }

    public static KingPvpManager getInstance() {
        return SingletonHolder.instance;
    }

    private ReentrantReadWriteLock kingPvpRankLock = new ReentrantReadWriteLock();

    public int getKpOnlineNum(int playerId) {
        int stageBig = 0;
        KingPvp kingPvp = KingPvpCache.getInstance().getKingPvp(playerId);
        if (kingPvp != null) {
            stageBig = kingPvp.getStage() / 100;
        }
        int ret = 1;
        List<KingLineupNumTemplate> lineupNumTemplates = KingPvpTemplateCache.getInstance().getLineupNumTemplates();
        for (KingLineupNumTemplate kingLineupNumTemplate : lineupNumTemplates) {
            if (stageBig <= kingLineupNumTemplate.getSTAGE()) {
                ret = kingLineupNumTemplate.getLINEUPNUM();
                break;
            }
        }
        return ret;
    }

    public KingPvp createKingPvp(int playerId) {
        KingPvp kingPvp = new KingPvp();
        kingPvp.setPlayerId(playerId);
        kingPvp.setStar(0);
        kingPvp.setStage(0);
        kingPvp.setHstage(0);
        return kingPvp;
    }

    public KingPvpPlayer searchEnemy(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        KingPvp kingPvp = KingPvpCache.getInstance().getKingPvp(playerId);
        //定位赛
        if (kingPvp == null || kingPvp.getLocate() == null || kingPvp.getLocate().size() < 5) {
            List<KingPvp> toSelectList = new ArrayList<>();
            kingPvpRankLock.readLock().lock();
            for (int i = 0; i < 5; i++) {
                List<KingPvp> rankList = KingPvpCache.getInstance().getRankList(i);
                toSelectList.addAll(rankList);
            }
            kingPvpRankLock.readLock().unlock();
            KingPvpPlayer kingPvpPlayer;
            boolean isSelectPlayer = RandomUtils.nextInt(0, 16) < toSelectList.size();
            if (isSelectPlayer) {
                int randIndex = RandomUtils.nextInt(0, toSelectList.size());
                KingPvp targetKingPvp = toSelectList.get(randIndex);
                kingPvpPlayer = buildKingPvpEnemyPlayer(targetKingPvp);
            } else {
                //选择机器人
                int robotLevel = RandomUtils.nextInt(140, 180);
                kingPvpPlayer = buildKingPvpEnemyRobot(robotLevel);
            }
            return kingPvpPlayer;
        } else {
            Integer stage = kingPvp.getStage();
            int stageSmall = stage % 100;
            int startIndex = (stage / 100 - 1) * 5 + stage % 100 - 1;
            int endIndex = (stage / 100 - 1) * 5 + 4;
            Integer star = kingPvp.getStar();
            List<KingPvp> toSelectList = new ArrayList<>();
            if (star >= 5 && stageSmall == 5) {//晋级赛
                startIndex += 1;
                endIndex += 5;
            }
            kingPvpRankLock.readLock().lock();
            for (int i = startIndex; i <= endIndex && i < 26; i++) {
                List<KingPvp> rankList = KingPvpCache.getInstance().getRankList(i);
                toSelectList.addAll(rankList);
            }
            kingPvpRankLock.readLock().unlock();
            //remove self
            toSelectList.remove(kingPvp);
            KingPvpPlayer kingPvpPlayer;
            boolean isSelectPlayer = RandomUtils.nextInt(0, 16) < toSelectList.size();
            if (isSelectPlayer) {
                int randIndex = RandomUtils.nextInt(0, toSelectList.size());
                KingPvp targetKingPvp = toSelectList.get(randIndex);
                kingPvpPlayer = buildKingPvpEnemyPlayer(targetKingPvp);
            } else {
                //选择机器人
                int robotLevel = RandomUtils.nextInt(120 + startIndex * 8, 160 + startIndex * 8);
                kingPvpPlayer = buildKingPvpEnemyRobot(robotLevel);
            }
            return kingPvpPlayer;
        }
    }

    private KingPvpPlayer buildKingPvpEnemyPlayer(KingPvp targetKingPvp) {
        Integer targetPlayerId = targetKingPvp.getPlayerId();
        PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetPlayerId);
        KingPvpPlayer kingPvpPlayer = new KingPvpPlayer();
        kingPvpPlayer.setRname(poc.getName());
        kingPvpPlayer.setLevel(poc.getLevel());
        kingPvpPlayer.setIconid(poc.getIconid());
        kingPvpPlayer.setHeadid(poc.getHeadid());
        kingPvpPlayer.setFrameid(poc.getFrameid());
        //是否隐藏对手
        kingPvpPlayer.setHide(isHideEnemy());
        kingPvpPlayer.setId(targetPlayerId);
        kingPvpPlayer.setVip(poc.getVipLevel());
        kingPvpPlayer.setCreateTime(System.currentTimeMillis());
        kingPvpPlayer.setPower(poc.getPower());
        List<DbBattleset> dbBattlesetList = new ArrayList<>();
        BattleFormation battleFormation = PlayerOfflineManager.getInstance().getBattleFormation(targetPlayerId);
        Map<Long, GeneralBean> generalAll = PlayerOfflineManager.getInstance().getGeneralAll(targetPlayerId);
        Map<Integer, MythicalAnimal> mythicalAll = PlayerOfflineManager.getInstance().getMythicalAll(targetPlayerId);
        //
        int myKpFormationNum = getMyKpFormationNum(targetKingPvp.getStage());
        for (int i = 0; i < 3; i++) {
            String kingpvpFormation = "kingpvp" + (i + 1);
            int formationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, kingpvpFormation);
            Map<Integer, Long> kpFormationSet = BattleFormationManager.getInstance().getFormationByType(formationIndex, battleFormation);
            if (kpFormationSet != null && i + 1 <= myKpFormationNum) {
                DbBattleset dbBattleset = new DbBattleset();
                //mythic
                Map<Integer, Integer> mythics = battleFormation.getMythics();
                if (mythics != null && mythics.containsKey(formationIndex)) {
                    Integer mythicalId = mythics.get(formationIndex);
                    MythicalAnimal mythicalAnimal = mythicalAll.get(mythicalId);
                    dbBattleset.setMythic(mythicalAnimal);
                }
                //team
                Map<Integer, GeneralBean> generalBeanMap = new HashMap<>();
                for (Map.Entry<Integer, Long> aEntry : kpFormationSet.entrySet()) {
                    GeneralBean generalBean = generalAll.get(aEntry.getValue());
                    if (generalBean == null) {
                        continue;
                    }
                    generalBeanMap.put(aEntry.getKey(), generalBean);
                }
                dbBattleset.setTeam(generalBeanMap);
                dbBattlesetList.add(dbBattleset);
            }
        }
        kingPvpPlayer.setDbBattlesetList(dbBattlesetList);
        return kingPvpPlayer;
    }

    public KingPvpPlayer buildKingPvpEnemyRobot(int robotLevel) {
        robotLevel = Math.max(robotLevel, 140);
        Map<Integer, BattlePlayerBase> battlePlayerMap = FormationRobotManager.getInstance().generateRobot(robotLevel, 6);
        BattlePlayerBase next = battlePlayerMap.values().iterator().next();
        int gsid = next.getGsid();
        GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        String rname = heroTemplate.getNAME() + heroTemplate.getSTAR() + "星" + GeneralTemplateCache.getInstance().getOccuCn(heroTemplate.getOCCU());
        KingPvpPlayer kingPvpPlayer = new KingPvpPlayer();
        kingPvpPlayer.setRname(rname);
        kingPvpPlayer.setLevel(robotLevel);
        int headid = PlayerHeadManager.getInstance().getHeadid(gsid);
        int iconId = PlayerHeadManager.getInstance().headId2IconId(headid);
        kingPvpPlayer.setIconid(iconId);
        kingPvpPlayer.setHeadid(headid);
        kingPvpPlayer.setFrameid(51001);
        //是否隐藏对手
        kingPvpPlayer.setHide(isHideEnemy());
        kingPvpPlayer.setId(0);
        kingPvpPlayer.setVip(0);
        kingPvpPlayer.setCreateTime(System.currentTimeMillis());
        kingPvpPlayer.setPower(FormationRobotManager.getInstance().getPower(battlePlayerMap));
        kingPvpPlayer.setBattlePlayerMap(battlePlayerMap);
        return kingPvpPlayer;
    }

    private int isHideEnemy() {
        return RandomUtils.nextInt(0, 10) < 1 ? 1 : 0;
    }

    public void stageChange(int oldStage, int newStage, KingPvp kingPvp) {
        kingPvpRankLock.writeLock().lock();
        if (oldStage > 0) {
            int rankIndex = KingPvpCache.getInstance().getIndexByStage(oldStage);
            List<KingPvp> rankList = KingPvpCache.getInstance().getRankList(rankIndex);
            if (rankList != null) {
                rankList.remove(kingPvp);
            }
        }
        int rankIndex = KingPvpCache.getInstance().getIndexByStage(newStage);
        List<KingPvp> rankList = KingPvpCache.getInstance().getRankList(rankIndex);
        if (rankList == null) {
            rankList = new ArrayList<>();
            KingPvpCache.getInstance().addRankList(rankIndex, rankList);
        }
        rankList.add(kingPvp);
        kingPvpRankLock.writeLock().unlock();
    }

    public int getMyKpFormationNum(int stage) {
        int stageBig = stage / 100;
        int ret = 1;
        List<KingLineupNumTemplate> lineupNumTemplates = KingPvpTemplateCache.getInstance().getLineupNumTemplates();
        for (KingLineupNumTemplate kingLineupNumTemplate : lineupNumTemplates) {
            if (stageBig <= kingLineupNumTemplate.getSTAGE()) {
                ret = kingLineupNumTemplate.getLINEUPNUM();
                break;
            }
        }
        return ret;
    }

    public void kpSeasonEnd(int season) {
        //在秋季最后一天24点关闭。
        if (season != 3) {
            return;
        }
        GameServer.executorService.execute(() -> {
            //前10大师自动晋升王者
            List<KingPvp> newRankList = KingPvpDao.getInstance().randKingPvp(500);
            for (int i = 0; i < 10 && i < newRankList.size(); i++) {
                KingPvp kingPvp = newRankList.get(i);
                if (kingPvp.getStage() >= 601) {
                    kingPvp.setStage(701);
                    KingPvpDao.getInstance().updateKingPvpStage(kingPvp);
                }
            }
            for (KingPvp kingPvp : newRankList) {
                int stage = kingPvp.getStage();
                KingStageTemplate kingStageTemplate = KingPvpTemplateCache.getInstance().getKingStageTemplate(stage);
                String mailTitle = "Phần thưởng hoạt động tranh đoạt thành trì quốc chiến"; //"王者演武赛季排行奖励"
                String mailContent = String.format("Bạn đã đạt được vị trí thứ %s trong đấu trường diễn võ, bạn đã nhận được các phần thưởng sau: Hãy tiếp tục cố gắng!", kingStageTemplate.getNAME()); //String.format("您在王者演武中达到段位：%s，以下是奖励：", kingStageTemplate.getNAME());
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : kingStageTemplate.getREWARDS()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                MailManager.getInstance().sendSysMailToSingle(kingPvp.getPlayerId(), mailTitle, mailContent, mailAtt);
            }
        });
    }

    //王者演武每个赛季在春季第一天0点开启
    public void kpSeasonStart(int season) {
        if (season != 4) {
            return;
        }
        GameServer.executorService.execute(() -> {
            KingPvpCache.getInstance().clearAll();
            KingPvpDao.getInstance().clearAll();
        });
    }

}
