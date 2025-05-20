package game.module.bigbattle.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.bigbattle.bean.MonthBoss;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonthBossDao {

    private static final Logger logger = LoggerFactory.getLogger(MonthBossDao.class);

    private final DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static MonthBossDao instance = new MonthBossDao();
    }

    public static MonthBossDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<MonthBoss> multiMonthBossHanlder = rs -> {
        MonthBoss monthBoss = null;
        if (rs.next()) {
            monthBoss = new MonthBoss();
            monthBoss.setId(rs.getInt("id"));
            monthBoss.setPlayerId(rs.getInt("player_id"));
            String level_index_str = rs.getString("level_index");
            List<Integer> level_indexList = SimpleTextConvert.decodeIntList(level_index_str);
            monthBoss.setLevelIndex(level_indexList);
            String last_damage_str = rs.getString("last_damage");
            List<Long> last_damageList = SimpleTextConvert.decodeLongList(last_damage_str);
            monthBoss.setLastDamage(last_damageList);
            String now_hp_str = rs.getString("now_hp");
            List<Long> now_hpList = SimpleTextConvert.decodeLongList(now_hp_str);
            monthBoss.setNowHp(now_hpList);
            monthBoss.setLastVisitTime(rs.getTimestamp("last_visit_time"));
        }
        return monthBoss;
    };

    /**
     * @param playerId
     * @return
     */
    public MonthBoss getMonthBoss(int playerId) {
        MonthBoss ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from month_boss where player_id = ?",
                    multiMonthBossHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param monthBoss
     * @return
     */
    public boolean addMonthBoss(MonthBoss monthBoss) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        List<Integer> levelIndexList = monthBoss.getLevelIndex();
        String levelIndex_s = SimpleTextConvert.encodeCollection(levelIndexList);
        List<Long> lastDamageList = monthBoss.getLastDamage();
        String lastDamage_s = SimpleTextConvert.encodeCollection(lastDamageList);
        List<Long> nowHpList = monthBoss.getNowHp();
        String nowhp_s = SimpleTextConvert.encodeCollection(nowHpList);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into month_boss(player_id,level_index,last_damage,now_hp,last_visit_time) values(?,?,?,?,NOW())",
                    monthBoss.getPlayerId(), levelIndex_s, lastDamage_s, nowhp_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                monthBoss.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    /**
     * @param monthBoss
     */
    public void updateMonthBoss(MonthBoss monthBoss) {
        QueryRunner runner = new QueryRunner(dataSource);

        List<Integer> levelIndexList = monthBoss.getLevelIndex();
        String levelIndex_s = SimpleTextConvert.encodeCollection(levelIndexList);
        List<Long> lastDamageList = monthBoss.getLastDamage();
        String lastDamage_s = SimpleTextConvert.encodeCollection(lastDamageList);
        List<Long> nowHpList = monthBoss.getNowHp();
        String nowhp_s = SimpleTextConvert.encodeCollection(nowHpList);

        try {
            runner.update(
                    "update month_boss set level_index=?,last_damage=?,now_hp=?,last_visit_time=NOW() where id=?",
                    levelIndex_s, lastDamage_s, nowhp_s, monthBoss.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeMonthBoss(int monthBossId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from month_boss where id=?", monthBossId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
