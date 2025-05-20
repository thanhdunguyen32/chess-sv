package game.module.chapter.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.chapter.bean.DbBattleset;
import game.module.chapter.bean.PowerFormation;
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

public class PowerFormationDao {

    private static Logger logger = LoggerFactory.getLogger(PowerFormationDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static PowerFormationDao instance = new PowerFormationDao();
    }

    public static PowerFormationDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<List<PowerFormation>> multiPowerFormationHanlder = rs -> {
        List<PowerFormation> retlist = new ArrayList<>();
        while (rs.next()) {
            PowerFormation PowerFormation = new PowerFormation();
            PowerFormation.setId(rs.getInt("id"));
            PowerFormation.setPlayerId(rs.getInt("player_id"));
            PowerFormation.setName(rs.getString("name"));
            PowerFormation.setLevel(rs.getInt("level"));
            PowerFormation.setIconId(rs.getInt("iconid"));
            PowerFormation.setHeadId(rs.getInt("headid"));
            PowerFormation.setFrameId(rs.getInt("frameid"));
            PowerFormation.setPower(rs.getInt("power"));
            DbBattleset dbBattleset = ProtostuffUtil.deserialize(rs.getBytes("battleset_blob"), DbBattleset.class);
            PowerFormation.setDbBattleset(dbBattleset);
            retlist.add(PowerFormation);
        }
        return retlist;
    };

    /**
     * @return
     */
    public List<PowerFormation> getPowerFormation() {
        List<PowerFormation> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from power_formation", multiPowerFormationHanlder);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param powerFormation
     * @return
     */
    public boolean addPowerFormation(PowerFormation powerFormation) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        DbBattleset dbBattleset = powerFormation.getDbBattleset();
        byte[] dbBattleset_s = ProtostuffUtil.serialize(dbBattleset);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into power_formation(player_id,name,level,iconid,headid,frameid,power,battleset_blob) values(?,?,?,?,?,?,?,?)",
                    powerFormation.getPlayerId(), powerFormation.getName(), powerFormation.getLevel(), powerFormation.getIconId(), powerFormation.getHeadId(),
                    powerFormation.getFrameId(), powerFormation.getPower(), dbBattleset_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                powerFormation.setId(theId);
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
     * @param powerFormation
     * @return
     */
    public void updatePowerFormation(PowerFormation powerFormation) {
        QueryRunner runner = new QueryRunner(dataSource);

        DbBattleset dbBattleset = powerFormation.getDbBattleset();
        byte[] dbBattleset_s = ProtostuffUtil.serialize(dbBattleset);

        try {
            runner.update(
                    "update power_formation set player_id=?,name=?,level=?,iconid=?,headid=?,frameid=?,power=?,battleset_blob=? where id=?",
                    powerFormation.getPlayerId(), powerFormation.getName(), powerFormation.getLevel(), powerFormation.getIconId(),
                    powerFormation.getHeadId(), powerFormation.getFrameId(), powerFormation.getPower(), dbBattleset_s, powerFormation.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removePowerFormation(int PowerFormationId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from power_formation where id=?", PowerFormationId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
