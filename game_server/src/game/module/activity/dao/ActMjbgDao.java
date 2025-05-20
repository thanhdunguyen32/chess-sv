package game.module.activity.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.activity.bean.ActMjbg;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ActMjbgDao {

    private static Logger logger = LoggerFactory.getLogger(ActMjbgDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static ActMjbgDao instance = new ActMjbgDao();
    }

    public static ActMjbgDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<ActMjbg> multiActMjbgHanlder = rs -> {
        ActMjbg actMjbg = null;
        if (rs.next()) {
            actMjbg = new ActMjbg();
            actMjbg.setId(rs.getInt("id"));
            actMjbg.setPlayerId(rs.getInt("player_id"));
            actMjbg.setIndex(rs.getInt("pindex"));
            actMjbg.setBoxOpen(SimpleTextConvert.decodeIntMap(rs.getString("box_open")));
            actMjbg.setRewardOpen(SimpleTextConvert.decodeIntMap(rs.getString("reward_open")));
            actMjbg.setBigRewardIndex(rs.getInt("big_reward_index"));
            actMjbg.setBigRewardGet(rs.getBoolean("big_reward_get"));
        }
        return actMjbg;
    };

    /**
     * @param playerId
     * @return
     */
    public ActMjbg getActMjbg(int playerId) {
        ActMjbg ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from act_mjbg where player_id = ?",
                    multiActMjbgHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param actMjbg
     * @return
     */
    public boolean addActMjbg(ActMjbg actMjbg) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String boxOpen_s = SimpleTextConvert.encodeMap(actMjbg.getBoxOpen());
        String rewardOpen_s = SimpleTextConvert.encodeMap(actMjbg.getRewardOpen());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into act_mjbg(player_id,pindex,box_open,reward_open,big_reward_index,big_reward_get) values(?,?,?,?,?,?)",
                    actMjbg.getPlayerId(), actMjbg.getIndex(), boxOpen_s, rewardOpen_s, actMjbg.getBigRewardIndex(), actMjbg.getBigRewardGet());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                actMjbg.setId(theId);
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
     * @param actMjbg
     * @return
     */
    public void updateActMjbg(ActMjbg actMjbg) {
        QueryRunner runner = new QueryRunner(dataSource);

        String boxOpen_s = SimpleTextConvert.encodeMap(actMjbg.getBoxOpen());
        String rewardOpen_s = SimpleTextConvert.encodeMap(actMjbg.getRewardOpen());

        try {
            runner.update(
                    "update act_mjbg set pindex=?,box_open=?,reward_open=?,big_reward_index=?,big_reward_get=? where id=?", actMjbg.getIndex(), boxOpen_s,
                    rewardOpen_s, actMjbg.getBigRewardIndex(), actMjbg.getBigRewardGet(), actMjbg.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeActMjbg(int actMjbgsId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from act_mjbg where id=?", actMjbgsId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void truncateMjbg() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("truncate act_mjbg");
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
