package game.module.user.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.user.bean.GoldBuy;
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

public class GoldBuyDao {

    private static Logger logger = LoggerFactory.getLogger(GoldBuyDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static GoldBuyDao instance = new GoldBuyDao();
    }

    public static GoldBuyDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<GoldBuy> multiGoldBuyHanlder = rs -> {
        GoldBuy goldBuy = null;
        if (rs.next()) {
            goldBuy = new GoldBuy();
            goldBuy.setId(rs.getInt("id"));
            goldBuy.setPlayerId(rs.getInt("player_id"));
            //
            String buy_seconds = rs.getString("buy_seconds");
            List<Long> buySeconds = SimpleTextConvert.decodeLongList(buy_seconds);
            goldBuy.setBuyTime(buySeconds);
        }
        return goldBuy;
    };

    /**
     * @param playerId
     * @return
     */
    public GoldBuy getGoldBuy(int playerId) {
        GoldBuy ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from gold_buy where player_id = ?",
                    multiGoldBuyHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param goldBuy
     * @return
     */
    public boolean addGoldBuy(GoldBuy goldBuy) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        List<Long> buySeconds = goldBuy.getBuyTime();
        String buySecondsStr = SimpleTextConvert.encodeCollection(buySeconds);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into gold_buy(player_id,buy_seconds) values(?,?)",
                    goldBuy.getPlayerId(), buySecondsStr);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                goldBuy.setId(theId);
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
     * @param goldBuy
     * @return
     */
    public void updateGoldBuy(GoldBuy goldBuy) {
        QueryRunner runner = new QueryRunner(dataSource);

        List<Long> buySeconds = goldBuy.getBuyTime();
        String buySecondsStr = SimpleTextConvert.encodeCollection(buySeconds);

        try {
            runner.update(
                    "update gold_buy set buy_seconds=? where id=?", buySecondsStr, goldBuy.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeGoldBuy(int playerGoldBuysId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from gold_buy where id=?", playerGoldBuysId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
