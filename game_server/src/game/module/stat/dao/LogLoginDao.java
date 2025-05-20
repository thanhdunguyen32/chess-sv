package game.module.stat.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;

public class LogLoginDao {

	private static Logger logger = LoggerFactory.getLogger(LogLoginDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static LogLoginDao instance = new LogLoginDao();
	}

	public static LogLoginDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * date_format(NOW(),'%Y-%m-%d')
	 * 
	 * @param playerId
	 * @return
	 */
	public boolean checkLogLoginExist(int playerId) {
		boolean ret = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT EXISTS(select 1 from log_login where player_id = ?)",
					DaoCommonHandler.Booleanhandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public boolean insertLogLogin(int playerId) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			int ret = runner.update("insert into log_login(player_id,login_time) values(?,NOW())",
					playerId);
			if (ret > 0) {
				addRet = true;
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return addRet;
	}

	public void updateLogLogin(int playerId) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			int ret = runner.update(
					"update log_login set login_time = NOW() where player_id=?", playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public int getActivePlayerCount() {
		int ret = 0;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT count(distinct(player_id)) from log_login where date_format(login_time,'%Y-%m-%d') = curdate()", DaoCommonHandler.Integerhandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

}
