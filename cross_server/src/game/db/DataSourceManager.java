package game.db;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceManager {

	private static Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

	private static DataSourceManager thisBean;

	private DataSource dataSource;

	private DataSourceManager(String mysqlUrl, String mysqlClassname, String mysqlUsername, String mysqlPassword) {
		try {
			logger.info("init tomcat jdbc DataSource");
			// tomcat-jdbc属性配置
			PoolProperties p = new PoolProperties();
			p.setUrl(mysqlUrl);
			p.setDriverClassName(mysqlClassname);
			p.setUsername(mysqlUsername);
			p.setPassword(mysqlPassword);
			p.setJmxEnabled(true);
			p.setTestWhileIdle(false);
			p.setTestOnBorrow(true);
			p.setValidationQuery("SELECT 1");
			p.setTestOnReturn(false);
			p.setValidationInterval(30000);
			p.setTimeBetweenEvictionRunsMillis(30000);
			// 最大连接数据库连接数，设置为0时，表示没有限制
			p.setMaxActive(30);
			// 初始化时创建的连接数
			p.setInitialSize(5);
			// 最大等待秒数，单位为毫秒， 超过时间会报出错误信息
			p.setMaxWait(30000);
			p.setRemoveAbandonedTimeout(60);
			p.setMinEvictableIdleTimeMillis(30000);
			// 最小等待连接中的数量，设置为0时，表示没有限制
			p.setMinIdle(10);
			p.setMaxIdle(10);
			p.setLogAbandoned(true);
			p.setRemoveAbandoned(true);
			// 设置从数据源中返回的连接是否采用自动提交机制，默认值为 true
			p.setDefaultAutoCommit(true);
			p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
					+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
			DataSource myDatasource = new DataSource();
			myDatasource.setPoolProperties(p);
			setDataSource(myDatasource);
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
		dataSource.close();
	}

}
