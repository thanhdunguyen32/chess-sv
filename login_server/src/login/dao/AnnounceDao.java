package login.dao;

import game.db.DataSourceManager;
import login.bean.AnnounceBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnounceDao {

    private static Logger logger = LoggerFactory.getLogger(AnnounceDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static AnnounceDao instance = new AnnounceDao();
    }

    public static AnnounceDao getInstance() {
        return SingletonHolder.instance;
    }

    public List<AnnounceBean> getServerList() {
        List<AnnounceBean> ret = null;
        ResultSetHandler<List<AnnounceBean>> handler = new ResultSetHandler<List<AnnounceBean>>() {
            @Override
            public List<AnnounceBean> handle(ResultSet rs) throws SQLException {
                List<AnnounceBean> userBeanList = new ArrayList<AnnounceBean>();
                while (rs.next()) {
                    AnnounceBean retBean = new AnnounceBean();
                    retBean.setId(rs.getInt("id"));
                    retBean.setContent(rs.getString("content"));
                    userBeanList.add(retBean);
                }
                return userBeanList;
            }
        };
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from announce order by id asc", handler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public void updateAnnounce(int announceId, String announcementContent) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("update announce set content=? where id = ?", announcementContent,
                    announceId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void addAnnounce(int announceId, String announcementContent) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("insert into announce values(?,?)", announceId, announcementContent);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
