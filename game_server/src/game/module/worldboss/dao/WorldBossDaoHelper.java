package game.module.worldboss.dao;

import game.GameServer;
import game.module.worldboss.bean.WorldBoss;

public class WorldBossDaoHelper {

    public static void asyncInsertWorldBoss(final WorldBoss playerWorldBosss) {
        GameServer.executorService.execute(() -> WorldBossDao.getInstance().addWorldBoss(playerWorldBosss));
    }

    public static void asyncUpdateWorldBoss(final WorldBoss playerWorldBosss) {
        GameServer.executorService.execute(() -> WorldBossDao.getInstance().updateWorldBoss(playerWorldBosss));
    }

    public static void asyncRemoveWorldBoss() {
        GameServer.executorService.execute(() -> WorldBossDao.getInstance().removeWorldBoss());
    }

}
