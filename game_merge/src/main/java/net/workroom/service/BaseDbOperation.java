package net.workroom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

public abstract class BaseDbOperation {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	Map<String, Integer> tableMaxId = new HashMap<String, Integer>();// 每张老表的最大id

	private static Logger logger = LoggerFactory.getLogger(BaseDbOperation.class);

	protected String targetDbName;

	protected Set<String> excludeTableNames = new HashSet<String>();

	@PostConstruct
	abstract void init();
	
	public void mergeOneDb(String targetDbName, String sourceDbName) {
		this.setTargetDbName(targetDbName);
		parseOneDb(sourceDbName);
	}

	public abstract void parseOneDb(String sourceDbName);

	protected void copyTblToTargetDb(String sourceDbName) {
		String sql1 = String.format("select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = '%s'", getTargetDbName());
		List<String> tableNameList = jdbcTemplate.queryForList(sql1, String.class);
		tableNameList.removeAll(excludeTableNames);
		jdbcTemplate.execute("use " + getTargetDbName());
		for (String tblName : tableNameList) {
			logger.info("copy table:{}", tblName);
			String dropTriggerSql = String.format("drop trigger if exists trigger_%s", tblName);
			jdbcTemplate.execute(dropTriggerSql);
			String idFieldName = "id";
			String triggerSql = String.format("CREATE TRIGGER trigger_%s BEFORE INSERT ON %s FOR EACH ROW BEGIN set NEW.%s = null;END", tblName, tblName, idFieldName);
			jdbcTemplate.execute(triggerSql);
			String mergeSql = String.format("insert into %s.%s (select * from %s.%s order by %s asc)", getTargetDbName(), tblName, sourceDbName, tblName, idFieldName);
			jdbcTemplate.execute(mergeSql);
			// 删除触发器
			jdbcTemplate.execute(dropTriggerSql);
		}
	}

	protected void clearTmpTable() {
		logger.info("drop table id_relations");
		jdbcTemplate.execute("use " + getTargetDbName());
		jdbcTemplate.execute("drop table IF EXISTS id_relations");
	}

	protected void createIdRelations() {
		logger.info("create table id_relations");
		jdbcTemplate.execute("use " + getTargetDbName());
		jdbcTemplate.execute("SET global max_heap_table_size=768000000");
		String tmpTbl = "CREATE TABLE `id_relations` (`f_id` int(11) NOT NULL AUTO_INCREMENT, `old_id` int(11) NOT NULL DEFAULT '0', `new_id` int(11) NOT NULL DEFAULT '0',"
				+ " `tbl_name` varchar(255) NOT NULL DEFAULT '', PRIMARY KEY (`f_id`),KEY `id_and_name` (`old_id`,`tbl_name`)) ENGINE=HEAP DEFAULT CHARSET=utf8;";
		jdbcTemplate.execute(tmpTbl);
	}

	/**
	 * 目标数据库每张表的最大id
	 */
	protected void initTableMaxId() {
		logger.info("get table max id");
		tableMaxId.clear();
		String sql1 = String.format("select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = '%s'", getTargetDbName());
		List<String> tableNameList = jdbcTemplate.queryForList(sql1, String.class);
		tableNameList.removeAll(excludeTableNames);
		for (String tblName : tableNameList) {
			String idFieldName = "id";
			Integer maxId = jdbcTemplate.queryForObject(String.format("select max(%s) from %s.%s", idFieldName, getTargetDbName(), tblName),Integer.class);
			maxId = maxId == null ? 0 : maxId;
			tableMaxId.put(tblName, maxId);
		}
	}

	protected void parseHeroId(String tblName, String heroFieldName) {
		logger.info("update heroId:tbl={}", tblName);
		String idFieldName = "id";
		String sql = String.format("update %s,id_relations set %s.%s = id_relations.new_id where %s.%s > ? and %s.%s = id_relations.old_id and id_relations.tbl_name = ?", tblName,
				tblName, heroFieldName, tblName, idFieldName, tblName, heroFieldName);
		jdbcTemplate.update(sql, tableMaxId.get(tblName), "player");
	}

	protected Map<Integer,Integer> mapId(String sourceDbName, String tblName) {
		logger.info("map id:sourceDbName={},tblName={}", sourceDbName, tblName);
		Map<Integer,Integer> retMap = new HashMap<Integer, Integer>();
		String oldIdSql = String.format("select id from %s.%s order by id asc", sourceDbName, tblName);
		List<Map<String, Object>> oldIdMapList = jdbcTemplate.queryForList(oldIdSql);
		List<Integer> oldIdList = new ArrayList<Integer>();
		for (Map<String, Object> map : oldIdMapList) {
			Object idObj = map.get("id");
			oldIdList.add(((Number) idObj).intValue());
		}
		String afterInsertIdSql = String.format("select id from %s.%s where id > %s order by id asc", getTargetDbName(), tblName, tableMaxId.get(tblName));
		List<Integer> newIdList = new ArrayList<Integer>();
		List<Map<String, Object>> objMapList = jdbcTemplate.queryForList(afterInsertIdSql);
		for (Map<String, Object> map : objMapList) {
			Object idObj = map.get("id");
			newIdList.add(((Number) idObj).intValue());
		}
		// 创建map
		for (int i = 0; i < oldIdList.size(); i++) {
			jdbcTemplate
					.update("insert into " + getTargetDbName() + ".id_relations(old_id,new_id,tbl_name) values(?,?,?)", oldIdList.get(i), newIdList.get(i), tblName);
			retMap.put(oldIdList.get(i), newIdList.get(i));
		}
		return retMap;
	}

	public String getTargetDbName() {
		return targetDbName;
	}

	public int getTableMaxId(String tblName) {
		return tableMaxId.get(tblName);
	}
	
	public void setTargetDbName(String targetDbName) {
		this.targetDbName = targetDbName;
	}
	
}
