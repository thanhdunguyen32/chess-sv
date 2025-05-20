package game.module.activity.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.activity.bean.ActTnqw;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ActTnqwDao {

    private static Logger logger = LoggerFactory.getLogger(ActTnqwDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static ActTnqwDao instance = new ActTnqwDao();
    }

    public static ActTnqwDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<ActTnqw> multiActTnqwHanlder = rs -> {
        ActTnqw actTnqw = null;
        if (rs.next()) {
            actTnqw = new ActTnqw();
            actTnqw.setId(rs.getInt("id"));
            actTnqw.setPlayerId(rs.getInt("player_id"));
            actTnqw.setLastDamage(SimpleTextConvert.decodeIntLongMap(rs.getString("last_damage")));
            actTnqw.setNowHp(SimpleTextConvert.decodeIntLongMap(rs.getString("now_hp")));
        }
        return actTnqw;
    };

    /**
     * @param playerId
     * @return
     */
    public ActTnqw getActTnqw(int playerId) {
        ActTnqw ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from act_tnqw where player_id = ?",
                    multiActTnqwHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param actTnqw
     * @return
     */
    public boolean addActTnqw(ActTnqw actTnqw) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String lastDamage_s = SimpleTextConvert.encodeMap(actTnqw.getLastDamage());
        String nowHp_s = SimpleTextConvert.encodeMap(actTnqw.getNowHp());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into act_tnqw(player_id,last_damage,now_hp) values(?,?,?)",
                    actTnqw.getPlayerId(), lastDamage_s, nowHp_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                actTnqw.setId(theId);
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
     * @param actTnqw
     * @return
     */
    public void updateActTnqw(ActTnqw actTnqw) {
        QueryRunner runner = new QueryRunner(dataSource);

        String lastDamage_s = SimpleTextConvert.encodeMap(actTnqw.getLastDamage());
        String nowHp_s = SimpleTextConvert.encodeMap(actTnqw.getNowHp());

        try {
            runner.update(
                    "update act_tnqw set last_damage=?,now_hp=? where id=?", lastDamage_s, nowHp_s, actTnqw.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeActTnqw(int actTnqwsId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from act_tnqw where id=?", actTnqwsId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void truncateTnqw() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("truncate act_tnqw");
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
