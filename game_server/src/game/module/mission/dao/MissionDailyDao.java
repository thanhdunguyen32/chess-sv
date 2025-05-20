package game.module.mission.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.mission.bean.MissionDaily;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MissionDailyDao {

	private static Logger logger = LoggerFactory.getLogger(MissionDailyDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static MissionDailyDao instance = new MissionDailyDao();
	}

	public static MissionDailyDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 任务--进度数据
	 */
	private ResultSetHandler<MissionDaily> oneMissionProHandler = rs -> {
		MissionDaily missionDaily = null;
		if (rs.next()) {
			missionDaily = new MissionDaily();
			missionDaily.setId(rs.getInt("id"));
			missionDaily.setPlayerId(rs.getInt("player_id"));
			missionDaily.setUpdateTime(rs.getTimestamp("update_time"));
		}
		return missionDaily;
	};

	/**
	 * 获得玩家的所有任务进度<br/>
	 * 排序：已完成任务>未完成任务<br/>
	 * 第一优先级相同的情况下按照id从小到大排序
	 * 
	 * @param playerId
	 * @return
	 */
	public MissionDaily getPlayerMissionDaily(int playerId) {
		MissionDaily ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from mission_daily where player_id = ?",
					oneMissionProHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 添加一条任务进度
	 * 
	 * @param missionDaily
	 * @return
	 */
	public boolean addMissionDaily(MissionDaily missionDaily) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			int ret = runner.update(conn,
					"insert into mission_daily(player_id, update_time) values(?,?)",
					missionDaily.getPlayerId(), missionDaily.getUpdateTime());
			if (ret > 0) {
				addRet = true;
			}
			if (addRet) {
				int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				missionDaily.setId(theId);
			}

		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return addRet;
	}

	/**
	 * 更新一条任务进度
	 * 
	 * @param missionDaily
	 * @return
	 */
	public void updateMissionDaily(MissionDaily missionDaily) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update(
					"update mission_daily set update_time=? where id=?", missionDaily.getUpdateTime(), missionDaily.getId());
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * 删除一条任务进度(任务完成且奖励已领取)
	 * 
	 * @param missionId
	 */
	public void removeMission(int missionId) {
		QueryRunner runner = new QueryRunner(dataSource);

		try {
			runner.update("delete from mission_daily where id=?", missionId);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
