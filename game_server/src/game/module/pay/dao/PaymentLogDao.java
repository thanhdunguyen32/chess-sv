package game.module.pay.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.pay.bean.LogPayEntity;
import game.module.pay.bean.TopPayPlayerBean;

public class PaymentLogDao {

	private static Logger logger = LoggerFactory.getLogger(PaymentLogDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static PaymentLogDao instance = new PaymentLogDao();
	}

	public static PaymentLogDao getInstance() {
		return SingletonHolder.instance;
	}
	
	private ResultSetHandler<Set<Integer>> multiPlayerIdHandler = new ResultSetHandler<Set<Integer>>() {
		@Override
		public Set<Integer> handle(ResultSet rs) throws SQLException {
			Set<Integer> userBeanSet = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
			while (rs.next()) {
				userBeanSet.add(rs.getInt(1));
			}
			return userBeanSet;
		}
	};
	
	private ResultSetHandler<List<TopPayPlayerBean>> topPayPlayersHandler = new ResultSetHandler<List<TopPayPlayerBean>>() {
		@Override
		public List<TopPayPlayerBean> handle(ResultSet rs) throws SQLException {
			List<TopPayPlayerBean> retList = new ArrayList<>();
			while (rs.next()) {
				TopPayPlayerBean topPayPlayerBean = new TopPayPlayerBean();
				topPayPlayerBean.setPlayer_id(rs.getInt(1));
				topPayPlayerBean.setName(rs.getString(2));
				topPayPlayerBean.setAccount_id(rs.getString(3));
				topPayPlayerBean.setSumMoney(rs.getInt(4));
				retList.add(topPayPlayerBean);
			}
			return retList;
		}
	};

	private ResultSetHandler<List<LogPayEntity>> topupAllHandler = new ResultSetHandler<List<LogPayEntity>>() {
		@Override
		public List<LogPayEntity> handle(ResultSet rs) throws SQLException {
			List<LogPayEntity> retList = new ArrayList<>();
			while (rs.next()) {
				LogPayEntity topPayPlayerBean = new LogPayEntity();
				topPayPlayerBean.setPlayerId(rs.getInt(1));
				topPayPlayerBean.setPlayerName(rs.getString(2));
				topPayPlayerBean.setPaySum(rs.getInt(3));
				topPayPlayerBean.setOrderId(rs.getString(4));
				topPayPlayerBean.setPayTime(rs.getTimestamp(5));
				retList.add(topPayPlayerBean);
			}
			return retList;
		}
	};
	
	private ResultSetHandler<Integer> countHandler = new ResultSetHandler<Integer>() {
		@Override
		public Integer handle(ResultSet rs) throws SQLException {
			Integer ret = 0;
			while (rs.next()) {
				ret++;
			}
			return ret;
		}
	};

	public boolean saveTopupLog(int playerId, String productId, String transactionCode, int addPoint, String openId,int serverId) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			int ret = runner.update(
					"insert into log_topup(player_id,product_id,transaction_code,add_point,open_id,server_id,add_time) values(?,?,?,?,?,?,NOW())",
					playerId, productId, transactionCode, addPoint, openId,serverId);
			if (ret > 0) {
				addRet = true;
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return addRet;
	}
	
	public boolean checkRechargeExist(String productId, String openId, int serverId) {
		boolean ret = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			logger.info("Check recharge: openId={}, serverId={}, productId={}", openId, serverId, productId);
			ret = runner.query(
				"SELECT EXISTS(select 1 from log_topup where open_id = ? and server_id = ? and product_id=?)",
				DaoCommonHandler.Booleanhandler, 
				openId, serverId, productId);
			logger.info("Check result: {}", ret);
		} catch (SQLException e) {
			logger.error("Check recharge failed", e);
			return ret;
		}
		return ret;
	}

	public boolean saveRewardDiamond(int playerId, int rewardDiamond, String openId,int serverId) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			int ret = runner.update(
					"insert into log_topup(player_id,reward_diamond,open_id,server_id,add_time) values(?,?,?,?,NOW())", playerId,
					rewardDiamond, openId,serverId);
			if (ret > 0) {
				addRet = true;
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return addRet;
	}

	public boolean checkOrderExist(String orderId) {
		boolean ret = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT EXISTS(select 1 from log_topup where transaction_code = ?)",
					DaoCommonHandler.Booleanhandler, orderId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public Set<Integer> getPaymentPlayers() {
		Set<Integer> retSet = new HashSet<>();
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			retSet = runner.query("SELECT distinct(player_id) from log_topup", multiPlayerIdHandler);
		} catch (SQLException e) {
			logger.error("", e);
		}
		return retSet;
	}

	/**
	 * 
	 * 小7平台特殊处理
	 * 
	 * @param playerId
	 * @param transactionCode
	 * @param productId
	 * @param openId
	 * @return
	 */

	public boolean saveToPayTopupLog(int playerId, String transactionCode, int productId, String openId, int serverId) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			int ret = runner.update(
					"insert into log_topup(player_id,transaction_code,product_id,open_id,server_id,add_time,status) values(?,?,?,?,?,NOW(),0)",
					playerId, transactionCode, productId, openId, serverId);
			if (ret > 0) {
				addRet = true;
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return addRet;
	}

	public void updatePayLog(int add_point, String transaction_code) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("update log_topup set add_point = ?,status = 1 where transaction_code = ?", add_point,
					transaction_code);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	public int getToPayProductId(String orderId) {
		int ret = -1;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select product_id from log_topup where transaction_code = ? and status = 0",
					DaoCommonHandler.Integerhandler, orderId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}
	
	public List<Integer> getTodayPayPlayers() {
		List<Integer> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT count(distinct(player_id)),sum(add_point) from log_topup where date_format(add_time,'%Y-%m-%d') = curdate()", DaoCommonHandler.Integer2Handler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public int getTodayNewPayPlayers() {
		int ret = 0;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT min(add_time) as min_add_time,player_id from log_topup group by player_id having date_format(min_add_time,'%Y-%m-%d') = curdate()", countHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public List<TopPayPlayerBean> getTopPayPlayers() {
		List<TopPayPlayerBean> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query(
					"select log_topup.player_id,player.name,player.account_id,sum(log_topup.add_point) as SUM_ADD_POINT from log_topup left join player on log_topup.player_id = player.id group by log_topup.player_id order by SUM_ADD_POINT desc",
					topPayPlayersHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}
	
	public List<LogPayEntity> getLogTopupAll(){
		List<LogPayEntity> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query(
					"select log_topup.player_id,player.name,log_topup.add_point,log_topup.transaction_code,log_topup.add_time from log_topup left join player on log_topup.player_id = player.id",
					topupAllHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}
	
	public List<TopPayPlayerBean> getTopupByDate(String queryDate) {
		List<TopPayPlayerBean> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query(
					"select log_topup.player_id,player.name,player.account_id,sum(log_topup.add_point) as SUM_ADD_POINT from log_topup left join player on log_topup.player_id = player.id  where str_to_date('"
							+ queryDate
							+ "', '%Y-%m-%d') = date_format(log_topup.add_time,'%Y-%m-%d') group by log_topup.player_id order by SUM_ADD_POINT desc",
					topPayPlayersHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public boolean saveTopupLogFanli(int playerId, String productId, String transactionCode, int addPoint, String openId,int serverId) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			int ret = runner.update(
					"insert into log_topup(player_id,product_id,transaction_code,reward_diamond,open_id,server_id,add_time) values(?,?,?,?,?,?,NOW())",
					playerId, productId, transactionCode, addPoint, openId,serverId);
			if (ret > 0) {
				addRet = true;
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return addRet;
	}
	

}
