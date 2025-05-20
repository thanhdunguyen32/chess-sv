package game.module.mine.dao;

import game.db.DataSourceManager;
import game.module.mine.bean.DBMine;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MineDao {

    private static Logger logger = LoggerFactory.getLogger(MineDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static MineDao instance = new MineDao();
    }

    public static MineDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<DBMine> luckyRankHandler = new ResultSetHandler<DBMine>() {
        @Override
        public DBMine handle(ResultSet rs) throws SQLException {
            DBMine retEntity = null;
            if (rs.next()) {
                // record pack
                retEntity = ProtostuffUtil.deserialize(rs.getBytes("data_pack"), DBMine.class);
            }
            return retEntity;
        }
    };

    public DBMine getMine() {
        DBMine ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from mine limit 1", luckyRankHandler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public void updateMine(DBMine mineEntity) {
        byte[] content1 = null;
        if (mineEntity != null) {
            content1 = ProtostuffUtil.serialize(mineEntity);
        }
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update("update mine set data_pack=?,update_time=NOW()", content1);
        } catch (SQLException e1) {
            logger.error("", e1);
        }
    }

    public boolean insertMine(DBMine mineEntity) {
        int addRet = 0;
        QueryRunner queryRunner = new QueryRunner(dataSource);
        byte[] content1 = null;
        if (mineEntity != null) {
            content1 = ProtostuffUtil.serialize(mineEntity);
        }
        try {
            addRet = queryRunner.update("insert into mine" + "(data_pack, update_time) values(?,NOW())", content1);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return addRet > 0;
    }

}
