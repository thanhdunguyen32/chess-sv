package game.module.season.dao;

import game.db.DataSourceManager;
import game.module.season.bean.BattleSeason;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SeasonDao {

    private static Logger logger = LoggerFactory.getLogger(SeasonDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static SeasonDao instance = new SeasonDao();
    }

    public static SeasonDao getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务--进度数据
     */
    private ResultSetHandler<BattleSeason> oneMissionProHandler = rs -> {
        BattleSeason battleSeason = null;
        if (rs.next()) {
            battleSeason = new BattleSeason();
            battleSeason.setEtime(rs.getTimestamp("etime"));
            battleSeason.setYear(rs.getInt("year"));
            battleSeason.setSeason(rs.getInt("season"));
            battleSeason.setMonthEndTime(rs.getTimestamp("month_etime"));
            //
            List<Integer> addon_vals = SimpleTextConvert.decodeIntList(rs.getString("addon_vals"));
            battleSeason.setAddonVals(addon_vals);
            //
            List<Integer> pos = SimpleTextConvert.decodeIntList(rs.getString("pos"));
            battleSeason.setPos(pos);
        }
        return battleSeason;
    };

    /**
     * 获得玩家的所有任务进度<br/>
     * 排序：已完成任务>未完成任务<br/>
     * 第一优先级相同的情况下按照id从小到大排序
     *
     * @return
     */
    public BattleSeason getBattleSeason() {
        BattleSeason ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from season",
                    oneMissionProHandler);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param battleSeason
     * @return
     */
    public boolean addBattleSeason(BattleSeason battleSeason) {
        boolean addRet = true;
        QueryRunner runner = new QueryRunner(dataSource);

        List<Integer> addonVals = battleSeason.getAddonVals();
        String addonVals_str = SimpleTextConvert.encodeCollection(addonVals);
        List<Integer> pos = battleSeason.getPos();
        String pos_str = SimpleTextConvert.encodeCollection(pos);
        try {
            runner.update(
                    "insert into season(addon_vals, etime, year, season, pos, month_etime) values(?,?,?,?,?,?)",
                    addonVals_str, battleSeason.getEtime(), battleSeason.getYear(), battleSeason.getSeason(), pos_str, battleSeason.getMonthEndTime());
        } catch (SQLException e) {
            logger.error("", e);
        }
        return addRet;
    }

    /**
     * 更新一条任务进度
     *
     * @param battleSeason
     * @return
     */
    public void updateBattleSeason(BattleSeason battleSeason) {
        List<Integer> addonVals = battleSeason.getAddonVals();
        String addonVals_str = SimpleTextConvert.encodeCollection(addonVals);
        List<Integer> pos = battleSeason.getPos();
        String pos_str = SimpleTextConvert.encodeCollection(pos);
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "update season set addon_vals=?,etime=?,year=?,season=?,pos=?", addonVals_str, battleSeason.getEtime(), battleSeason.getYear(),
                    battleSeason.getSeason(), pos_str);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     * 删除一条任务进度(任务完成且奖励已领取)
     */
    public void removeBattleSeason() {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update("delete from season");
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void updateMonthETime(Date monthEndTime) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "update season set month_etime=?", monthEndTime);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
