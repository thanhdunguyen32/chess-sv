package login.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginAccountDao {

	private static Logger logger = LoggerFactory.getLogger(LoginAccountDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static LoginAccountDao instance = new LoginAccountDao();
	}

	public static LoginAccountDao getInstance() {
		return SingletonHolder.instance;
	}

	public boolean checkExist(String uname, String pwd) {
		boolean ret = false;
		ResultSetHandler<Boolean> handler = rs -> {
			int retFlag = 0;
			if (rs.next()) {
				retFlag = rs.getInt(1);
			}
			return retFlag > 0;
		};
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT EXISTS(select 1 from login_account where uname = ? and pwd = md5(?))", handler,
					uname, pwd);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public boolean checkGuestExist(int accountId) {
		boolean ret = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT EXISTS(select 1 from login_account where id = ? and uname = 'guest')",
					DaoCommonHandler.Booleanhandler, accountId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}
	
	public int getUidByNamePwd(String uname, String pwd) {
		int ret = -1;
		ResultSetHandler<Integer> handler = new ResultSetHandler<Integer>() {
			@Override
			public Integer handle(ResultSet rs) throws SQLException {
				int retFlag = -1;
				if (rs.next()) {
					retFlag = rs.getInt(1);
				}
				return retFlag;
			}
		};
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select Id from login_account where uname = ? and pwd = md5(?)", handler, uname, pwd);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public Integer insertLoginAccount(String uname, String pwd) {
		QueryRunner runner = new QueryRunner(dataSource);
		boolean addRet = false;
		try {
			int ret = runner.update("insert into login_account(uname,pwd) values(?,md5(?))", uname, pwd);
			if (ret > 0) {
				addRet = true;
			}
			if (addRet) {
				int itemId = runner.query("SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				return itemId;
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return 0;
	}

}
