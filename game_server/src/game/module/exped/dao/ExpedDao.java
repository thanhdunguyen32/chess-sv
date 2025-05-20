package game.module.exped.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.exped.bean.ExpedBean;
import game.module.exped.bean.ExpedPlayer;
import lion.common.ProtostuffUtil;
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

public class ExpedDao {

    private static Logger logger = LoggerFactory.getLogger(ExpedDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static ExpedDao instance = new ExpedDao();
    }

    public static ExpedDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<ExpedBean> multiExpedHanlder = rs -> {
        ExpedBean expedBean = null;
        if (rs.next()) {
            expedBean = new ExpedBean();
            expedBean.setId(rs.getInt("id"));
            expedBean.setPlayerId(rs.getInt("player_id"));
            Map<Long, Integer> hps = SimpleTextConvert.decodeLongMap(rs.getString("my_hp"));
            expedBean.setMyHp(hps);
            Map<Integer, Integer> enemy_hp = SimpleTextConvert.decodeIntMap(rs.getString("enemy_hp"));
            expedBean.setEnemyHp(enemy_hp);
            List<Integer> wishs = SimpleTextConvert.decodeIntList(rs.getString("wish"));
            expedBean.setWishCount(wishs);
            ExpedPlayer expedPlayer = ProtostuffUtil.deserialize(rs.getBytes("checkpoint_enemy"), ExpedPlayer.class);
            expedBean.setCheckpointEnemy(expedPlayer);
            expedBean.setLastResetTime(rs.getTimestamp("last_reset_time"));
        }
        return expedBean;
    };

    /**
     * @param playerId
     * @return
     */
    public ExpedBean getExped(int playerId) {
        ExpedBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from exped where player_id = ?",
                    multiExpedHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param expedBean
     * @return
     */
    public boolean addExpedBean(ExpedBean expedBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        ExpedPlayer checkpointEnemy = expedBean.getCheckpointEnemy();
        byte[] checkpointEnemy_b = ProtostuffUtil.serialize(checkpointEnemy);

        String hps = SimpleTextConvert.encodeMap(expedBean.getMyHp());
        String enemy_hp = SimpleTextConvert.encodeMap(expedBean.getEnemyHp());
        String wish_str = SimpleTextConvert.encodeCollection(expedBean.getWishCount());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into exped(player_id,my_hp,enemy_hp,wish,checkpoint_enemy,last_reset_time) values" +
                            "(?,?,?,?,?,?)",
                    expedBean.getPlayerId(), hps,enemy_hp, wish_str, checkpointEnemy_b, expedBean.getLastResetTime());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                expedBean.setId(theId);
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
     * @param expedBean
     * @return
     */
    public void updateExped(ExpedBean expedBean) {
        QueryRunner runner = new QueryRunner(dataSource);

        ExpedPlayer checkpointEnemy = expedBean.getCheckpointEnemy();
        byte[] checkpointEnemy_b = ProtostuffUtil.serialize(checkpointEnemy);

        String hps = SimpleTextConvert.encodeMap(expedBean.getMyHp());
        String enemy_hp = SimpleTextConvert.encodeMap(expedBean.getEnemyHp());
        String wish_str = SimpleTextConvert.encodeCollection(expedBean.getWishCount());

        try {
            runner.update(
                    "update exped set my_hp=?,enemy_hp=?,wish=?,checkpoint_enemy=?,last_reset_time=? where id=?",
                    hps, enemy_hp, wish_str, checkpointEnemy_b, expedBean.getLastResetTime(), expedBean.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeExped(int expedBeanId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from exped where id=?", expedBeanId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
