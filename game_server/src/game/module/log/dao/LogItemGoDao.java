package game.module.log.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.db.DataSourceManager;
import game.module.log.bean.LogItemGo;

/**
 * 玩家货币消耗Dao
 * 
 * @author zhangning
 * 
 * @Date 2015年5月27日 下午7:29:28
 */
public class LogItemGoDao {

	private static Logger logger = LoggerFactory.getLogger(LogItemGoDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static LogItemGoDao instance = new LogItemGoDao();
	}

	public static LogItemGoDao getInstance() {
		return SingletonHolder.instance;
	}
	
	
	private ResultSetHandler<List<LogItemGo>> logQueryHandler = new ResultSetHandler<List<LogItemGo>>() {
		@Override
		public List<LogItemGo> handle(ResultSet rs) throws SQLException {
			List<LogItemGo> retList = new ArrayList<>();
			while (rs.next()) {
				LogItemGo topPayPlayerBean = new LogItemGo();
				topPayPlayerBean.setPlayerId(rs.getInt(1));
				topPayPlayerBean.setPlayerName(rs.getString(2));
				topPayPlayerBean.setModuleType(rs.getInt(3));
				topPayPlayerBean.setItemId(rs.getInt(4));
				topPayPlayerBean.setChangeValue(rs.getInt(5));
				topPayPlayerBean.setCreateTime(rs.getTimestamp(6));
				retList.add(topPayPlayerBean);
			}
			return retList;
		}
	};

	/**
	 * 批量插入货币收益日志
	 * 
	 * @param logCosts
	 * @return
	 */
	public int addLogCostList(Collection<LogItemGo> logCosts) {
		int addCnt = 0;
		Connection con = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			PreparedStatement pstmt = con
					.prepareStatement("insert into log_item_go(player_id, module_type, item_id, change_value, add_time) values(?,?,?,?,?)");

			for (LogItemGo logCost : logCosts) {
				pstmt.setInt(1, logCost.getPlayerId());
				pstmt.setInt(2, logCost.getModuleType());
				pstmt.setInt(3, logCost.getItemId());
				pstmt.setInt(4, logCost.getChangeValue());
				pstmt.setTimestamp(5, new Timestamp(logCost.getCreateTime().getTime()));
				pstmt.addBatch();
				addCnt++;

				if (addCnt % 50 == 0) {
					pstmt.executeBatch();
					con.commit();
					pstmt.clearBatch();
				}
			}
			pstmt.executeBatch();
			con.commit();
			pstmt.clearBatch();

			pstmt.close();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}

		return addCnt;
	}
	
	public List<LogItemGo> queryLog(int itemId, int playerId) {
		List<LogItemGo> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			String commonQueryStr = "select log_item_go.player_id,player.name,log_item_go.module_type,log_item_go.item_id,log_item_go.change_value,log_item_go.add_time from log_item_go"
					+ " left join player on log_item_go.player_id = player.id where";
			String orderByClause = " order by log_item_go.add_time desc limit 100000";
			if (itemId > 0 && playerId == 0) {
				ret = runner.query(commonQueryStr + " log_item_go.item_id = ?"+orderByClause, logQueryHandler, itemId);
			} else if (itemId == 0 && playerId > 0) {
				ret = runner.query(commonQueryStr + " log_item_go.player_id = ?"+orderByClause, logQueryHandler, playerId);
			} else {
				ret = runner.query(commonQueryStr + " log_item_go.item_id = ? and log_item_go.player_id = ?"+orderByClause,
						logQueryHandler, itemId, playerId);
			}
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

}
