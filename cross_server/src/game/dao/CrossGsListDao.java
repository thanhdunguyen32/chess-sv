package game.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.bean.CrossGsBean;
import game.db.DataSourceManager;

public class CrossGsListDao {

	private static Logger logger = LoggerFactory.getLogger(CrossGsListDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static CrossGsListDao instance = new CrossGsListDao();
	}

	public static CrossGsListDao getInstance() {
		return SingletonHolder.instance;
	}

	public List<CrossGsBean> getServerList() {
		List<CrossGsBean> ret = null;
		ResultSetHandler<List<CrossGsBean>> handler = new ResultSetHandler<List<CrossGsBean>>() {
			@Override
			public List<CrossGsBean> handle(ResultSet rs) throws SQLException {
				List<CrossGsBean> userBeanList = new ArrayList<CrossGsBean>();
				while (rs.next()) {
					CrossGsBean retBean = new CrossGsBean();
					retBean.setId(rs.getInt("Id"));
					retBean.setName(rs.getString("name"));
					retBean.setIp(rs.getString("ip"));
					retBean.setPort(rs.getInt("port"));
					retBean.setStatus(rs.getShort("status"));
					retBean.setLanPort(rs.getInt("lan_port"));
					userBeanList.add(retBean);
				}
				return userBeanList;
			}
		};
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from cross_gs_list", handler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

}
