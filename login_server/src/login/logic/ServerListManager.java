package login.logic;

import login.bean.ServerList4Db;
import login.dao.ServerListDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerListManager {

	private static Logger logger = LoggerFactory.getLogger(ServerListManager.class);

	private volatile List<ServerList4Db> serverListCache = new ArrayList<ServerList4Db>();

	static class SingletonHolder {
		static ServerListManager instance = new ServerListManager();
	}

	public static ServerListManager getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<Integer, ServerList4Db> serverMap;

	public void reload() {
		logger.info("reload server_list template!");
		serverListCache = getServerListFromDb();
		Map<Integer, ServerList4Db> tmpServerMap = new HashMap<>();
		for (ServerList4Db serverList4Db : serverListCache) {
			tmpServerMap.put(serverList4Db.getId(), serverList4Db);
		}
		serverMap = tmpServerMap;
	}

	public List<ServerList4Db> getServerList() {
		return serverListCache;
	}

	public ServerList4Db getServer(int serverId) {
		return serverMap.get(serverId);
	}

	public List<ServerList4Db> getServerListFromDb() {
		List<ServerList4Db> retList = ServerListDao.getInstance().getServerList();
		return retList;
	}
}
