package game.module.worldboss.dao;

import game.db.DataSourceManager;
import game.module.worldboss.bean.WorldBoss;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class WorldBossDao {

    private static Logger logger = LoggerFactory.getLogger(WorldBossDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static WorldBossDao instance = new WorldBossDao();
    }

    public static WorldBossDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<WorldBoss> multiWorldBossHanlder = rs -> {
        WorldBoss worldBoss = null;
        if (rs.next()) {
            worldBoss = new WorldBoss();
            worldBoss.setPlayerLastDamage(SimpleTextConvert.decodeIntLongMap(rs.getString("player_last_damage")));
            worldBoss.setPlayerDamageSum(SimpleTextConvert.decodeIntLongMap(rs.getString("player_damage_sum")));
            worldBoss.setLegionDamageSum(SimpleTextConvert.decodeLongLongMap(rs.getString("legion_damage_sum")));
            worldBoss.setReward(rs.getBoolean("is_reward"));
        }
        return worldBoss;
    };

    /**
     * @return
     */
    public WorldBoss getWorldBoss() {
        WorldBoss ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from world_boss",
                    multiWorldBossHanlder);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param worldBoss
     * @return
     */
    public boolean addWorldBoss(WorldBoss worldBoss) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String player_last_damage_str = SimpleTextConvert.encodeMap(worldBoss.getPlayerLastDamage());
        String player_damage_sum_str = SimpleTextConvert.encodeMap(worldBoss.getPlayerDamageSum());
        String legion_damage_sum_str = SimpleTextConvert.encodeMap(worldBoss.getLegionDamageSum());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into world_boss(player_last_damage,player_damage_sum,legion_damage_sum,is_reward) values(?,?,?,?)",
                    player_last_damage_str, player_damage_sum_str, legion_damage_sum_str, worldBoss.getReward());
            if (ret > 0) {
                addRet = true;
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
     * @param worldBoss
     * @return
     */
    public void updateWorldBoss(WorldBoss worldBoss) {
        QueryRunner runner = new QueryRunner(dataSource);

        String player_last_damage_str = SimpleTextConvert.encodeMap(worldBoss.getPlayerLastDamage());
        String player_damage_sum_str = SimpleTextConvert.encodeMap(worldBoss.getPlayerDamageSum());
        String legion_damage_sum_str = SimpleTextConvert.encodeMap(worldBoss.getLegionDamageSum());
        try {
            runner.update(
                    "update world_boss set player_last_damage=?,player_damage_sum=?,legion_damage_sum=?,is_reward=?", player_last_damage_str,
                    player_damage_sum_str, legion_damage_sum_str, worldBoss.getReward());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeWorldBoss() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from world_boss");
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
