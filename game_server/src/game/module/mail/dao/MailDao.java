package game.module.mail.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.mail.bean.MailBean;
import game.module.mail.constants.MailConstants;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 邮件Dao
 * 
 * @author zhangning
 * @Date 2014年12月29日 下午2:06:17
 *
 */
public class MailDao {

	private static Logger logger = LoggerFactory.getLogger(MailDao.class);

	private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

	static class SingletonHolder {
		static MailDao instance = new MailDao();
	}

	public static MailDao getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 邮件
	 */
	private final ResultSetHandler<List<MailBean>> multiMailHandler = rs -> {
		List<MailBean> mailBeanList = new ArrayList<>();
		while (rs.next()) {
			MailBean mailBean = new MailBean();
			mailBean.setId(rs.getInt("Id"));
			mailBean.setPlayerId(rs.getInt("player_id"));
			mailBean.setType(rs.getByte("type"));
			mailBean.setState(rs.getByte("state"));
			mailBean.setFromType(rs.getString("from_type"));
			mailBean.setFromFid(rs.getInt("from_fid"));
			mailBean.setFromLegionId(rs.getInt("from_legionid"));
			mailBean.setTitle(rs.getString("title"));
			mailBean.setContent(rs.getString("content"));
			mailBean.setCreateTime(rs.getTimestamp("create_time"));
			mailBean.setEndTime(rs.getTimestamp("end_time"));

			String attach_str = rs.getString("attachs");
			if(StringUtils.isNotBlank(attach_str)) {
				Map<Integer, Integer> attachMap = SimpleTextConvert.decodeIntMap(attach_str);
				mailBean.setAttachs(attachMap);
			}
			mailBeanList.add(mailBean);
		}
		return mailBeanList;
	};

	/**
	 * 获取玩家所有邮件
	 * 
	 * @param playerId
	 * @return
	 */
	public List<MailBean> getPlayerMailAll(int playerId) {
		List<MailBean> ret = null;
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			ret = runner.query("select * from mail where player_id = ? order by state desc, create_time asc",
					multiMailHandler, playerId);
		} catch (SQLException e) {
			logger.error("", e);
		}

		return ret;
	}

	/**
	 * 添加一个邮件
	 * 
	 * @param mailBean
	 * @return
	 */
	public boolean addOneMail(MailBean mailBean) {
		boolean addRet = false;
		QueryRunner runner = new QueryRunner(dataSource);
		Connection conn = null;

		String attach_str = SimpleTextConvert.encodeMap(mailBean.getAttachs());

		try {
			conn = dataSource.getConnection();
			int ret = runner.update(conn,
					"insert into mail(player_id, type, state, from_type, from_fid,from_legionid,title,content,create_time,end_time,attachs) values" +
							"(?,?,?,?,?,?,?,?,?,?,?)",
					mailBean.getPlayerId(), mailBean.getType(), mailBean.getState(), mailBean.getFromType(), mailBean.getFromFid(), mailBean.getFromLegionId(),
					mailBean.getTitle(), mailBean.getContent(), mailBean.getCreateTime(), mailBean.getEndTime(),attach_str);
			if (ret > 0) {
				addRet = true;
			}
			if (addRet) {
				int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
				mailBean.setId(theId);
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return addRet;
	}

	/**
	 * 更新邮件读取状态
	 * 
	 * @param mailBean
	 * @return
	 */
	public void updateMailStatus(MailBean mailBean) {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
			runner.update("update mail set state=? where id=?", mailBean.getState(), mailBean.getId());
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * 删除邮件
	 * 
	 * @param id
	 *            ：邮件唯一ID
	 */
	public void removeMail(int id) {
		QueryRunner runner = new QueryRunner(dataSource);

		try {
			runner.update("delete from mail where id=?", id);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * 检查过期邮件
	 */
	public void checkPastDueMail() {
		QueryRunner runner = new QueryRunner(dataSource);
		try {
//			List<MailTemplate> mailTemplates = MailTemplateCache.getInstance().getAllMailTemplates();
//			if (mailTemplates != null && !mailTemplates.isEmpty()) {
//				for (MailTemplate mailTemplate : mailTemplates) {
//					int delDay = mailTemplate.getDeletetime() == 0 ? 30 : mailTemplate.getDeletetime();
//					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//					String delDate = format.format(System.currentTimeMillis());
//
//					runner.update("delete from mail where template_id=? and DATE_ADD(create_time, INTERVAL ? DAY)<=?",
//							mailTemplate.getId(), delDay, delDate);
//				}
//			}
			int delDay = MailConstants.SYS_MAIL_MAX_DELTIME;
			runner.update("delete from mail where DATE_ADD(create_time, INTERVAL ? DAY)<=NOW()", delDay);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
