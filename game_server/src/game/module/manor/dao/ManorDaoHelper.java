package game.module.manor.dao;

import game.GameServer;
import game.module.manor.bean.ManorBean;
import lion.common.ProtostuffUtil;

public class ManorDaoHelper {

    public static void asyncInsertManor(final ManorBean playerManors) {
        GameServer.executorService.execute(() -> ManorDao.getInstance().addManorBean(playerManors));
    }

    public static void asyncUpdateManor(final ManorBean manorBean) {
        ManorBean.DbManorBuilding checkpointEnemy = manorBean.getManorBuilding();
        byte[] buidingBlob = ProtostuffUtil.serialize(checkpointEnemy);
        ManorBean.DbManorField manorField = manorBean.getManorField();
        byte[] fieldBlob = ProtostuffUtil.serialize(manorField);
        GameServer.executorService.execute(() -> ManorDao.getInstance().updateManor(manorBean, buidingBlob, fieldBlob));
    }

    public static void asyncRemoveManor(int manorId) {
        GameServer.executorService.execute(() -> ManorDao.getInstance().removeManor(manorId));
    }

}
