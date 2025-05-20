package game.module.activity.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.activity.bean.ActCxry;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ActCxryDao {

    private static Logger logger = LoggerFactory.getLogger(ActCxryDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static ActCxryDao instance = new ActCxryDao();
    }

    public static ActCxryDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<ActCxry> multiActCxryHanlder = rs -> {
        ActCxry actCxry = null;
        if (rs.next()) {
            actCxry = new ActCxry();
            actCxry.setId(rs.getInt("id"));
            actCxry.setPlayerId(rs.getInt("player_id"));
            actCxry.setNum(rs.getInt("num"));
            actCxry.setWishGenerals(SimpleTextConvert.decodeIntMap(rs.getString("wish_generals")));
        }
        return actCxry;
    };

    /**
     * @param playerId
     * @return
     */
    public ActCxry getActCxry(int playerId) {
        ActCxry ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from act_cxry where player_id = ?",
                    multiActCxryHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param actCxry
     * @return
     */
    public boolean addActCxry(ActCxry actCxry) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String wishGenerals_s = SimpleTextConvert.encodeMap(actCxry.getWishGenerals());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into act_cxry(player_id,num,wish_generals) values(?,?,?)",
                    actCxry.getPlayerId(), actCxry.getNum(), wishGenerals_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                actCxry.setId(theId);
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
     * @param actCxry
     * @return
     */
    public void updateActCxry(ActCxry actCxry) {
        QueryRunner runner = new QueryRunner(dataSource);

        String wishGenerals_s = SimpleTextConvert.encodeMap(actCxry.getWishGenerals());
        try {
            runner.update(
                    "update act_cxry set num=?,wish_generals=? where id=?", actCxry.getNum(), wishGenerals_s, actCxry.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeActCxry(int actCxrysId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from act_cxry where id=?", actCxrysId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void truncateCxry() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("truncate act_cxry");
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
