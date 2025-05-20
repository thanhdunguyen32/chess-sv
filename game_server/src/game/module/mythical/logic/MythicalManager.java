package game.module.mythical.logic;

import game.module.mythical.bean.MythicalAnimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author HeXuhui
 */
public class MythicalManager {

    private static Logger logger = LoggerFactory.getLogger(MythicalManager.class);

    static class SingletonHolder {

        static MythicalManager instance = new MythicalManager();

    }

    public static MythicalManager getInstance() {
        return SingletonHolder.instance;
    }

    public MythicalAnimal createMythicalAnimal(int playerId, int templateId) {
        MythicalAnimal mythicalAnimal = new MythicalAnimal();
        mythicalAnimal.setPlayerId(playerId);
        mythicalAnimal.setTemplateId(templateId);
        mythicalAnimal.setLevel(1);
        mythicalAnimal.setPclass(1);
        mythicalAnimal.setPassiveSkills(new ArrayList<>(Arrays.asList(1,0,0,0)));
        return mythicalAnimal;
    }

}
