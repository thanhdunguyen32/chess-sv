package game.module.spin.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.spin.bean.SpinBean;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SpinDao {

    private static final Logger logger = LoggerFactory.getLogger(SpinDao.class);

    private final DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static SpinDao instance = new SpinDao();
    }

    public static SpinDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<SpinBean> multiSpinBeanHanlder = rs -> {
        SpinBean spinBean = null;
        if (rs.next()) {
            spinBean = new SpinBean();
            spinBean.setId(rs.getInt("id"));
            spinBean.setPlayerId(rs.getInt("player_id"));
            String rewards_normal_str = rs.getString("rewards_normal");
            List<List<Integer>> rewards_normal_lists = SimpleTextConvert.decodeIntListList(rewards_normal_str);
            spinBean.setRewardsNormal(rewards_normal_lists);
            String rewards_advance_str = rs.getString("rewards_advance");
            List<List<Integer>> rewards_advance_lists = SimpleTextConvert.decodeIntListList(rewards_advance_str);
            spinBean.setRewardsAdvance(rewards_advance_lists);
            String score_buy_normal_str = rs.getString("score_buy_normal");
            List<Integer> score_buy_normal_lists = SimpleTextConvert.decodeIntList(score_buy_normal_str);
            spinBean.setScoreBuyNormal(score_buy_normal_lists);
            String score_buy_advance_str = rs.getString("score_buy_advance");
            List<Integer> score_buy_advance_str_lists = SimpleTextConvert.decodeIntList(score_buy_advance_str);
            spinBean.setScoreBuyAdvance(score_buy_advance_str_lists);
            spinBean.setLastRefreshTimeNormal(rs.getTimestamp("last_refersh_time_normal"));
            spinBean.setLastRefreshTimeAdvance(rs.getTimestamp("last_refersh_time_advance"));
        }
        return spinBean;
    };

    /**
     *
     * @param playerId
     * @return
     */
    public SpinBean getSpinBean(int playerId) {
        SpinBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from spin where player_id = ?",
                    multiSpinBeanHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param spinBean
     * @return
     */
    public boolean addSpinBean(SpinBean spinBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        List<List<Integer>> rewardsNormal = spinBean.getRewardsNormal();
        String rewardsNormal_s = SimpleTextConvert.encodeIntListList(rewardsNormal);
        List<List<Integer>> rewardsAdvance = spinBean.getRewardsAdvance();
        String rewardsAdvance_s = SimpleTextConvert.encodeIntListList(rewardsAdvance);
        List<Integer> scoreBuyNormals = spinBean.getScoreBuyNormal();
        String scoreBuyNormal_s = SimpleTextConvert.encodeCollection(scoreBuyNormals);
        List<Integer> scoreBuyAdvances = spinBean.getScoreBuyAdvance();
        String scoreBuyAdvance_s = SimpleTextConvert.encodeCollection(scoreBuyAdvances);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into spin(player_id,rewards_normal,rewards_advance,score_buy_normal,score_buy_advance,last_refersh_time_normal," +
                            "last_refersh_time_advance) values(?,?,?,?,?,?,?)",
                    spinBean.getPlayerId(), rewardsNormal_s, rewardsAdvance_s, scoreBuyNormal_s, scoreBuyAdvance_s, spinBean.getLastRefreshTimeNormal(),
                    spinBean.getLastRefreshTimeAdvance());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                spinBean.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    /**
     *
     * @param spinBean
     */
    public void updateSpinBean(SpinBean spinBean) {
        QueryRunner runner = new QueryRunner(dataSource);

        List<List<Integer>> rewardsNormal = spinBean.getRewardsNormal();
        String rewardsNormal_s = SimpleTextConvert.encodeIntListList(rewardsNormal);
        List<List<Integer>> rewardsAdvance = spinBean.getRewardsAdvance();
        String rewardsAdvance_s = SimpleTextConvert.encodeIntListList(rewardsAdvance);
        List<Integer> scoreBuyNormals = spinBean.getScoreBuyNormal();
        String scoreBuyNormal_s = SimpleTextConvert.encodeCollection(scoreBuyNormals);
        List<Integer> scoreBuyAdvances = spinBean.getScoreBuyAdvance();
        String scoreBuyAdvance_s = SimpleTextConvert.encodeCollection(scoreBuyAdvances);

        try {
            runner.update(
                    "update spin set rewards_normal=?,rewards_advance=?,score_buy_normal=?,score_buy_advance=?,last_refersh_time_normal=?," +
                            "last_refersh_time_advance=? where id=?",
                    rewardsNormal_s, rewardsAdvance_s, scoreBuyNormal_s, scoreBuyAdvance_s, spinBean.getLastRefreshTimeNormal(),
                    spinBean.getLastRefreshTimeAdvance(), spinBean.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeSpinBean(int playerSpinBeansId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from spin where id=?", playerSpinBeansId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
