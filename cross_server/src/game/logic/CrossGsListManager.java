package game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.bean.CrossGsBean;
import game.dao.CrossGsListDao;

public class CrossGsListManager {

	private static Logger logger = LoggerFactory.getLogger(CrossGsListManager.class);

	private volatile List<CrossGsBean> serverListCache = new ArrayList<CrossGsBean>();

	static class SingletonHolder {
		static CrossGsListManager instance = new CrossGsListManager();
	}

	public static CrossGsListManager getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<Integer, CrossGsBean> serverMap;

	public void reload() {
		logger.info("reload cross_gs_list template!");
		serverListCache = getServerListFromDb();
		Map<Integer, CrossGsBean> tmpServerMap = new HashMap<>();
		for (CrossGsBean serverList4Db : serverListCache) {
			tmpServerMap.put(serverList4Db.getId(), serverList4Db);
		}
		serverMap = tmpServerMap;
	}

	public List<CrossGsBean> getServerList() {
		return serverListCache;
	}

	public CrossGsBean getServer(int serverId) {
		return serverMap.get(serverId);
	}

	public List<CrossGsBean> getServerListFromDb() {
		List<CrossGsBean> retList = CrossGsListDao.getInstance().getServerList();
		return retList;
	}
}
