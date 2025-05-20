package game.module.sign.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import game.module.sign.bean.SignIn;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;

/**
 * 签到Dao
 * 
 * @author zhangning
 * 
 * @Date 2015年1月12日 下午5:38:13
 */
public class SignDao {

	private static Logger logger = LoggerFactory.getLogger(SignDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static SignDao instance = new SignDao();
	}

	public static SignDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 签到
	 */
	private ResultSetHandler<SignIn> aSignHandler = new ResultSetHandler<SignIn>() {
		@Override
		public SignIn handle(ResultSet rs) throws SQLException {
			SignIn signIn = null;
			if (rs.next()) {
				signIn = new SignIn();
				signIn.setId(rs.getInt("id"));
				signIn.setPlayerId(rs.getInt("player_id"));
				signIn.setLastSignTime(rs.getTimestamp("last_sign_time"));
			}
			return signIn;
		}
	};

	/**
	 * 获取玩家签到
	 * 
	 * @param playerId
	 * @return
	 */
	public SignIn getPlayerSign(int playerId) {
		SignIn ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from sign_in where player_id = ?", aSignHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}
		return ret;
	}

	/**
	 * 添加一条签到记录
	 * 
	 * @param signIn
	 * @return
	 */
	public boolean addSignIn(SignIn signIn) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			int ret = runner.update(conn, "insert into sign_in(player_id, last_sign_time) values(?,?)",
					signIn.getPlayerId(), signIn.getLastSignTime());
			if (ret > 0) {
				addRet = true;
			}
			if (addRet) {
				int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				signIn.setId(theId);
			}

		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return addRet;
	}

	/**
	 * 更新签到
	 * 
	 * @param signIn
	 * @return
	 */
	public void updateSign(SignIn signIn) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("update sign_in set last_sign_time=? where player_id=?", signIn.getLastSignTime(), signIn.getPlayerId());
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
