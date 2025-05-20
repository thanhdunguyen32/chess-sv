package game.module.guozhan.dao;

import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import game.db.DataSourceManager;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class GuozhanDao {

	private static Logger logger = LoggerFactory.getLogger(GuozhanDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static GuozhanDao instance = new GuozhanDao();
	}

	public static GuozhanDao getInstance() {
		return SingletonHolder.instance;
	}
	
	private ResultSetHandler<DBGuoZhanOffice> luckyRankHandler = new ResultSetHandler<DBGuoZhanOffice>() {
		@Override
		public DBGuoZhanOffice handle(ResultSet rs) throws SQLException {
			DBGuoZhanOffice retEntity = null;
			if (rs.next()) {
				// record pack
				try {
					byte[] officeBlob = rs.getBytes("office_position");
					if(officeBlob != null && officeBlob.length>0) {
						retEntity = DBGuoZhanOffice.parseFrom(officeBlob);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return retEntity;
		}
	};
	
	private ResultSetHandler<DBGuoZhanFight> guozhanFightHandler = new ResultSetHandler<DBGuoZhanFight>() {
		@Override
		public DBGuoZhanFight handle(ResultSet rs) throws SQLException {
			DBGuoZhanFight retEntity = null;
			if (rs.next()) {
				// record pack
				try {
					InputStream is = rs.getBinaryStream("city_fight");
					retEntity = DBGuoZhanFight.parseFrom(is);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return retEntity;
		}
	};

	private ResultSetHandler<Date> guozhanTimeHandler = new ResultSetHandler<Date>() {
		@Override
		public Date handle(ResultSet rs) throws SQLException {
			Date retEntity = null;
			if (rs.next()) {
				retEntity = rs.getTimestamp("last_union_award_time");
			}
			return retEntity;
		}
	};
	
	public DBGuoZhanOffice getGuozhan() {
		DBGuoZhanOffice ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from guozhan limit 1", luckyRankHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}
	
	public DBGuoZhanFight getGuozhanFight() {
		DBGuoZhanFight ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from guozhan limit 1", guozhanFightHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public Date getGuozhanLastUnionRewardTime() {
		Date ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT last_union_award_time from guozhan limit 1", guozhanTimeHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public void updateGuozhan(DBGuoZhanOffice guozhanEntity) {
		byte[] content1 = null;
		if (guozhanEntity != null) {
			content1 = guozhanEntity.toByteArray();
		}
		QueryRunner queryRunner = new QueryRunner(dataSource);
		try {
			queryRunner.update("update guozhan set office_position=?", content1);
		} catch (SQLException e1) {
			logger.error("", e1);
		}
	}

	public boolean updateGuozhanOffice(DBGuoZhanOffice guozhanEntity) {
		int addRet = 0;
		QueryRunner queryRunner = new QueryRunner(dataSource);
		byte[] guozhan_s = null;
		if (guozhanEntity != null) {
			guozhan_s = guozhanEntity.toByteArray();
		}
		try {
			addRet = queryRunner.update("update guozhan set office_position = ?", guozhan_s);
		} catch (SQLException e) {
			logger.error("", e);
		}
		return addRet > 0;
	}

	public void updateGuozhanFight(DBGuoZhanFight guozhanFight,Date lastUnionRewardTime) {
		byte[] guozhanFight_s = null;
		if (guozhanFight != null) {
			guozhanFight_s = guozhanFight.toByteArray();
		}
		QueryRunner queryRunner = new QueryRunner(dataSource);
		try {
			queryRunner.update("update guozhan set city_fight=?,last_union_award_time=?", guozhanFight_s, lastUnionRewardTime);
		} catch (SQLException e1) {
			logger.error("", e1);
		}
	}

	public boolean insertGuozhanFight(DBGuoZhanFight guoZhanFight) {
		int addRet = 0;
		QueryRunner queryRunner = new QueryRunner(dataSource);
		byte[] guozhanFight_s = null;
		if (guoZhanFight != null) {
			guozhanFight_s = guoZhanFight.toByteArray();
		}
		try {
			addRet = queryRunner.update("insert into guozhan" + "(city_fight) values(?)", guozhanFight_s);
		} catch (SQLException e) {
			logger.error("", e);
		}
		return addRet > 0;
	}

}