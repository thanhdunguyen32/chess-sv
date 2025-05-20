package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.entity.DailyStatEntity;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DailyStatDao {

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    private ResultSetHandler<List<DailyStatEntity>> handler = rs -> {
        List<DailyStatEntity> heroEntities = new ArrayList<>();
        while (rs.next()) {
            DailyStatEntity hero = new DailyStatEntity();
            hero.setNewPlayer(rs.getInt("new_player"));
            hero.setActivePlayer(rs.getInt("active_player"));
            hero.setNewPay(rs.getInt("new_pay"));
            hero.setPayCount(rs.getInt("pay_count"));
            hero.setPaySum(rs.getInt("pay_sum"));
            hero.setYesterdayRetain(rs.getFloat("yesterday_retain"));
            hero.setStatTime(rs.getTimestamp("stat_time"));
            heroEntities.add(hero);
        }
        return heroEntities;
    };

    public List<DailyStatEntity> findAll() {
        DataSource baseDs = dynamicRoutingDataSource.getDataSource("base");
        List<DailyStatEntity> heroEntities = null;
        QueryRunner runner = new QueryRunner(baseDs);
        try {
            heroEntities = runner.query(
                    "select * from daily_stat order by stat_time desc", handler);
        } catch (SQLException e) {
            log.error("", e);
        }
        return heroEntities;
    }

    public boolean save(DailyStatEntity newEntity) {
        DataSource baseDs = dynamicRoutingDataSource.getDataSource("base");
        QueryRunner runner = new QueryRunner(baseDs);
        boolean ret = false;
        try {
            int insertRet = runner.update(
                    "insert into daily_stat(new_player,active_player,new_pay,pay_count,pay_sum,yesterday_retain,stat_time) " +
                            "values(?,?,?,?,?,?,?)", newEntity.getNewPlayer(), newEntity.getActivePlayer(), newEntity.getNewPay(),
                    newEntity.getPayCount(), newEntity.getPaySum(), newEntity.getYesterdayRetain(),newEntity.getStatTime());
            ret = insertRet > 0;
        } catch (SQLException e) {
            log.error("", e);
        }
        return ret;
    }
}
