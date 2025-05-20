package game.module.secret.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageSecret.DBSecretBoxAward;
import db.proto.ProtoMessageSecret.DBSecretUsedHero;
import game.db.DataSourceManager;
import game.module.secret.bean.Secret;

/**
 * 秘密基地Dao
 * 
 * @author zhangning
 * 
 * @Date 2015年1月26日 下午3:16:27
 */
public class SecretDao {

	private static Logger logger = LoggerFactory.getLogger(SecretDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static SecretDao instance = new SecretDao();
	}

	public static SecretDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 秘密基地
	 */
	private ResultSetHandler<Secret> secretHandler = new ResultSetHandler<Secret>() {
		@Override
		public Secret handle(ResultSet rs) throws SQLException {
			Secret secret = null;
			while (rs.next()) {
				secret = new Secret();
				secret.setId(rs.getInt("Id"));
				secret.setPlayerId(rs.getInt("player_id"));
				secret.setMapId(rs.getInt("map_id"));
				secret.setProgress(rs.getInt("progress"));
				secret.setReviveCount(rs.getInt("revive_count"));
				try {
					InputStream is = rs.getBinaryStream("box_award");
					if(is != null) {
						DBSecretBoxAward boxmAward = DBSecretBoxAward.parseFrom(is);
						secret.setBoxAward(boxmAward);
					}
				} catch (IOException e) {
					logger.error("",e);
				}
				try {
					InputStream is = rs.getBinaryStream("my_cost");
					if(is != null) {
						DBSecretUsedHero myCost = DBSecretUsedHero.parseFrom(is);
						secret.setMyCost(myCost);
					}
				} catch (IOException e) {
					logger.error("",e);
				}
				try {
					InputStream is = rs.getBinaryStream("enemy_cost");
					if(is != null) {
						DBSecretUsedHero enemy_cost = DBSecretUsedHero.parseFrom(is);
						secret.setEnemyCost(enemy_cost);
					}
				} catch (IOException e) {
					logger.error("",e);
				}
				try {
					InputStream is = rs.getBinaryStream("online_formation");
					if(is != null) {
						DBSecretUsedHero online_formation = DBSecretUsedHero.parseFrom(is);
						secret.setFormationHeros(online_formation);
					}
				} catch (IOException e) {
					logger.error("",e);
				}
				secret.setResetTime(rs.getTimestamp("reset_time"));
			}
			return secret;
		}
	};

	/**
	 * 获取玩家秘密基地信息
	 * 
	 * @param playerId
	 * @return
	 */
	public Secret getPlayerSecret(int playerId) {
		Secret ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from secret where player_id = ?", secretHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 添加秘密基地信息
	 * 
	 * @param secret
	 * @return
	 */
	public void addSecret(Secret secret) {
		int addRet = 0;
		Connection con = null;
		try {
			InputStream inputStream1;
			DBSecretBoxAward randomAward = secret.getBoxAward();
			byte[] content1 = new byte[0];
			if (randomAward != null) {
				content1 = randomAward.toByteArray();
			}
			inputStream1 = new ByteArrayInputStream(content1);

			final InputStream inputStream2;
			DBSecretUsedHero formationHeros = secret.getFormationHeros();
			byte[] content2 = new byte[0];
			if (formationHeros != null) {
				content2 = formationHeros.toByteArray();
			}
			inputStream2 = new ByteArrayInputStream(content2);

			final InputStream inputStream3;
			DBSecretUsedHero myCost = secret.getMyCost();
			byte[] content3 = new byte[0];
			if (myCost != null) {
				content3 = myCost.toByteArray();
			}
			inputStream3 = new ByteArrayInputStream(content3);
			
			final InputStream inputStream4;
			DBSecretUsedHero enemyCost = secret.getEnemyCost();
			byte[] content4 = new byte[0];
			if (enemyCost != null) {
				content4 = enemyCost.toByteArray();
			}
			inputStream4 = new ByteArrayInputStream(content4);

			con = dataSource.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("insert into secret(player_id, map_id, progress,revive_count, box_award, my_cost, enemy_cost, reset_time, online_formation) values(?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, secret.getPlayerId());
			pstmt.setInt(2, secret.getMapId());
			pstmt.setInt(3, secret.getProgress());
			pstmt.setInt(4, secret.getReviveCount());
			pstmt.setBlob(5, inputStream1);
			pstmt.setBlob(6, inputStream3);
			pstmt.setBlob(7, inputStream4);
			if (secret.getResetTime() != null) {
				pstmt.setTimestamp(8, new Timestamp(secret.getResetTime().getTime()));
			} else {
				pstmt.setTimestamp(8, null);
			}
			pstmt.setBlob(9, inputStream2);
			addRet = pstmt.executeUpdate();
			pstmt.close();

			if (addRet > 0) {
				PreparedStatement pstmt2 = con.prepareStatement("SELECT LAST_INSERT_ID()");
				ResultSet resultSet = pstmt2.executeQuery();
				if (resultSet.next()) {
					int theId = resultSet.getInt(1);
					secret.setId(theId);
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
	 * 更新随机奖励领取记录
	 * 
	 * @param secret
	 */
	public void updateRandomAward(Secret secret) {
		final InputStream inputStream;
		DBSecretBoxAward randomAward = secret.getBoxAward();
		byte[] content1 = new byte[0];
		if (randomAward != null) {
			content1 = randomAward.toByteArray();
		}
		inputStream = new ByteArrayInputStream(content1);

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement("update secret set box_award = ? where id = ?");
			pstmt.setBlob(1, inputStream);
			pstmt.setInt(2, secret.getId());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	/**
	 * 更新阵型中的英雄集合
	 * 
	 * @param secret
	 */
	public void updateSecretHero(Secret secret) {
		final InputStream inputStream;
		DBSecretUsedHero formationHeros = secret.getFormationHeros();
		byte[] content2 = new byte[0];
		if (formationHeros != null) {
			content2 = formationHeros.toByteArray();
		}
		inputStream = new ByteArrayInputStream(content2);

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement("update secret set online_formation = ? where id = ?");
			pstmt.setBlob(1, inputStream);
			pstmt.setInt(2, secret.getId());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	/**
	 * 更新已经使用过的英雄集合
	 * 
	 * @param secret
	 */
	public void updateUsedHero(Secret secret) {
		final InputStream inputStream;
		DBSecretUsedHero myCost = secret.getMyCost();
		byte[] content3 = new byte[0];
		if (myCost != null) {
			content3 = myCost.toByteArray();
		}
		inputStream = new ByteArrayInputStream(content3);

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement("update secret set my_cost = ?,revive_count=? where id = ?");
			pstmt.setBlob(1, inputStream);
			pstmt.setInt(2, secret.getReviveCount());
			pstmt.setInt(3, secret.getId());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	/**
	 * 更新秘密基地所有信息
	 * 
	 * @param secret
	 */
	public void updateSecretAll(Secret secret) {
		InputStream inputStream1;
		DBSecretBoxAward randomAward = secret.getBoxAward();
		byte[] content1 = new byte[0];
		if (randomAward != null) {
			content1 = randomAward.toByteArray();
		}
		inputStream1 = new ByteArrayInputStream(content1);

		final InputStream inputStream2;
		DBSecretUsedHero formationHeros = secret.getFormationHeros();
		byte[] content2 = new byte[0];
		if (formationHeros != null) {
			content2 = formationHeros.toByteArray();
		}
		inputStream2 = new ByteArrayInputStream(content2);

		final InputStream inputStream3;
		DBSecretUsedHero myCost = secret.getMyCost();
		byte[] content3 = new byte[0];
		if (myCost != null) {
			content3 = myCost.toByteArray();
		}
		inputStream3 = new ByteArrayInputStream(content3);
		
		final InputStream inputStream4;
		DBSecretUsedHero enemyCost = secret.getEnemyCost();
		byte[] content4 = new byte[0];
		if (enemyCost != null) {
			content4 = enemyCost.toByteArray();
		}
		inputStream4 = new ByteArrayInputStream(content4);

		Connection con = null;
		try {
			con = dataSource.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("update secret set map_id=?, progress=?,revive_count=?, reset_time=?, box_award = ?, online_formation=?, my_cost = ?, enemy_cost = ? where id = ?");
			pstmt.setInt(1, secret.getMapId());
			pstmt.setInt(2, secret.getProgress());
			pstmt.setInt(3, secret.getReviveCount());
			pstmt.setTimestamp(4, new Timestamp(secret.getResetTime().getTime()));
			pstmt.setBlob(5, inputStream1);
			pstmt.setBlob(6, inputStream2);
			pstmt.setBlob(7, inputStream3);
			pstmt.setBlob(8, inputStream4);
			pstmt.setInt(9, secret.getId());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

}
