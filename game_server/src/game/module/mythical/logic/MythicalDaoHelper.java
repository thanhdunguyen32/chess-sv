package game.module.mythical.logic;

import game.GameServer;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalDao;

public class MythicalDaoHelper {

	public static void asyncInsertMythicalAnimal(final MythicalAnimal onlineGiftBean) {
		GameServer.executorService.execute(() -> MythicalAnimalDao.getInstance().addMythicalAnimal(onlineGiftBean));
	}

	public static void asyncUpdateMythicalAnimal(final MythicalAnimal onlineGiftBean) {
		GameServer.executorService.execute(() -> MythicalAnimalDao.getInstance().updateMythicalAnimal(onlineGiftBean));
	}

	public static void asyncRemoveMythicalAnimal(int onlineGiftId) {
		GameServer.executorService.execute(() -> MythicalAnimalDao.getInstance().removeMythicalAnimal(onlineGiftId));
	}

}
