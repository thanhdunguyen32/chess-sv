package game.module.vip.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.vip.bean.VipCount;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class VipCountDao {

    private static Logger logger = LoggerFactory.getLogger(VipCountDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {

        static VipCountDao instance = new VipCountDao();
    }

    public static VipCountDao getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务--进度数据
     */
    private ResultSetHandler<VipCount> oneVipCountHanlder = rs -> {
        VipCount vipCount = null;
        if (rs.next()) {
            vipCount = new VipCount();
            vipCount.setId(rs.getInt("id"));
            vipCount.setPlayerId(rs.getInt("player_id"));
            vipCount.setActCount(rs.getInt("act_count"));
            vipCount.setLastActTime(rs.getTimestamp("last_act_time"));
        }
        return vipCount;
    };

    /**
     * 获得玩家的所有任务进度<br/>
     * 排序：已完成任务>未完成任务<br/>
     * 第一优先级相同的情况下按照id从小到大排序
     *
     * @param playerId
     * @return
     */
    public VipCount getVipCount(int playerId) {
        VipCount ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from vip_count where player_id = ?",
                    oneVipCountHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param vipCount
     * @return
     */
    public boolean addVipCount(VipCount vipCount) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into vip_count(player_id, act_count, last_act_time) " +
                            "values(?,?,?)",
                    vipCount.getPlayerId(), vipCount.getActCount(), vipCount.getLastActTime());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                vipCount.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    public void updateVipCount(VipCount vipCount) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("update vip_count set act_count=?,last_act_time=? where id=?",
                    vipCount.getActCount(), vipCount.getLastActTime(), vipCount.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     * 删除一条任务进度(任务完成且奖励已领取)
     *
     * @param buildId
     */
    public void removeVipCount(int buildId) {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update("delete from vip_count where id=?", buildId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
