package game.module.shop.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.shop.bean.ShopBean;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ShopDao {

    private static Logger logger = LoggerFactory.getLogger(ShopDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static ShopDao instance = new ShopDao();
    }

    public static ShopDao getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务--进度数据
     */
    private ResultSetHandler<ShopBean> shopProHandler = rs -> {
        ShopBean shopBean = null;
        if (rs.next()) {
            shopBean = new ShopBean();
            shopBean.setId(rs.getInt("id"));
            shopBean.setPlayerId(rs.getInt("player_id"));
            shopBean.setLastDayBuyTime(rs.getTimestamp("last_day_buy_time"));
            //
            String day_buy_count_str = rs.getString("day_buy_count");
            Map<Integer, Integer> day_buy_countList = SimpleTextConvert.decodeIntMap(day_buy_count_str);
            shopBean.setDayBuyCount(day_buy_countList);
            //
            String decomp_buy_count_str = rs.getString("decomp_buy_count");
            Map<Integer, Integer> decomp_buy_countList = SimpleTextConvert.decodeIntMap(decomp_buy_count_str);
            shopBean.setDecompBuyCount(decomp_buy_countList);
            //
            String star_buy_count_str = rs.getString("star_buy_count");
            Map<Integer, Integer> star_buy_countList = SimpleTextConvert.decodeIntMap(star_buy_count_str);
            shopBean.setStarBuyCount(star_buy_countList);
            //
            String legion_buy_count_str = rs.getString("legion_buy_count");
            Map<Integer, Integer> legion_buy_countList = SimpleTextConvert.decodeIntMap(legion_buy_count_str);
            shopBean.setLegionBuyCount(legion_buy_countList);
        }
        return shopBean;
    };

    /**
     * 获得玩家的所有任务进度<br/>
     * 排序：已完成任务>未完成任务<br/>
     * 第一优先级相同的情况下按照id从小到大排序
     *
     * @param playerId
     * @return
     */
    public ShopBean getPlayerShopBean(int playerId) {
        ShopBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from shop where player_id = ?",
                    shopProHandler, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param shopBean
     * @return
     */
    public boolean addShopBean(ShopBean shopBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        Map<Integer, Integer> passiveSkills = shopBean.getDayBuyCount();
        String dayBuyCountStr = SimpleTextConvert.encodeMap(passiveSkills);
        Map<Integer, Integer> decompBuyCount = shopBean.getDecompBuyCount();
        String decompBuyCountStr = SimpleTextConvert.encodeMap(decompBuyCount);
        Map<Integer, Integer> starBuyCount = shopBean.getStarBuyCount();
        String starBuyCountStr = SimpleTextConvert.encodeMap(starBuyCount);
        Map<Integer, Integer> legionBuyCount = shopBean.getLegionBuyCount();
        String legionBuyCountStr = SimpleTextConvert.encodeMap(legionBuyCount);
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into shop(player_id, last_day_buy_time, day_buy_count,decomp_buy_count,star_buy_count,legion_buy_count) " +
                            "values(?,?,?,?,?,?)",
                    shopBean.getPlayerId(), shopBean.getLastDayBuyTime(), dayBuyCountStr,decompBuyCountStr ,starBuyCountStr,legionBuyCountStr);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                shopBean.setId(theId);
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
     * @param shopBean
     * @return
     */
    public void updateShopBean(ShopBean shopBean) {
        QueryRunner runner = new QueryRunner(dataSource);
        Map<Integer, Integer> passiveSkills = shopBean.getDayBuyCount();
        String dayBuyCountStr = SimpleTextConvert.encodeMap(passiveSkills);
        Map<Integer, Integer> decompBuyCount = shopBean.getDecompBuyCount();
        String decompBuyCountStr = SimpleTextConvert.encodeMap(decompBuyCount);
        Map<Integer, Integer> starBuyCount = shopBean.getStarBuyCount();
        String starBuyCountStr = SimpleTextConvert.encodeMap(starBuyCount);
        Map<Integer, Integer> legionBuyCount = shopBean.getLegionBuyCount();
        String legionBuyCountStr = SimpleTextConvert.encodeMap(legionBuyCount);
        try {
            runner.update(
                    "update shop set last_day_buy_time=?, day_buy_count=?,decomp_buy_count=?,star_buy_count=?,legion_buy_count=? " +
                            "where id=?",
                    shopBean.getLastDayBuyTime(), dayBuyCountStr,decompBuyCountStr,starBuyCountStr,legionBuyCountStr, shopBean.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     * 删除一条任务进度(任务完成且奖励已领取)
     *
     * @param taskId
     */
    public void removeShopBean(int taskId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from shop where id=?", taskId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
