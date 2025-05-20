package game.module.item.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import game.module.item.bean.Item;

public class ItemDao {

	private static Logger logger = LoggerFactory.getLogger(ItemDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
		static ItemDao instance = new ItemDao();
	}

	public static ItemDao getInstance() {
		return SingletonHolder.instance;
	}

	private ResultSetHandler<List<Item>> multiItemHandler = new ResultSetHandler<List<Item>>() {
		@Override
		public List<Item> handle(ResultSet rs) throws SQLException {
			List<Item> itemBeanList = new ArrayList<Item>();
			while (rs.next()) {
				Item itemBean = new Item();
				itemBean.setId(rs.getInt("Id"));
				itemBean.setPlayerId(rs.getInt("player_id"));
				itemBean.setTemplateId(rs.getInt("template_id"));
				itemBean.setCount(rs.getInt("count"));
				itemBean.setGainTime(rs.getTimestamp("gain_time"));
				itemBeanList.add(itemBean);
			}
			return itemBeanList;
		}
	};

	private ResultSetHandler<Item> oneItemHandler = new ResultSetHandler<Item>() {
		@Override
		public Item handle(ResultSet rs) throws SQLException {
			Item itemBean = null;
			if (rs.next()) {
				itemBean = new Item();
				itemBean.setId(rs.getInt("Id"));
				itemBean.setPlayerId(rs.getInt("player_id"));
				itemBean.setTemplateId(rs.getInt("template_id"));
				itemBean.setCount(rs.getInt("count"));
				itemBean.setGainTime(rs.getTimestamp("gain_time"));
			}
			return itemBean;
		}
	};

	public boolean addItem(Item item) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			int ret = runner.update(conn,"insert into item(player_id,template_id,count,gain_time) values(?,?,?,?)", item.getPlayerId(),
					item.getTemplateId(), item.getCount(), item.getGainTime());
			if (ret > 0) {
				addRet = true;
			}
			// get item id
			if (addRet) {
				int itemId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				item.setId(itemId);
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return addRet;
	}

	public List<Item> getItemsByPlayerId(int playerId) {
		List<Item> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from item where player_id = ?", multiItemHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public Item getItemsById(int itemId) {
		Item ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from item where id = ?", oneItemHandler, itemId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public void updateItemCount(int itemTemplateId, int count, int playerId) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			String updateSql = "update item set count = ? where template_id = ? and player_id=?";
			runner.update(updateSql, count, itemTemplateId, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public void addItemCount(int itemTemplateId, int changeCount, int playerId) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			String updateSql = "update item set count = count + ? where template_id = ? and player_id=?";
			runner.update(updateSql, changeCount, itemTemplateId, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public void removeItem(int itemTemplateId, int playerId) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("delete from item where template_id = ? and player_id=?", itemTemplateId, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public boolean checkItemExist(int playerId, int itemId) {
		boolean ret = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT EXISTS(select 1 from item where player_id = ? and template_id=?)",
					DaoCommonHandler.Booleanhandler,playerId,itemId);
		} catch (SQLException e) {
			logger.error("", e);
		}
		return ret;
	}

}
