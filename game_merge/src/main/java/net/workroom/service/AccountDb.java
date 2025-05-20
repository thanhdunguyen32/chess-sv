package net.workroom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AccountDb extends BaseDbOperation {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private HeroDb heroDb;

	private static Logger logger = LoggerFactory.getLogger(AccountDb.class);

	@PostConstruct
	public void init() {
		excludeTableNames.add("t_onlineinfo");
		excludeTableNames.add("t_trade_log");
	}

	/**
	 * 去掉唯一索引
	 */
	private void dropIndexs() {
		logger.info("drop indexs");
		jdbcTemplate.execute("use " + getTargetDbName());
		jdbcTemplate.execute("alter table t_account drop index idx_loginname");
		jdbcTemplate.execute("alter table t_activities drop index idx_accountid_type");
		jdbcTemplate.execute("alter table t_order drop index token");
	}

	private void addIndexs() {
		logger.info("add indexs");
		jdbcTemplate.execute("use " + getTargetDbName());
		jdbcTemplate.execute("alter table t_account add key `idx_loginname` (`f_loginname`)");
		jdbcTemplate.execute("alter table t_activities add key `idx_accountid_type` (`f_account_id`,`f_type`,`f_hero_id`)");
		jdbcTemplate.execute("alter table t_order add key `token` (`token`)");
	}

	public void parseOneDb(String sourceDbName) {
		logger.info("处理一个database的合表操作：source={}", sourceDbName);
		clearTmpTable();
		dropIndexs();
		specialParseBefore();
		initTableMaxId();
		copyTblToTargetDb(sourceDbName);
		specialParseAfter();
		createIdRelations();
		mapId(sourceDbName, "t_account");
		addIndexs();
//		parseAccountId("t_activities", "f_account_id");
//		parseAccountId("t_order", "accountid");
//		parseAccountId("t_order_offline", "f_account_id");
		// 跨数据库处理账号id
//		String targetHeroDbName = heroDb.getTargetDbName();
//		int heroTableMaxId = heroDb.getTableMaxId("t_hero");
//		parseAcrossAccountId(targetHeroDbName + ".t_hero", "f_account_id", heroTableMaxId);
//		int heroMarketTaskTableMaxId = heroDb.getTableMaxId("t_hero_market_task");
//		parseAcrossAccountId(targetHeroDbName + ".t_hero_market_task", "f_account_id", heroMarketTaskTableMaxId);
//		int heroActivationCardTableMaxId = heroDb.getTableMaxId("t_hero_activation_card");
//		parseAcrossAccountId(targetHeroDbName + ".t_hero_activation_card", "f_account_id", heroActivationCardTableMaxId);
//		int heroSecurityCodeTblMaxId = heroDb.getTableMaxId("t_hero_security_code");
//		parseAcrossAccountId(targetHeroDbName + ".t_hero_security_code", "f_account_id", heroSecurityCodeTblMaxId);
		// 跨数据处理角色id
//		parseHeroId("t_order", "heroid", targetHeroDbName);
//		parseHeroId("t_order_offline", "f_hero_id", targetHeroDbName);
//		parseHeroId("t_activities", "f_hero_id", targetHeroDbName);
		//处理system_id
		parseSystemId();
	}

	private void parseSystemId() {
		logger.info("parse system_id");
		String targetHeroDbName = heroDb.getTargetDbName();
		String sql = String.format("delete from %s.t_system_id", targetHeroDbName);
		jdbcTemplate.execute(sql);
		sql = String.format("insert into %s.t_system_id (f_id,f_type,f_next_id)  values (1,'order',(SELECT max(f_id) from t_order)+1)", targetHeroDbName);
		jdbcTemplate.execute(sql);
		sql = String.format("insert into %s.t_system_id (f_id,f_type,f_next_id)  values (2,'mail',(SELECT max(f_id) from %s.t_hero_mail)+1)", targetHeroDbName,targetHeroDbName);
		jdbcTemplate.execute(sql);
	}

	private void specialParseAfter() {
		logger.info("parse t_order#f_id remove AUTO_INCREMENT");
		jdbcTemplate.execute("ALTER TABLE `t_order` MODIFY COLUMN `f_id` bigint(20) NOT NULL");		
	}

	private void specialParseBefore() {
		logger.info("parse t_order#f_id add AUTO_INCREMENT");
		jdbcTemplate.execute("ALTER TABLE `t_order` MODIFY COLUMN `f_id` bigint(20) NOT NULL AUTO_INCREMENT");
	}

	protected void parseHeroId(String tblName, String heroFieldName, String dbName) {
		logger.info("update heroId:tbl={}", tblName);
		String idFieldName = "f_id";
		String sql = String.format(
				"update %s,%s.id_relations set %s.%s = %s.id_relations.new_id where %s.%s > ? and %s.%s = %s.id_relations.old_id and %s.id_relations.tbl_name = ?", tblName,
				dbName, tblName, heroFieldName, dbName, tblName, idFieldName, tblName, heroFieldName, dbName, dbName);
		//logger.info("update heroId:sql={}",sql);
		jdbcTemplate.update(sql, tableMaxId.get(tblName), "t_hero");
	}

	protected void parseAccountId(String tblName, String accountFieldName) {
		logger.info("update AccountId,table={}", tblName);
		String idFieldName = "f_id";
		if (tblName.equals("t_hero_goods") || tblName.equals("t_hero_task")) {
			idFieldName = "f_base_id";
		}
		String sql = String.format("update %s,id_relations set %s.%s = id_relations.new_id where %s.%s > ? and %s.%s = id_relations.old_id and id_relations.tbl_name = ?", tblName,
				tblName, accountFieldName, tblName, idFieldName, tblName, accountFieldName);
		//logger.info("update AccountId,sql={}", sql);
		jdbcTemplate.update(sql, tableMaxId.get(tblName), "t_account");
	}

	protected void parseAcrossAccountId(String tblName, String accountFieldName, int tblMaxId) {
		logger.info("update AccountId,table={}", tblName);
		String idFieldName = "f_id";
		String sql = String.format("update %s,id_relations set %s.%s = id_relations.new_id where %s.%s > ? and %s.%s = id_relations.old_id and id_relations.tbl_name = ?", tblName,
				tblName, accountFieldName, tblName, idFieldName, tblName, accountFieldName);
		//logger.info("update AccountId,sql={}", sql);
		jdbcTemplate.update(sql, tblMaxId, "t_account");
	}

}
