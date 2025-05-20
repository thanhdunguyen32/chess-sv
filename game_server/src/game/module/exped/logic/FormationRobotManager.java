package game.module.exped.logic;

import game.module.battle.dao.BattlePlayerBase;
import game.module.battle.logic.DbUtils;
import game.module.exped.dao.MyFormationRobotTemplateCache;
import game.module.hero.dao.GeneralAwakeTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.template.ChapterBattleTemplate;
import game.module.template.GeneralTemplate;
import game.module.template.MyFormationRobotTemplate;
import lion.math.RandomDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HeXuhui
 */
public class FormationRobotManager {

    private static final Logger logger = LoggerFactory.getLogger(FormationRobotManager.class);

    static class SingletonHolder {
        static FormationRobotManager instance = new FormationRobotManager();
    }

    public static FormationRobotManager getInstance() {
        return SingletonHolder.instance;
    }

    public Map<Integer, BattlePlayerBase> generateRobot(int playerLevel) {
        Map<Integer, BattlePlayerBase> retmap = new HashMap<>();
        MyFormationRobotTemplate myFormationRobotTemplate = MyFormationRobotTemplateCache.getInstance().getMyFormationRobotTemplate(playerLevel);
        Integer online_num = myFormationRobotTemplate.getOnline_num();
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        int[] gsid = myFormationRobotTemplate.getGsid();
        for (int agsid : gsid) {
            rd.put(1, agsid);
        }
        RandomDispatcher<Integer> formationPosDispatcher = new RandomDispatcher<>();
        for (int i = 0; i < 32; i++) {
            formationPosDispatcher.put(1, i);
        }
        for (int i = 0; i < online_num; i++) {
            Integer agsid = rd.randomRemove();
            Integer formationPos = formationPosDispatcher.randomRemove();
            BattlePlayerBase pveProperInfo = DbUtils.getPveProperInfo(formationPos, agsid, playerLevel, myFormationRobotTemplate.getPclass(),
                    myFormationRobotTemplate.getExhp(), myFormationRobotTemplate.getExatk());
            retmap.put(formationPos, pveProperInfo);
        }
        return retmap;
    }

    public Map<Integer, BattlePlayerBase> generateRobot(int playerLevel,int generalStar) {
        Map<Integer, BattlePlayerBase> retmap = new HashMap<>();
        MyFormationRobotTemplate myFormationRobotTemplate = MyFormationRobotTemplateCache.getInstance().getMyFormationRobotTemplate(playerLevel);
        Integer online_num = myFormationRobotTemplate.getOnline_num();
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        int[] gsid = myFormationRobotTemplate.getGsid();
        for (int agsid : gsid) {
            rd.put(1, agsid);
        }
        RandomDispatcher<Integer> formationPosDispatcher = new RandomDispatcher<>();
        for (int i = 0; i < 32; i++) {
            formationPosDispatcher.put(1, i);
        }
        for (int i = 0; i < online_num; i++) {
            Integer agsid = rd.randomRemove();
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(agsid);
            Integer gStar = generalTemplate.getSTAR();
            while (gStar < generalStar) {
                agsid = GeneralAwakeTemplateCache.getInstance().getAwakeGsid(agsid);
                generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(agsid);
                gStar = generalTemplate.getSTAR();
            }
            Integer formationPos = formationPosDispatcher.randomRemove();
            BattlePlayerBase pveProperInfo = DbUtils.getPveProperInfo(formationPos, agsid, playerLevel, myFormationRobotTemplate.getPclass(),
                    myFormationRobotTemplate.getExhp(), myFormationRobotTemplate.getExatk());
            retmap.put(formationPos, pveProperInfo);
        }
        return retmap;
    }

    public Map<Integer, ChapterBattleTemplate> generateRobotRaw(int playerLevel, Integer pOnlineNum) {
        Map<Integer, ChapterBattleTemplate> retmap = new HashMap<>();
        MyFormationRobotTemplate myFormationRobotTemplate = MyFormationRobotTemplateCache.getInstance().getMyFormationRobotTemplate(playerLevel);
        Integer online_num = myFormationRobotTemplate.getOnline_num();
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        int[] gsid = myFormationRobotTemplate.getGsid();
        for (int agsid : gsid) {
            rd.put(1, agsid);
        }
        RandomDispatcher<Integer> formationPosDispatcher = new RandomDispatcher<>();
        for (int i = 0; i < 32; i++) {
            formationPosDispatcher.put(1, i);
        }
        for (int i = 0; i < (pOnlineNum != null ? pOnlineNum : online_num); i++) {
            Integer agsid = rd.randomRemove();
            Integer formationPos = formationPosDispatcher.randomRemove();
            ChapterBattleTemplate chapterBattleTemplate = new ChapterBattleTemplate(agsid, playerLevel, myFormationRobotTemplate.getPclass(), null,
                    myFormationRobotTemplate.getExhp(), myFormationRobotTemplate.getExatk());
            retmap.put(formationPos, chapterBattleTemplate);
        }
        return retmap;
    }

    public int getPower(Map<Integer, BattlePlayerBase> formationMap) {
        int sumPower = 0;
        for (BattlePlayerBase battlePlayer : formationMap.values()) {
            int robotPower = DbUtils.getRobotPower(battlePlayer);
            sumPower += robotPower;
        }
        return sumPower;
    }

}
