package game.module.hero.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.hero.bean.GeneralExchange;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GeneralExchangeDao {

    private static Logger logger = LoggerFactory.getLogger(GeneralExchangeDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static GeneralExchangeDao instance = new GeneralExchangeDao();
    }

    public static GeneralExchangeDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<GeneralExchange> multiGeneralExchangeHanlder = rs -> {
        GeneralExchange generalExchange = null;
        if (rs.next()) {
            generalExchange = new GeneralExchange();
            generalExchange.setId(rs.getInt("id"));
            generalExchange.setPlayerId(rs.getInt("player_id"));
            generalExchange.setOldGeneralUuid(rs.getLong("old_general_uuid"));
            generalExchange.setNewGeneralTemplateId(rs.getInt("new_general_template_id"));
        }
        return generalExchange;
    };

    /**
     * @param playerId
     * @return
     */
    public GeneralExchange getGeneralExchange(int playerId) {
        GeneralExchange ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from general_exchange where player_id = ?",
                    multiGeneralExchangeHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param generalExchange
     * @return
     */
    public boolean addGeneralExchange(GeneralExchange generalExchange) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into general_exchange(player_id,old_general_uuid,new_general_template_id) values(?,?,?)",
                    generalExchange.getPlayerId(), generalExchange.getOldGeneralUuid(), generalExchange.getNewGeneralTemplateId());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                generalExchange.setId(theId);
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
     * @param generalExchange
     * @return
     */
    public void updateGeneralExchange(GeneralExchange generalExchange) {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update(
                    "update general_exchange set old_general_uuid=?,new_general_template_id=? where id=?", generalExchange.getOldGeneralUuid(),
                    generalExchange.getNewGeneralTemplateId(), generalExchange.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeGeneralExchange(int playerGeneralExchangesId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from general_exchange where id=?", playerGeneralExchangesId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
