package game.module.mission.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Admin
 */
public class MissionAchieveManager {

    private static Logger logger = LoggerFactory.getLogger(MissionAchieveManager.class);

    static class SingletonHolder {

        static MissionAchieveManager instance = new MissionAchieveManager();
    }

    public static MissionAchieveManager getInstance() {
        return SingletonHolder.instance;
    }


}
