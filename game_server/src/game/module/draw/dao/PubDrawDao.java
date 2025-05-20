package game.module.draw.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.draw.bean.PubDraw;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PubDrawDao {

    private static Logger logger = LoggerFactory.getLogger(PubDrawDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static PubDrawDao instance = new PubDrawDao();
    }

    public static PubDrawDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<PubDraw> multiPubDrawHanlder = rs -> {
        PubDraw pubDraw = null;
        if (rs.next()) {
            pubDraw = new PubDraw();
            pubDraw.setId(rs.getInt("id"));
            pubDraw.setPlayerId(rs.getInt("player_id"));
            pubDraw.setLastNormalTime(rs.getTimestamp("last_normal_time"));
            pubDraw.setLastAdvanceTime(rs.getTimestamp("last_advance_time"));
        }
        return pubDraw;
    };

    /**
     * @param playerId
     * @return
     */
    public PubDraw getPubDraw(int playerId) {
        PubDraw ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from pub_draw where player_id = ?",
                    multiPubDrawHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param pubDraw
     * @return
     */
    public boolean addPubDraw(PubDraw pubDraw) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into pub_draw(player_id,last_normal_time,last_advance_time) values(?,?,?)",
                    pubDraw.getPlayerId(), pubDraw.getLastNormalTime(),pubDraw.getLastAdvanceTime());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                pubDraw.setId(theId);
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
     * @param pubDraw
     * @return
     */
    public void updatePubDraw(PubDraw pubDraw) {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update(
                    "update pub_draw set last_normal_time=?,last_advance_time=? where id=?", pubDraw.getLastNormalTime(),
                    pubDraw.getLastAdvanceTime(), pubDraw.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removePubDraw(int playerPubDrawsId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from pub_draw where id=?", playerPubDrawsId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
