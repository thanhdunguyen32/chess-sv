package game.module.tower.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.tower.bean.TowerReplay;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TowerReplayDao {

    private static final Logger logger = LoggerFactory.getLogger(TowerReplayDao.class);

    private final DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static TowerReplayDao instance = new TowerReplayDao();
    }

    public static TowerReplayDao getInstance() {
        return SingletonHolder.instance;
    }

    private final ResultSetHandler<List<TowerReplay>> multiTowerReplayHanlder = rs -> {
        List<TowerReplay> heroList = new ArrayList<>();
        while (rs.next()) {
            TowerReplay towerReplay = new TowerReplay();
            towerReplay.setId(rs.getInt("id"));
            towerReplay.setTowerLevel(rs.getInt("tower_level"));
            TowerReplay.DbTowerReplay dbTowerReplay = ProtostuffUtil.deserialize(rs.getBytes("replay"), TowerReplay.DbTowerReplay.class);
            towerReplay.setDbTowerReplay(dbTowerReplay);
            heroList.add(towerReplay);
        }
        return heroList;
    };

    /**
     * @return
     */
    public List<TowerReplay> getTowerReplays() {
        List<TowerReplay> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from tower_replay", multiTowerReplayHanlder);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param towerReplay
     * @return
     */
    public boolean addTowerReplay(TowerReplay towerReplay) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        TowerReplay.DbTowerReplay dbTowerReplayPlayers = towerReplay.getDbTowerReplay();
        byte[] dbTowerReplayPlayers_s = ProtostuffUtil.serialize(dbTowerReplayPlayers);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into tower_replay(tower_level,replay) values" +
                            "(?,?)",
                    towerReplay.getTowerLevel(), dbTowerReplayPlayers_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                towerReplay.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    public void updateTowerReplay(TowerReplay towerReplay) {
        QueryRunner runner = new QueryRunner(dataSource);
        TowerReplay.DbTowerReplay dbTowerReplayPlayers = towerReplay.getDbTowerReplay();
        byte[] dbTowerReplayPlayers_s = ProtostuffUtil.serialize(dbTowerReplayPlayers);
        try {
            runner.update("update tower_replay set tower_level=?,replay=? where id = ?", towerReplay.getTowerLevel(), dbTowerReplayPlayers_s, towerReplay.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeTowerReplay(int towerReplayId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from tower_replay where id=?", towerReplayId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
