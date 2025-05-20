package game.module.friend.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.friend.bean.FriendBoss;
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

public class FriendBossDao {

    private static Logger logger = LoggerFactory.getLogger(FriendBossDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static FriendBossDao instance = new FriendBossDao();
    }

    public static FriendBossDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<List<FriendBoss>> multiFriendBossHanlder = rs -> {
        List<FriendBoss> retlist = new ArrayList<>();
        while (rs.next()) {
            FriendBoss friendBoss = new FriendBoss();
            friendBoss.setId(rs.getInt("id"));
            friendBoss.setPlayerId(rs.getInt("player_id"));
            FriendBoss.DbFriendBoss dbFriendBoss = ProtostuffUtil.deserialize(rs.getBytes("boss"), FriendBoss.DbFriendBoss.class);
            friendBoss.setDbFriendBoss(dbFriendBoss);
            retlist.add(friendBoss);
        }
        return retlist;
    };

    /**
     * @return
     */
    public List<FriendBoss> getFriendBoss() {
        List<FriendBoss> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from friend_boss", multiFriendBossHanlder);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param friendBoss
     * @return
     */
    public boolean addFriendBoss(FriendBoss friendBoss) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        FriendBoss.DbFriendBoss dbFriendBoss = friendBoss.getDbFriendBoss();
        byte[] dbFriendBoss_b = ProtostuffUtil.serialize(dbFriendBoss);
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into friend_boss(player_id,boss) values(?,?)",
                    friendBoss.getPlayerId(), dbFriendBoss_b);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                friendBoss.setId(theId);
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
     * @param friendBoss
     * @return
     */
    public void updateFriendBoss(FriendBoss friendBoss) {
        QueryRunner runner = new QueryRunner(dataSource);

        FriendBoss.DbFriendBoss dbFriendBoss = friendBoss.getDbFriendBoss();
        byte[] dbFriendBoss_b = ProtostuffUtil.serialize(dbFriendBoss);
        try {
            runner.update(
                    "update friend_boss set boss=? where id=?", dbFriendBoss_b, friendBoss.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeFriendBoss(int friendBossId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from friend_boss where id=?", friendBossId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
