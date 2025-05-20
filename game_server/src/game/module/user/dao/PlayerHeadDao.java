package game.module.user.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.user.bean.PlayerHead;
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
import java.util.Set;

public class PlayerHeadDao {

    private static Logger logger = LoggerFactory.getLogger(PlayerHeadDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static PlayerHeadDao instance = new PlayerHeadDao();
    }

    public static PlayerHeadDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<PlayerHead> multiPlayerHeadHanlder = rs -> {
        PlayerHead playerHead = null;
        if (rs.next()) {
            playerHead = new PlayerHead();
            playerHead.setId(rs.getInt("id"));
            playerHead.setPlayerId(rs.getInt("player_id"));
            //
            String head_icons_str = rs.getString("head_icons");
            Set<Integer> head_icons = SimpleTextConvert.decodeIntSet(head_icons_str);
            playerHead.setHeadIcons(head_icons);
            //
            String head_frames_str = rs.getString("head_frames");
            Set<Integer> head_frames = SimpleTextConvert.decodeIntSet(head_frames_str);
            playerHead.setHeadFrames(head_frames);
            //
            String head_images_str = rs.getString("head_images");
            Set<Integer> head_images = SimpleTextConvert.decodeIntSet(head_images_str);
            playerHead.setHeadImages(head_images);
        }
        return playerHead;
    };

    /**
     * @param playerId
     * @return
     */
    public PlayerHead getPlayerHead(int playerId) {
        PlayerHead ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from player_head where player_id = ?",
                    multiPlayerHeadHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param playerHead
     * @return
     */
    public boolean addPlayerHead(PlayerHead playerHead) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        Set<Integer> headIcons = playerHead.getHeadIcons();
        String headIconsStr = SimpleTextConvert.encodeCollection(headIcons);
        Set<Integer> headFrames = playerHead.getHeadFrames();
        String headFramesStr = SimpleTextConvert.encodeCollection(headFrames);
        Set<Integer> headImages = playerHead.getHeadImages();
        String headImagesStr = SimpleTextConvert.encodeCollection(headImages);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into player_head(player_id,head_icons,head_frames,head_images) values(?,?,?,?)",
                    playerHead.getPlayerId(), headIconsStr, headFramesStr, headImagesStr);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                playerHead.setId(theId);
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
     * @return
     */
    public void updatePlayerHead(int playerHeadId,String headIconsStr,String headFramesStr,String headImagesStr) {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update(
                    "update player_head set head_icons=?,head_frames=?,head_images=? where id=?", headIconsStr,headFramesStr,headImagesStr, playerHeadId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removePlayerHead(int playerPlayerHeadsId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from player_head where id=?", playerPlayerHeadsId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
