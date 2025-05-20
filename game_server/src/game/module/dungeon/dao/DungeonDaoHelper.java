package game.module.dungeon.dao;

import game.GameServer;
import game.module.dungeon.bean.DungeonBean;

public class DungeonDaoHelper {

    public static void asyncInsertDungeon(final DungeonBean playerDungeons) {
        GameServer.executorService.execute(() -> DungeonDao.getInstance().addDungeonBean(playerDungeons));
    }

    public static void asyncUpdateDungeon(final DungeonBean playerDungeons) {
        GameServer.executorService.execute(() -> DungeonDao.getInstance().updateDungeon(playerDungeons));
    }

    public static void asyncRemoveDungeon(int dungeonId) {
        GameServer.executorService.execute(() -> DungeonDao.getInstance().removeDungeon(dungeonId));
    }

}
