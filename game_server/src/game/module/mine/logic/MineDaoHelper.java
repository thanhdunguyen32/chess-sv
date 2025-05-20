package game.module.mine.logic;

import game.GameServer;
import game.module.mine.bean.DBMine;
import game.module.mine.dao.MineDao;

public class MineDaoHelper {

	public static void asyncInsertMineEntity(final DBMine mineEntity) {
		GameServer.executorService.execute(() -> MineDao.getInstance().insertMine(mineEntity));
	}
	
}
