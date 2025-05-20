package game.module.manor.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.manor.bean.ManorBean;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ManorDao {

    private static Logger logger = LoggerFactory.getLogger(ManorDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static ManorDao instance = new ManorDao();
    }

    public static ManorDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<ManorBean> multiManorHanlder = rs -> {
        ManorBean manorBean = null;
        if (rs.next()) {
            manorBean = new ManorBean();
            manorBean.setId(rs.getInt("id"));
            manorBean.setPlayerId(rs.getInt("player_id"));
            manorBean.setLevel(rs.getInt("level"));
            manorBean.setGainFoodTime(rs.getTimestamp("gain_food_time"));
            ManorBean.DbManorBuilding manorBuilding = ProtostuffUtil.deserialize(rs.getBytes("buildings"), ManorBean.DbManorBuilding.class);
            manorBean.setManorBuilding(manorBuilding);
            ManorBean.DbManorField manorField = ProtostuffUtil.deserialize(rs.getBytes("field"), ManorBean.DbManorField.class);
            manorBean.setManorField(manorField);
        }
        return manorBean;
    };

    /**
     * @param playerId
     * @return
     */
    public ManorBean getManor(int playerId) {
        ManorBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from manor where player_id = ?",
                    multiManorHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param manorBean
     * @return
     */
    public boolean addManorBean(ManorBean manorBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        ManorBean.DbManorBuilding dbManorBuilding = manorBean.getManorBuilding();
        byte[] dbManorBuildingBytes = ProtostuffUtil.serialize(dbManorBuilding);

        ManorBean.DbManorField manorField = manorBean.getManorField();
        byte[] manorFieldBytes = ProtostuffUtil.serialize(manorField);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into manor(player_id,level,buildings,gain_food_time,field) values(?,?,?,?,?)",
                    manorBean.getPlayerId(), manorBean.getLevel(), dbManorBuildingBytes, manorBean.getGainFoodTime(), manorFieldBytes);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                manorBean.setId(theId);
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
     * @param manorBean
     * @return
     */
    public void updateManor(ManorBean manorBean,byte[] buildingBlob,byte[] fieldBlob) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(
                    "update manor set level=?,buildings=?,gain_food_time=?,field=? where id=?", manorBean.getLevel(), buildingBlob,
                    manorBean.getGainFoodTime(), fieldBlob, manorBean.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeManor(int manorBeanId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from manor where id=?", manorBeanId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
