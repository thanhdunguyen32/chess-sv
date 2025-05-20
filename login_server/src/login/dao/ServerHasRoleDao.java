package login.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import lion.common.SimpleTextConvert;
import login.bean.ServerHasRole;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHasRoleDao {

    private static final Logger logger = LoggerFactory.getLogger(ServerHasRoleDao.class);

    private final DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static ServerHasRoleDao instance = new ServerHasRoleDao();
    }

    public static ServerHasRoleDao getInstance() {
        return SingletonHolder.instance;
    }

    public Map<String,ServerHasRole> getServerHasRoles() {
        Map<String,ServerHasRole> ret = null;
        ResultSetHandler<Map<String,ServerHasRole>> handler = rs -> {
            Map<String,ServerHasRole> serverHasRoles = new ConcurrentHashMap<>();
            while (rs.next()) {
                ServerHasRole retBean = new ServerHasRole();
                retBean.setId(rs.getInt("id"));
                retBean.setAccountId(rs.getString("account_id"));
                retBean.setLastLoginServerId(rs.getInt("last_login_server"));
                String server_levels = rs.getString("server_levels");
                Map<Integer, Integer> serverLevelsMap = SimpleTextConvert.decodeIntMap(server_levels);
                retBean.setServerLevels(serverLevelsMap);
                serverHasRoles.put(retBean.getAccountId(),retBean);
            }
            return serverHasRoles;
        };
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from server_has_role", handler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public void updateServerHasRole(ServerHasRole serverHasRole) {
        String server_levels_str = SimpleTextConvert.encodeMap(serverHasRole.getServerLevels());
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("update server_has_role set last_login_server=?,server_levels=? where id = ?", serverHasRole.getLastLoginServerId(),
                    server_levels_str,serverHasRole.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public boolean addServerHasRole(ServerHasRole serverHasRole) {
        String server_levels_str = SimpleTextConvert.encodeMap(serverHasRole.getServerLevels());
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into server_has_role(account_id, last_login_server, server_levels) values(?,?,?)",
                    serverHasRole.getAccountId(), serverHasRole.getLastLoginServerId(), server_levels_str);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                serverHasRole.setId(theId);
            }
        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

}
