package game.module.user.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.entity.IntPair;
import game.module.lan.bean.LoginSessionBean;
import game.module.offline.bean.PlayerBaseBean;
import game.module.rank.logic.RankManager;
import game.module.user.bean.PlayerBean;
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

public class PlayerDao {

    private static Logger logger = LoggerFactory.getLogger(PlayerDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    private DataSource dataSourceLogin = DataSourceManager.getInstance().getDataSourceLogin();

    static class SingletonHolder {
        static PlayerDao instance = new PlayerDao();
    }

    public static PlayerDao getInstance() {
        return SingletonHolder.instance;
    }

    /**
     *
     */
    private final ResultSetHandler<PlayerBean> aPlayerHandler = rs -> {
        PlayerBean playerBean = null;
        if (rs.next()) {
            playerBean = new PlayerBean();
            playerBean.setId(rs.getInt("id"));
            playerBean.setAccountId(rs.getString("account_id"));
            playerBean.setName(rs.getString("name"));
            playerBean.setSex(rs.getInt("sex"));
            playerBean.setGold(rs.getInt("gold"));
            playerBean.setMoney(rs.getInt("money"));
            playerBean.setLevel(rs.getInt("level"));
            playerBean.setLevelExp(rs.getInt("level_exp"));
            playerBean.setVipLevel(rs.getInt("vip_level"));
            playerBean.setVipExp(rs.getInt("vip_exp"));
            playerBean.setIconid(rs.getInt("icon_id"));
            playerBean.setHeadid(rs.getInt("head_id"));
            playerBean.setFrameid(rs.getInt("frame_id"));
            playerBean.setImageid(rs.getInt("image_id"));
            playerBean.setPower(rs.getInt("power"));
            playerBean.setServerId(rs.getInt("server_id"));
            playerBean.setCreateTime(rs.getTimestamp("create_time"));
            playerBean.setDownlineTime(rs.getTimestamp("downline_time"));
            playerBean.setGuideProgress(rs.getInt("guide_progress"));
        }
        return playerBean;
    };

    private final ResultSetHandler<List<PlayerBean>> multiPlayerHandler = rs -> {
        List<PlayerBean> userBeanList = new ArrayList<PlayerBean>();
        while (rs.next()) {
            PlayerBean playerBean = new PlayerBean();
            playerBean.setId(rs.getInt("id"));
            playerBean.setAccountId(rs.getString("account_id"));
            playerBean.setName(rs.getString("name"));
            playerBean.setSex(rs.getInt("sex"));
            playerBean.setGold(rs.getInt("gold"));
            playerBean.setMoney(rs.getInt("money"));
            playerBean.setLevel(rs.getInt("level"));
            playerBean.setLevelExp(rs.getInt("level_exp"));
            playerBean.setVipLevel(rs.getInt("vip_level"));
            playerBean.setVipExp(rs.getInt("vip_exp"));
            playerBean.setIconid(rs.getInt("icon_id"));
            playerBean.setHeadid(rs.getInt("head_id"));
            playerBean.setFrameid(rs.getInt("frame_id"));
            playerBean.setImageid(rs.getInt("image_id"));
            playerBean.setPower(rs.getInt("power"));
            playerBean.setServerId(rs.getInt("server_id"));
            playerBean.setCreateTime(rs.getTimestamp("create_time"));
            playerBean.setDownlineTime(rs.getTimestamp("downline_time"));
            playerBean.setGuideProgress(rs.getInt("guide_progress"));
            userBeanList.add(playerBean);
        }
        return userBeanList;
    };

    private final ResultSetHandler<List<PlayerBaseBean>> offlinePlayerHandler = rs -> {
        List<PlayerBaseBean> userBeanList = new ArrayList<PlayerBaseBean>();
        while (rs.next()) {
            PlayerBaseBean playerBean = new PlayerBaseBean();
            playerBean.setId(rs.getInt("id"));
            playerBean.setAccountId(rs.getString("account_id"));
            playerBean.setServerId(rs.getInt("server_id"));
            playerBean.setName(rs.getString("name"));
            playerBean.setSex(rs.getInt("sex"));
            playerBean.setLevel(rs.getInt("level"));
            playerBean.setVipLevel(rs.getInt("vip_level"));
            playerBean.setIconid(rs.getInt("icon_id"));
            playerBean.setHeadid(rs.getInt("head_id"));
            playerBean.setFrameid(rs.getInt("frame_id"));
            playerBean.setImageid(rs.getInt("image_id"));
            playerBean.setPower(rs.getInt("power"));
            playerBean.setDownlineTime(rs.getTimestamp("downline_time"));
            userBeanList.add(playerBean);
        }
        return userBeanList;
    };

    public boolean checkExistByUserName(String userName) {
        boolean ret = false;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT EXISTS(select 1 from player where name = ?)", DaoCommonHandler.Booleanhandler, userName);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public boolean checkPlayerExist(String openId, int serverId) {
        boolean ret = false;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT EXISTS(select 1 from player where account_id = ? and server_id = ?)", DaoCommonHandler.Booleanhandler, openId, serverId);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public PlayerBean getPlayerById(int playerId) {
        PlayerBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from player where id = ?", aPlayerHandler, playerId);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public PlayerBean getPlayerByName(String userName) {
        PlayerBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from player where name = ?", aPlayerHandler, userName);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<PlayerBean> getPlayersByPlatfrom(LoginSessionBean lsb) {
        return getPlayerByOpenId(lsb.getOpenId(), lsb.getServerId());
    }

    public int getFeedbackCount(String openId) {
        int ret = 0;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT add_point from topup_feedback where open_id = ? and type = 0 and status = 0",
                    DaoCommonHandler.Integerhandler, openId);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<PlayerBean> getPlayerByOpenId(String openId, int serverId) {
        List<PlayerBean> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from player where account_id = ? and server_id = ?", multiPlayerHandler, openId, serverId);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<PlayerBean> getAllPlayers() {
        List<PlayerBean> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from player", multiPlayerHandler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public void addUser(PlayerBean pb) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into player(account_id,name,sex,gold,money,level,level_exp,vip_level,vip_exp,server_id,icon_id,head_id,frame_id," +
                            "image_id,power,create_time,guide_progress) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)",
                    pb.getAccountId(), pb.getName(), pb.getSex(), pb.getGold(), pb.getMoney(), pb.getLevel(), pb.getLevelExp(),
                    pb.getVipLevel(), pb.getVipExp(), pb.getServerId(), pb.getIconid(), pb.getHeadid(), pb.getFrameid(), pb.getImageid(), pb.getPower(),
                    pb.getGuideProgress());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                pb.setId(theId);
            }
        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public void updateVipLev(int newVipLev, int newVipExp, int playerId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("update player set vip_level = ?, vip_exp = ? where id = ?", newVipLev, newVipExp, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public List<PlayerBean> getUserByUids(String uidAllStr) {
        List<PlayerBean> ret = null;

        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from user where id in (?)", multiPlayerHandler, uidAllStr);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public boolean updatePlayerBean(PlayerBean playerBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "update player set name=?,sex=?,gold=?,money=?,level=?,level_exp=?,vip_level=?,vip_exp=?,server_id=?,icon_id=?," +
                            "head_id=?,frame_id=?,image_id=?,power=?,downline_time=?,upgrade_time=?,guide_progress=? where id = ?",
                    playerBean.getName(), playerBean.getSex(), playerBean.getGold(), playerBean.getMoney(), playerBean.getLevel(),
                    playerBean.getLevelExp(), playerBean.getVipLevel(), playerBean.getVipExp(), playerBean.getServerId(), playerBean.getIconid(),
                    playerBean.getHeadid(), playerBean.getFrameid(), playerBean.getImageid(), playerBean.getPower(), playerBean.getDownlineTime(),
                    playerBean.getUpgradeTime(), playerBean.getGuideProgress(), playerBean.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
        return addRet;
    }

    public List<PlayerBaseBean> getPlayersForOffline() {
        List<PlayerBaseBean> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from player order by level desc limit 2000", offlinePlayerHandler);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    public List<PlayerBean> rankPlayerLevel() {
        List<PlayerBean> ret = null;

        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from player order by level desc,upgrade_time desc limit ?", multiPlayerHandler,
                    RankManager.RANK_SIZE);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<Integer> rankPlayerLevelMy() {
        List<Integer> ret = null;

        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT id from player order by level desc,upgrade_time desc",
                    DaoCommonHandler.IntegerListHandler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<PlayerBean> rankPlayerPower() {
        List<PlayerBean> ret = null;

        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from player order by power desc limit ?", multiPlayerHandler,
                    RankManager.RANK_SIZE);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<Integer> rankPlayerPowerMy() {
        List<Integer> ret = null;

        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT id from player order by power desc",
                    DaoCommonHandler.IntegerListHandler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public void addMoney(int addDiamond, String openId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("update player set money = money + ? where account_id = ?", addDiamond, openId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public List<PlayerBean> searchPlayerByName(String playerName) {
        List<PlayerBean> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from player where name like ? order by level desc,create_time desc limit ?",
                    multiPlayerHandler, "%" + playerName + "%", 10);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<IntPair> getPlayerAllVip() {
        List<IntPair> ret = new ArrayList<>();
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT id,vip_level from player", rs -> {
                List<IntPair> userBeanList = new ArrayList<IntPair>();
                while (rs.next()) {
                    IntPair playerBean = new IntPair(rs.getInt("id"), rs.getInt("vip_level"));
                    userBeanList.add(playerBean);
                }
                return userBeanList;
            });
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public int getNewPlayerCount() {
        int ret = 0;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT count(*) from player where date_format(create_time,'%Y-%m-%d') = curdate()", DaoCommonHandler.Integerhandler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<PlayerBean> getPlayerByUsernameAndServerID(String username, int serverID) {
        List<PlayerBean> ret = null;
        int accountId = 0;
        QueryRunner runnerLogin = new QueryRunner(dataSourceLogin);
        try {
            accountId = runnerLogin.query("SELECT id FROM login_account WHERE uname = ?", DaoCommonHandler.Integerhandler, username);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * FROM player where server_id = ? AND account_id = ?", multiPlayerHandler, serverID, accountId);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    /**
     * 昨日留存
     *
     * @return
     */
    public List<Integer> getYesterdayRetain() {
        List<Integer> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query(
                    "select count(distinct(log_login.player_id)) as \"today\",(SELECT count(player.id) from player where date_format(player.create_time," +
                            "'%Y-%m-%d') = date_add(curdate(), interval -1 day)) as \"-1day\" from log_login where date_format(log_login.login_time," +
                            "'%Y-%m-%d') = curdate() and log_login.player_id in (SELECT player.id from player where date_format(player.create_time," +
                            "'%Y-%m-%d') = date_add(curdate(), interval -1 day))",
                    DaoCommonHandler.Integer2Handler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

}
