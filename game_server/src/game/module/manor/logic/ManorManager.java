package game.module.manor.logic;

import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.manor.bean.DbBattleGeneral;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.dao.ManorTemplateCache;
import game.module.manor.dao.MyManorTemplateCache;
import game.module.template.ChapterBattleTemplate;
import game.module.template.ManorTemplate;
import game.module.template.MyManorTemplate;
import game.module.template.RewardTemplateSimple;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author HeXuhui
 */
public class ManorManager {

    private static Logger logger = LoggerFactory.getLogger(ManorManager.class);

    static class SingletonHolder {

        static ManorManager instance = new ManorManager();


    }

    public static ManorManager getInstance() {
        return SingletonHolder.instance;
    }

    public ManorBean createManor(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ManorBean expedBean = new ManorBean();
        expedBean.setPlayerId(playerId);
        int manorLevel = 1;
        expedBean.setLevel(manorLevel);
        expedBean.setGainFoodTime(new Date());
        expedBean.setManorField(createManorField(manorLevel));
        ManorTemplate.ManorHomeTemplate manorHomeTemplate = ManorTemplateCache.getInstance().getManorHomeTemplate(manorLevel);
        Integer maxFood = manorHomeTemplate.getUPITEM().get(0).getCOUNT();
        AwardUtils.setRes(playingRole, GameConfig.PLAYER.FOOD, maxFood, true);
        return expedBean;
    }

    public ManorBean.DbManorField createManorField(int manorLevel) {
        ManorBean.DbManorField dbManorField = new ManorBean.DbManorField();
        Date now = new Date();
        dbManorField.setBossRefreshTime(DateUtils.addHours(now, ManorConstants.BOSS_REFERSH_HOUR));
        dbManorField.setEnemyRefreshTime(DateUtils.addHours(now, ManorConstants.ENEMY_REFERSH_HOUR));
        dbManorField.setMop(1);
        //enemy
        List<ManorBean.DbManorEnemy> enemys = new ArrayList<>();
        dbManorField.setEnemys(enemys);
        ManorTemplate.ManorHomeTemplate manorHomeTemplate = ManorTemplateCache.getInstance().getManorHomeTemplate(manorLevel);
        Integer fieldCount = manorHomeTemplate.getCOUNT();
        for (int i = 0; i < fieldCount; i++) {
            ManorBean.DbManorEnemy fieldEnemy1 = createFieldEnemy1(i, manorLevel);
            enemys.add(fieldEnemy1);
        }
        //boss
        createFieldBoss(null, dbManorField, manorLevel);
        return dbManorField;
    }

    private void createFieldBoss(PlayingRole playingRole, ManorBean.DbManorField dbManorField, int manorLevel) {
        MyManorTemplate.MyManorBossTemplate bossTemplate = MyManorTemplateCache.getInstance().getBossTemplate(manorLevel);
        dbManorField.setBossid(bossTemplate.getBossid());
        dbManorField.setBossNowHp(bossTemplate.getMaxhp());
        dbManorField.setBossMaxHp(bossTemplate.getMaxhp());
        dbManorField.setBossState(2);
        dbManorField.setBossLastDamage(0);
        Map<Integer, DbBattleGeneral> formationHeros = new HashMap<>(bossTemplate.getBset().size());
        dbManorField.setBossFormationHeros(formationHeros);
        for (Map.Entry<Integer, ChapterBattleTemplate> aPosGeneral : bossTemplate.getBset().entrySet()) {
            DbBattleGeneral dbBattleGeneral = new DbBattleGeneral();
            formationHeros.put(aPosGeneral.getKey(), dbBattleGeneral);
            ChapterBattleTemplate generalConfig = aPosGeneral.getValue();
            dbBattleGeneral.setChapterBattleTemplate(generalConfig);
            dbBattleGeneral.setNowhp(generalConfig.getExhp().intValue());
            dbBattleGeneral.setMaxhp(generalConfig.getExhp().intValue());
        }
        //team count reset
        if (playingRole != null) {
            AwardUtils.setRes(playingRole, ManorConstants.MANOR_BOSS_TEAM_FIGHT_COUNT_MARK, 0, true);
        }
    }

    public ManorBean.DbManorEnemy createFieldEnemy1(int i, int manorLevel) {
        Map<Integer, Integer> fightTemplate = ManorTemplateCache.getInstance().getFight();
        //id
        List<Integer> ids = new ArrayList<>();
        for (Integer aid : fightTemplate.keySet()) {
            if (aid / 10 == manorLevel) {
                ids.add(aid);
            }
        }
        int randIndex = RandomUtils.nextInt(0, ids.size());
        int id = ids.get(randIndex);
        ManorBean.DbManorEnemy dbManorEnemy = new ManorBean.DbManorEnemy();
        dbManorEnemy.setId(id);
        //随机生成box
        if (RandomUtils.nextInt(0, 10) < 4) {
            dbManorEnemy.setHasBoxOpen(false);
            randIndex = RandomUtils.nextInt(0, ManorConstants.MANOR_FIELD_BOX_REWARDS.length);
            int[] randReward1 = ManorConstants.MANOR_FIELD_BOX_REWARDS[randIndex];
            int randCount = RandomUtils.nextInt(randReward1[1], randReward1[2] + 1);
            dbManorEnemy.setBoxItem(Collections.singletonList(new RewardTemplateSimple(randReward1[0], randCount)));
        }
        //formation generals
        List<Map<Integer, ChapterBattleTemplate>> enemyTemplates = MyManorTemplateCache.getInstance().getEnemyTemplate(manorLevel);
        Map<Integer, ChapterBattleTemplate> enemyFormation = enemyTemplates.get(RandomUtils.nextInt(0, enemyTemplates.size()));
        Map<Integer, DbBattleGeneral> formationHeros = new HashMap<>(enemyFormation.size());
        dbManorEnemy.setFormationHeros(formationHeros);
        for (Map.Entry<Integer, ChapterBattleTemplate> aPosGeneral : enemyFormation.entrySet()) {
            DbBattleGeneral dbBattleGeneral = new DbBattleGeneral();
            formationHeros.put(aPosGeneral.getKey(), dbBattleGeneral);
            ChapterBattleTemplate generalConfig = aPosGeneral.getValue();
            dbBattleGeneral.setChapterBattleTemplate(generalConfig);
            dbBattleGeneral.setNowhp(generalConfig.getExhp().intValue());
            dbBattleGeneral.setMaxhp(generalConfig.getExhp().intValue());
        }
        return dbManorEnemy;
    }

    public void fieldReset(PlayingRole playingRole, boolean isForce) {
        int playerId = playingRole.getId();
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        ManorBean.DbManorField dbManorField = manorBean.getManorField();
        int manorLevel = manorBean.getLevel();
        if (dbManorField == null) {
            manorBean.setManorField(createManorField(manorLevel));
            ManorDaoHelper.asyncUpdateManor(manorBean);
        } else {
            Date now = new Date();
            boolean isDo = false;
            if (isForce || dbManorField.getBossRefreshTime().before(now)) {
                createFieldBoss(playingRole, dbManorField, manorLevel);
                dbManorField.setBossRefreshTime(DateUtils.addHours(now, ManorConstants.BOSS_REFERSH_HOUR));
                isDo = true;
            }
            if (isForce || dbManorField.getEnemyRefreshTime().before(now)) {
                dbManorField.getEnemys().clear();
                ManorTemplate.ManorHomeTemplate manorHomeTemplate = ManorTemplateCache.getInstance().getManorHomeTemplate(manorLevel);
                Integer fieldCount = manorHomeTemplate.getCOUNT();
                for (int i = 0; i < fieldCount; i++) {
                    ManorBean.DbManorEnemy fieldEnemy1 = createFieldEnemy1(i, manorLevel);
                    dbManorField.getEnemys().add(fieldEnemy1);
                }
                dbManorField.setEnemyRefreshTime(DateUtils.addHours(now, ManorConstants.ENEMY_REFERSH_HOUR));
                isDo = true;
            }
            if (isDo) {
                ManorDaoHelper.asyncUpdateManor(manorBean);
            }
        }
    }

}
