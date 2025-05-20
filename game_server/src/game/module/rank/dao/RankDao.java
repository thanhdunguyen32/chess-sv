package game.module.rank.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.rank.bean.*;
import game.util.Global;
import io.protostuff.ProtostuffIOUtil;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RankDao {

	private static Logger logger = LoggerFactory.getLogger(RankDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static RankDao instance = new RankDao();
	}

	public static RankDao getInstance() {
		return SingletonHolder.instance;
	}

	private ResultSetHandler<DBPlayerLevelRank> playerLevelRankAllHandler = rs -> {
		DBPlayerLevelRank dbPlayerLevelRank = new DBPlayerLevelRank();
		if (rs.next()) {
			// record pack
			try {
				InputStream is = rs.getBinaryStream("rank_player_level");
				if (is != null) {
					ProtostuffIOUtil.mergeFrom(is, dbPlayerLevelRank, DBPlayerLevelRank.getSchema());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dbPlayerLevelRank;
	};

	private ResultSetHandler<DBRankHeroStars> heroStarsRankAllHandler = new ResultSetHandler<DBRankHeroStars>() {
		@Override
		public DBRankHeroStars handle(ResultSet rs) throws SQLException {
			DBRankHeroStars dbRankHeroStars = new DBRankHeroStars();
			if (rs.next()) {
				// record pack
				try {
					InputStream is = rs.getBinaryStream("rank_hero_stars");
					if (is != null) {
						ProtostuffIOUtil.mergeFrom(is, dbRankHeroStars, DBRankHeroStars.getSchema());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return dbRankHeroStars;
		}
	};

	private ResultSetHandler<DBRankBattleForce> teamBfRankHandler = new ResultSetHandler<DBRankBattleForce>() {
		@Override
		public DBRankBattleForce handle(ResultSet rs) throws SQLException {
			DBRankBattleForce retObj = new DBRankBattleForce();
			if (rs.next()) {
				// record pack
				DBRankBattleForce dbRankBattleForce = new DBRankBattleForce();
				try {
					InputStream is = rs.getBinaryStream("rank_team_bf");
					if (is != null) {
						ProtostuffIOUtil.mergeFrom(is, dbRankBattleForce, DBRankBattleForce.getSchema());
						retObj = dbRankBattleForce;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return retObj;
		}
	};

	private ResultSetHandler<DbRankTower> towerRankHanlder = rs -> {
		DbRankTower retObj = new DbRankTower();
		if (rs.next()) {
			// record pack
			byte[] is = rs.getBytes("rank_tower");
			if (is != null) {
				retObj = ProtostuffUtil.deserialize(is,DbRankTower.class);
			}
		}
		return retObj;
	};

	private ResultSetHandler<DbRankDungeon> dungeonRankHanlder = rs -> {
		DbRankDungeon retObj = new DbRankDungeon();
		if (rs.next()) {
			// record pack
			byte[] is = rs.getBytes("rank_dungeon");
			if (is != null) {
				retObj = ProtostuffUtil.deserialize(is,DbRankDungeon.class);
			}
		}
		return retObj;
	};

	private ResultSetHandler<DbRankKingPvp> kingPvpRankHanlder = rs -> {
		DbRankKingPvp retObj = new DbRankKingPvp();
		if (rs.next()) {
			// record pack
			byte[] is = rs.getBytes("rank_king_pvp");
			if (is != null) {
				retObj = ProtostuffUtil.deserialize(is,DbRankKingPvp.class);
			}
		}
		return retObj;
	};

	private ResultSetHandler<DbRankArena> arenaRankHanlder = rs -> {
		DbRankArena retObj = new DbRankArena();
		if (rs.next()) {
			// record pack
			byte[] is = rs.getBytes("rank_arena");
			if (is != null) {
				retObj = ProtostuffUtil.deserialize(is,DbRankArena.class);
			}
		}
		return retObj;
	};

	public DBPlayerLevelRank getDBRankPlayerLevel() {
		DBPlayerLevelRank ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT rank_player_level from system_blob", playerLevelRankAllHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDBRankPlayerLevel(DBPlayerLevelRank dbPlayerLevelRank, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffIOUtil.toByteArray(dbPlayerLevelRank, DBPlayerLevelRank.getSchema(),
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

	public boolean isSystemBlobExist() {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			int itemCount = runner.query("SELECT count(*) from system_blob", DaoCommonHandler.Integerhandler);
			if (itemCount > 0) {
				addRet = true;
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return addRet;
	}

	public void insertDBRankPlayerLevel(DBPlayerLevelRank dbPlayerLevelRank) {
		String sqlStr = "insert into system_blob(rank_player_level) values(?)";
		saveDBRankPlayerLevel(dbPlayerLevelRank, sqlStr);
	}

	public void updateDBRankPlayerLevel(DBPlayerLevelRank dbPlayerLevelRank) {
		String sqlStr = "update system_blob set rank_player_level = ?";
		saveDBRankPlayerLevel(dbPlayerLevelRank, sqlStr);
	}


	/*
	 * ------------------------------------------- rank hero stars
	 * -------------------------------------------
	 */
	public DBRankHeroStars getDBRankHeroStars() {
		DBRankHeroStars ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT rank_hero_stars from system_blob", heroStarsRankAllHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDBRankHeroStars(DBRankHeroStars dbRankHeroStars, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffIOUtil.toByteArray(dbRankHeroStars, DBRankHeroStars.getSchema(),
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

	public void insertDBRankHeroStars(DBRankHeroStars dbRankHeroStars) {
		String sqlStr = "insert into system_blob(rank_hero_stars) values(?)";
		saveDBRankHeroStars(dbRankHeroStars, sqlStr);
	}

	public void updateDBRankHeroStars(DBRankHeroStars dbRankHeroStars) {
		String sqlStr = "update system_blob set rank_hero_stars = ?";
		saveDBRankHeroStars(dbRankHeroStars, sqlStr);
	}

	/*
	 * ------------------------------------------- rank team battle force
	 * -------------------------------------------
	 */

	public DBRankBattleForce getDBRankTeamBf() {
		DBRankBattleForce ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT rank_team_bf from system_blob", teamBfRankHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDBRankTeamBf(DBRankBattleForce dbRankBattleForce, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffIOUtil.toByteArray(dbRankBattleForce, DBRankBattleForce.getSchema(),
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

	public void insertDBRankTeamBf(DBRankBattleForce dbRankBattleForce) {
		String sqlStr = "insert into system_blob(rank_team_bf) values(?)";
		saveDBRankTeamBf(dbRankBattleForce, sqlStr);
	}

	public void updateDBRankTeamBf(DBRankBattleForce dbRankBattleForce) {
		String sqlStr = "update system_blob set rank_team_bf = ?";
		saveDBRankTeamBf(dbRankBattleForce, sqlStr);
	}

	/*
	 * ------------------------------------------- rank tower
	 * -------------------------------------------
	 */
	public DbRankTower getDbRankTower() {
		DbRankTower ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT rank_tower from system_blob", towerRankHanlder);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDbRankTower(DbRankTower dbRankBattleForce, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffUtil.serialize(dbRankBattleForce);
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

	public void insertDbRankTower(DbRankTower dbRankBattleForce) {
		String sqlStr = "insert into system_blob(rank_tower) values(?)";
		saveDbRankTower(dbRankBattleForce, sqlStr);
	}

	public void updateDbRankTower(DbRankTower dbRankBattleForce) {
		String sqlStr = "update system_blob set rank_tower = ?";
		saveDbRankTower(dbRankBattleForce, sqlStr);
	}
	/*
	 * ------------------------------------------- rank dungeon
	 * -------------------------------------------
	 */
	public DbRankDungeon getDbRankDungeon() {
		DbRankDungeon ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT rank_dungeon from system_blob", dungeonRankHanlder);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDbRankDungeon(DbRankDungeon dbRankBattleForce, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffUtil.serialize(dbRankBattleForce);
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

	public void insertDbRankDungeon(DbRankDungeon dbRankBattleForce) {
		String sqlStr = "insert into system_blob(rank_dungeon) values(?)";
		saveDbRankDungeon(dbRankBattleForce, sqlStr);
	}

	public void updateDbRankDungeon(DbRankDungeon dbRankBattleForce) {
		String sqlStr = "update system_blob set rank_dungeon = ?";
		saveDbRankDungeon(dbRankBattleForce, sqlStr);
	}
	/*
	 * ------------------------------------------- rank kingPvp
	 * -------------------------------------------
	 */
	public DbRankKingPvp getDbRankKingPvp() {
		DbRankKingPvp ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT rank_king_pvp from system_blob", kingPvpRankHanlder);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDbRankKingPvp(DbRankKingPvp dbRankBattleForce, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffUtil.serialize(dbRankBattleForce);
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

	public void insertDbRankKingPvp(DbRankKingPvp dbRankBattleForce) {
		String sqlStr = "insert into system_blob(rank_king_pvp) values(?)";
		saveDbRankKingPvp(dbRankBattleForce, sqlStr);
	}

	public void updateDbRankKingPvp(DbRankKingPvp dbRankBattleForce) {
		String sqlStr = "update system_blob set rank_king_pvp = ?";
		saveDbRankKingPvp(dbRankBattleForce, sqlStr);
	}

	/*
	 * ------------------------------------------- rank arena score
	 * -------------------------------------------
	 */
	public DbRankArena getDbRankArena() {
		DbRankArena ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT rank_arena from system_blob", arenaRankHanlder);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDbRankArena(DbRankArena dbRankBattleForce, String sqlStr) {
		final InputStream inputStream;
		byte[] content = ProtostuffUtil.serialize(dbRankBattleForce);
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

	public void insertDbRankArena(DbRankArena dbRankBattleForce) {
		String sqlStr = "insert into system_blob(rank_arena) values(?)";
		saveDbRankArena(dbRankBattleForce, sqlStr);
	}

	public void updateDbRankArena(DbRankArena dbRankBattleForce) {
		String sqlStr = "update system_blob set rank_arena = ?";
		saveDbRankArena(dbRankBattleForce, sqlStr);
	}

	/**
	 * 排行榜点赞信息
	 */
	private ResultSetHandler<DbRankLike> arenaAllHandler = rs -> {
		DbRankLike retObj = null;
		if (rs.next()) {
			// record pack
			byte[] is = rs.getBytes("rank_like");
			retObj = ProtostuffUtil.deserialize(is,DbRankLike.class);
		}
		return retObj;
	};

	public DbRankLike getDBRankLike() {
		DbRankLike ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT rank_like from system_blob", arenaAllHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDBArenaPlayers(DbRankLike chatVisitAll, String sqlStr) {
		byte[] rawBytes = ProtostuffUtil.serialize(chatVisitAll);
		Connection con = null;
		try {
			con = dataSource.getConnection();
			QueryRunner queryRunner = new QueryRunner(dataSource);
			queryRunner.update(con, sqlStr, rawBytes);
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}

	public void insertDBRankLike(DbRankLike chatVisitAll) {
		String sqlStr = "insert into system_blob(rank_like) values(?)";
		saveDBArenaPlayers(chatVisitAll, sqlStr);
	}

	public void updateDBRankLike(DbRankLike chatVisitAll) {
		String sqlStr = "update system_blob set rank_like = ?";
		saveDBArenaPlayers(chatVisitAll, sqlStr);
	}

}
