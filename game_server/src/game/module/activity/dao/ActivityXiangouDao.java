package game.module.activity.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.activity.bean.ActivityXiangou;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件Dao
 * 
 * @author zhangning
 * @Date 2014年12月29日 下午2:06:17
 *
 */
public class ActivityXiangouDao {

	private static Logger logger = LoggerFactory.getLogger(ActivityXiangouDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static ActivityXiangouDao instance = new ActivityXiangouDao();
	}

	public static ActivityXiangouDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 邮件
	 */
	private ResultSetHandler<List<ActivityXiangou>> multiActivityXiangouHandler = new ResultSetHandler<List<ActivityXiangou>>() {
		@Override
		public List<ActivityXiangou> handle(ResultSet rs) throws SQLException {
			List<ActivityXiangou> activityXiangouList = new ArrayList<>();
			while (rs.next()) {
				ActivityXiangou activityXiangou = new ActivityXiangou();
				activityXiangou.setId(rs.getInt("id"));
				activityXiangou.setPlayerId(rs.getInt("player_id"));
				activityXiangou.setGstar(rs.getInt("gstar"));
				activityXiangou.setLevel(rs.getInt("level"));
				activityXiangou.setEndTime(rs.getTimestamp("end_time"));
				activityXiangou.setBuyCount(rs.getInt("buy_count"));
				activityXiangou.setLastBuyTime(rs.getTimestamp("last_buy_time"));
				activityXiangouList.add(activityXiangou);
			}
			return activityXiangouList;
		}
	};

	/**
	 * 获取玩家所有邮件
	 * 
	 * @param playerId
	 * @return
	 */
	public List<ActivityXiangou> getPlayerActivityXiangouAll(int playerId) {
		List<ActivityXiangou> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from activity_xiangou where player_id = ?",
					multiActivityXiangouHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 添加一个邮件
	 * 
	 * @param activityXiangou
	 * @return
	 */
	public boolean addOneActivityXiangou(ActivityXiangou activityXiangou) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			int ret = runner.update(conn,
					"insert into activity_xiangou(player_id, gstar, level, end_time, buy_count,last_buy_time) values(?,?,?,?,?,?)",
					activityXiangou.getPlayerId(), activityXiangou.getGstar(), activityXiangou.getLevel(), activityXiangou.getEndTime(),
					activityXiangou.getBuyCount(), activityXiangou.getLastBuyTime());
			if (ret > 0) {
				addRet = true;
			}
			if (addRet) {
				int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				activityXiangou.setId(theId);
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return addRet;
	}

	/**
	 * 更新邮件读取状态
	 * 
	 * @param activityXiangou
	 * @return
	 */
	public void updateActivityXiangou(ActivityXiangou activityXiangou) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("update activity_xiangou set gstar=?,level=?,end_time=?,buy_count=?,last_buy_time=? where id=?", activityXiangou.getGstar(),
					activityXiangou.getLevel(),activityXiangou.getEndTime(),activityXiangou.getBuyCount(),activityXiangou.getLastBuyTime(),
					activityXiangou.getId());
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * 删除邮件
	 * 
	 * @param id
	 *            ：邮件唯一ID
	 */
	public void removeActivityXiangou(int id) {
		QueryRunner runner = new QueryRunner(dataSource);

		try {
			runner.update("delete from activity_xiangou where id=?", id);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
