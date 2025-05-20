package game.module.tower.dao;

import game.GameServer;
import game.module.tower.bean.TowerReplay;

public class TowerReplayDaoHelper {

    public static void asyncInsertTowerReplay(final TowerReplay playerTowerReplays) {
        GameServer.executorService.execute(() -> TowerReplayDao.getInstance().addTowerReplay(playerTowerReplays));
    }

    public static void asyncUpdateTowerReplay(final TowerReplay playerTowerReplays) {
        GameServer.executorService.execute(() -> TowerReplayDao.getInstance().updateTowerReplay(playerTowerReplays));
    }

    public static void asyncRemoveTowerReplay(int affairId) {
        GameServer.executorService.execute(() -> TowerReplayDao.getInstance().removeTowerReplay(affairId));
    }

}
