package game.module.chapter.logic;

import game.entity.PlayingRole;
import game.module.chapter.bean.DbBattleset;
import game.module.chapter.bean.PowerFormation;
import game.module.chapter.dao.PowerFormationCache;
import game.module.chapter.dao.PowerFormationDaoHelper;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class PowerFormationManager {

    private static Logger logger = LoggerFactory.getLogger(PowerFormationManager.class);

    static class SingletonHolder {
        static PowerFormationManager instance = new PowerFormationManager();
    }

    public static PowerFormationManager getInstance() {
        return SingletonHolder.instance;
    }

    public static final int POWER_FORMATION_STEP = 3000;

    public static final int POWER_FORMATION_STEP1_SIZE = 3;

    public int getPowerFormationNum(int power) {
        return power / POWER_FORMATION_STEP;
    }

    public void refreshPowerFormation(int oldpower, int newPower, int mythicId, Map<Integer, Long> formation, PlayingRole playingRole) {
        int oldNum = getPowerFormationNum(oldpower);
        int newNum = getPowerFormationNum(newPower);
        if (newNum <= oldNum) {
            return;
        }
        int playerId = playingRole.getId();
        Queue<PowerFormation> powerFormations = PowerFormationCache.getInstance().getPowerFormationByPower(newPower);
        PowerFormation powerFormation;
        if (powerFormations == null || powerFormations.size() < POWER_FORMATION_STEP1_SIZE) {
            powerFormation = new PowerFormation();
        } else {
            powerFormation = powerFormations.remove();
        }
        powerFormation.setPlayerId(playerId);
        powerFormation.setName(playingRole.getPlayerBean().getName());
        powerFormation.setLevel(playingRole.getPlayerBean().getLevel());
        powerFormation.setIconId(playingRole.getPlayerBean().getIconid());
        powerFormation.setHeadId(playingRole.getPlayerBean().getHeadid());
        powerFormation.setFrameId(playingRole.getPlayerBean().getFrameid());
        powerFormation.setPower(newPower);
        DbBattleset dbBattleset = new DbBattleset();
        powerFormation.setDbBattleset(dbBattleset);
        if (mythicId > 0) {
            MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicId);
            if (mythicalAnimal != null) {
                dbBattleset.setMythic(mythicalAnimal);
            }
        }
        Map<Integer, GeneralBean> generalBeanMap = new HashMap<>();
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        for (Map.Entry<Integer, Long> aEntry : formation.entrySet()) {
            GeneralBean generalBean = generalAll.get(aEntry.getValue());
            if(generalBean == null) {
                continue;
            }
            generalBeanMap.put(aEntry.getKey(), generalBean);
        }
        dbBattleset.setTeam(generalBeanMap);
        //save2db
        if (powerFormation.getId() == null) {
            PowerFormationDaoHelper.asyncInsertPowerFormation(powerFormation);
        } else {
            PowerFormationDaoHelper.asyncUpdatePowerFormation(powerFormation);
        }
        //add cache
        PowerFormationCache.getInstance().addPowerFormation(powerFormation);
    }

}
