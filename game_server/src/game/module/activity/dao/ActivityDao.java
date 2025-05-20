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

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.activity.bean.Activity4Gm;
import game.module.activity.bean.ActivityBean;
import game.module.activity.bean.DBActivityAwardDouble;
import game.module.activity.bean.DBActivityChengZhangJiJin;
import game.module.activity.bean.DBActivityCommon;
import game.module.activity.bean.DBActivityHeroLibao;
import game.module.activity.bean.DBActivityLotteryWheel;
import game.module.activity.bean.DBActivityQCJJ;
import game.module.activity.bean.DBActivityQiZhenYiBao;
import game.module.activity.bean.DBActivityQuanMinFuLi;
import game.module.activity.constants.ActivityConstants;
import game.module.db.bean.DBQiZhenYiBaoPlayers;
import game.module.db.bean.DbChongZhiBang;
import game.util.Global;
import io.protostuff.ProtostuffIOUtil;

public class ActivityDao {

	private static Logger logger = LoggerFactory.getLogger(ActivityDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static ActivityDao instance = new ActivityDao();
	}

	public static ActivityDao getInstance() {
		return SingletonHolder.instance;
	}

	private ResultSetHandler<List<Activity4Gm>> multiActivityHandler = new ResultSetHandler<List<Activity4Gm>>() {
		@Override
		public List<Activity4Gm> handle(ResultSet rs) throws SQLException {
			List<Activity4Gm> beanList = new ArrayList<Activity4Gm>();
			while (rs.next()) {
				Activity4Gm aBean = new Activity4Gm();
				aBean.setType(rs.getInt("type"));
				aBean.setStartTime(rs.getTimestamp("start_time"));
				aBean.setEndTime(rs.getTimestamp("end_time"));
				aBean.setTitle(rs.getString("title"));
				aBean.setIsOpen(rs.getInt("is_open"));
				beanList.add(aBean);
			}
			return beanList;
		}
	};

	private ResultSetHandler<Activity4Gm> oneActivityHandler = new ResultSetHandler<Activity4Gm>() {
		@Override
		public Activity4Gm handle(ResultSet rs) throws SQLException {
			Activity4Gm aBean = null;
			if (rs.next()) {
				aBean = new Activity4Gm();
				aBean.setType(rs.getInt("type"));
				aBean.setStartTime(rs.getTimestamp("start_time"));
				aBean.setEndTime(rs.getTimestamp("end_time"));
				aBean.setTitle(rs.getString("title"));
				aBean.setDescription(rs.getString("description"));
				aBean.setIsOpen(rs.getInt("is_open"));
				aBean.setParams(rs.getBytes("param"));
			}
			return aBean;
		}
	};

	public List<Activity4Gm> getAllActivityBase() {
		List<Activity4Gm> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from activity", multiActivityHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public Activity4Gm getActivity4Gm(int id) {
		Activity4Gm ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from activity where type = ?", oneActivityHandler, id);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public boolean checkActivityExist(int type) {
		boolean ret = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT EXISTS(select 1 from activity where type = ?)", DaoCommonHandler.Booleanhandler,
					type);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	// ------------------------------------ActivityBean----------------------------------------//

	private ResultSetHandler<List<ActivityBean>> multiActivityBeanHandler = new ResultSetHandler<List<ActivityBean>>() {
		@Override
		public List<ActivityBean> handle(ResultSet rs) throws SQLException {
			List<ActivityBean> beanList = new ArrayList<ActivityBean>();
			while (rs.next()) {
				ActivityBean aBean = new ActivityBean();
				aBean.setType(rs.getInt("type"));
				aBean.setStartTime(rs.getTimestamp("start_time"));
				aBean.setEndTime(rs.getTimestamp("end_time"));
				aBean.setTitle(rs.getString("title"));
				aBean.setDescription(rs.getString("description"));
				aBean.setIsOpen(rs.getInt("is_open"));
				if (aBean.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN) {
					DBActivityQCJJ dBActivitySale = new DBActivityQCJJ();
					try {
						InputStream is = rs.getBinaryStream("param");
						ProtostuffIOUtil.mergeFrom(is, dBActivitySale, DBActivityQCJJ.getSchema());
					} catch (IOException e) {
						e.printStackTrace();
					}
					aBean.setActivityQCJJ(dBActivitySale);
				} else if (aBean.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN) {
					DBActivityChengZhangJiJin dBActivitySale = new DBActivityChengZhangJiJin();
					try {
						InputStream is = rs.getBinaryStream("param");
						ProtostuffIOUtil.mergeFrom(is, dBActivitySale, DBActivityChengZhangJiJin.getSchema());
					} catch (IOException e) {
						e.printStackTrace();
					}
					aBean.setDbActivityChengZhangJiJin(dBActivitySale);
				} else if (aBean.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI) {
					DBActivityQuanMinFuLi dBActivitySale = new DBActivityQuanMinFuLi();
					try {
						InputStream is = rs.getBinaryStream("param");
						ProtostuffIOUtil.mergeFrom(is, dBActivitySale, DBActivityQuanMinFuLi.getSchema());
					} catch (IOException e) {
						e.printStackTrace();
					}
					aBean.setActivityQuanMinFuLi(dBActivitySale);
				} else if (aBean.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO) {
					DBActivityHeroLibao dBActivitySale = new DBActivityHeroLibao();
					try {
						InputStream is = rs.getBinaryStream("param");
						ProtostuffIOUtil.mergeFrom(is, dBActivitySale, DBActivityHeroLibao.getSchema());
					} catch (IOException e) {
						e.printStackTrace();
					}
					aBean.setActivityHeroLibao(dBActivitySale);
				} else if (aBean.getType() == ActivityConstants.TYPE_AWARD_DOUBLE) {
					DBActivityAwardDouble dBActivityAwardDouble = new DBActivityAwardDouble();
					try {
						InputStream is = rs.getBinaryStream("param");
						ProtostuffIOUtil.mergeFrom(is, dBActivityAwardDouble, DBActivityAwardDouble.getSchema());
					} catch (IOException e) {
						e.printStackTrace();
					}
					aBean.setDbActivityAwardDouble(dBActivityAwardDouble);
				} else if (aBean.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL) {
					DBActivityLotteryWheel dBActivityAwardDouble = new DBActivityLotteryWheel();
					try {
						InputStream is = rs.getBinaryStream("param");
						ProtostuffIOUtil.mergeFrom(is, dBActivityAwardDouble, DBActivityLotteryWheel.getSchema());
					} catch (IOException e) {
						e.printStackTrace();
					}
					aBean.setActivityLotteryWheel(dBActivityAwardDouble);
				} else if (aBean.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_QI_ZHEN_YI_BAO) {
					DBActivityQiZhenYiBao dBActivityAwardDouble = new DBActivityQiZhenYiBao();
					try {
						InputStream is = rs.getBinaryStream("param");
						ProtostuffIOUtil.mergeFrom(is, dBActivityAwardDouble, DBActivityQiZhenYiBao.getSchema());
					} catch (IOException e) {
						e.printStackTrace();
					}
					aBean.setActivityQiZhenYiBao(dBActivityAwardDouble);
				} else {
					DBActivityCommon dBActivitySale = new DBActivityCommon();
					try {
						InputStream is = rs.getBinaryStream("param");
						ProtostuffIOUtil.mergeFrom(is, dBActivitySale, DBActivityCommon.getSchema());
					} catch (IOException e) {
						e.printStackTrace();
					}
					aBean.setActivityCommon(dBActivitySale);
				}
				beanList.add(aBean);
			}
			return beanList;
		}
	};

	public List<ActivityBean> getAllActivityBean() {
		List<ActivityBean> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from activity", multiActivityBeanHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public void updateActivityBean(Activity4Gm activityBean) {
		final InputStream inputStream;
		inputStream = new ByteArrayInputStream(activityBean.getParams());

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"update activity set start_time=?,end_time=?,title=?,description=?,is_open = ?,param=? where type = ?");
			pstmt.setTimestamp(1, new Timestamp(activityBean.getStartTime().getTime()));
			pstmt.setTimestamp(2, new Timestamp(activityBean.getEndTime().getTime()));
			pstmt.setString(3, activityBean.getTitle());
			pstmt.setString(4, activityBean.getDescription());
			pstmt.setInt(5, activityBean.getIsOpen());
			pstmt.setBinaryStream(6, inputStream);
			pstmt.setInt(7, activityBean.getType());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	public boolean insertActivityBean(Activity4Gm activityBean) {
		final InputStream inputStream;
		inputStream = new ByteArrayInputStream(activityBean.getParams());

		Connection con = null;
		int ret = 0;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"insert into activity(start_time,end_time,title,description,is_open,param,type) values(?,?,?,?,?,?,?)");
			pstmt.setTimestamp(1, new Timestamp(activityBean.getStartTime().getTime()));
			pstmt.setTimestamp(2, new Timestamp(activityBean.getEndTime().getTime()));
			pstmt.setString(3, activityBean.getTitle());
			pstmt.setString(4, activityBean.getDescription());
			pstmt.setInt(5, activityBean.getIsOpen());
			pstmt.setBinaryStream(6, inputStream);
			pstmt.setInt(7, activityBean.getType());
			ret = pstmt.executeUpdate();
			pstmt.close();
			// get id
			if (ret > 0) {
				// success
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}

		return true;
	}

	/**
	 * 奇珍异宝活动
	 */
	private ResultSetHandler<DBQiZhenYiBaoPlayers> heroStarsRankAllHandler = new ResultSetHandler<DBQiZhenYiBaoPlayers>() {
		@Override
		public DBQiZhenYiBaoPlayers handle(ResultSet rs) throws SQLException {
			DBQiZhenYiBaoPlayers dbRankHeroStars = new DBQiZhenYiBaoPlayers();
			if (rs.next()) {
				// record pack
				try {
					InputStream is = rs.getBinaryStream("qi_zhen_yi_bao_activity");
					if (is != null) {
						ProtostuffIOUtil.mergeFrom(is, dbRankHeroStars, DBQiZhenYiBaoPlayers.getSchema());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return dbRankHeroStars;
		}
	};

	public DBQiZhenYiBaoPlayers getQiZhenYiBaoLog() {
		DBQiZhenYiBaoPlayers ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT qi_zhen_yi_bao_activity from system_blob", heroStarsRankAllHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	/**
	 * 
	 */
	private void saveDBQiZhenYiBao(DBQiZhenYiBaoPlayers dbFriendHelpChannel, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffIOUtil.toByteArray(dbFriendHelpChannel, DBQiZhenYiBaoPlayers.getSchema(),
				Global.getProtoBuffer());
		inputStream = new ByteArrayInputStream(content);

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sqlStr);
			pstmt.setBlob(1, inputStream);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	public void updateDBQiZhenYiBao(DBQiZhenYiBaoPlayers dBFriendHelpChannel) {
		String sqlStr = "update system_blob set qi_zhen_yi_bao_activity = ?";
		saveDBQiZhenYiBao(dBFriendHelpChannel, sqlStr);
	}

	/**
	 * 充值榜活动
	 */
	private ResultSetHandler<DbChongZhiBang> dbChongZhiBangHandler = new ResultSetHandler<DbChongZhiBang>() {
		@Override
		public DbChongZhiBang handle(ResultSet rs) throws SQLException {
			DbChongZhiBang dbRankHeroStars = new DbChongZhiBang();
			if (rs.next()) {
				// record pack
				try {
					InputStream is = rs.getBinaryStream("chong_zhi_bang_activity");
					if (is != null) {
						ProtostuffIOUtil.mergeFrom(is, dbRankHeroStars, DbChongZhiBang.getSchema());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return dbRankHeroStars;
		}
	};

	public DbChongZhiBang getChongZhiBang() {
		DbChongZhiBang ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT chong_zhi_bang_activity from system_blob", dbChongZhiBangHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	/**
	 * 
	 */
	private void saveDbChongZhiBang(DbChongZhiBang dbFriendHelpChannel, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffIOUtil.toByteArray(dbFriendHelpChannel, DbChongZhiBang.getSchema(),
				Global.getProtoBuffer());
		inputStream = new ByteArrayInputStream(content);

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sqlStr);
			pstmt.setBlob(1, inputStream);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	public void updateDbChongZhiBang(DbChongZhiBang dBFriendHelpChannel) {
		String sqlStr = "update system_blob set chong_zhi_bang_activity = ?";
		saveDbChongZhiBang(dBFriendHelpChannel, sqlStr);
	}

	/**
	 * 消费榜活动
	 */
	private ResultSetHandler<DbChongZhiBang> dbXiaoFeiBangHandler = new ResultSetHandler<DbChongZhiBang>() {
		@Override
		public DbChongZhiBang handle(ResultSet rs) throws SQLException {
			DbChongZhiBang dbRankHeroStars = new DbChongZhiBang();
			if (rs.next()) {
				// record pack
				try {
					InputStream is = rs.getBinaryStream("xiao_fei_bang_activity");
					if (is != null) {
						ProtostuffIOUtil.mergeFrom(is, dbRankHeroStars, DbChongZhiBang.getSchema());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return dbRankHeroStars;
		}
	};

	public DbChongZhiBang getDBActivityXiaoFeiBang() {
		DbChongZhiBang ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT xiao_fei_bang_activity from system_blob", dbXiaoFeiBangHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	/**
	 * 
	 */
	private void saveDBActivityXiaoFeiBang(DbChongZhiBang dbFriendHelpChannel, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffIOUtil.toByteArray(dbFriendHelpChannel, DbChongZhiBang.getSchema(),
				Global.getProtoBuffer());
		inputStream = new ByteArrayInputStream(content);

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sqlStr);
			pstmt.setBlob(1, inputStream);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	public void updateDBActivityXiaoFeiBang(DbChongZhiBang dBFriendHelpChannel) {
		String sqlStr = "update system_blob set xiao_fei_bang_activity = ?";
		saveDBActivityXiaoFeiBang(dBFriendHelpChannel, sqlStr);
	}
}
