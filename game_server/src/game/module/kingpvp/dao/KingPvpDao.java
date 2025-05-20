package game.module.kingpvp.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.kingpvp.bean.KingPvp;
import game.module.rank.logic.RankManager;
import lion.common.SimpleTextConvert;
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

public class KingPvpDao {

    private static Logger logger = LoggerFactory.getLogger(KingPvpDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static KingPvpDao instance = new KingPvpDao();
    }

    public static KingPvpDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<List<KingPvp>> multiKingPvpHanlder = rs -> {
        List<KingPvp> retlist = new ArrayList<>();
        while (rs.next()) {
            KingPvp kingPvp = new KingPvp();
            kingPvp.setId(rs.getInt("id"));
            kingPvp.setPlayerId(rs.getInt("player_id"));
            kingPvp.setStage(rs.getInt("stage"));
            kingPvp.setStar(rs.getInt("star"));
            kingPvp.setHstage(rs.getInt("hstage"));
            kingPvp.setPromotion(SimpleTextConvert.decodeIntList(rs.getString("promotion")));
            kingPvp.setLocate(SimpleTextConvert.decodeIntList(rs.getString("locate")));
            kingPvp.setLastUpdateTime(rs.getTimestamp("last_update_time"));
            retlist.add(kingPvp);
        }
        return retlist;
    };

    /**
     * @return
     */
    public List<KingPvp> getKingPvp() {
        List<KingPvp> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from king_pvp order by stage desc,star desc,last_update_time asc", multiKingPvpHanlder);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param kingPvp
     * @return
     */
    public boolean addKingPvp(KingPvp kingPvp) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String promition_s = SimpleTextConvert.encodeCollection(kingPvp.getPromotion());
        String locate_s = SimpleTextConvert.encodeCollection(kingPvp.getLocate());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into king_pvp(player_id,stage,star,hstage,promotion,locate,last_update_time) values(?,?,?,?,?,?,?)",
                    kingPvp.getPlayerId(), kingPvp.getStage(), kingPvp.getStar(), kingPvp.getHstage(), promition_s, locate_s, kingPvp.getLastUpdateTime());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                kingPvp.setId(theId);
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
     * @param kingPvp
     * @return
     */
    public void updateKingPvp(KingPvp kingPvp) {
        QueryRunner runner = new QueryRunner(dataSource);

        String promition_s = SimpleTextConvert.encodeCollection(kingPvp.getPromotion());
        String locate_s = SimpleTextConvert.encodeCollection(kingPvp.getLocate());
        try {
            runner.update(
                    "update king_pvp set stage=?,star=?,hstage=?,promotion=?,locate=?,last_update_time=? where id=?", kingPvp.getStage(), kingPvp.getStar(),
                    kingPvp.getHstage(), promition_s, locate_s, kingPvp.getLastUpdateTime(), kingPvp.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void updateKingPvpStage(KingPvp kingPvp) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "update king_pvp set stage=? where id=?", kingPvp.getStage(), kingPvp.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeKingPvp(int kingPvpId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from king_pvp where id=?", kingPvpId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public List<KingPvp> randKingPvp(int rankSize) {
        List<KingPvp> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from king_pvp where stage > 0 order by stage desc,star desc limit ?",
                    rs -> {
                        List<KingPvp> retlist = new ArrayList<>();
                        while (rs.next()) {
                            KingPvp kingPvpBean = new KingPvp();
                            kingPvpBean.setId(rs.getInt("id"));
                            kingPvpBean.setPlayerId(rs.getInt("player_id"));
                            kingPvpBean.setStage(rs.getInt("stage"));
                            kingPvpBean.setStar(rs.getInt("star"));
                            retlist.add(kingPvpBean);
                        }
                        return retlist;
                    }, rankSize);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    public List<Integer> rankKingPvpLevelMy() {
        List<Integer> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select player_id from king_pvp order by stage desc,star desc",
                    DaoCommonHandler.IntegerListHandler);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    public void clearAll() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "truncate king_pvp");
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
