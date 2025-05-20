package game.module.player.dao;

import game.bean.BusPlayerBean;
import game.db.DataSourceManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusPlayerDao {

	private static Logger logger = LoggerFactory.getLogger(BusPlayerDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static BusPlayerDao instance = new BusPlayerDao();
	}

	public static BusPlayerDao getInstance() {
		return SingletonHolder.instance;
	}

	public List<BusPlayerBean> busPlayerBeans() {
		List<BusPlayerBean> ret = null;
		ResultSetHandler<List<BusPlayerBean>> handler = new ResultSetHandler<List<BusPlayerBean>>() {
			@Override
			public List<BusPlayerBean> handle(ResultSet rs) throws SQLException {
				List<BusPlayerBean> busPlayerList = new ArrayList<BusPlayerBean>();
				while (rs.next()) {
					BusPlayerBean playerBean = new BusPlayerBean();
					playerBean.setZoneId(rs.getInt("zone_id"));
					playerBean.setGarenaOpenId(rs.getString("garena_id"));
					playerBean.setPlayerId(rs.getInt("player_id"));
					playerBean.setName(rs.getString("name"));
					playerBean.setIcon(rs.getInt("icon"));
					playerBean.setLevel(rs.getInt("level"));

					busPlayerList.add(playerBean);
				}
				return busPlayerList;
			}
		};

		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from bus_player", handler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public void insertBusPlayer(BusPlayerBean busPlayerBean) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update(
					"insert into bus_player(zone_id, garena_id, player_id, name, icon, level) values(?,?,?,?,?,?)",
					busPlayerBean.getZoneId(), busPlayerBean.getGarenaOpenId(), busPlayerBean.getPlayerId(),
					busPlayerBean.getName(), busPlayerBean.getIcon(), busPlayerBean.getLevel());
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public void updateBusPlayerLevel(String garenaOpenId, Integer zoneId, Integer playerId, Integer level) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("update bus_player set level = ? where garena_id = ? and zone_id = ? and player_id = ?",
					level, garenaOpenId, zoneId, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
