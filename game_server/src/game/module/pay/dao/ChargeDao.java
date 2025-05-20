package game.module.pay.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.pay.bean.ChargeEntity;
import game.module.pay.bean.DbPaymentLevels;
import game.module.pay.bean.DbVipPackGet;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChargeDao {

	private static Logger logger = LoggerFactory.getLogger(ChargeDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static ChargeDao instance = new ChargeDao();
	}

	public static ChargeDao getInstance() {
		return SingletonHolder.instance;
	}

	private ResultSetHandler<ChargeEntity> oneChargeEntityHandler = new ResultSetHandler<ChargeEntity>() {
		@Override
		public ChargeEntity handle(ResultSet rs) throws SQLException {
			ChargeEntity be = null;
			if (rs.next()) {
				be = new ChargeEntity();
				be.setId(rs.getInt("id"));
				be.setPlayerId(rs.getInt("player_id"));
				be.setFirstPayTime(rs.getTimestamp("first_pay_time"));
				be.setCzjj(rs.getBoolean("czjj"));
				be.setGzYuekaEndTime(rs.getTimestamp("gz_yueka_end_time"));
				be.setZzYuekaEndTime(rs.getTimestamp("zz_yueka_end_time"));
				be.setBwEndTime(rs.getTimestamp("bw_end_time"));
				be.setQzEndTime(rs.getTimestamp("qz_end_time"));
				be.setJtEndTime(rs.getTimestamp("jt_end_time"));
				be.setYdEndTime(rs.getTimestamp("yd_end_time"));
				be.setZxns(rs.getBoolean("zxns"));
				byte[] is = rs.getBytes("vip_pack");
				if (is != null) {
					DbVipPackGet dbItemAttrEx = ProtostuffUtil.deserialize(is, DbVipPackGet.class);
					be.setDbVipPackGet(dbItemAttrEx);
				}

				byte[] is2 = rs.getBytes("payment_pack");
				if (is2 != null) {
					DbPaymentLevels dbPaymentLevels = ProtostuffUtil.deserialize(is2, DbPaymentLevels.class);
					be.setDbPaymentLevels(dbPaymentLevels);
				}
			}
			return be;
		}
	};

	public boolean addChargeEntity(ChargeEntity itemSingleEntity) {
		int addRet = 0;
		Connection con = null;
		QueryRunner queryRunner = new QueryRunner(dataSource);
		try {
			DbVipPackGet dbItemAttrEx = itemSingleEntity.getDbVipPackGet();
			byte[] normalPack = ProtostuffUtil.serialize(dbItemAttrEx);

			DbPaymentLevels dbPaymentLevels = itemSingleEntity.getDbPaymentLevels();
			byte[] dbPaymentLevelsBlob = ProtostuffUtil.serialize(dbPaymentLevels);

			con = dataSource.getConnection();
			addRet = queryRunner.update(con, "insert into charge"
					+ "(player_id,first_pay_time,vip_pack,payment_pack,gz_yueka_end_time,zz_yueka_end_time,czjj,bw_end_time,qz_end_time,jt_end_time," +
							"yd_end_time,zxns) values(?,?,?,?,?,?,?,?,?,?,?,?)", itemSingleEntity.getPlayerId(), itemSingleEntity.getFirstPayTime(),
					normalPack, dbPaymentLevelsBlob, itemSingleEntity.getGzYuekaEndTime(), itemSingleEntity.getZzYuekaEndTime(),itemSingleEntity.getCzjj(),
					itemSingleEntity.getBwEndTime(),itemSingleEntity.getQzEndTime(),itemSingleEntity.getJtEndTime(),
					itemSingleEntity.getYdEndTime(),itemSingleEntity.getZxns());
			// get id
			if (addRet > 0) {
				int theId = queryRunner.query(con, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				itemSingleEntity.setId(theId);
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
		return addRet > 0;
	}

	public ChargeEntity getChargeEntityByPlayerId(int playerId) {
		ChargeEntity ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from charge where player_id = ?", oneChargeEntityHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public void updateCharge(ChargeEntity itemSingleEntity) {
		DbVipPackGet dbItemAttrEx = itemSingleEntity.getDbVipPackGet();
		byte[] normalPack = ProtostuffUtil.serialize(dbItemAttrEx);
		DbPaymentLevels dbPaymentLevels = itemSingleEntity.getDbPaymentLevels();
		byte[] dbPaymentLevelsBlob = ProtostuffUtil.serialize(dbPaymentLevels);
		QueryRunner queryRunner = new QueryRunner(dataSource);
		try {
			queryRunner.update(
					"update charge set first_pay_time=?,vip_pack=?,payment_pack=?,gz_yueka_end_time=?,zz_yueka_end_time=?,czjj=?,bw_end_time=?,qz_end_time=?," +
							" jt_end_time=?,yd_end_time=?,zxns=? where id = ?",
					itemSingleEntity.getFirstPayTime(), normalPack, dbPaymentLevelsBlob, itemSingleEntity.getGzYuekaEndTime(),
					itemSingleEntity.getZzYuekaEndTime(), itemSingleEntity.getCzjj(),itemSingleEntity.getBwEndTime(),
					itemSingleEntity.getQzEndTime(),itemSingleEntity.getJtEndTime(),itemSingleEntity.getYdEndTime(),itemSingleEntity.getZxns(),
					itemSingleEntity.getId());
		} catch (SQLException e1) {
			logger.error("", e1);
		}
	}

	public boolean isFirstPay(int playerId) {
		boolean ret = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = !runner.query("select first_pay from charge where player_id = ?", DaoCommonHandler.Booleanhandler,
					playerId);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	public List<ChargeEntity> getAllYuekaNianka() {
		List<ChargeEntity> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT * from charge where yueka_end_time is not NULL or long_yueka = 1",
					new ResultSetHandler<List<ChargeEntity>>() {
						@Override
						public List<ChargeEntity> handle(ResultSet rs) throws SQLException {
							List<ChargeEntity> ret = new ArrayList<>();
							while (rs.next()) {
								ChargeEntity be = new ChargeEntity();
								be.setId(rs.getInt("id"));
								be.setPlayerId(rs.getInt("player_id"));
								be.setGzYuekaEndTime(rs.getTimestamp("gz_yueka_end_time"));
								be.setZzYuekaEndTime(rs.getTimestamp("zz_yueka_end_time"));
								ret.add(be);
							}
							return ret;
						}

					});
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

}
