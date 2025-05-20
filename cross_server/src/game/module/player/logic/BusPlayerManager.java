package game.module.player.logic;

import game.CrossServer;
import game.bean.BusPlayerBean;
import game.module.player.dao.BusPlayerDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author zhangning
 * 
 * @Date 2015年10月27日 下午4:17:51
 */
public class BusPlayerManager {

	private static Logger logger = LoggerFactory.getLogger(BusPlayerManager.class);

	BusPlayerDao busPlayerDao = BusPlayerDao.getInstance();

	static class SingletonHolder {
		static BusPlayerManager instance = new BusPlayerManager();
	}

	public static BusPlayerManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 异步添加一条新注册玩家
	 *
	 * @param busPlayerBean
	 */
	public void asyncInsertBusPlayer(final BusPlayerBean busPlayerBean) {
		CrossServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				busPlayerDao.insertBusPlayer(busPlayerBean);
			}
		});
	}

	public void asyncUpdateBusPlayerLevel(final String garenaOpenId, final Integer zoneId, final Integer playerId,
			final Integer level) {
		CrossServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				busPlayerDao.updateBusPlayerLevel(garenaOpenId, zoneId, playerId, level);
			}
		});
	}

}
