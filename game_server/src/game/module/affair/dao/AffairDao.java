package game.module.affair.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.affair.bean.DbAffairs;
import game.module.affair.bean.PlayerAffairs;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class AffairDao {

    private static Logger logger = LoggerFactory.getLogger(AffairDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static AffairDao instance = new AffairDao();
    }

    public static AffairDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<PlayerAffairs> multiAffairHanlder = rs -> {
        PlayerAffairs playerAffairs = null;
        if (rs.next()) {
            playerAffairs = new PlayerAffairs();
            playerAffairs.setId(rs.getInt("id"));
            playerAffairs.setPlayerId(rs.getInt("player_id"));
            playerAffairs.setLastVisitTime(rs.getTimestamp("last_visit_time"));
            DbAffairs dbAffairs = ProtostuffUtil.deserialize(rs.getBytes("affairs_blob"), DbAffairs.class);
            playerAffairs.setDbAffairs(dbAffairs);
        }
        return playerAffairs;
    };

    /**
     * @param playerId
     * @return
     */
    public PlayerAffairs getAffairs(int playerId) {
        PlayerAffairs ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from affair where player_id = ?",
                    multiAffairHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param playerAffairs
     * @return
     */
    public boolean addPlayerAffairs(PlayerAffairs playerAffairs) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        DbAffairs dbAffairs = playerAffairs.getDbAffairs();
        byte[] affairsBlob = ProtostuffUtil.serialize(dbAffairs);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into affair(player_id,last_visit_time,affairs_blob) values" +
                            "(?,?,?)",
                    playerAffairs.getPlayerId(), playerAffairs.getLastVisitTime(), affairsBlob);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                playerAffairs.setId(theId);
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
     * @param playerAffairs
     * @return
     */
    public void updateAffair(PlayerAffairs playerAffairs) {
        QueryRunner runner = new QueryRunner(dataSource);

        DbAffairs dbAffairs = playerAffairs.getDbAffairs();
        byte[] affairsBlob = ProtostuffUtil.serialize(dbAffairs);

        try {
            runner.update(
                    "update affair set last_visit_time=?,affairs_blob=? where id=?",
                    playerAffairs.getLastVisitTime(), affairsBlob, playerAffairs.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeAffair(int playerAffairsId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from affair where id=?", playerAffairsId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
