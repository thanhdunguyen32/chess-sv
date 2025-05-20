package test.dao;

import game.module.user.dao.UserDao;

public class UserDaoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserDao userDao = UserDao.getInstance();
		boolean ret = userDao.checkExistByUserName("hxh");
		System.out.println(ret);
	}

}
