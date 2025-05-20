package test.dao;

import game.module.hero.logic.HeroConstants;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;

public class PlayerDaoTest {

	public static void main(String[] args) {
		PlayerDao playerDao = PlayerDao.getInstance();
		boolean isUserExist = playerDao.checkExistByUserName("hxh");
		System.out.println("isUserExist:" + isUserExist);

		PlayerBean pb = playerDao.getPlayerById(101);
		System.out.println("pb:" + pb);

//		PlayerBean pb2 = playerDao.addUser(101, "hxh", 2, 102, 1, 0, 1, 0, "192.168.1.2",10,"","",HeroConstants.NORMAL_HERO_SKILL_MAX_POINT);
//		System.out.println("pb2:" + pb2);
	}

}
