package game.db;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceManager {

	private static Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

	private static DataSourceManager thisBean;

    private DataSource dataSource;
    
    private DataSource dataSourceLogin;

	private DataSourceManager(String mysqlGameUrl, String mysqlGameClassname, 
							String mysqlGameUsername, String mysqlGamePassword,
							String mysqlLoginUrl, String mysqlLoginClassname, 
							String mysqlLoginUsername, String mysqlLoginPassword) {
		try {
			// Init Game Database
			HikariConfig configGame = new HikariConfig();
			configGame.setJdbcUrl(mysqlGameUrl);
			configGame.setUsername(mysqlGameUsername);
			configGame.setPassword(mysqlGamePassword);
			configGame.addDataSourceProperty("cachePrepStmts", "true");
			configGame.addDataSourceProperty("prepStmtCacheSize", "250");
			configGame.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

			HikariDataSource dsGame = new HikariDataSource(configGame);
			logger.info("init Hikari jdbc Game DataSource success - {}", dsGame);
			setDataSource(dsGame);

			// Init Login Database
			HikariConfig configLogin = new HikariConfig();
			configLogin.setJdbcUrl(mysqlLoginUrl);
			configLogin.setUsername(mysqlLoginUsername);
			configLogin.setPassword(mysqlLoginPassword);
			configLogin.addDataSourceProperty("cachePrepStmts", "true");
			configLogin.addDataSourceProperty("prepStmtCacheSize", "250");
			configLogin.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

			HikariDataSource dsLogin = new HikariDataSource(configLogin);
			logger.info("init Hikari jdbc Login DataSource success - {}", dsLogin);
			setDataSourceLogin(dsLogin);

		} catch (Exception e) {
			logger.error("init db fail!", e);
		}
	}

	public static void init(String mysqlGameUrl, String mysqlGameClassname, 
							String mysqlGameUsername, String mysqlGamePassword,
							String mysqlLoginUrl, String mysqlLoginClassname, 
							String mysqlLoginUsername, String mysqlLoginPassword) {
		thisBean = new DataSourceManager(mysqlGameUrl, mysqlGameClassname, 
										mysqlGameUsername, mysqlGamePassword,
										mysqlLoginUrl, mysqlLoginClassname, 
										mysqlLoginUsername, mysqlLoginPassword);
	}

	public static DataSourceManager getInstance() {
		return thisBean;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public DataSource getDataSourceLogin() {
		return dataSourceLogin;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDataSourceLogin(DataSource dataSourceLogin) {
		this.dataSourceLogin = dataSourceLogin;
	}

	public void shutdown() {
		if (dataSource instanceof HikariDataSource) {
			((HikariDataSource) dataSource).close();
		}
		if (dataSourceLogin instanceof HikariDataSource) {
			((HikariDataSource) dataSourceLogin).close();
		}
	}

}
