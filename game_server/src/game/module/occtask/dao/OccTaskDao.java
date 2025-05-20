package game.module.occtask.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.occtask.bean.OccTask;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class OccTaskDao {

    private static Logger logger = LoggerFactory.getLogger(OccTaskDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static OccTaskDao instance = new OccTaskDao();
    }

    public static OccTaskDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<OccTask> multiOccTaskHanlder = rs -> {
        OccTask occTask = null;
        if (rs.next()) {
            occTask = new OccTask();
            occTask.setId(rs.getInt("id"));
            occTask.setPlayerId(rs.getInt("player_id"));
            occTask.setIndex(rs.getInt("idx"));
            occTask.setOcctype(rs.getInt("occtype"));
            occTask.setTaskStatus(SimpleTextConvert.decodeIntMap(rs.getString("task_status")));
            occTask.setRewardId(rs.getInt("reward_id"));
        }
        return occTask;
    };

    /**
     * @param playerId
     * @return
     */
    public OccTask getOccTask(int playerId) {
        OccTask ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from occ_task where player_id = ?",
                    multiOccTaskHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param occTask
     * @return
     */
    public boolean addOccTask(OccTask occTask) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String task_status_str = SimpleTextConvert.encodeMap(occTask.getTaskStatus());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into occ_task(player_id,idx,occtype,task_status,reward_id) values(?,?,?,?,?)",
                    occTask.getPlayerId(), occTask.getIndex(), occTask.getOcctype(), task_status_str, occTask.getRewardId());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                occTask.setId(theId);
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
     * @param occTask
     * @return
     */
    public void updateOccTask(OccTask occTask) {
        QueryRunner runner = new QueryRunner(dataSource);

        String task_status_str = SimpleTextConvert.encodeMap(occTask.getTaskStatus());
        try {
            runner.update(
                    "update occ_task set idx=?,occtype=?,task_status=?,reward_id=? where id=?",
                    occTask.getIndex(), occTask.getOcctype(), task_status_str, occTask.getRewardId(), occTask.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeOccTask(int occTaskId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from occ_task where id=?", occTaskId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
