package game.module.user.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
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

public class PlayerOtherDao {

    private static Logger logger = LoggerFactory.getLogger(PlayerOtherDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static PlayerOtherDao instance = new PlayerOtherDao();
    }

    public static PlayerOtherDao getInstance() {
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
    public List<PlayerProp> getPlayerOther(int playerId) {
        List<PlayerProp> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from player_other where player_id = ?",
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
    public boolean addPlayerOther(PlayerProp playerProp) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into player_other(player_id,gsid,count) values(?,?,?)",
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
    public void updatePlayerOther(PlayerProp playerProp) {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update(
                    "update player_other set gsid=?,count=? where id=?", playerProp.getGsid(), playerProp.getCount(), playerProp.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removePlayerOther(int playerPlayerOthersId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from player_other where id=?", playerPlayerOthersId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public int resetOtherCount(String gsidStr) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            return runner.update(
                    "update player_other set count=0 where gsid in(" + gsidStr + ")");
        } catch (SQLException e) {
            logger.error("", e);
        }
        return 0;
    }

}
