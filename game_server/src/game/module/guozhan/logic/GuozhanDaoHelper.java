package game.module.guozhan.logic;

import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import game.GameServer;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.GuozhanDao;
import game.module.guozhan.dao.GuozhanPlayerDao;

public class GuozhanDaoHelper {

	public static void asyncInsertGuozhanPlayer(final GuozhanPlayer mineEntity) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				GuozhanPlayerDao.getInstance().addGuozhanPlayer(mineEntity);
			}
		});
	}
	
	public static void asyncUpdateGuozhanPlayer(GuozhanPlayer guozhanPlayer) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				GuozhanPlayerDao.getInstance().updateGuozhanPlayerAll(guozhanPlayer);
			}
		});
	}

	public static void asyncUpdateGuozhanNation(GuozhanPlayer guozhanPlayer) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				GuozhanPlayerDao.getInstance().updateGuozhanPlayerNation(guozhanPlayer);
			}
		});
	}
	
	public static void asyncUpdateGuozhanOffice(DBGuoZhanOffice guoZhanOffice) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				GuozhanDao.getInstance().updateGuozhanOffice(guoZhanOffice);
			}
		});
	}

	public static void asyncInsertGuozhaFight(DBGuoZhanFight guoZhanFight) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				GuozhanDao.getInstance().insertGuozhanFight(guoZhanFight);
			}
		});
	}

}
