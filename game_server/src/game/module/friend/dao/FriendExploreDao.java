package game.module.friend.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.friend.bean.FriendExplore;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class FriendExploreDao {

    private static Logger logger = LoggerFactory.getLogger(FriendExploreDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static FriendExploreDao instance = new FriendExploreDao();
    }

    public static FriendExploreDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<FriendExplore> multiFriendExploreHanlder = rs -> {
        FriendExplore friendExplore = null;
        if (rs.next()) {
            friendExplore = new FriendExplore();
            friendExplore.setId(rs.getInt("id"));
            friendExplore.setPlayerId(rs.getInt("player_id"));
            FriendExplore.DbFriendChapter dbFriendChapter = ProtostuffUtil.deserialize(rs.getBytes("chapter"), FriendExplore.DbFriendChapter.class);
            friendExplore.setDbFriendChapter(dbFriendChapter);
        }
        return friendExplore;
    };

    /**
     * @param playerId
     * @return
     */
    public FriendExplore getFriendExplore(int playerId) {
        FriendExplore ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from friend_explore where player_id = ?",
                    multiFriendExploreHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param friendExplore
     * @return
     */
    public boolean addFriendExplore(FriendExplore friendExplore) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        FriendExplore.DbFriendChapter dbFriendChapter = friendExplore.getDbFriendChapter();
        byte[] dbFriendChapter_s = ProtostuffUtil.serialize(dbFriendChapter);
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into friend_explore(player_id,chapter) values(?,?)",
                    friendExplore.getPlayerId(), dbFriendChapter_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                friendExplore.setId(theId);
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
     * @param friendExplore
     * @return
     */
    public void updateFriendExplore(FriendExplore friendExplore) {
        QueryRunner runner = new QueryRunner(dataSource);

        FriendExplore.DbFriendChapter dbFriendChapter = friendExplore.getDbFriendChapter();
        byte[] dbFriendChapter_s = ProtostuffUtil.serialize(dbFriendChapter);
        try {
            runner.update(
                    "update friend_explore set chapter=? where id=?", dbFriendChapter_s, friendExplore.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeFriendExplore(int friendExploreId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from friend_explore where id=?", friendExploreId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
