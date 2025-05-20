package game.module.pay.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.pay.bean.BuyCostBean;

public class BuyCostDao {

	private static Logger logger = LoggerFactory.getLogger(BuyCostDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static BuyCostDao instance = new BuyCostDao();
	}

	public static BuyCostDao getInstance() {
		return SingletonHolder.instance;
	}

	private ResultSetHandler<BuyCostBean> aPlayerBoxHandler = new ResultSetHandler<BuyCostBean>() {
		@Override
		public BuyCostBean handle(ResultSet rs) throws SQLException {
			BuyCostBean playerBean = null;
			if (rs.next()) {
				playerBean = new BuyCostBean();
				playerBean.setId(rs.getInt("id"));
				playerBean.setPlayerId(rs.getInt("player_id"));
				playerBean.setSkillPointCount(rs.getInt("skill_point_count"));
				playerBean.setSkillPointTime(rs.getTimestamp("skill_point_time"));
			}
			return playerBean;
		}
	};

	public BuyCostBean getBuyCostBean(int pId) {
		BuyCostBean ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from buy_cost where player_id = ?", aPlayerBoxHandler, pId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public boolean saveBuyCostBean(BuyCostBean bcb) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			int ret = runner.update(conn,
					"insert into buy_cost(player_id,skill_point_count,skill_point_time) values(?,?,?)",
					bcb.getPlayerId(), bcb.getSkillPointCount(), bcb.getSkillPointTime());
			if (ret > 0) {
				addRet = true;
			}
			if (addRet) {
				int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				bcb.setId(theId);
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return addRet;
	}

	public void updateBuyCostBean(BuyCostBean bcb) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("update buy_cost set skill_point_count = ?,skill_point_time = ? where id = ?",
					bcb.getSkillPointCount(), bcb.getSkillPointTime(), bcb.getId());
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
