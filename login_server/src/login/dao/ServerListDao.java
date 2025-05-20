package login.dao;

import game.db.DataSourceManager;
import login.bean.ServerList4Db;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerListDao {

	private static Logger logger = LoggerFactory.getLogger(ServerListDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static ServerListDao instance = new ServerListDao();
	}

	public static ServerListDao getInstance() {
		return SingletonHolder.instance;
	}

	public List<ServerList4Db> getServerList() {
		List<ServerList4Db> ret = null;
		ResultSetHandler<List<ServerList4Db>> handler = rs -> {
			List<ServerList4Db> userBeanList = new ArrayList<>();
			while (rs.next()) {
				ServerList4Db retBean = new ServerList4Db();
				retBean.setId(rs.getInt("Id"));
				retBean.setName(rs.getString("name"));
				retBean.setIp(rs.getString("ip"));
				retBean.setPort(rs.getInt("port"));
				retBean.setPortSsl(rs.getInt("port_ssl"));
				retBean.setStatus(rs.getShort("status"));
				retBean.setLanPort(rs.getInt("lan_port"));
                retBean.setHttpUrl(rs.getString("http_url"));
				retBean.setUrlSsl(rs.getString("url_ssl"));
                retBean.setIsSsl(rs.getBoolean("is_ssl"));
				userBeanList.add(retBean);
			}
			return userBeanList;
		};
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from server_list order by id asc", handler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public void updateServerStatus(int serverId, int status) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("update server_list set status = ? where Id = ?", status, serverId);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
