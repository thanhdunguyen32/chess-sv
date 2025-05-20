package game.module.pay.dao;

import game.GameServer;
import game.module.pay.bean.ChargeEntity;

public class ChargeDaohelper {

	public static void asyncInsertChargeEntity(final ChargeEntity chargeEntity) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				ChargeDao.getInstance().addChargeEntity(chargeEntity);
			}
		});
	}

	public static void asyncUpdateChargeEntity(final ChargeEntity chargeEntity) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				ChargeDao.getInstance().updateCharge(chargeEntity);
			}
		});
	}

}
