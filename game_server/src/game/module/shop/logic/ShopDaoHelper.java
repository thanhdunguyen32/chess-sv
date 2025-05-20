package game.module.shop.logic;

import game.GameServer;
import game.module.shop.bean.ShopBean;
import game.module.shop.dao.ShopDao;

public class ShopDaoHelper {

	public static void asyncInsertShopBean(final ShopBean onlineGiftBean) {
		GameServer.executorService.execute(() -> ShopDao.getInstance().addShopBean(onlineGiftBean));
	}

	public static void asyncUpdateShopBean(final ShopBean onlineGiftBean) {
		GameServer.executorService.execute(() -> ShopDao.getInstance().updateShopBean(onlineGiftBean));
	}

	public static void asyncRemoveShopBean(int onlineGiftId) {
		GameServer.executorService.execute(() -> ShopDao.getInstance().removeShopBean(onlineGiftId));
	}

}
