package game.module.guozhan.dao;

import db.proto.ProtoMessageGuozhan.DBGuozhanPlayer;
import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.guozhan.bean.GuozhanPlayer;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 秘密基地Dao
 * 
 * @author zhangning
 * 
 * @Date 2015年1月26日 下午3:16:27
 */
public class GuozhanPlayerDao {

	private static Logger logger = LoggerFactory.getLogger(GuozhanPlayerDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static GuozhanPlayerDao instance = new GuozhanPlayerDao();
	}

	public static GuozhanPlayerDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 秘密基地
	 */
	private ResultSetHandler<GuozhanPlayer> guozhan_selfHandler = new ResultSetHandler<GuozhanPlayer>() {
		@Override
		public GuozhanPlayer handle(ResultSet rs) throws SQLException {
			GuozhanPlayer guozhan_self = null;
			while (rs.next()) {
				guozhan_self = new GuozhanPlayer();
				guozhan_self.setId(rs.getInt("Id"));
				guozhan_self.setPlayerId(rs.getInt("player_id"));
				guozhan_self.setStay_city_index(rs.getInt("stay_city_index"));
				try {
					InputStream is = rs.getBinaryStream("pass_city_indexs");
					if(is != null) {
						DBGuozhanPlayer boxmAward = DBGuozhanPlayer.parseFrom(is);
						guozhan_self.setDbGuozhanPlayer(boxmAward);
					}
				} catch (IOException e) {
					logger.error("",e);
				}
				guozhan_self.setNation(rs.getInt("nation"));
				guozhan_self.setNationChangeTime(rs.getTimestamp("nation_change_time"));
			}
			return guozhan_self;
		}
	};
	
	private ResultSetHandler<Map<Integer,Integer>> nationIdAllHandler = new ResultSetHandler<Map<Integer,Integer>>() {
		@Override
		public Map<Integer,Integer> handle(ResultSet rs) throws SQLException {
			Map<Integer,Integer> retMap = new HashMap<>();
			while (rs.next()) {
				int nationId = rs.getInt("nation");
				int playerId = rs.getInt("player_id");
				retMap.put(playerId, nationId);
			}
			return retMap;
		}
	};

	/**
	 * 获取玩家秘密基地信息
	 * 
	 * @param playerId
	 * @return
	 */
	public GuozhanPlayer getPlayerGuozhanPlayer(int playerId) {
		GuozhanPlayer ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from guozhan_self where player_id = ?", guozhan_selfHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 添加秘密基地信息
	 * 
	 * @param guozhan_self
	 * @return
	 */
	public void addGuozhanPlayer(GuozhanPlayer guozhan_self) {
		int addRet = 0;
		Connection con = null;
		QueryRunner queryRunner = new QueryRunner(dataSource);
		try {
			DBGuozhanPlayer dbGuozhanPlayer = guozhan_self.getDbGuozhanPlayer();
			byte[] content1 = null;
			if (dbGuozhanPlayer != null) {
				content1 = dbGuozhanPlayer.toByteArray();
			}

			con = dataSource.getConnection();
			addRet = queryRunner.update(con,
					"insert into guozhan_self(player_id,pass_city_indexs,stay_city_index) values(?,?,?)",
					guozhan_self.getPlayerId(), content1, guozhan_self.getStay_city_index());
			// get id
			if (addRet > 0) {
				int theId = queryRunner.query(con, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				guozhan_self.setId(theId);
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	/**
	 * 更新秘密基地所有信息
	 * 
	 * @param guozhan_self
	 */
	public void updateGuozhanPlayerAll(GuozhanPlayer guozhan_self) {
		DBGuozhanPlayer dbItemAttrEx = guozhan_self.getDbGuozhanPlayer();
		byte[] content1 = null;
		if (dbItemAttrEx != null) {
			content1 = dbItemAttrEx.toByteArray();
		}
		QueryRunner queryRunner = new QueryRunner(dataSource);
		try {
			queryRunner.update("update guozhan_self set pass_city_indexs=?,stay_city_index=? where id = ?", content1,
					guozhan_self.getStay_city_index(), guozhan_self.getId());
		} catch (SQLException e1) {
			logger.error("", e1);
		}
	}

	public void updateGuozhanPlayerNation(GuozhanPlayer guozhanPlayer) {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		try {
			queryRunner.update("update guozhan_self set nation=?,nation_change_time=? where id = ?", guozhanPlayer.getNation(),
					guozhanPlayer.getNationChangeTime(), guozhanPlayer.getId());
		} catch (SQLException e1) {
			logger.error("", e1);
		}
	}

	public Map<Integer,Integer> getGuoZhanNationAll(String idAll) {
		Map<Integer,Integer> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT player_id,nation from guozhan_self where player_id in (" + idAll + ")", nationIdAllHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public void clearNationIdAndTime() {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		try {
			queryRunner.update("update guozhan_self set nation=0,nation_change_time=NULL");
		} catch (SQLException e1) {
			logger.error("", e1);
		}
	}

}
