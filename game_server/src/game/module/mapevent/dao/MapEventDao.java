package game.module.mapevent.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.mapevent.bean.MapEvent;
import game.module.mapevent.bean.PlayerMapEvent;
import lion.common.ProtostuffUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MapEventDao {

    private static Logger logger = LoggerFactory.getLogger(MapEventDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static MapEventDao instance = new MapEventDao();
    }

    public static MapEventDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<PlayerMapEvent> multiMapEventHanlder = rs -> {
        PlayerMapEvent mapEvent = null;
        if (rs.next()) {
            mapEvent = new PlayerMapEvent();
            mapEvent.setId(rs.getInt("id"));
            mapEvent.setPlayerId(rs.getInt("player_id"));
            mapEvent.setLastGenerateTime(rs.getTimestamp("last_generate_time"));
            MapEvent.DBMapEvent dbMapEvent = ProtostuffUtil.deserialize(rs.getBytes("events_blob"), MapEvent.DBMapEvent.class);
            mapEvent.setDbMapEvent(dbMapEvent);
        }
        return mapEvent;
    };

    /**
     * @param playerId
     * @return
     */
    public PlayerMapEvent getMapEvent(int playerId) {
        PlayerMapEvent ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from map_event where player_id = ?",
                    multiMapEventHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param mapEvent
     * @return
     */
    public boolean addMapEvent(PlayerMapEvent mapEvent) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        MapEvent.DBMapEvent dbMapEvent = mapEvent.getDbMapEvent();
        byte[] eventBytes = ProtostuffUtil.serialize(dbMapEvent);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into map_event(player_id,last_generate_time,events_blob) values(?,?,?)",
                    mapEvent.getPlayerId(), mapEvent.getLastGenerateTime(), eventBytes);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                mapEvent.setId(theId);
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
     * @param mapEvent
     * @return
     */
    public void updateMapEvent(PlayerMapEvent mapEvent) {
        QueryRunner runner = new QueryRunner(dataSource);

        MapEvent.DBMapEvent dbMapEvent = mapEvent.getDbMapEvent();
        byte[] dbMapEventBytes = ProtostuffUtil.serialize(dbMapEvent);

        try {
            runner.update(
                    "update map_event set last_generate_time=?,events_blob=? where id=?", mapEvent.getLastGenerateTime(), dbMapEventBytes, mapEvent.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeMapEvent(int playerMapEventsId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from map_event where id=?", playerMapEventsId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
