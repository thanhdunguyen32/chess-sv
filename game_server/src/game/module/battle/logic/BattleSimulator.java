package game.module.battle.logic;

import game.module.battle.dao.BattlePlayer;
import game.module.battle.dao.BattlePlayerBase;
import game.module.battle.dao.RoleInfoTemplateCache;
import game.module.chapter.bean.DbBattleset;
import game.module.dungeon.bean.DungeonNode;
import game.module.exped.bean.ExpedPlayer;
import game.module.friend.bean.FriendBoss;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.kingpvp.bean.KingPvpPlayer;
import game.module.manor.bean.DbBattleGeneral;
import game.module.mine.bean.DBMinePoint;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.template.ChapterBattleTemplate;
import game.module.template.GeneralTemplate;
import game.module.template.RoleInfoTemplate;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleSimulator {

    private static Logger logger = LoggerFactory.getLogger(BattleSimulator.class);

    public WsMessageBase.IOBattleResult simulateDungeon(int playerId, Map<Integer, Long> battleFormation, Map<Long, Integer> onlineGenerals,
                                                        DungeonNode.DungeonNodeDetail dungeonNodeDetail) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            Integer myHpPercent = onlineGenerals.get(generalUuid);
            int currentHp = myHpPercent != null ? (int) (generalBean.getHp() * (myHpPercent / 10000f)) : generalBean.getHp();
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), currentHp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, ChapterBattleTemplate> chapterBattleTemplatePair : dungeonNodeDetail.getBattleset().entrySet()) {
            Integer enemypos = chapterBattleTemplatePair.getKey();
            ChapterBattleTemplate chapterBattleTemplate = chapterBattleTemplatePair.getValue();
            BattlePlayer pveProperInfo = DbUtils.getPveProperInfo(battleIndex, enemypos, chapterBattleTemplate);
            Integer hppercent = dungeonNodeDetail.getEnemyHpPercent().get(enemypos);
            pveProperInfo.setHp((int) (pveProperInfo.getMaxhp() * (hppercent / 10000f)));
            enemyPropertyMap.put(enemypos, pveProperInfo);
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateFriendBoss(int playerId, Map<Integer, Long> battleFormation, FriendBoss.DbFriendBoss friendBoss) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), generalBean.getHp(), generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, DbBattleGeneral> chapterBattleTemplate : friendBoss.getFormationHeros().entrySet()) {
            DbBattleGeneral dbBattleGeneral = chapterBattleTemplate.getValue();
            BattlePlayer pveProperInfo = DbUtils.getPveProperInfo(battleIndex, chapterBattleTemplate.getKey(), dbBattleGeneral.getChapterBattleTemplate());
            pveProperInfo.setHp(dbBattleGeneral.getNowhp());
            enemyPropertyMap.put(chapterBattleTemplate.getKey(), pveProperInfo);
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    static class SingletonHolder {
        static BattleSimulator instance = new BattleSimulator();
    }

    public static BattleSimulator getInstance() {
        return SingletonHolder.instance;
    }

    public WsMessageBase.IOBattleResult simulateTemplateHp(int playerId, Map<Integer, Long> battleFormation, Map<Integer, DbBattleGeneral> enemys) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        if(generalAll == null){
            generalAll = PlayerOfflineManager.getInstance().getGeneralAll(playerId);
        }
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            if(generalBean == null){
                continue;
            }
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), generalBean.getHp(), generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, DbBattleGeneral> chapterBattleTemplate : enemys.entrySet()) {
            DbBattleGeneral dbBattleGeneral = chapterBattleTemplate.getValue();
            BattlePlayer pveProperInfo = DbUtils.getPveProperInfo(battleIndex, chapterBattleTemplate.getKey(), dbBattleGeneral.getChapterBattleTemplate());
            pveProperInfo.setHp(dbBattleGeneral.getNowhp());
            enemyPropertyMap.put(chapterBattleTemplate.getKey(), pveProperInfo);
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateTemplate(int playerId, Map<Integer, Long> battleFormation, Map<Integer, ChapterBattleTemplate> enemys) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), generalBean.getHp(), generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, ChapterBattleTemplate> chapterBattleTemplate : enemys.entrySet()) {
            BattlePlayer pveProperInfo = DbUtils.getPveProperInfo(battleIndex, chapterBattleTemplate.getKey(), chapterBattleTemplate.getValue());
            enemyPropertyMap.put(chapterBattleTemplate.getKey(), pveProperInfo);
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateFriend(int playerId, Map<Integer, Long> battleFormation, Map<Long, Integer> myHpMap, int rightPlayerId,
                                                      Map<Integer, Long> rightBattleFormation, Map<Integer, Integer> enemyHpMap) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            if (myHpMap != null && myHpMap.containsKey(generalUuid)) {
                myhp = (int) ((long) myhp * myHpMap.get(generalUuid) / 10000L);
            }
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        generalAll = PlayerOfflineManager.getInstance().getGeneralAll(rightPlayerId);
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, Long> aEntry : rightBattleFormation.entrySet()) {
            int formationPos = aEntry.getKey();
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            if(generalBean == null){
                continue;
            }
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            if (enemyHpMap != null && enemyHpMap.containsKey(formationPos)) {
                myhp = (int) ((long) myhp * enemyHpMap.get(formationPos) / 10000L);
            }
            enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    formationPos, generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateExped(int playerId, Map<Integer, Long> battleFormation, Map<Long, Integer> myHpMap, ExpedPlayer expedPlayer,
                                                      Map<Integer, Integer> enemyHpMap) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            if (myHpMap != null && myHpMap.containsKey(generalUuid)) {
                myhp = (int) ((long) myhp * myHpMap.get(generalUuid) / 10000L);
            }
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        if (expedPlayer.getDbBattleset() != null) {
            Map<Integer, GeneralBean> rightBattleFormation = expedPlayer.getDbBattleset().getTeam();
            for (Map.Entry<Integer, GeneralBean> aEntry : rightBattleFormation.entrySet()) {
                int formationPos = aEntry.getKey();
                GeneralBean generalBean = aEntry.getValue();
                GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
                int myhp = generalBean.getHp();
                if (enemyHpMap != null && enemyHpMap.containsKey(formationPos)) {
                    myhp = (int) ((long) myhp * enemyHpMap.get(formationPos) / 10000L);
                }
                enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                        formationPos, generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                        generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(),
                        generalBean.getPcrid()));
                battleIndex++;
            }
        } else if (expedPlayer.getBattlePlayerMap() != null) {
            for (Map.Entry<Integer, BattlePlayerBase> aEntry : expedPlayer.getBattlePlayerMap().entrySet()) {
                int formationPos = aEntry.getKey();
                BattlePlayerBase battlePlayer = aEntry.getValue();
                int myhp = battlePlayer.getMaxhp();
                if (enemyHpMap != null && enemyHpMap.containsKey(formationPos)) {
                    myhp = (int) ((long) myhp * enemyHpMap.get(formationPos) / 10000L);
                }
                battlePlayer.setHp(myhp);
                enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, battlePlayer.getGsid(), battlePlayer.getLevel(), battlePlayer.getPclass(),
                        formationPos, battlePlayer.getHp(), myhp, battlePlayer.getAtk(), battlePlayer.getDef(), battlePlayer.getMdef(),
                        battlePlayer.getAtktime(), battlePlayer.getRange(), battlePlayer.getMsp(), battlePlayer.getPcri(), battlePlayer.getPcrid()));
                battleIndex++;
            }
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateMine(int playerId, Map<Integer, Long> battleFormation, DBMinePoint dbMinePoint) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        if (dbMinePoint.getDbBattleset() != null) {
            Map<Integer, GeneralBean> rightBattleFormation = dbMinePoint.getDbBattleset().getTeam();
            for (Map.Entry<Integer, GeneralBean> aEntry : rightBattleFormation.entrySet()) {
                int formationPos = aEntry.getKey();
                GeneralBean generalBean = aEntry.getValue();
                GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
                int myhp = generalBean.getHp();
                enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                        formationPos, generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                        generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(),
                        generalBean.getPcrid()));
                battleIndex++;
            }
        } else if (dbMinePoint.getBattlePlayerMap() != null) {
            for (Map.Entry<Integer, BattlePlayerBase> aEntry : dbMinePoint.getBattlePlayerMap().entrySet()) {
                int formationPos = aEntry.getKey();
                BattlePlayerBase battlePlayer = aEntry.getValue();
                int myhp = battlePlayer.getMaxhp();
                battlePlayer.setHp(myhp);
                enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, battlePlayer.getGsid(), battlePlayer.getLevel(), battlePlayer.getPclass(),
                        formationPos, battlePlayer.getHp(), myhp, battlePlayer.getAtk(), battlePlayer.getDef(), battlePlayer.getMdef(),
                        battlePlayer.getAtktime(), battlePlayer.getRange(), battlePlayer.getMsp(), battlePlayer.getPcri(), battlePlayer.getPcrid()));
                battleIndex++;
            }
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateGuozhanOffice(int playerId, Map<Integer, Long> battleFormation,
                                                              Map<Integer, GeneralBean> enemyPlayerFormation,Map<Integer, BattlePlayerBase> robotFormation) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        if (enemyPlayerFormation != null) {
            for (Map.Entry<Integer, GeneralBean> aEntry : enemyPlayerFormation.entrySet()) {
                int formationPos = aEntry.getKey();
                GeneralBean generalBean = aEntry.getValue();
                GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
                int myhp = generalBean.getHp();
                enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                        formationPos, generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                        generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(),
                        generalBean.getPcrid()));
                battleIndex++;
            }
        } else if (robotFormation != null) {
            for (Map.Entry<Integer, BattlePlayerBase> aEntry : robotFormation.entrySet()) {
                int formationPos = aEntry.getKey();
                BattlePlayerBase battlePlayer = aEntry.getValue();
                int myhp = battlePlayer.getMaxhp();
                battlePlayer.setHp(myhp);
                enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, battlePlayer.getGsid(), battlePlayer.getLevel(), battlePlayer.getPclass(),
                        formationPos, battlePlayer.getHp(), myhp, battlePlayer.getAtk(), battlePlayer.getDef(), battlePlayer.getMdef(),
                        battlePlayer.getAtktime(), battlePlayer.getRange(), battlePlayer.getMsp(), battlePlayer.getPcri(), battlePlayer.getPcrid()));
                battleIndex++;
            }
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }
    public WsMessageBase.IOBattleResult simulateGuozhanPvp(int playerId, Map<Integer, Long> battleFormation,int myHpPerc,
                                                              Map<Integer, GeneralBean> enemyPlayerFormation, int enemyHpPerc) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            myhp *= myHpPerc/100f;
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, GeneralBean> aEntry : enemyPlayerFormation.entrySet()) {
            int formationPos = aEntry.getKey();
            GeneralBean generalBean = aEntry.getValue();
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            myhp *= enemyHpPerc/100f;
            enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    formationPos, generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(),
                    generalBean.getPcrid()));
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateGuoZhan(int playerId, Map<Integer, Long> battleFormation, Map<Integer, BattlePlayerBase> robotFormation) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, BattlePlayerBase> aEntry : robotFormation.entrySet()) {
            int formationPos = aEntry.getKey();
            BattlePlayerBase battlePlayer = aEntry.getValue();
            int myhp = battlePlayer.getMaxhp();
            battlePlayer.setHp(myhp);
            enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, battlePlayer.getGsid(), battlePlayer.getLevel(), battlePlayer.getPclass(),
                    formationPos, battlePlayer.getHp(), myhp, battlePlayer.getAtk(), battlePlayer.getDef(), battlePlayer.getMdef(),
                    battlePlayer.getAtktime(), battlePlayer.getRange(), battlePlayer.getMsp(), battlePlayer.getPcri(), battlePlayer.getPcrid()));
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateKingPvpRobot(int playerId, Map<Integer, Long> battleFormation, Map<Integer, Integer> myHpMap,
                                                             Map<Integer, BattlePlayerBase> robotFormation, Map<Integer, Integer> enemyHpMap) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            int formationPos = aEntry.getKey();
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            if (myHpMap != null && myHpMap.containsKey(formationPos)) {
                myhp = (int) ((long) myhp * myHpMap.get(formationPos) / 10000L);
            }
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, BattlePlayerBase> aEntry : robotFormation.entrySet()) {
            int formationPos = aEntry.getKey();
            BattlePlayerBase battlePlayer = aEntry.getValue();
            int myhp = battlePlayer.getMaxhp();
            if (enemyHpMap != null && enemyHpMap.containsKey(formationPos)) {
                myhp = (int) ((long) myhp * enemyHpMap.get(formationPos) / 10000L);
            }
            battlePlayer.setHp(myhp);
            enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, battlePlayer.getGsid(), battlePlayer.getLevel(), battlePlayer.getPclass(),
                    formationPos, battlePlayer.getHp(), myhp, battlePlayer.getAtk(), battlePlayer.getDef(), battlePlayer.getMdef(),
                    battlePlayer.getAtktime(), battlePlayer.getRange(), battlePlayer.getMsp(), battlePlayer.getPcri(), battlePlayer.getPcrid()));
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulateKingPvpPlayer(int playerId, Map<Integer, Long> battleFormation, Map<Integer, Integer> myHpMap,
                                                         DbBattleset enemyBattleset,
                                                      Map<Integer, Integer> enemyHpMap) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            int formationPos = aEntry.getKey();
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            if (myHpMap != null && myHpMap.containsKey(formationPos)) {
                myhp = (int) ((long) myhp * myHpMap.get(formationPos) / 10000L);
            }
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        Map<Integer, GeneralBean> rightBattleFormation = enemyBattleset.getTeam();
        for (Map.Entry<Integer, GeneralBean> aEntry : rightBattleFormation.entrySet()) {
            int formationPos = aEntry.getKey();
            GeneralBean generalBean = aEntry.getValue();
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            int myhp = generalBean.getHp();
            if (enemyHpMap != null && enemyHpMap.containsKey(formationPos)) {
                myhp = (int) ((long) myhp * enemyHpMap.get(formationPos) / 10000L);
            }
            enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    formationPos, generalBean.getHp(), myhp, generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(),
                    generalBean.getPcrid()));
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulatePvp(int playerId, Map<Integer, Long> battleFormation, int rightPlayerId,
                                                    Map<Integer, Long> rightBattleFormation) {
        //copy my property
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        Map<Integer, BattlePlayer> myPropertyMap = new HashMap<>();
        int battleIndex = 0;
        for (Map.Entry<Integer, Long> aEntry : battleFormation.entrySet()) {
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            myPropertyMap.put(aEntry.getKey(), new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    aEntry.getKey(), generalBean.getHp(), generalBean.getHp(), generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        //init enemy property
        generalAll = PlayerOfflineManager.getInstance().getGeneralAll(rightPlayerId);
        Map<Integer, BattlePlayer> enemyPropertyMap = new HashMap<>();
        for (Map.Entry<Integer, Long> aEntry : rightBattleFormation.entrySet()) {
            int formationPos = aEntry.getKey();
            Long generalUuid = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(generalUuid);
            if(generalBean == null){
                continue;
            }
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            enemyPropertyMap.put(formationPos, new BattlePlayer(battleIndex, generalBean.getTemplateId(), generalBean.getLevel(), generalBean.getPclass(),
                    formationPos, generalBean.getHp(), generalBean.getHp(), generalBean.getAtk(), generalBean.getDef(), generalBean.getMdef(),
                    generalTemplate.getPROPERTY().getATKTIME(), generalBean.getRange(), generalBean.getMsp(), generalBean.getPcri(), generalBean.getPcrid()));
            battleIndex++;
        }
        return simulate(myPropertyMap, enemyPropertyMap);
    }

    public WsMessageBase.IOBattleResult simulate(Map<Integer, BattlePlayer> myPropertyMap, Map<Integer, BattlePlayer> enemyPropertyMap) {
        boolean ret = false;
//        logger.info("battle simulate start!left={},right={}", myPropertyMap, enemyPropertyMap);
        long startTime = System.currentTimeMillis();
        int totalFrames = getTotalFrames();
        //
        Map<Integer, Integer> leftHurtMap = new HashMap<>();
        for (Map.Entry<Integer, BattlePlayer> aEntry : myPropertyMap.entrySet()) {
            leftHurtMap.put(aEntry.getKey(), 0);
        }
        Map<Integer, Integer> rightHurtMap = new HashMap<>();
        for (Map.Entry<Integer, BattlePlayer> chapterBattleTemplate : enemyPropertyMap.entrySet()) {
            rightHurtMap.put(chapterBattleTemplate.getKey(), 0);
        }
        //battle start
        BTools bTools = new BTools(RandomUtils.nextInt());
        int _step;
        for (_step = 0; _step < totalFrames; _step++) {
            //loop left
            boolean isLeftAllDie = true;
            for (Map.Entry<Integer, BattlePlayer> aEntry : myPropertyMap.entrySet()) {
                Integer pos = aEntry.getKey();
                BattlePlayer battlePlayer = aEntry.getValue();
                //die
                if (battlePlayer.getHp() <= 0) {
                    continue;
                }
                isLeftAllDie = false;
                doAttack(battlePlayer, _step, enemyPropertyMap, pos, bTools, leftHurtMap);
            }
            //loop enemy
            boolean isRightAllDie = true;
            for (Map.Entry<Integer, BattlePlayer> aEntry : enemyPropertyMap.entrySet()) {
                int enemyPos = aEntry.getKey();
                BattlePlayer battlePlayer = aEntry.getValue();
                //die
                if (battlePlayer.getHp() <= 0) {
                    continue;
                }
                isRightAllDie = false;
                //轮到它attack
                doAttack(battlePlayer, _step, myPropertyMap, enemyPos, bTools, rightHurtMap);
            }
            //check win
            if (isLeftAllDie && !isRightAllDie) {
                ret = false;
                break;
            }
            if (!isLeftAllDie && isRightAllDie) {
                ret = true;
                break;
            }
        }
//        logger.info("battle simulate cost:{}ms,ret={},step={},myPlayers={},enemyPlayers={}", System.currentTimeMillis() - startTime, ret, _step, myPropertyMap,
//                enemyPropertyMap);
        //ret
        WsMessageBase.IOBattleResult ioBattleResult = new WsMessageBase.IOBattleResult();
        ioBattleResult.version = 202004292337L;
        ioBattleResult.ret = ret ? "win" : "lose";
        ioBattleResult.round = _step;
        ioBattleResult.lhp = new HashMap<>();
        ioBattleResult.lper = new HashMap<>();
        int leftSumHpPercent = 0;
        for (Map.Entry<Integer, BattlePlayer> aEntry : myPropertyMap.entrySet()) {
            Integer pos = aEntry.getKey();
            int hpval = Math.max(myPropertyMap.get(pos).getHp(), 0);
            ioBattleResult.lhp.put(pos, (long)hpval);
            int hppercent = 0;
            if (hpval > 0) {
                BattlePlayer battlePlayer = myPropertyMap.get(pos);
                hppercent = (int) (battlePlayer.getHp() * 10000L / battlePlayer.getMaxhp());
                ioBattleResult.lper.put(pos, hppercent);
            } else {
                ioBattleResult.lper.put(pos, 0);
            }
            leftSumHpPercent += hppercent;
        }
        ioBattleResult.ltper = leftSumHpPercent / myPropertyMap.size();
        ioBattleResult.rhp = new HashMap<>();
        ioBattleResult.rper = new HashMap<>();
        int rightSumHpPercent = 0;
        for (Map.Entry<Integer, BattlePlayer> aEntry : enemyPropertyMap.entrySet()) {
            Integer pos = aEntry.getKey();
            int hpval = Math.max(enemyPropertyMap.get(pos).getHp(), 0);
            ioBattleResult.rhp.put(pos, (long)hpval);
            int hppercent = 0;
            if (hpval > 0) {
                BattlePlayer battlePlayer = enemyPropertyMap.get(pos);
                hppercent = (int) (battlePlayer.getHp() * 10000L / battlePlayer.getMaxhp());
                ioBattleResult.rper.put(pos, hppercent);
            } else {
                ioBattleResult.rper.put(pos, 0);
            }
            rightSumHpPercent += hppercent;
        }
        ioBattleResult.rtper = rightSumHpPercent / enemyPropertyMap.size();
        //
        WsMessageBase.IOBattleReport ioBattleReport = new WsMessageBase.IOBattleReport();
        ioBattleResult.report = ioBattleReport;
        ioBattleReport.left = new ArrayList<>();
        for (Map.Entry<Integer, BattlePlayer> aEntry : myPropertyMap.entrySet()) {
            BattlePlayer battlePlayer = aEntry.getValue();
            ioBattleReport.left.add(new WsMessageBase.IOBattleReportItem(battlePlayer.getGsid(), leftHurtMap.get(aEntry.getKey()), 0,
                    battlePlayer.getLevel(), battlePlayer.getPclass()));
        }
        ioBattleReport.right = new ArrayList<>();
        for (Map.Entry<Integer, BattlePlayer> aEntry : enemyPropertyMap.entrySet()) {
            BattlePlayer chapterBattleTemplate = aEntry.getValue();
            ioBattleReport.right.add(new WsMessageBase.IOBattleReportItem(chapterBattleTemplate.getGsid(), rightHurtMap.get(aEntry.getKey()), 0,
                    chapterBattleTemplate.getLevel(), chapterBattleTemplate.getPclass()));
        }
        return ioBattleResult;
    }

    private void doAttack(BattlePlayer battlePlayer, int _step, Map<Integer, BattlePlayer> enemyPropertyMap, int formationPos, BTools bTools,
                          Map<Integer, Integer> hurtMap) {
        //轮到它attack
        Integer generalTemplateId = battlePlayer.getGsid();
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
        String generalModel = generalTemplate.getMODEL();
        RoleInfoTemplate roleInfoTemplate = RoleInfoTemplateCache.getInstance().getRoleInfoTemplateById(generalModel);
        if (_step % roleInfoTemplate.getAttinfo().getBatti() == roleInfoTemplate.getAttinfo().getHframe()) {
            BattlePlayer targetPlayer = battlePlayer.getTargetPlayer();
            if (targetPlayer == null || targetPlayer.getHp() <= 0) {
                targetPlayer = searchTargetPlayer(enemyPropertyMap);
                battlePlayer.setTargetPlayer(targetPlayer);
            }
            if (targetPlayer != null) {
                BTools.BattleHurt battleHurt = bTools.getHurm(battlePlayer.getLevel(), battlePlayer.getAtk(), targetPlayer.getDef(), 0,
                        battlePlayer.getPcri() / 1000f, 0f, 0f, 0f, 0f
                        , 0f, 2);
                //技能百分比加成
                battleHurt.hurm *= 1.3f;
                // logger.info("hp change,step={},playerIndex={},hp={}, diff={}", _step, battlePlayer.getBattleIndex(), targetPlayer.getHp(), -battleHurt.hurm);
                targetPlayer.setHp(targetPlayer.getHp() - battleHurt.hurm);
                hurtMap.put(formationPos, hurtMap.get(formationPos) + battleHurt.hurm);
//                        if (targetPlayer.getHp() <= 0) {
//                            myPropertyMap.remove(targetPlayer.getPos());
//                        }
            }
        }
    }

    private BattlePlayer searchTargetPlayer(Map<Integer, BattlePlayer> enemyPropertyMap) {
        List<BattlePlayer> toSelectList = new ArrayList<>();
        for (BattlePlayer battlePlayer : enemyPropertyMap.values()) {
            if (battlePlayer.getHp() > 0) {
                toSelectList.add(battlePlayer);
            }
        }
        if (toSelectList.size() > 0) {
            return toSelectList.get(RandomUtils.nextInt(0, toSelectList.size()));
        }
        return null;
    }

    public int getTotalFrames() {
        return 3600;
    }

    public static void main(String[] args) {
        BTools.BattleHurt bh = new BTools.BattleHurt(102,0,5.3f);
        bh.hurm *= 1.3f;
        logger.info("{}",bh.hurm);
    }

}
