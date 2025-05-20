package game.module.vip.dao;

import game.GameServer;
import game.module.vip.bean.VipCount;

public class VipCountDaoHelper {

    public static void asyncInsertVipCount(VipCount vipCount) {
        GameServer.executorService.execute(() -> VipCountDao.getInstance().addVipCount(vipCount));
    }

    public static void asyncRemoveVipCount(int taskId) {
        GameServer.executorService.execute(() -> VipCountDao.getInstance().removeVipCount(taskId));
    }

    public static void asyncUpdateVipCount(VipCount buildingBean) {
        GameServer.executorService.execute(() -> VipCountDao.getInstance().updateVipCount(buildingBean));
    }
}
