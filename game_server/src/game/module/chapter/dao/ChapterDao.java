package game.module.chapter.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.chapter.bean.ChapterBean;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ChapterDao {

    private static Logger logger = LoggerFactory.getLogger(ChapterDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static ChapterDao instance = new ChapterDao();
    }

    public static ChapterDao getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务--进度数据
     */
    private ResultSetHandler<ChapterBean> chapterProHandler = rs -> {
        ChapterBean chapterBean = null;
        if (rs.next()) {
            chapterBean = new ChapterBean();
            chapterBean.setId(rs.getInt("id"));
            chapterBean.setPlayerId(rs.getInt("player_id"));
            chapterBean.setMaxMapId(rs.getInt("max_map_id"));
            chapterBean.setLastGainTime(rs.getTimestamp("last_gain_time"));
        }
        return chapterBean;
    };

    /**
     * 获得玩家的所有任务进度<br/>
     * 排序：已完成任务>未完成任务<br/>
     * 第一优先级相同的情况下按照id从小到大排序
     *
     * @param playerId
     * @return
     */
    public ChapterBean getPlayerChapterBean(int playerId) {
        ChapterBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from chapter where player_id = ?",
                    chapterProHandler, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param chapterBean
     * @return
     */
    public boolean addChapterBean(ChapterBean chapterBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into chapter(player_id, max_map_id,last_gain_time) values(?,?,?)",
                    chapterBean.getPlayerId(), chapterBean.getMaxMapId(), chapterBean.getLastGainTime());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                chapterBean.setId(theId);
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
     * @param chapterBean
     * @return
     */
    public void updateChapterBean(ChapterBean chapterBean) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "update chapter set max_map_id=?, last_gain_time=? where id=?",
                    chapterBean.getMaxMapId(), chapterBean.getLastGainTime(), chapterBean.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     * 删除一条任务进度(任务完成且奖励已领取)
     *
     * @param taskId
     */
    public void removeChapterBean(int taskId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from chapter where id=?", taskId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
