package game.module.pvp.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.pvp.bean.PvpRecord;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PvpRecordDao {

    private static Logger logger = LoggerFactory.getLogger(PvpRecordDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static PvpRecordDao instance = new PvpRecordDao();
    }

    public static PvpRecordDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<PvpRecord> multiPvpRecordHanlder = rs -> {
        PvpRecord pvpRecord = null;
        if (rs.next()) {
            pvpRecord = new PvpRecord();
            pvpRecord.setId(rs.getInt("id"));
            pvpRecord.setPlayerId(rs.getInt("player_id"));
            //
            PvpRecord.DbPvpRecord dbPvpRecord = ProtostuffUtil.deserialize(rs.getBytes("records"), PvpRecord.DbPvpRecord.class);
            pvpRecord.setDbPvpRecord(dbPvpRecord);
        }
        return pvpRecord;
    };

    /**
     * @param playerId
     * @return
     */
    public PvpRecord getPvpRecord(int playerId) {
        PvpRecord ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from pvp_record where player_id = ?",
                    multiPvpRecordHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param pvpRecord
     * @return
     */
    public boolean addPvpRecord(PvpRecord pvpRecord) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        PvpRecord.DbPvpRecord dbPvpRecord = pvpRecord.getDbPvpRecord();
        byte[] dbPvpRecord_b = ProtostuffUtil.serialize(dbPvpRecord);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into pvp_record(player_id,records) values(?,?)",
                    pvpRecord.getPlayerId(), dbPvpRecord_b);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                pvpRecord.setId(theId);
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
     * @param pvpRecord
     * @return
     */
    public void updatePvpRecord(PvpRecord pvpRecord) {
        QueryRunner runner = new QueryRunner(dataSource);

        PvpRecord.DbPvpRecord dbPvpRecord = pvpRecord.getDbPvpRecord();
        byte[] dbPvpRecord_b = ProtostuffUtil.serialize(dbPvpRecord);

        try {
            runner.update(
                    "update pvp_record set records=? where id=?", dbPvpRecord_b, pvpRecord.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removePvpRecord(int playerPvpRecordsId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from pvp_record where id=?", playerPvpRecordsId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
