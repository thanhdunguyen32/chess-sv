package game.module.manor.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.manor.bean.SurrenderPersuade;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SurrenderPersuadeDao {

    private static Logger logger = LoggerFactory.getLogger(SurrenderPersuadeDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static SurrenderPersuadeDao instance = new SurrenderPersuadeDao();
    }

    public static SurrenderPersuadeDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<SurrenderPersuade> multiSurrenderPersuadeHanlder = rs -> {
        SurrenderPersuade surrenderPersuade = null;
        if (rs.next()) {
            surrenderPersuade = new SurrenderPersuade();
            surrenderPersuade.setId(rs.getInt("id"));
            surrenderPersuade.setPlayerId(rs.getInt("player_id"));
            surrenderPersuade.setSurrendermap(SimpleTextConvert.decodeIntMap(rs.getString("persuade_state")));
        }
        return surrenderPersuade;
    };

    /**
     * @param playerId
     * @return
     */
    public SurrenderPersuade getSurrenderPersuade(int playerId) {
        SurrenderPersuade ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from surrender where player_id = ?",
                    multiSurrenderPersuadeHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param surrenderPersuade
     * @return
     */
    public boolean addSurrenderPersuade(SurrenderPersuade surrenderPersuade) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String s_persuade_state = SimpleTextConvert.encodeMap(surrenderPersuade.getSurrendermap());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into surrender(player_id,persuade_state) values(?,?)",
                    surrenderPersuade.getPlayerId(), s_persuade_state);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                surrenderPersuade.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    /**
     * 更新一条任务进度
     *
     * @param surrenderPersuade
     * @return
     */
    public void updateSurrenderPersuade(SurrenderPersuade surrenderPersuade) {
        QueryRunner runner = new QueryRunner(dataSource);

        String s_persuade_state = SimpleTextConvert.encodeMap(surrenderPersuade.getSurrendermap());
        try {
            runner.update(
                    "update surrender set persuade_state=? where id=?", s_persuade_state, surrenderPersuade.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeSurrenderPersuade(int surrenderPersuadeId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from surrender where id=?", surrenderPersuadeId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
