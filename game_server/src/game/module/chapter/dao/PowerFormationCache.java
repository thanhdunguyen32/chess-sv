package game.module.chapter.dao;

import game.module.chapter.bean.PowerFormation;
import game.module.chapter.logic.PowerFormationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class PowerFormationCache {

    private static final Logger logger = LoggerFactory.getLogger(PowerFormationCache.class);

    static class SingletonHolder {
        static PowerFormationCache instance = new PowerFormationCache();
    }

    public static PowerFormationCache getInstance() {
        return SingletonHolder.instance;
    }

    private final List<Queue<PowerFormation>> formationList = new CopyOnWriteArrayList<>();//3500

    public void loadFromDb() {
        logger.info("powerFormation loadFromDb!");
        //init cache
        for (int i = 0; i < 3500; i++) {
            formationList.add(null);
        }
        //load from db
        List<PowerFormation> powerFormationes = PowerFormationDao.getInstance().getPowerFormation();
        for (PowerFormation powerFormationBean : powerFormationes) {
            Integer power = powerFormationBean.getPower();
            int powerFormationNum = PowerFormationManager.getInstance().getPowerFormationNum(power);
            Queue<PowerFormation> powerFormations = formationList.get(powerFormationNum);
            if (powerFormations == null) {
                powerFormations = new LinkedList<>();
                formationList.set(powerFormationNum, powerFormations);
            }
            powerFormations.add(powerFormationBean);
        }
    }

    public Queue<PowerFormation> getPowerFormationByPower(int power) {
        int powerFormationNum = PowerFormationManager.getInstance().getPowerFormationNum(power);
        return formationList.get(powerFormationNum);
    }

    public Queue<PowerFormation> getPowerFormationByIndex(int powerNumIndex) {
        return formationList.get(powerNumIndex);
    }

    public void addPowerFormation(PowerFormation powerFormationBean) {
        Integer power = powerFormationBean.getPower();
        int powerFormationNum = PowerFormationManager.getInstance().getPowerFormationNum(power);
        Queue<PowerFormation> powerFormations = formationList.get(powerFormationNum);
        if (powerFormations == null) {
            powerFormations = new LinkedBlockingDeque<>();
            formationList.set(powerFormationNum, powerFormations);
        }
        powerFormations.add(powerFormationBean);
    }

    public void removePowerFormation(int powerNum) {
        formationList.set(powerNum, null);
    }

    public List<Queue<PowerFormation>> getPowerFormationAll() {
        return formationList;
    }

}
