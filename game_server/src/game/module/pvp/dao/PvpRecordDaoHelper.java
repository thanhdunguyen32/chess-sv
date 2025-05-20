package game.module.pvp.dao;

import game.GameServer;
import game.module.pvp.bean.PvpRecord;

public class PvpRecordDaoHelper {

    public static void asyncInsertPvpRecord(final PvpRecord playerPvpRecords) {
        GameServer.executorService.execute(() -> PvpRecordDao.getInstance().addPvpRecord(playerPvpRecords));
    }

    public static void asyncUpdatePvpRecord(final PvpRecord playerPvpRecords) {
        GameServer.executorService.execute(() -> PvpRecordDao.getInstance().updatePvpRecord(playerPvpRecords));
    }

    public static void asyncRemovePvpRecord(int affairId) {
        GameServer.executorService.execute(() -> PvpRecordDao.getInstance().removePvpRecord(affairId));
    }

}
