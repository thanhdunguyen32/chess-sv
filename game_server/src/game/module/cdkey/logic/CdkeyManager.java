package game.module.cdkey.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.GameServer;
import game.module.cdkey.bean.Cdkey;
import game.module.cdkey.dao.CdkDao;
import game.module.cdkey.dao.CdkeyDao;

/**
 * 签到逻辑处理
 * 
 * @author zhangning
 * 
 * @Date 2015年1月12日 下午7:02:30
 */
public class CdkeyManager {

	private static Logger logger = LoggerFactory.getLogger(CdkeyManager.class);

	CdkeyDao cdkeyDao = CdkeyDao.getInstance();

	CdkDao cdkDao = CdkDao.getInstance();

	static class SingletonHolder {
		static CdkeyManager instance = new CdkeyManager();
	}

	public static CdkeyManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 异步添加一条激活码记录
	 * 
	 * @param cdkey
	 */
	public void asyncInsertCdkey(final Cdkey cdkey) {
		GameServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				cdkeyDao.addOneCdkey(cdkey);
			}
		});
	}

	/**
	 * 异步删除过期激活码
	 * 
	 * @param id
	 *            ：激活码唯一ID
	 */
	public void asyncRemoveCdk(final int id) {
		GameServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				cdkDao.removeOneCdk(id);
			}
		});
	}

}
