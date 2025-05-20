package game.module.legion.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.legion.bean.DbLegionBoss;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import lion.common.ProtostuffUtil;
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

public class LegionDao {

    private static Logger logger = LoggerFactory.getLogger(LegionDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static LegionDao instance = new LegionDao();
    }

    public static LegionDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<List<LegionBean>> multiLegionBeanHanlder = rs -> {
        List<LegionBean> heroList = new ArrayList<>();
        while (rs.next()) {
            LegionBean friendBean = new LegionBean();
            friendBean.setId(rs.getInt("id"));
            friendBean.setUuid(rs.getLong("uuid"));
            friendBean.setName(rs.getString("name"));
            friendBean.setNotice(rs.getString("notice"));
            friendBean.setMinLevel(rs.getInt("min_level"));
            friendBean.setPass(rs.getBoolean("is_pass"));
            friendBean.setCeoId(rs.getInt("ceo_id"));
            friendBean.setPower(rs.getInt("power"));
            friendBean.setExp(rs.getInt("exp"));
            friendBean.setFexp(rs.getInt("fexp"));
            friendBean.setFlevel(rs.getInt("flevel"));
            friendBean.setKceo(rs.getBoolean("kceo"));
            friendBean.setLevel(rs.getInt("level"));
            friendBean.setPos(rs.getInt("pos"));
            LegionPlayer.DbLegionPlayers dbLegionPlayers = ProtostuffUtil.deserialize(rs.getBytes("members"), LegionPlayer.DbLegionPlayers.class);
            friendBean.setDbLegionPlayers(dbLegionPlayers);
            DbLegionBoss dbLegionBoss = ProtostuffUtil.deserialize(rs.getBytes("boss"), DbLegionBoss.class);
            friendBean.setLegionBoss(dbLegionBoss);
            friendBean.setApplyPlayers(SimpleTextConvert.decodeIntLongMap(rs.getString("apply_players")));
            heroList.add(friendBean);
        }
        return heroList;
    };

    /**
     * @return
     */
    public List<LegionBean> getLegionBeans() {
        List<LegionBean> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from legion", multiLegionBeanHanlder);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param legionBean
     * @return
     */
    public boolean addLegionBean(LegionBean legionBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        LegionPlayer.DbLegionPlayers dbLegionPlayers = legionBean.getDbLegionPlayers();
        byte[] dbLegionPlayers_s = ProtostuffUtil.serialize(dbLegionPlayers);

        DbLegionBoss dbLegionBoss = legionBean.getLegionBoss();
        byte[] dbLegionBoss_s = ProtostuffUtil.serialize(dbLegionBoss);

        String apply_players_str = SimpleTextConvert.encodeMap(legionBean.getApplyPlayers());

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into legion(uuid,name,notice,min_level,is_pass,ceo_id,power,exp,fexp,flevel,kceo,level,pos,members,apply_players,boss) values" +
                            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    legionBean.getUuid(), legionBean.getName(), legionBean.getNotice(), legionBean.getMinLevel(), legionBean.getPass(), legionBean.getCeoId(),
                    legionBean.getPower(), legionBean.getExp(), legionBean.getFexp(), legionBean.getFlevel(), legionBean.getKceo(), legionBean.getLevel(),
                    legionBean.getPos(), dbLegionPlayers_s, apply_players_str, dbLegionBoss_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                legionBean.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    public void updateLegion(LegionBean legionBean) {
        QueryRunner runner = new QueryRunner(dataSource);
        LegionPlayer.DbLegionPlayers dbLegionPlayers = legionBean.getDbLegionPlayers();
        byte[] dbLegionPlayers_s = ProtostuffUtil.serialize(dbLegionPlayers);

        DbLegionBoss dbLegionBoss = legionBean.getLegionBoss();
        byte[] dbLegionBoss_s = ProtostuffUtil.serialize(dbLegionBoss);
        String apply_players_str = SimpleTextConvert.encodeMap(legionBean.getApplyPlayers());
        try {
            runner.update("update legion set name=?,notice=?,min_level=?,is_pass=?,ceo_id=?,power=?,exp=?,fexp=?,flevel=?,kceo=?,level=?," +
                            "pos=?,members=?,apply_players=?,boss=? where id = ?", legionBean.getName(), legionBean.getNotice(), legionBean.getMinLevel(),
                    legionBean.getPass(), legionBean.getCeoId(), legionBean.getPower(), legionBean.getExp(), legionBean.getFexp(), legionBean.getFlevel(),
                    legionBean.getKceo(), legionBean.getLevel(), legionBean.getPos(), dbLegionPlayers_s, apply_players_str, dbLegionBoss_s, legionBean.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeLegion(int legionId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from legion where id=?", legionId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
