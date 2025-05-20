package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.DaoCommonHandler;
import cc.mrbird.febs.system.entity.Cdk;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

@Component
public class CdkDaoDb {

	private static Logger logger = LoggerFactory.getLogger(CdkDaoDb.class);

	@Autowired
	private DynamicRoutingDataSource dynamicRoutingDataSource;

	public int getCdkIdFromDb() {
		DataSource dataSource = dynamicRoutingDataSource.getDataSource("base");
		int cdkId = 0;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			Connection conn = runner.getDataSource().getConnection();
			cdkId = runner.query(conn, "select max(cdk_id) from tcdk;", DaoCommonHandler.Integerhandler);
		} catch (SQLException e) {
			logger.error("", e);
		}
		return cdkId;
	}
	
	public int batchAddCdkey(Collection<Cdk> cdkAll) {
		DataSource dataSource = dynamicRoutingDataSource.getDataSource("base");
		int addCnt = 0;
		Connection con = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			PreparedStatement pstmt = con
					.prepareStatement("insert into tcdk(platform, area, cdk,cdk_name,award_id,reuse,start_time,end_time) values(?,?,?,?,?,?,?,?)");

			for (Cdk logCount : cdkAll) {
				pstmt.setInt(1, logCount.getPlatform());
				pstmt.setInt(2, logCount.getArea());
				pstmt.setString(3, logCount.getCdk());
				pstmt.setString(4, logCount.getCdkName());
				pstmt.setInt(5, logCount.getAwardId());
				pstmt.setInt(6, logCount.getReuse());
				if (logCount.getStartTime() == null) {
					pstmt.setTimestamp(7, null);
				} else {
					pstmt.setTimestamp(7, new Timestamp(logCount.getStartTime().getTime()));
				}
				if (logCount.getEndTime() == null) {
					pstmt.setTimestamp(8, null);
				} else {
					pstmt.setTimestamp(8, new Timestamp(logCount.getEndTime().getTime()));
				}
				pstmt.addBatch();
				addCnt++;

				if (addCnt % 1000 == 0) {
					pstmt.executeBatch();
					con.commit();
					pstmt.clearBatch();
				}
			}
			pstmt.executeBatch();
			con.commit();
			pstmt.clearBatch();

			pstmt.close();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(con);
		}

		return addCnt;
	}

}
