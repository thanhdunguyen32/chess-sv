package game.module.hero.logic;

import game.GameServer;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralDao;

public class GeneralDaoHelper {

	public static void asyncInsertHero(final GeneralBean heroBean) {
		GameServer.executorService.execute(() -> GeneralDao.getInstance().addHeroBean(heroBean));
	}

	public static void asyncUpdateHero(final GeneralBean heroBean) {
		GameServer.executorService.execute(() -> GeneralDao.getInstance().updateHero(heroBean));
	}

	public static void asyncRemoveHero(long heroId) {
		GameServer.executorService.execute(() -> GeneralDao.getInstance().removeHero(heroId));
	}

}
