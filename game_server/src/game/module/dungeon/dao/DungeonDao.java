package game.module.dungeon.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.bean.DungeonBuff;
import game.module.dungeon.bean.DungeonNode;
import game.module.rank.logic.RankManager;
import lion.common.ProtostuffUtil;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DungeonDao {

    private static Logger logger = LoggerFactory.getLogger(DungeonDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static DungeonDao instance = new DungeonDao();
    }

    public static DungeonDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<DungeonBean> multiDungeonHanlder = rs -> {
        DungeonBean dungeonBean = null;
        if (rs.next()) {
            dungeonBean = new DungeonBean();
            dungeonBean.setId(rs.getInt("id"));
            dungeonBean.setPlayerId(rs.getInt("player_id"));
            dungeonBean.setChapterIndex(rs.getInt("chapter_index"));
            dungeonBean.setNode(rs.getInt("node"));
            dungeonBean.setPos(rs.getInt("pos"));
            dungeonBean.setChapterAwardGet(SimpleTextConvert.decodeIntList(rs.getString("chapter_award_get")));
            dungeonBean.setOnlineGenerals(SimpleTextConvert.decodeLongMap(rs.getString("online_generals")));
            dungeonBean.setPotions(SimpleTextConvert.decodeIntMap(rs.getString("potions")));
            dungeonBean.setShopBuy(SimpleTextConvert.decodeIntSet(rs.getString("shop_buy")));
            DungeonNode.DbDungeonNode dbDungeonNode = ProtostuffUtil.deserialize(rs.getBytes("nodes"), DungeonNode.DbDungeonNode.class);
            dungeonBean.setDbDungeonNode(dbDungeonNode);
            DungeonBuff dungeonBuff = ProtostuffUtil.deserialize(rs.getBytes("buff"), DungeonBuff.class);
            dungeonBean.setDungeonBuff(dungeonBuff);
            DungeonBuff spbuff = ProtostuffUtil.deserialize(rs.getBytes("sp_buff"), DungeonBuff.class);
            dungeonBean.setSpBuff(spbuff);
            dungeonBean.setUpdateTime(rs.getTimestamp("update_time"));
        }
        return dungeonBean;
    };

    /**
     * @param playerId
     * @return
     */
    public DungeonBean getDungeon(int playerId) {
        DungeonBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from dungeon where player_id = ?",
                    multiDungeonHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param dungeonBean
     * @return
     */
    public boolean addDungeonBean(DungeonBean dungeonBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        DungeonNode.DbDungeonNode dbDungeonNode = dungeonBean.getDbDungeonNode();
        byte[] dbDungeonNode_b = ProtostuffUtil.serialize(dbDungeonNode);

        DungeonBuff dungeonBuff = dungeonBean.getDungeonBuff();
        byte[] dungeonBuff_b = ProtostuffUtil.serialize(dungeonBuff);

        DungeonBuff spBuff = dungeonBean.getSpBuff();
        byte[] spBuff_s = ProtostuffUtil.serialize(spBuff);

        String s_chapterAwardGet = SimpleTextConvert.encodeCollection(dungeonBean.getChapterAwardGet());
        String s_onlineGenerals = SimpleTextConvert.encodeMap(dungeonBean.getOnlineGenerals());
        String s_potions = SimpleTextConvert.encodeMap(dungeonBean.getPotions());
        String s_shopbuy = SimpleTextConvert.encodeCollection(dungeonBean.getShopBuy());
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into dungeon(player_id,chapter_index,node,pos,chapter_award_get,online_generals,potions,nodes,buff,sp_buff,shop_buy,update_time) " +
                            "values(?,?,?,?,?,?,?,?,?,?,?,?)",
                    dungeonBean.getPlayerId(), dungeonBean.getChapterIndex(), dungeonBean.getNode(), dungeonBean.getPos(), s_chapterAwardGet,
                    s_onlineGenerals, s_potions, dbDungeonNode_b, dungeonBuff_b, spBuff_s, s_shopbuy, dungeonBean.getUpdateTime());
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                dungeonBean.setId(theId);
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
     * @param dungeonBean
     * @return
     */
    public void updateDungeon(DungeonBean dungeonBean) {
        QueryRunner runner = new QueryRunner(dataSource);

        DungeonNode.DbDungeonNode dbDungeonNode = dungeonBean.getDbDungeonNode();
        byte[] dbDungeonNode_b = ProtostuffUtil.serialize(dbDungeonNode);
        DungeonBuff dungeonBuff = dungeonBean.getDungeonBuff();
        byte[] dungeonBuff_b = ProtostuffUtil.serialize(dungeonBuff);
        DungeonBuff spBuff = dungeonBean.getSpBuff();
        byte[] spBuff_b = ProtostuffUtil.serialize(spBuff);
        String s_chapterAwardGet = SimpleTextConvert.encodeCollection(dungeonBean.getChapterAwardGet());
        String s_onlineGenerals = SimpleTextConvert.encodeMap(dungeonBean.getOnlineGenerals());
        String s_potions = SimpleTextConvert.encodeMap(dungeonBean.getPotions());
        String s_shopbuy = SimpleTextConvert.encodeCollection(dungeonBean.getShopBuy());
        try {
            runner.update(
                    "update dungeon set chapter_index=?,node=?,pos=?,chapter_award_get=?,online_generals=?,potions=?,nodes=?,buff=?,sp_buff=?,shop_buy=?," +
                            "update_time = ? where id=?",
                    dungeonBean.getChapterIndex(), dungeonBean.getNode(), dungeonBean.getPos(), s_chapterAwardGet, s_onlineGenerals, s_potions, dbDungeonNode_b, dungeonBuff_b,
                    spBuff_b, s_shopbuy, dungeonBean.getUpdateTime(), dungeonBean.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeDungeon(int dungeonBeanId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from dungeon where id=?", dungeonBeanId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public List<DungeonBean> randDungeon() {
        List<DungeonBean> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from dungeon order by chapter_index desc,node desc limit ?",
                    new ResultSetHandler<List<DungeonBean>>() {
                        @Override
                        public List<DungeonBean> handle(ResultSet rs) throws SQLException {
                            List<DungeonBean> retlist = new ArrayList<>();
                            while (rs.next()) {
                                DungeonBean dungeonBean = new DungeonBean();
                                dungeonBean.setId(rs.getInt("id"));
                                dungeonBean.setPlayerId(rs.getInt("player_id"));
                                dungeonBean.setChapterIndex(rs.getInt("chapter_index"));
                                dungeonBean.setNode(rs.getInt("node"));
                                retlist.add(dungeonBean);
                            }
                            return retlist;
                        }
                    }, RankManager.RANK_SIZE);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

    public List<Integer> rankDungeonLevelMy() {
        List<Integer> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select player_id from dungeon order by chapter_index desc,node desc",
                    DaoCommonHandler.IntegerListHandler);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

}
