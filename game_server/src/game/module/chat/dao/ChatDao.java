package game.module.chat.dao;

import game.db.DataSourceManager;

import game.module.chat.bean.DbChat;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ChatDao {

	private static Logger logger = LoggerFactory.getLogger(ChatDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static ChatDao instance = new ChatDao();
	}

	public static ChatDao getInstance() {
		return SingletonHolder.instance;
	}

	private ResultSetHandler<DbChat> arenaAllHandler = rs -> {
		DbChat retObj = null;
		if (rs.next()) {
			// record pack
			byte[] is = rs.getBytes("player_chat_visit");
			retObj = ProtostuffUtil.deserialize(is,DbChat.class);
		}
		return retObj;
	};

	public DbChat getDBChatVisit() {
		DbChat ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("SELECT player_chat_visit from system_blob", arenaAllHandler);
		} catch (SQLException e) {
			logger.error("", e);
			return ret;
		}
		return ret;
	}

	private void saveDBArenaPlayers(DbChat chatVisitAll, String sqlStr) {
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

	public void insertDBChatVisit(DbChat chatVisitAll) {
		String sqlStr = "insert into system_blob(player_chat_visit) values(?)";
		saveDBArenaPlayers(chatVisitAll, sqlStr);
	}

	public void updateDBChatVisit(DbChat chatVisitAll) {
		String sqlStr = "update system_blob set player_chat_visit = ?";
		saveDBArenaPlayers(chatVisitAll, sqlStr);
	}
}
