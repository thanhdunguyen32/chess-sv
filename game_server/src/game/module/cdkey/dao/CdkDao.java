package game.module.cdkey.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.cdkey.bean.Cdk;

/**
 * 激活码Dao
 * 
 * @author zhangning
 * 
 * @Date 2015年2月13日 上午7:14:12
 */
public class CdkDao {

	private static Logger logger = LoggerFactory.getLogger(CdkDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static CdkDao instance = new CdkDao();
	}

	public static CdkDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 激活码
	 */
	private ResultSetHandler<List<Cdk>> multiCdkeyHandler = new ResultSetHandler<List<Cdk>>() {
		@Override
		public List<Cdk> handle(ResultSet rs) throws SQLException {
			List<Cdk> cdKeyList = new ArrayList<Cdk>();
			while (rs.next()) {
				Cdk cdk = new Cdk();
				cdk.setId(rs.getInt("Id"));
				cdk.setPlatform(rs.getInt("platform"));
				cdk.setArea(rs.getInt("area"));
				cdk.setCdk(rs.getString("cdk"));
				cdk.setAwardId(rs.getInt("award_id"));
				cdk.setMoneyYuan(rs.getInt("money_yuan"));
				cdk.setIsReuse(rs.getInt("reuse"));
				cdk.setStartTime(rs.getTimestamp("start_time"));
				cdk.setEndTime(rs.getTimestamp("end_time"));

				cdKeyList.add(cdk);
			}
			return cdKeyList;
		}
	};

	private ResultSetHandler<Cdk> oneCdkeyHandler = new ResultSetHandler<Cdk>() {
		@Override
		public Cdk handle(ResultSet rs) throws SQLException {
			Cdk cdk = null;
			if (rs.next()) {
				cdk = new Cdk();
				cdk.setId(rs.getInt("Id"));
				cdk.setPlatform(rs.getInt("platform"));
				cdk.setArea(rs.getInt("area"));
				cdk.setCdk(rs.getString("cdk"));
				cdk.setAwardId(rs.getInt("award_id"));
				cdk.setMoneyYuan(rs.getInt("money_yuan"));
				cdk.setIsReuse(rs.getInt("reuse"));
				cdk.setStartTime(rs.getTimestamp("start_time"));
				cdk.setEndTime(rs.getTimestamp("end_time"));
			}
			return cdk;
		}
	};

	/**
	 * 某个平台的激活码集合<br/>
	 * platform = 0: 所有平台共有
	 * 
	 * @param platform
	 * @return
	 */
	public List<Cdk> getCdksOfPlat(int platform) {
		List<Cdk> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from tcdk where platform = ? or platform = 0", multiCdkeyHandler, platform);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 某个平台下某个区的激活码集合
	 * 
	 * @param platform
	 * @param area
	 * @return
	 */
	public List<Cdk> getCdksOfPlat(int platform, int area) {
		List<Cdk> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from tcdk where platform = ? and area = ? or platform = 0", multiCdkeyHandler,
					platform, area);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 添加一条激活码记录
	 * 
	 * @param cdk
	 * @return
	 */
	// public boolean addOneCdk(Cdk cdk) {
	// boolean addRet = false;
	// QueryRunner runner = new QueryRunner(dataSource);
	// Connection conn = null;
	//
	// try {
	// conn = dataSource.getConnection();
	// int ret = runner
	// .update(conn,
	// "insert into tcdk(platform, area, cdk, cdk_name, award_id, start_time,
	// end_time, remark) values(?,?,?,?,?,?,?,?)",
	// cdk.getPlatform(), cdk.getArea(), cdk.getCdk(), cdk.getCdkName(),
	// cdk.getAwardId(),
	// cdk.getStartTime(), cdk.getEndTime(), cdk.getRemark());
	// if (ret > 0) {
	// addRet = true;
	// }
	// if (addRet) {
	// int theId = runner.query(conn, "SELECT LAST_INSERT_ID();",
	// DaoCommonHandler.Integerhandler);
	// cdk.setId(theId);
	// }
	//
	// } catch (SQLException e) {
	// logger.error("", e);
	// } finally {
	// DbUtils.closeQuietly(conn);
	// }
	// return addRet;
	// }

	/**
	 * 删除一条过期激活码
	 * 
	 * @param id
	 */
	public void removeOneCdk(int id) {
		QueryRunner runner = new QueryRunner(dataSource);

		try {
			runner.update("delete from tcdk where Id=?", id);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public boolean checkExist(String key) {
		boolean ret = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT EXISTS(select 1 from tcdk where cdk = ?)", DaoCommonHandler.Booleanhandler, key);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public Cdk getCdk(String keyStr) {
		Cdk ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from tcdk where cdk = ?", oneCdkeyHandler, keyStr);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

}
