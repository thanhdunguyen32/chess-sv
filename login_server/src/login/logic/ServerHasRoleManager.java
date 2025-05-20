package login.logic;

import login.LoginServer;
import login.bean.ServerHasRole;
import login.dao.ServerHasRoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ServerHasRoleManager {

    private static Logger logger = LoggerFactory.getLogger(ServerHasRoleManager.class);

    static class SingletonHolder {
        static ServerHasRoleManager instance = new ServerHasRoleManager();
    }

    public static ServerHasRoleManager getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<String, ServerHasRole> serverMap;

    public void reload() {
        logger.info("reload server_has_role info!");
        serverMap = ServerHasRoleDao.getInstance().getServerHasRoles();
    }

    public ServerHasRole getServerHasRole(String accountId) {
        return serverMap.get(accountId);
    }

    public void addLastLoginServer(String accountId, int serverId) {
        ServerHasRole serverHasRole = serverMap.get(accountId);
        if (serverHasRole == null) {
            serverHasRole = new ServerHasRole();
            serverHasRole.setAccountId(accountId);
            serverHasRole.setLastLoginServerId(serverId);
            ServerHasRole finalServerHasRole = serverHasRole;
            LoginServer.executorService.execute(() -> {
                ServerHasRoleDao.getInstance().addServerHasRole(finalServerHasRole);
            });
            serverMap.put(accountId, serverHasRole);
        } else {
            if (serverHasRole.getLastLoginServerId() == null || serverHasRole.getLastLoginServerId() != serverId) {
                serverHasRole.setLastLoginServerId(serverId);
                ServerHasRole finalServerHasRole1 = serverHasRole;
                LoginServer.executorService.execute(() -> {
                    ServerHasRoleDao.getInstance().addServerHasRole(finalServerHasRole1);
                });
            }
        }
    }

    public void addHasRoleServer(String accountId, int serverId, int roleLevel) {
        ServerHasRole serverHasRole = serverMap.get(accountId);
        Map<Integer, Integer> serverLevels = serverHasRole.getServerLevels();
        if (serverLevels == null || !serverLevels.containsKey(serverId) || serverLevels.get(serverId) != roleLevel) {
            //
            if (serverLevels == null) {
                serverLevels = new HashMap<>();
                serverHasRole.setServerLevels(serverLevels);
            }
            serverLevels.put(serverId, roleLevel);
            LoginServer.executorService.execute(() -> {
                ServerHasRoleDao.getInstance().updateServerHasRole(serverHasRole);
            });
        }
    }

}
