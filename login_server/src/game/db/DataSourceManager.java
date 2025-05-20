package game.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DataSourceManager {

	private static Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

	private static DataSourceManager thisBean;

	private DataSource dataSource;

	private DataSourceManager(String mysqlUrl, String mysqlClassname, String mysqlUsername, String mysqlPassword) {
		try {
			logger.info("init Hikari jdbc DataSource");

			HikariConfig config = new HikariConfig();
//			config.setDataSourceClassName(mysqlClassname);
			config.setJdbcUrl(mysqlUrl);
			config.setUsername(mysqlUsername);
			config.setPassword(mysqlPassword);
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

			HikariDataSource ds = new HikariDataSource(config);
			setDataSource(ds);
		} catch (Exception e) {
			logger.error("init db fail!", e);
		}
	}

	public static void init(String mysqlUrl, String mysqlClassname, String mysqlUsername, String mysqlPassword) {
		thisBean = new DataSourceManager(mysqlUrl, mysqlClassname, mysqlUsername, mysqlPassword);
	}

	public static DataSourceManager getInstance() {
		return thisBean;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void shutdown() {
		if (dataSource instanceof HikariDataSource) {
			((HikariDataSource) dataSource).close();
		}
	}

}
