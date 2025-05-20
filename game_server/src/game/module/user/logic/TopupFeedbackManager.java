package game.module.user.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.db.DataSourceManager;

public class TopupFeedbackManager {

	private static Logger logger = LoggerFactory.getLogger(TopupFeedbackManager.class);

	static class SingletonHolder {
		static TopupFeedbackManager instance = new TopupFeedbackManager();
	}

	public static TopupFeedbackManager getInstance() {
		return SingletonHolder.instance;
	}
	
	private volatile Map<String,Boolean> hasFeedbackMap = new ConcurrentHashMap<>();

	public void reload() {
		logger.info("reload 充值，月卡，成长基金反馈!");
		hasFeedbackMap = checkHasFeedback();
		logger.info("测试服充值反馈，hasFeedback={}", hasFeedbackMap.size());
	}
	
	public static ResultSetHandler<Map<String, Boolean>> HasFeedbackHanlder = new ResultSetHandler<Map<String, Boolean>>() {
		@Override
		public Map<String, Boolean> handle(ResultSet rs) throws SQLException {
			Map<String, Boolean> retMap = new ConcurrentHashMap<>();
			while (rs.next()) {
				String openid = rs.getString(1);
				int maxStatus = rs.getInt(2);
				if (maxStatus > 0) {
					retMap.put(openid, false);
				} else {
					retMap.put(openid, true);
				}
			}
			return retMap;
		}
	};
	
	public static ResultSetHandler<List<TopupFeedbackBean>> TopupFeedbackAllHanlder = new ResultSetHandler<List<TopupFeedbackBean>>() {
		@Override
		public List<TopupFeedbackBean> handle(ResultSet rs) throws SQLException {
			List<TopupFeedbackBean> retlist = new ArrayList<>();
			while (rs.next()) {
				String transaction_code = rs.getString("transaction_code");
				int add_point = rs.getInt("add_point");
				String open_id = rs.getString("open_id");
				String product_id = rs.getString("product_id");
				retlist.add(new TopupFeedbackBean(transaction_code, add_point, open_id, product_id));
			}
			return retlist;
		}
	};
	
	public Map<String,Boolean> checkHasFeedback() {
		Map<String,Boolean> ret = null;
		DataSource dataSource = DataSourceManager.getInstance().getDataSource();
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select open_id,max(status) from log_topup_feedback group by open_id", HasFeedbackHanlder);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}
	
	public List<TopupFeedbackBean> getTopupFeedback(String openId) {
		List<TopupFeedbackBean> ret = null;
		DataSource dataSource = DataSourceManager.getInstance().getDataSource();
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query(
					"select transaction_code,add_point,open_id,product_id from log_topup_feedback where open_id = ?",
					TopupFeedbackAllHanlder, openId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public void updateStatus(String openId) {
		DataSource dataSource = DataSourceManager.getInstance().getDataSource();
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("update log_topup_feedback set status = 1 where open_id = ?", openId);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public boolean isHasFeedback(String openId) {
		if (hasFeedbackMap.containsKey(openId) && hasFeedbackMap.get(openId) == true) {
			return true;
		}
		return false;
	}
	
	public void setFeedbackFinish(String openId) {
		hasFeedbackMap.put(openId, false);
		updateStatus(openId);
	}

	public static final class TopupFeedbackBean {
		public String transaction_code;
		public Integer add_point;
		public String open_id;
		public String product_id;
		public TopupFeedbackBean(String transaction_code, Integer add_point, String open_id, String product_id) {
			this.transaction_code = transaction_code;
			this.add_point = add_point;
			this.open_id = open_id;
			this.product_id = product_id;
		}
	}

}
