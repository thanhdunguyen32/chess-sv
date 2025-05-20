package game.module.tower.dao;

import game.module.tower.bean.TowerReplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TowerReplayCache {

    private static Logger logger = LoggerFactory.getLogger(TowerReplayCache.class);

    static class SingletonHolder {
        static TowerReplayCache instance = new TowerReplayCache();
    }

    public static TowerReplayCache getInstance() {
        return SingletonHolder.instance;
    }

    private final Map<Integer, TowerReplay> towerReplayCache = new ConcurrentHashMap<>();

    public void loadFromDb() {
        logger.info("towerReplay loadFromDb!");
        List<TowerReplay> towerReplays = TowerReplayDao.getInstance().getTowerReplays();
        towerReplayCache.clear();
        for (TowerReplay towerReplay : towerReplays) {
            towerReplayCache.put(towerReplay.getTowerLevel(), towerReplay);
        }
    }

    public TowerReplay getTowerReplay(int towerLevel) {
        return towerReplayCache.get(towerLevel);
    }

    public void addTowerReplay(TowerReplay towerReplay) {
        towerReplayCache.put(towerReplay.getTowerLevel(), towerReplay);
    }

    public void removeTowerReplay(int towerLevel) {
        towerReplayCache.remove(towerLevel);
    }

    public Collection<TowerReplay> getTowerReplayAll() {
        return towerReplayCache.values();
    }

}
