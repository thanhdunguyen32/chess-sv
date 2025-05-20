package game.module.friend.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendHeartSend;
import game.module.friend.bean.FriendRequest;
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

public class FriendDao {

    private static Logger logger = LoggerFactory.getLogger(FriendDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {

        static FriendDao instance = new FriendDao();
    }
    public static FriendDao getInstance() {
        return FriendDao.SingletonHolder.instance;
    }

    private ResultSetHandler<List<FriendBean>> multiFriendHanlder = rs -> {
        List<FriendBean> heroList = new ArrayList<>();
        while (rs.next()) {
            FriendBean friendBean = new FriendBean();
            friendBean.setId(rs.getInt("id"));
            friendBean.setPlayerId(rs.getInt("player_id"));
            friendBean.setFriendId(rs.getInt("friend_id"));
            heroList.add(friendBean);
        }
        return heroList;
    };

    private ResultSetHandler<List<FriendRequest>> multiFriendRequestHanlder = rs -> {
        List<FriendRequest> heroList = new ArrayList<>();
        while (rs.next()) {
            FriendRequest friendBean = new FriendRequest();
            friendBean.setId(rs.getInt("id"));
            friendBean.setPlayerId(rs.getInt("player_id"));
            friendBean.setRequestPlayerId(rs.getInt("request_player_id"));
            heroList.add(friendBean);
        }
        return heroList;
    };

    public FriendHeartSend getFriendHeartSend() {
        FriendHeartSend ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT friend_heart_send from system_blob", rs -> {
                FriendHeartSend dbPlayerLevelRank = null;
                if (rs.next()) {
                    // record pack
                    byte[] is = rs.getBytes(1);
                    dbPlayerLevelRank = ProtostuffUtil.deserialize(is, FriendHeartSend.class);
                }
                return dbPlayerLevelRank;
            });
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public void updateFriendHeartSend(FriendHeartSend friendHeartSend) {
        byte[] content1 = ProtostuffUtil.serialize(friendHeartSend);
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "update system_blob set friend_heart_send=?", content1);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     *
     * @param playerId
     * @return
     */
    public List<FriendBean> getFriends(int playerId) {
        List<FriendBean> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from friend where player_id = ?",
                    multiFriendHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param friendBean
     * @return
     */
    public boolean addFriendBean(FriendBean friendBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into friend(player_id,friend_id) values" +
                            "(?,?)",
                    friendBean.getPlayerId(), friendBean.getFriendId());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                friendBean.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    public void removeFriend(int friendBeanId){
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from friend where id=?", friendBeanId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeFriend(int playerId, int friendId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from friend where player_id=? and friend_id=?", playerId, friendId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     *
     * @param playerId
     * @return
     */
    public List<FriendRequest> getFriendRequests(int playerId) {
        List<FriendRequest> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from friend_request where player_id = ?",
                    multiFriendRequestHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param friendRequest
     * @return
     */
    public boolean addFriendRequest(FriendRequest friendRequest) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into friend_request(player_id,request_player_id) values" +
                            "(?,?)",
                    friendRequest.getPlayerId(), friendRequest.getRequestPlayerId());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                friendRequest.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    public void removeFriendRequest(int friendRequestId){
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from friend_request where id=?", friendRequestId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }


    public void removeFriendRequest(int playerId, int friendId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from friend_request where player_id=? and request_player_id = ?", playerId, friendId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public boolean checkFriendRequestExist(int playerId, int requestFriendId) {
        boolean ret = false;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT EXISTS(select 1 from friend_request where player_id = ? and request_player_id=?)",
                    DaoCommonHandler.Booleanhandler,playerId,requestFriendId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    public boolean checkFriendExist(int playerId, int friendId) {
        boolean ret = false;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT EXISTS(select 1 from friend where player_id = ? and friend_id=?)",
                    DaoCommonHandler.Booleanhandler,playerId,friendId);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

}
