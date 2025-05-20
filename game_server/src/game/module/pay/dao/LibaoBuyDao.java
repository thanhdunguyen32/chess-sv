package game.module.pay.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.pay.bean.LibaoBuy;
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

public class LibaoBuyDao {

    private static Logger logger = LoggerFactory.getLogger(LibaoBuyDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static LibaoBuyDao instance = new LibaoBuyDao();
    }

    public static LibaoBuyDao getInstance() {
        return SingletonHolder.instance;
    }

    private final ResultSetHandler<LibaoBuy> oneLibaoBuyHanlder = rs -> {
        LibaoBuy libaoBuy = null;
        if (rs.next()) {
            libaoBuy = new LibaoBuy();
            libaoBuy.setId(rs.getInt("id"));
            libaoBuy.setPlayerId(rs.getInt("player_id"));
            libaoBuy.setBuyCount(SimpleTextConvert.decodeStrIntMap(rs.getString("buy_count")));
        }
        return libaoBuy;
    };

    private final ResultSetHandler<List<LibaoBuy>> multiLibaoBuyHanlder = rs -> {
        List<LibaoBuy> libaoBuys = new ArrayList<>();
        while (rs.next()) {
            LibaoBuy libaoBuy = new LibaoBuy();
            libaoBuy.setId(rs.getInt("id"));
            libaoBuy.setPlayerId(rs.getInt("player_id"));
            libaoBuy.setBuyCount(SimpleTextConvert.decodeStrIntMap(rs.getString("buy_count")));
            libaoBuys.add(libaoBuy);
        }
        return libaoBuys;
    };

    /**
     * @param playerId
     * @return
     */
    public LibaoBuy getLibaoBuy(int playerId) {
        LibaoBuy ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from libao_buy where player_id = ?",
                    oneLibaoBuyHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }


    public List<LibaoBuy> getLibaoBuyAll() {
        List<LibaoBuy> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from libao_buy",
                    multiLibaoBuyHanlder);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param libaoBuy
     * @return
     */
    public boolean addLibaoBuy(LibaoBuy libaoBuy) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String buyCount_s = SimpleTextConvert.encodeMap(libaoBuy.getBuyCount());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into libao_buy(player_id,buy_count) values" +
                            "(?,?)",
                    libaoBuy.getPlayerId(), buyCount_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                libaoBuy.setId(theId);
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
     * @param libaoBuy
     * @return
     */
    public void updateLibaoBuy(LibaoBuy libaoBuy) {
        QueryRunner runner = new QueryRunner(dataSource);

        String buyCount_s = SimpleTextConvert.encodeMap(libaoBuy.getBuyCount());
        try {
            runner.update(
                    "update libao_buy set buy_count=? where id=?", buyCount_s, libaoBuy.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeLibaoBuy(int libaoBuyId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from libao_buy where id=?", libaoBuyId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
