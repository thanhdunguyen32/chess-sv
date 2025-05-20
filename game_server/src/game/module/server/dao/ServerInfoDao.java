package game.module.server.dao;


import game.module.server.bean.ServerInfoBean;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.activity.bean.ActCxry;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerInfoDao {
    
    private static final Logger logger = LoggerFactory.getLogger(ServerInfoDao.class);
    
    // DataSource
    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();
    
    // Singleton
    private static class SingletonHolder {
        static ServerInfoDao instance = new ServerInfoDao();
    }
    public static ServerInfoDao getInstance() {
        return SingletonHolder.instance;
    }
    
    // ResultSetHandler: 1 record
    private ResultSetHandler<ServerInfoBean> singleServerInfoHandler = rs -> {
        if (rs.next()) {
            ServerInfoBean s = new ServerInfoBean();
            s.setId(rs.getInt("id"));
            s.setName(rs.getString("name"));
            s.setIp(rs.getString("ip"));
            s.setPort(rs.getInt("port"));
            s.setPortSsl(rs.getInt("port_ssl"));
            s.setStatus(rs.getInt("status"));
            s.setLanPort(rs.getInt("lan_port"));
            s.setHttpUrl(rs.getString("http_url"));
            
            // Kiểm tra sự tồn tại của cột "time_open"
            ResultSetMetaData metaData = rs.getMetaData();
            boolean hasTimeOpenColumn = false;
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if ("time_open".equalsIgnoreCase(metaData.getColumnName(i))) {
                    hasTimeOpenColumn = true;
                    break;
                }
            }
            
            // Lấy giá trị "time_open" nếu cột tồn tại
            if (hasTimeOpenColumn) {
                s.setTime_open(rs.getTimestamp("time_open"));
            } else {
                s.setTime_open(null);
            }
            
            return s;
        }
        return null;
    };
    
    private ResultSetHandler<List<ServerInfoBean>> multiServerInfoHandler = rs -> {
        List<ServerInfoBean> list = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        boolean hasTimeOpenColumn = false;
        
        // Kiểm tra sự tồn tại của cột "time_open"
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if ("time_open".equalsIgnoreCase(metaData.getColumnName(i))) {
                hasTimeOpenColumn = true;
                break;
            }
        }
        
        while (rs.next()) {
            ServerInfoBean s = new ServerInfoBean();
            s.setId(rs.getInt("id"));
            s.setName(rs.getString("name"));
            s.setIp(rs.getString("ip"));
            s.setPort(rs.getInt("port"));
            s.setPortSsl(rs.getInt("port_ssl"));
            s.setStatus(rs.getInt("status"));
            s.setLanPort(rs.getInt("lan_port"));
            s.setHttpUrl(rs.getString("http_url"));
            
            // Lấy giá trị "time_open" nếu cột tồn tại
            if (hasTimeOpenColumn) {
                s.setTime_open(rs.getTimestamp("time_open"));
            } else {
                s.setTime_open(null);
            }
            
            list.add(s);
        }
        return list;
    };
    
    
    /**
     * Lấy thông tin 1 server theo id
     */
    public ServerInfoBean getServerById(int id) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            return runner.query("SELECT * FROM server_info WHERE id = ?",
                    singleServerInfoHandler, id);
        } catch (SQLException e) {
            logger.error("getServerById error!", e);
        }
        return null;
    }
    
    /**
     * Lấy toàn bộ server
     */
    public List<ServerInfoBean> getAllServers() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            return runner.query("SELECT * FROM server_list", multiServerInfoHandler);
        } catch (SQLException e) {
            logger.error("getAllServers error!", e);
        }
        return Collections.emptyList();
    }
    
    /**
     * Thêm 1 server
     * @param s
     * @return true nếu insert OK
     */
    public boolean addServer(ServerInfoBean s) {
        boolean ret = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            // created_at => NOW() hoặc s.getCreatedAt()
            // Mình ví dụ:
            //   "INSERT INTO server_info(name,ip,port,port_ssl,status,lan_port,http_url,created_at) VALUES(?,?,?,?,?,?,?,?)"
            // Nếu cột created_at auto => skip
            int rows = runner.update(conn,
                    "INSERT INTO server_info(name,ip,port,port_ssl,status,lan_port,http_url) "
                            + "VALUES(?,?,?,?,?,?,?)",
                    s.getName(), s.getIp(), s.getPort(), s.getPortSsl(),
                    s.getStatus(), s.getLanPort(), s.getHttpUrl());
            
            if (rows > 0) {
                ret = true;
                // Lấy last_insert_id:
                Integer theId = runner.query(conn, "SELECT LAST_INSERT_ID()", DaoCommonHandler.Integerhandler);
                s.setId(theId);
            }
        } catch (SQLException e) {
            logger.error("addServer error!", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return ret;
    }
    
    /**
     * Update server
     */
    public void updateServer(ServerInfoBean s) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            // Tùy cột => "UPDATE server_info SET name=?, ip=?, ... WHERE id=?"
            runner.update("UPDATE server_info SET name=?, ip=?, port=?, port_ssl=?, status=?, lan_port=?, http_url=? "
                            + "WHERE id=?",
                    s.getName(), s.getIp(), s.getPort(), s.getPortSsl(),
                    s.getStatus(), s.getLanPort(), s.getHttpUrl(),
                    s.getId());
            
        } catch (SQLException e) {
            logger.error("updateServer error!", e);
        }
    }
    
    /**
     * Xoá server theo id
     */
    public void removeServer(int serverId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("DELETE FROM server_info WHERE id=?", serverId);
        } catch (SQLException e) {
            logger.error("removeServer error!", e);
        }
    }
    
    /**
     * Xoá tất cả server
     */
    public void truncateServerInfo() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("TRUNCATE TABLE server_info");
        } catch (SQLException e) {
            logger.error("truncateServerInfo error!", e);
        }
    }
    
}
