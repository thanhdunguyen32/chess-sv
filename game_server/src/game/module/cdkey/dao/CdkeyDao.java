package game.module.cdkey.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.cdkey.bean.Cdkey;

/**
 * 激活码Dao
 * 
 * @author zhangning
 * 
 * @Date 2015年2月13日 上午7:14:12
 */
public class CdkeyDao {

	private static Logger logger = LoggerFactory.getLogger(CdkeyDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static CdkeyDao instance = new CdkeyDao();
	}

	public static CdkeyDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 激活码
	 */
	private ResultSetHandler<List<Cdkey>> multiCdkeyHandler = new ResultSetHandler<List<Cdkey>>() {
		@Override
		public List<Cdkey> handle(ResultSet rs) throws SQLException {
			List<Cdkey> cdKeyList = new ArrayList<Cdkey>();
			while (rs.next()) {
				Cdkey cdkey = new Cdkey();
				cdkey.setId(rs.getInt("Id"));
				cdkey.setPlatform(rs.getInt("platform"));
				cdkey.setArea(rs.getInt("area"));
				cdkey.setCdkey(rs.getString("cdkey"));
				cdkey.setPlayerId(rs.getInt("player_id"));
				cdkey.setCdkId(rs.getInt("cdk_id"));

				cdKeyList.add(cdkey);
			}
			return cdKeyList;
		}
	};

	/**
	 * 该平台, 已经领取过的激活码集合
	 * 
	 * @param platform
	 * @return
	 */
	public List<Cdkey> getUsedCdkeys(int platform) {
		List<Cdkey> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from cdkey where platform = ? or platform = 0", multiCdkeyHandler, platform);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 添加一条激活码记录
	 * 
	 * @param cdkey
	 * @return
	 */
	public boolean addOneCdkey(Cdkey cdkey) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			int ret = runner.update(conn,
					"insert into cdkey(platform, area, cdkey, player_id, cdk_id) values(?,?,?,?,?)",
					cdkey.getPlatform(), cdkey.getArea(), cdkey.getCdkey(), cdkey.getPlayerId(), cdkey.getCdkId());
			if (ret > 0) {
				addRet = true;
			}
			if (addRet) {
				int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				cdkey.setId(theId);
			}

		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return addRet;
	}

	/**
	 * 删除一条过期激活码
	 * 
	 * @param id
	 */
	public void removeOneCdkey(int id) {
		QueryRunner runner = new QueryRunner(dataSource);

		try {
			runner.update("delete from cdkey where Id=?", id);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
