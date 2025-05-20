package game.module.user.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.rank.logic.RankManager;
import game.module.user.bean.PlayerProp;
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

public class PlayerServerPropDao {

    private static Logger logger = LoggerFactory.getLogger(PlayerServerPropDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static PlayerServerPropDao instance = new PlayerServerPropDao();
    }

    public static PlayerServerPropDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<List<PlayerProp>> multiPlayerOtherHanlder = rs -> {
        List<PlayerProp> playerProps = new ArrayList<>();
        while (rs.next()) {
            PlayerProp playerProp = new PlayerProp();
            playerProp.setId(rs.getInt("id"));
            playerProp.setPlayerId(rs.getInt("player_id"));
            playerProp.setGsid(rs.getInt("gsid"));
            playerProp.setCount(rs.getInt("count"));
            playerProps.add(playerProp);
        }
        return playerProps;
    };

    /**
     * @param playerId
     * @return
     */
    public List<PlayerProp> getPlayerServerProps(int playerId) {
        List<PlayerProp> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from player_server_prop where player_id = ?",
                    multiPlayerOtherHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param playerProp
     * @return
     */
    public boolean addPlayerServerProp(PlayerProp playerProp) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into player_server_prop(player_id,gsid,count) values(?,?,?)",
                    playerProp.getPlayerId(), playerProp.getGsid(), playerProp.getCount());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                playerProp.setId(theId);
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
     * @param playerProp
     * @return
     */
    public void updatePlayerServerProp(PlayerProp playerProp) {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update(
                    "update player_server_prop set gsid=?,count=? where id=?", playerProp.getGsid(), playerProp.getCount(), playerProp.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removePlayerServerProp(int playerPlayerOthersId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from player_server_prop where id=?", playerPlayerOthersId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public int resetPlayerServerProp(String gsidStr) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            return runner.update(
                    "update player_server_prop set count=0 where gsid in(" + gsidStr + ")");
        } catch (SQLException e) {
            logger.error("", e);
        }
        return 0;
    }

    public List<PlayerProp> rankTowerLevel(int towerGsid) {
        List<PlayerProp> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from player_server_prop where gsid = ? order by count desc limit ?",
                    multiPlayerOtherHanlder, towerGsid, RankManager.RANK_SIZE);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    public List<Integer> rankTowerLevelMy(int towerGsid) {
        List<Integer> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select player_id from player_server_prop where gsid = ? order by count desc",
                    DaoCommonHandler.IntegerListHandler, towerGsid);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    public int resetCount(String gsidStr) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            return runner.update(
                    "update player_server_prop set count=0 where gsid in(" + gsidStr + ")");
        } catch (SQLException e) {
            logger.error("", e);
        }
        return 0;
    }

}
