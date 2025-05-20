package game.module.spin.dao;

import game.GameServer;
import game.module.spin.bean.SpinBean;

public class SpinDaoHelper {

    public static void asyncInsertSpinBean(final SpinBean playerSpinBeans) {
        GameServer.executorService.execute(() -> SpinDao.getInstance().addSpinBean(playerSpinBeans));
    }

    public static void asyncUpdateSpinBean(final SpinBean playerSpinBeans) {
        GameServer.executorService.execute(() -> SpinDao.getInstance().updateSpinBean(playerSpinBeans));
    }

    public static void asyncRemoveSpinBean(int affairId) {
        GameServer.executorService.execute(() -> SpinDao.getInstance().removeSpinBean(affairId));
    }

}
