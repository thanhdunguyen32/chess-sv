package game.module.pvp.dao;

import game.db.DataSourceManager;
import game.module.pvp.bean.PvpBean;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

public class PvpDao {

    private static Logger logger = LoggerFactory.getLogger(PvpDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static PvpDao instance = new PvpDao();
    }

    public static PvpDao getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务--进度数据
     */
    private ResultSetHandler<PvpBean> oneMissionProHandler = rs -> {
        PvpBean pvpBean = null;
        if (rs.next()) {
            pvpBean = new PvpBean();
            Map<Integer,Integer> pvp_score_vals = SimpleTextConvert.decodeIntMap(rs.getString("pvp_score"));
            pvpBean.setPvpScore(pvp_score_vals);
        }
        return pvpBean;
    };

    /**
     * 获得玩家的所有任务进度<br/>
     * 排序：已完成任务>未完成任务<br/>
     * 第一优先级相同的情况下按照id从小到大排序
     *
     * @return
     */
    public PvpBean getPvpBean() {
        PvpBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from pvp",
                    oneMissionProHandler);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param pvpBean
     * @return
     */
    public boolean addPvpBean(PvpBean pvpBean) {
        boolean addRet = true;
        QueryRunner runner = new QueryRunner(dataSource);

        Map<Integer,Integer> addonVals = pvpBean.getPvpScore();
        String addonVals_str = SimpleTextConvert.encodeMap(addonVals);
        try {
            runner.update(
                    "insert into pvp(pvp_score) values(?)", addonVals_str);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return addRet;
    }

    /**
     * 更新一条任务进度
     *
     * @param pvpBean
     * @return
     */
    public void updatePvpBean(PvpBean pvpBean) {
        Map<Integer,Integer> addonVals = pvpBean.getPvpScore();
        String addonVals_str = SimpleTextConvert.encodeMap(addonVals);
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "update pvp set pvp_score=?", addonVals_str);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     * 删除一条任务进度(任务完成且奖励已领取)
     */
    public void removePvpBean() {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update("delete from pvp");
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
