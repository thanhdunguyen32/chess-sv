package game.module.activity.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.db.DataSourceManager;
import game.module.activity.bean.ActivityPlayer;
import game.module.activity.bean.DBActivityAnswer;
import game.module.activity.bean.DBActivityLevel;
import game.module.activity.bean.DBActivityMine;
import game.module.activity.bean.DBActivityPlayerGet;
import game.module.activity.bean.DBActivityPlayerHeroLibao;
import game.module.activity.constants.ActivityConstants;
import game.util.Global;
import io.protostuff.ProtostuffIOUtil;

/**
 * 活动
 * 
 * @author zhangning
 * 
 * @Date 2015年7月23日 下午4:08:35
 */
public class ActivityPlayerDao {

	private static Logger logger = LoggerFactory.getLogger(ActivityPlayerDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static ActivityPlayerDao instance = new ActivityPlayerDao();
	}

	public static ActivityPlayerDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 
	 */
	private ResultSetHandler<List<ActivityPlayer>> activityPlayerHandler = new ResultSetHandler<List<ActivityPlayer>>() {
		@Override
		public List<ActivityPlayer> handle(ResultSet rs) throws SQLException {
			List<ActivityPlayer> activitysPlayer = new ArrayList<ActivityPlayer>();
			while (rs.next()) {
				ActivityPlayer activityPlayer = new ActivityPlayer();
				activityPlayer.setId(rs.getInt("Id"));
				activityPlayer.setPlayerId(rs.getInt("player_id"));
				activityPlayer.setProgress(rs.getInt("progress"));
				activityPlayer.setType(rs.getInt("type"));
				activityPlayer.setResetTime(rs.getTimestamp("reset_time"));

				if (activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_RECHARGE
						|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_DAILY_PAY
						|| activityPlayer.getType() == ActivityConstants.TYPE_CONSUMPTION
						|| activityPlayer.getType() == ActivityConstants.TYPE_LOGIN
						|| activityPlayer.getType() == ActivityConstants.TYPE_STAGE
						|| activityPlayer.getType() == ActivityConstants.TYPE_ELITE_STAGE
						|| activityPlayer.getType() == ActivityConstants.TYPE_ARENA_WIN
						|| activityPlayer.getType() == ActivityConstants.TYPE_FREE_FUND
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_VIP_LIBAO
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_DAILY_ACTIVE
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_SHI_LIAN_JIANG_LI
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI
						|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE
						|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG
						|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG10
						|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_LOGIN) {
					DBActivityPlayerGet dbActivityPlayerGet = new DBActivityPlayerGet();
					try {
						InputStream is = rs.getBinaryStream("param");
						if (is != null) {
							ProtostuffIOUtil.mergeFrom(is, dbActivityPlayerGet, DBActivityPlayerGet.getSchema());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					activityPlayer.setActivityPlayerGet(dbActivityPlayerGet);
				} else if (activityPlayer.getType() == ActivityConstants.TYPE_LEVEL_GO) {
					DBActivityLevel dBActivityLevel = new DBActivityLevel();
					try {
						InputStream is = rs.getBinaryStream("param");
						if (is != null) {
							ProtostuffIOUtil.mergeFrom(is, dBActivityLevel, DBActivityLevel.getSchema());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					activityPlayer.setDbActivityLevel(dBActivityLevel);
				} else if (activityPlayer.getType() == ActivityConstants.TYPE_MINE) {
					DBActivityMine dBActivityMine = new DBActivityMine();
					try {
						InputStream is = rs.getBinaryStream("param");
						if (is != null) {
							ProtostuffIOUtil.mergeFrom(is, dBActivityMine, DBActivityMine.getSchema());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					activityPlayer.setDbActivityMine(dBActivityMine);
				} else if (activityPlayer.getType() == ActivityConstants.TYPE_ANSWER) {
					DBActivityAnswer dBActivityAnswer = new DBActivityAnswer();
					try {
						InputStream is = rs.getBinaryStream("param");
						if (is != null) {
							ProtostuffIOUtil.mergeFrom(is, dBActivityAnswer, DBActivityAnswer.getSchema());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					activityPlayer.setDbActivityAnswer(dBActivityAnswer);
				} else if (activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO) {
					DBActivityPlayerHeroLibao dBActivityAnswer = new DBActivityPlayerHeroLibao();
					try {
						InputStream is = rs.getBinaryStream("param");
						if (is != null) {
							ProtostuffIOUtil.mergeFrom(is, dBActivityAnswer, DBActivityPlayerHeroLibao.getSchema());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					activityPlayer.setHeroLibaoBuy(dBActivityAnswer);
				}

				activitysPlayer.add(activityPlayer);
			}
			return activitysPlayer;
		}
	};

	/**
	 * 获取玩家个人活动信息
	 * 
	 * @param playerId
	 * @return
	 */
	public List<ActivityPlayer> getActivityPlayer(int playerId) {
		List<ActivityPlayer> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from activity_player where player_id = ?", activityPlayerHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 获取某个活动的所有参与玩家
	 * 
	 * @param playerId
	 * @return
	 */
	public List<ActivityPlayer> getActivitiesType(int type) {
		List<ActivityPlayer> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from activity_player where type = ?", activityPlayerHandler, type);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 添加玩家活动信息
	 * 
	 * @param activityPlayer
	 * @return
	 */
	public void addActivityPlayer(ActivityPlayer activityPlayer) {
		int addRet = 0;
		Connection con = null;
		try {

			con = dataSource.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("insert into activity_player(player_id, progress, type, reset_time, param) values(?,?,?,?,?)");
			pstmt.setInt(1, activityPlayer.getPlayerId());
			pstmt.setInt(2, activityPlayer.getProgress());
			pstmt.setInt(3, activityPlayer.getType());
			if (activityPlayer.getResetTime() != null) {
				pstmt.setTimestamp(4, new Timestamp(activityPlayer.getResetTime().getTime()));
			} else {
				pstmt.setTimestamp(4, null);
			}

			if (activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_RECHARGE
					|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_DAILY_PAY
					|| activityPlayer.getType() == ActivityConstants.TYPE_CONSUMPTION
					|| activityPlayer.getType() == ActivityConstants.TYPE_LOGIN
					|| activityPlayer.getType() == ActivityConstants.TYPE_STAGE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ELITE_STAGE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ARENA_WIN
					|| activityPlayer.getType() == ActivityConstants.TYPE_FREE_FUND
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_VIP_LIBAO
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_DAILY_ACTIVE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_SHI_LIAN_JIANG_LI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI
					|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG
					|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG10
					|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_LOGIN) {
				if (activityPlayer.getActivityPlayerGet() == null) {
					pstmt.setObject(5, null);
				} else {
					final InputStream isAnswer;
					byte[] dbActivityAnswer = ProtostuffIOUtil.toByteArray(activityPlayer.getActivityPlayerGet(),
							DBActivityPlayerGet.getSchema(), Global.getProtoBuffer());
					isAnswer = new ByteArrayInputStream(dbActivityAnswer);
					pstmt.setBlob(5, isAnswer);
				}
			} else if (activityPlayer.getType().equals(ActivityConstants.TYPE_LEVEL_GO)) {
				if (activityPlayer.getDbActivityLevel() == null) {
					pstmt.setObject(5, null);
				} else {
					// 冲级奖励
					final InputStream levelGo;
					byte[] dbActivityLevelGo = ProtostuffIOUtil.toByteArray(activityPlayer.getDbActivityLevel(),
							DBActivityLevel.getSchema(), Global.getProtoBuffer());
					levelGo = new ByteArrayInputStream(dbActivityLevelGo);
					pstmt.setBlob(5, levelGo);
				}
			} else if (activityPlayer.getType().equals(ActivityConstants.TYPE_MINE)) {
				// 钻石矿井
				if (activityPlayer.getDbActivityMine() == null) {
					pstmt.setObject(5, null);
				} else {
					final InputStream mine;
					byte[] dbActivityMine = ProtostuffIOUtil.toByteArray(activityPlayer.getDbActivityMine(),
							DBActivityMine.getSchema(), Global.getProtoBuffer());
					mine = new ByteArrayInputStream(dbActivityMine);
					pstmt.setBlob(5, mine);
				}
			} else if (activityPlayer.getType().equals(ActivityConstants.TYPE_ANSWER)) {
				// 答题
				if (activityPlayer.getDbActivityAnswer() == null) {
					pstmt.setObject(5, null);
				} else {
					final InputStream isAnswer;
					byte[] dbActivityAnswer = ProtostuffIOUtil.toByteArray(activityPlayer.getDbActivityAnswer(),
							DBActivityAnswer.getSchema(), Global.getProtoBuffer());
					isAnswer = new ByteArrayInputStream(dbActivityAnswer);
					pstmt.setBlob(5, isAnswer);
				}
			}else if (activityPlayer.getType().equals(ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO)) {
				// 答题
				if (activityPlayer.getHeroLibaoBuy() == null) {
					pstmt.setObject(5, null);
				} else {
					final InputStream isAnswer;
					byte[] dbActivityAnswer = ProtostuffIOUtil.toByteArray(activityPlayer.getHeroLibaoBuy(),
							DBActivityPlayerHeroLibao.getSchema(), Global.getProtoBuffer());
					isAnswer = new ByteArrayInputStream(dbActivityAnswer);
					pstmt.setBlob(5, isAnswer);
				}
			}

			addRet = pstmt.executeUpdate();
			pstmt.close();

			if (addRet > 0) {
				PreparedStatement pstmt2 = con.prepareStatement("SELECT LAST_INSERT_ID()");
				ResultSet resultSet = pstmt2.executeQuery();
				if (resultSet.next()) {
					int theId = resultSet.getInt(1);
					activityPlayer.setId(theId);
				}
				pstmt2.close();
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	/**
	 * 更新活动进度
	 * 
	 * @param activityPlayer
	 * @return
	 */
	public void updateActivityPlayerProgress(ActivityPlayer activityPlayer) {
		QueryRunner runner = new QueryRunner(dataSource);

		try {
			runner.update("update activity_player set progress=?,reset_time=? where player_id=? and type=?",
					activityPlayer.getProgress(),activityPlayer.getResetTime(), activityPlayer.getPlayerId(), activityPlayer.getType());
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * 更新活动领奖信息
	 * 
	 * @param activityPlayer
	 */
	public void updateActivityPlayerAll(ActivityPlayer activityPlayer) {

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("update activity_player set progress = ?,reset_time=?, param = ? where player_id = ? and type = ?");

			int progress = activityPlayer.getProgress() == null ? 0 : activityPlayer.getProgress();
			pstmt.setInt(1, progress);
			if (activityPlayer.getResetTime() != null) {
				pstmt.setTimestamp(2, new Timestamp(activityPlayer.getResetTime().getTime()));
			} else {
				pstmt.setTimestamp(2, null);
			}
			if (activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_RECHARGE
					|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_DAILY_PAY
					|| activityPlayer.getType() == ActivityConstants.TYPE_CONSUMPTION
					|| activityPlayer.getType() == ActivityConstants.TYPE_LOGIN
					|| activityPlayer.getType() == ActivityConstants.TYPE_STAGE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ELITE_STAGE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ARENA_WIN
					|| activityPlayer.getType() == ActivityConstants.TYPE_FREE_FUND
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_VIP_LIBAO
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_DAILY_ACTIVE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_SHI_LIAN_JIANG_LI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN
					|| activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE
					|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG
					|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG10
					|| activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_LOGIN) {
				if (activityPlayer.getActivityPlayerGet() == null) {
					pstmt.setObject(3, null);
				} else {
					final InputStream isAnswer;
					byte[] dbActivityAnswer = ProtostuffIOUtil.toByteArray(activityPlayer.getActivityPlayerGet(),
							DBActivityPlayerGet.getSchema(), Global.getProtoBuffer());
					isAnswer = new ByteArrayInputStream(dbActivityAnswer);
					pstmt.setBlob(3, isAnswer);
				}
			} else if (activityPlayer.getType().equals(ActivityConstants.TYPE_LEVEL_GO)) {
				// 冲级奖励
				final InputStream isLevel;
				byte[] dbActivityLevel = ProtostuffIOUtil.toByteArray(activityPlayer.getDbActivityLevel(),
						DBActivityLevel.getSchema(), Global.getProtoBuffer());
				isLevel = new ByteArrayInputStream(dbActivityLevel);
				pstmt.setBlob(3, isLevel);
			} else if (activityPlayer.getType().equals(ActivityConstants.TYPE_MINE)) {
				// 钻石矿井
				final InputStream isMine;
				byte[] dbActivityMine = ProtostuffIOUtil.toByteArray(activityPlayer.getDbActivityMine(),
						DBActivityMine.getSchema(), Global.getProtoBuffer());
				isMine = new ByteArrayInputStream(dbActivityMine);
				pstmt.setBlob(3, isMine);
			} else if (activityPlayer.getType().equals(ActivityConstants.TYPE_ANSWER)) {
				// 答题
				final InputStream isAnswer;
				byte[] dbActivityAnswer = ProtostuffIOUtil.toByteArray(activityPlayer.getDbActivityAnswer(),
						DBActivityAnswer.getSchema(), Global.getProtoBuffer());
				isAnswer = new ByteArrayInputStream(dbActivityAnswer);
				pstmt.setBlob(3, isAnswer);
			} else if (activityPlayer.getType().equals(ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO)) {
				// 答题
				final InputStream isAnswer;
				byte[] dbActivityAnswer = ProtostuffIOUtil.toByteArray(activityPlayer.getHeroLibaoBuy(),
						DBActivityPlayerHeroLibao.getSchema(), Global.getProtoBuffer());
				isAnswer = new ByteArrayInputStream(dbActivityAnswer);
				pstmt.setBlob(3, isAnswer);
			}

			pstmt.setInt(4, activityPlayer.getPlayerId());
			pstmt.setInt(5, activityPlayer.getType());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	/**
	 * 删除某一活动的所有玩家进程
	 * 
	 * @param type
	 */
	public void removePlayerActivityType(int type) {
		QueryRunner runner = new QueryRunner(dataSource);

		try {
			runner.update("delete from activity_player where type=?", type);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * 删除某一玩家的某一类型活动
	 * 
	 * @param playerId
	 * @param type
	 */
	public void removeOnePlayerActivity(int playerId, int type) {
		QueryRunner runner = new QueryRunner(dataSource);

		try {
			runner.update("delete from activity_player where player_id = ? and type=?", playerId, type);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
