package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.entity.GsEntity;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GsDao {

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    private ResultSetHandler<List<GsEntity>> handler = rs -> {
        List<GsEntity> heroEntities = new ArrayList<>();
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
            GsEntity hero = new GsEntity();
            hero.setId(rs.getInt("id"));
            hero.setZone_id(rs.getInt("zone_id"));
            hero.setName(rs.getString("name"));
            hero.setHost(rs.getString("host"));
            hero.setPort(rs.getInt("port"));
            hero.setIs_selected(rs.getBoolean("is_selected"));
            
            // Chỉ lấy giá trị "time_open" nếu cột tồn tại
            if (hasTimeOpenColumn) {
                Timestamp time_open = rs.getTimestamp("time_open");
                if (time_open != null) {
                    hero.setTime_open(time_open.toLocalDateTime());
                } else {
                    hero.setTime_open(null);
                }
            } else {
                hero.setTime_open(null); // Nếu không có cột "time_open", gán giá trị null
            }
            
            heroEntities.add(hero);
        }
        return heroEntities;
    };

    public List<GsEntity> getGsAll() {
        DataSource baseDs = dynamicRoutingDataSource.getDataSource("base");
        List<GsEntity> heroEntities = null;
        QueryRunner runner = new QueryRunner(baseDs);
        try {
            heroEntities = runner.query(
                    "select * from gs_list", handler);
        } catch (SQLException e) {
            log.error("", e);
        }
        return heroEntities;
    }

    public GsEntity getGsById(int gs_id) {
        DataSource baseDs = dynamicRoutingDataSource.getDataSource("base");
        GsEntity retGsEntity = null;
        QueryRunner runner = new QueryRunner(baseDs);
        try {
            List<GsEntity> heroEntities = runner.query(
                    "select * from gs_list where id = ?", handler, gs_id);
            if (heroEntities != null && heroEntities.size() > 0) {
                retGsEntity = heroEntities.get(0);
            }
        } catch (SQLException e) {
            log.error("", e);
        }
        return retGsEntity;
    }
}
