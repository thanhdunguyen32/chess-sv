# ************************************************************
# Sequel Ace SQL dump
# Version 20080
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# Host: 172.104.47.85 (MySQL 8.0.40)
# Database: febs_base
# Generation Time: 2025-01-12 06:41:10 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table QRTZ_BLOB_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;

CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_CALENDARS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;

CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_CRON_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;

CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_FIRED_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;

CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_JOB_DETAILS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;

CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_LOCKS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_LOCKS`;

CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_PAUSED_TRIGGER_GRPS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;

CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_SCHEDULER_STATE
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;

CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_SIMPLE_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;

CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_SIMPROP_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;

CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int DEFAULT NULL,
  `INT_PROP_2` int DEFAULT NULL,
  `LONG_PROP_1` bigint DEFAULT NULL,
  `LONG_PROP_2` bigint DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table QRTZ_TRIGGERS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;

CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint DEFAULT NULL,
  `PREV_FIRE_TIME` bigint DEFAULT NULL,
  `PRIORITY` int DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table gs_list
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gs_list`;

CREATE TABLE `gs_list` (
  `id` int NOT NULL AUTO_INCREMENT,
  `zone_id` int NOT NULL COMMENT 'Zone ID',
  `name` varchar(50) NOT NULL COMMENT 'Server name',
  `host` varchar(50) NOT NULL COMMENT 'Server host',
  `port` int NOT NULL COMMENT 'Server port',
  `is_selected` tinyint(1) DEFAULT '0' COMMENT 'Is selected: 0-no, 1-yes',
  `time_open` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Game server list';

LOCK TABLES `gs_list` WRITE;
/*!40000 ALTER TABLE `gs_list` DISABLE KEYS */;

INSERT INTO `gs_list` (`id`, `zone_id`, `name`, `host`, `port`, `is_selected`, `time_open`)
VALUES
	(1,1,'Demo','172.104.47.85',12001,1,'2025-01-07 04:13:46'),
	(2,2,'Main','27.72.105.113',8801,1,'2025-01-07 04:13:46'),
	(3,3,'Company','192.168.2.68',12001,1,'2025-01-07 04:13:46'),
	(4,4,'Local','127.0.0.1',12001,1,'2025-01-09 05:09:05');

/*!40000 ALTER TABLE `gs_list` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table server_stats
# ------------------------------------------------------------

DROP TABLE IF EXISTS `server_stats`;

CREATE TABLE `server_stats` (
  `server_id` int NOT NULL COMMENT 'ID máy chủ',
  `server_name` varchar(50) NOT NULL COMMENT 'Tên máy chủ',
  `online_count` int NOT NULL DEFAULT '0' COMMENT 'Số người chơi online',
  `total_count` int NOT NULL DEFAULT '0' COMMENT 'Tổng số tài khoản',
  `create_time` datetime NOT NULL COMMENT 'Thời gian thống kê',
  PRIMARY KEY (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Thống kê máy chủ';



# Dump of table t_chat_content
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_chat_content`;

CREATE TABLE `t_chat_content` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `player_id` int NOT NULL COMMENT 'ID người chơi',
  `player_name` varchar(100) NOT NULL COMMENT 'Tên người chơi',
  `content` text NOT NULL COMMENT 'Nội dung chat',
  `chat_time` datetime NOT NULL COMMENT 'Thời gian chat',
  `server_id` int NOT NULL COMMENT 'ID máy chủ',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
  PRIMARY KEY (`id`),
  KEY `idx_player_id` (`player_id`),
  KEY `idx_server_id` (`server_id`),
  KEY `idx_chat_time` (`chat_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Bảng lưu lịch sử chat';



# Dump of table t_dept
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_dept`;

CREATE TABLE `t_dept` (
  `DEPT_ID` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `PARENT_ID` bigint NOT NULL COMMENT '上级部门ID',
  `DEPT_NAME` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '部门名称',
  `ORDER_NUM` bigint DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`DEPT_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='部门表';

LOCK TABLES `t_dept` WRITE;
/*!40000 ALTER TABLE `t_dept` DISABLE KEYS */;

INSERT INTO `t_dept` (`DEPT_ID`, `PARENT_ID`, `DEPT_NAME`, `ORDER_NUM`, `CREATE_TIME`, `MODIFY_TIME`)
VALUES
	(1,0,'Phòng phát triển',1,'2019-06-14 20:56:41',NULL),
	(2,1,'Phát triển một',1,'2019-06-14 20:58:46',NULL),
	(3,1,'Phòng phát triển 2',2,'2019-06-14 20:58:56',NULL),
	(4,0,'Phòng mua hàng',2,'2019-06-14 20:59:56',NULL),
	(5,0,'Sở Tài chính',3,'2019-06-14 21:00:08',NULL),
	(6,0,'Phòng kinh doanh',4,'2019-06-14 21:00:15',NULL),
	(7,0,'Phòng Kỹ thuật',5,'2019-06-14 21:00:42',NULL),
	(8,0,'Phòng hành chính',6,'2019-06-14 21:00:49',NULL),
	(9,0,'Phòng Nhân sự',8,'2019-06-14 21:01:14','2019-06-14 21:01:34'),
	(10,0,'Phòng hệ thống',7,'2019-06-14 21:01:31',NULL);

/*!40000 ALTER TABLE `t_dept` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_eximport
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_eximport`;

CREATE TABLE `t_eximport` (
  `FIELD1` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字段1',
  `FIELD2` int NOT NULL COMMENT '字段2',
  `FIELD3` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字段3',
  `CREATE_TIME` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='Excel导入导出测试';

LOCK TABLES `t_eximport` WRITE;
/*!40000 ALTER TABLE `t_eximport` DISABLE KEYS */;

INSERT INTO `t_eximport` (`FIELD1`, `FIELD2`, `FIELD3`, `CREATE_TIME`)
VALUES
	('字段1',1,'mrbird0@gmail.com','2019-06-13 03:14:06'),
	('字段1',2,'mrbird1@gmail.com','2019-06-13 03:14:06'),
	('字段1',3,'mrbird2@gmail.com','2019-06-13 03:14:06'),
	('字段1',4,'mrbird3@gmail.com','2019-06-13 03:14:06'),
	('字段1',5,'mrbird4@gmail.com','2019-06-13 03:14:06'),
	('字段1',6,'mrbird5@gmail.com','2019-06-13 03:14:06'),
	('字段1',7,'mrbird6@gmail.com','2019-06-13 03:14:06'),
	('字段1',8,'mrbird7@gmail.com','2019-06-13 03:14:06'),
	('字段1',9,'mrbird8@gmail.com','2019-06-13 03:14:06'),
	('字段1',10,'mrbird9@gmail.com','2019-06-13 03:14:06'),
	('字段1',11,'mrbird10@gmail.com','2019-06-13 03:14:06'),
	('字段1',12,'mrbird11@gmail.com','2019-06-13 03:14:06'),
	('字段1',13,'mrbird12@gmail.com','2019-06-13 03:14:06'),
	('字段1',14,'mrbird13@gmail.com','2019-06-13 03:14:06'),
	('字段1',15,'mrbird14@gmail.com','2019-06-13 03:14:06'),
	('字段1',16,'mrbird15@gmail.com','2019-06-13 03:14:06'),
	('字段1',17,'mrbird16@gmail.com','2019-06-13 03:14:06'),
	('字段1',18,'mrbird17@gmail.com','2019-06-13 03:14:06'),
	('字段1',19,'mrbird18@gmail.com','2019-06-13 03:14:06'),
	('字段1',20,'mrbird19@gmail.com','2019-06-13 03:14:06');

/*!40000 ALTER TABLE `t_eximport` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_generator_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_generator_config`;

CREATE TABLE `t_generator_config` (
  `id` int NOT NULL COMMENT '主键',
  `author` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '作者',
  `base_package` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '基础包名',
  `entity_package` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'entity文件存放路径',
  `mapper_package` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'mapper文件存放路径',
  `mapper_xml_package` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'mapper xml文件存放路径',
  `service_package` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'servcie文件存放路径',
  `service_impl_package` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'serviceImpl文件存放路径',
  `controller_package` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'controller文件存放路径',
  `is_trim` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '是否去除前缀 1是 0否',
  `trim_value` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '前缀内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='代码生成配置表';

LOCK TABLES `t_generator_config` WRITE;
/*!40000 ALTER TABLE `t_generator_config` DISABLE KEYS */;

INSERT INTO `t_generator_config` (`id`, `author`, `base_package`, `entity_package`, `mapper_package`, `mapper_xml_package`, `service_package`, `service_impl_package`, `controller_package`, `is_trim`, `trim_value`)
VALUES
	(1,'MrBird','cc.mrbird.febs.gen','entity','mapper','mapper','service','service.impl','controller','1','t_');

/*!40000 ALTER TABLE `t_generator_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_job`;

CREATE TABLE `t_job` (
  `JOB_ID` bigint NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `BEAN_NAME` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'spring bean名称',
  `METHOD_NAME` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '方法名',
  `PARAMS` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '参数',
  `CRON_EXPRESSION` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'cron表达式',
  `STATUS` char(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '任务状态  0：正常  1：暂停',
  `REMARK` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`JOB_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='定时任务表';

LOCK TABLES `t_job` WRITE;
/*!40000 ALTER TABLE `t_job` DISABLE KEYS */;

INSERT INTO `t_job` (`JOB_ID`, `BEAN_NAME`, `METHOD_NAME`, `PARAMS`, `CRON_EXPRESSION`, `STATUS`, `REMARK`, `CREATE_TIME`)
VALUES
	(1,'testTask','test','mrbird','0/1 * * * * ?','1','有参任务调度测试~~','2018-02-24 16:26:14'),
	(2,'testTask','test1',NULL,'0/10 * * * * ?','1','无参任务调度测试','2018-02-24 17:06:23'),
	(3,'testTask','test','hello world','0/1 * * * * ?','1','有参任务调度测试,每隔一秒触发','2018-02-26 09:28:26'),
	(11,'testTask','test2',NULL,'0/5 * * * * ?','1','测试异常','2018-02-26 11:15:30');

/*!40000 ALTER TABLE `t_job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_job_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_job_log`;

CREATE TABLE `t_job_log` (
  `LOG_ID` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `JOB_ID` bigint NOT NULL COMMENT '任务id',
  `BEAN_NAME` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'spring bean名称',
  `METHOD_NAME` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '方法名',
  `PARAMS` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '参数',
  `STATUS` char(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `ERROR` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '失败信息',
  `TIMES` decimal(11,0) DEFAULT NULL COMMENT '耗时(单位：毫秒)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`LOG_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='调度日志表';



# Dump of table t_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_log`;

CREATE TABLE `t_log` (
  `ID` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `USERNAME` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '操作用户',
  `OPERATION` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '操作内容',
  `TIME` decimal(11,0) DEFAULT NULL COMMENT '耗时',
  `METHOD` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '操作方法',
  `PARAMS` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '方法参数',
  `IP` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '操作者IP',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `location` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '操作地点',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='操作日志表';

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;

INSERT INTO `t_log` (`ID`, `USERNAME`, `OPERATION`, `TIME`, `METHOD`, `PARAMS`, `IP`, `CREATE_TIME`, `location`)
VALUES
	(1026,'MrBird','新增菜单/按钮',177,'cc.mrbird.febs.system.controller.MenuController.addMenu()',' menu: \"Menu(menuId=186, parentId=178, menuName=Lịch sử, url=/query/history, perms=history:view, icon=layui-icon-reloadtime, type=0, orderNum=2, createTime=Thu Dec 19 10:14:04 ICT 2024, modifyTime=null)\"',NULL,'2024-12-19 11:14:04',''),
	(1027,'MrBird','新增用户',26,'cc.mrbird.febs.system.controller.UserController.addUser()',' user: \"User(userId=8, username=DyKo, password=aac313bd94fb07add348039c3c1799cd, deptId=8, email=, mobile=, status=1, createTime=Wed Dec 25 02:59:23 UTC 2024, modifyTime=null, lastLoginTime=null, sex=2, avatar=default.jpg, theme=black, isTab=1, description=, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1,2,77,78,80,79, roleName=null, gsEntity=null, toSendGsIds=null)\"','116.108.131.87','2024-12-25 10:59:24','越南|0|0|0|0'),
	(1028,'DyKo','新增菜单/按钮',27,'cc.mrbird.febs.system.controller.MenuController.addMenu()',' menu: \"Menu(menuId=187, parentId=179, menuName=Setup even, url=/op/activity, perms=activity:view, icon=layui-icon-wrench, type=0, orderNum=1, createTime=Wed Dec 25 03:03:21 UTC 2024, modifyTime=null)\"',NULL,'2024-12-25 11:03:21',''),
	(1029,'DyKo','新增菜单/按钮',9,'cc.mrbird.febs.system.controller.MenuController.addMenu()',' menu: \"Menu(menuId=188, parentId=179, menuName=Setup even, url=/op/activity, perms=activity:view, icon=layui-icon-wrench, type=0, orderNum=1, createTime=Wed Dec 25 03:03:50 UTC 2024, modifyTime=null)\"','116.108.131.87','2024-12-25 11:03:50','越南|0|0|0|0'),
	(1030,'DyKo','删除菜单/按钮',27,'cc.mrbird.febs.system.controller.MenuController.deleteMenus()',' menuIds: \"187\"','116.108.131.87','2024-12-25 11:03:58','越南|0|0|0|0'),
	(1031,'DyKo','删除菜单/按钮',11,'cc.mrbird.febs.system.controller.MenuController.deleteMenus()',' menuIds: \"188\"','116.108.131.87','2024-12-25 11:04:06','越南|0|0|0|0'),
	(1032,'DyKo','新增菜单/按钮',8,'cc.mrbird.febs.system.controller.MenuController.addMenu()',' menu: \"Menu(menuId=189, parentId=182, menuName=Sửa event, url=null, perms=activity:add, icon=null, type=1, orderNum=null, createTime=Wed Dec 25 03:05:06 UTC 2024, modifyTime=null)\"','116.108.131.87','2024-12-25 11:05:07','越南|0|0|0|0'),
	(1033,'MrBird','发送邮件附件',26,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10\', validity=30}',NULL,'2024-12-26 16:26:44',''),
	(1034,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10 35001|10\n35001|10\n\', validity=30}','116.108.131.87','2024-12-26 16:27:12','越南|0|0|0|0'),
	(1035,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10\n35001|10\n35001|10\n\', validity=30}','116.108.131.87','2024-12-26 16:27:19','越南|0|0|0|0'),
	(1036,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10,\n\', validity=30}','116.108.131.87','2024-12-26 16:27:30','越南|0|0|0|0'),
	(1037,'MrBird','发送邮件附件',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10\n\', validity=30}','116.108.131.87','2024-12-26 16:27:34','越南|0|0|0|0'),
	(1038,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10 35001|10\n\', validity=30}','116.108.131.87','2024-12-26 16:27:37','越南|0|0|0|0'),
	(1039,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10;35001|10\n\', validity=30}',NULL,'2024-12-26 16:27:42',''),
	(1040,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10 - 35001|10\n\', validity=30}','116.108.131.87','2024-12-26 16:27:49','越南|0|0|0|0'),
	(1041,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10 - 35001|10\n\', validity=30}','116.108.131.87','2024-12-26 16:27:54','越南|0|0|0|0'),
	(1042,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35001|10 - 35001|10\n\', validity=30}','116.108.131.87','2024-12-26 16:27:59','越南|0|0|0|0'),
	(1043,'MrBird','发送邮件附件',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35002|10\', validity=30}','116.108.131.87','2024-12-26 16:30:21','越南|0|0|0|0'),
	(1044,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35002|10 35002|10\', validity=30}','116.108.131.87','2024-12-26 16:30:32','越南|0|0|0|0'),
	(1045,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35002|10, 35001|10\', validity=30}','116.108.131.87','2024-12-26 16:32:05','越南|0|0|0|0'),
	(1046,'MrBird','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35002,35001|5,1\', validity=30}','116.108.131.87','2024-12-26 16:33:19','越南|0|0|0|0'),
	(1047,'MrBird','发送邮件附件',0,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35002,35001|5\', validity=30}','116.108.131.87','2024-12-26 16:33:25','越南|0|0|0|0'),
	(1048,'MrBird','发送邮件附件',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'Mimi\', mailTitle=\'Test \', mailCont=\'test vui\', attch=\'35031|10-35032|5\', validity=30}','116.108.131.87','2024-12-26 16:33:58','越南|0|0|0|0'),
	(1049,'MrBird','发送邮件附件',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'22\', sender=\'ANH TÚ\', mailTitle=\'CHO EM OWEN\', mailCont=\'DEMQUAEMTUYETLAM\', attch=\'10105|1\', validity=30}','116.108.131.87','2024-12-26 17:00:32','越南|0|0|0|0'),
	(1050,'MrBird','发送邮件附件',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'ANH TÚ\', mailTitle=\'CHO EM OWEN\', mailCont=\'DEMQUAEMTUYETLAM\', attch=\'10105|1\', validity=30}','116.108.131.87','2024-12-26 17:00:47','越南|0|0|0|0'),
	(1051,'MrBird','发送邮件附件',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'ANH TÚ\', mailTitle=\'CHO EM OWEN\', mailCont=\'DEMQUAEMTUYETLAM\', attch=\'10105|15\', validity=30}','116.108.131.87','2024-12-26 17:01:04','越南|0|0|0|0'),
	(1052,'DyKo','发送邮件附件',20,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'143, 140\', sender=\'alo alo\', mailTitle=\'Aloal\', mailCont=\'test\', attch=\'Ok\', validity=30}',NULL,'2024-12-26 17:21:29',''),
	(1053,'DyKo','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'143, 140\', sender=\'alo alo\', mailTitle=\'Aloal\', mailCont=\'test\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:22:53','越南|0|0|0|0'),
	(1054,'DyKo','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'143, 140\', sender=\'alo alo\', mailTitle=\'Aloal\', mailCont=\'test\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:23:01','越南|0|0|0|0'),
	(1055,'DyKo','发送邮件附件',8,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'143\n140\', sender=\'alo alo\', mailTitle=\'Aloal\', mailCont=\'test\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:23:16','越南|0|0|0|0'),
	(1056,'DyKo','发送邮件附件',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[2], addressee=1, receiveId=\'143\', sender=\'alo alo\', mailTitle=\'Aloal\', mailCont=\'test\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:24:03','越南|0|0|0|0'),
	(1057,'DyKo','发送邮件附件',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[2], addressee=1, receiveId=\'143\', sender=\'alo alo\', mailTitle=\'Aloal\', mailCont=\'test\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:24:26','越南|0|0|0|0'),
	(1058,'DyKo','发送邮件附件',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'143\n140\', sender=\'alo alo\', mailTitle=\'Aloal\', mailCont=\'test\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:25:10','越南|0|0|0|0'),
	(1059,'DyKo','发送邮件附件',32,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[2], addressee=1, receiveId=\'143\', sender=\'ho\', mailTitle=\'op\', mailCont=\'da\', attch=\'38013|10\', validity=30}',NULL,'2024-12-26 17:28:44',''),
	(1060,'DyKo','发送邮件附件',11,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'143\n140\', sender=\'ho\', mailTitle=\'op\', mailCont=\'da\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:29:09','越南|0|0|0|0'),
	(1061,'DyKo','发送邮件附件',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'143\n140\', sender=\'DM HN\', mailTitle=\'DM HN\', mailCont=\'DM HN\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:29:40','越南|0|0|0|0'),
	(1062,'DyKo','发送邮件附件',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'143\n140\', sender=\'DM HN\', mailTitle=\'DM HN\', mailCont=\'DM HN\', attch=\'38013|10\', validity=30}','116.108.131.87','2024-12-26 17:37:14','越南|0|0|0|0'),
	(1063,'DyKo','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'140,143\', sender=\'ha\', mailTitle=\'ha\', mailCont=\'ha\', attch=\'30002|10\', validity=30}','116.108.131.87','2024-12-26 18:28:11','越南|0|0|0|0'),
	(1064,'DyKo','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'140,143\', sender=\'ha\', mailTitle=\'ha\', mailCont=\'ha\', attch=\'30002|10\', validity=30}','116.108.131.87','2024-12-26 18:28:29','越南|0|0|0|0'),
	(1065,'DyKo','发送邮件附件',0,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'140|143\', sender=\'ha\', mailTitle=\'ha\', mailCont=\'ha\', attch=\'30002|10\', validity=30}','116.108.131.87','2024-12-26 18:28:37','越南|0|0|0|0'),
	(1066,'DyKo','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'140,143\', sender=\'ha\', mailTitle=\'ha\', mailCont=\'ha\', attch=\'30002|10\', validity=30}','116.108.131.87','2024-12-26 18:29:24','越南|0|0|0|0'),
	(1067,'DyKo','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'140,143\', sender=\'ha\', mailTitle=\'ha\', mailCont=\'ha\', attch=\'30002|10\', validity=30}','116.108.131.87','2024-12-26 18:29:34','越南|0|0|0|0'),
	(1068,'DyKo','发送邮件附件',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'140;143\', sender=\'ha\', mailTitle=\'ha\', mailCont=\'ha\', attch=\'30002|10\', validity=30}','116.108.131.87','2024-12-26 18:29:42','越南|0|0|0|0'),
	(1069,'MrBird','发送邮件附件',252,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'55\n66\', sender=\'TÚ TEST\', mailTitle=\'TÚ TEST\', mailCont=\'TÚ TEST\', attch=\'30001|10\', validity=30}','116.108.131.87','2024-12-27 12:14:15','越南|0|0|0|0'),
	(1070,'MrBird','发送邮件附件',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'55\n66\', sender=\'TÚ TEST\', mailTitle=\'TÚ TEST\', mailCont=\'TÚ TEST\', attch=\'30001|10\', validity=30}','116.108.131.87','2024-12-27 12:15:07','越南|0|0|0|0'),
	(1071,'MrBird','发送邮件附件',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=1, receiveId=\'55\n66\', sender=\'TÚ TEST\', mailTitle=\'TÚ TEST\', mailCont=\'TÚ TEST\', attch=\'30001|10\', validity=30}','116.108.131.87','2024-12-27 12:20:46','越南|0|0|0|0'),
	(1072,'MrBird','发送邮件附件',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2], addressee=2, receiveId=\'55\n66\', sender=\'TÚ TEST\', mailTitle=\'TÚ TEST\', mailCont=\'TÚ TEST\', attch=\'30001|10\', validity=30}','116.108.131.87','2024-12-27 12:23:37','越南|0|0|0|0'),
	(1073,'DyKo','Gửi tệp đính kèm email',25,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=null, addressee=1, receiveId=\'143\', sender=\'helo \', mailTitle=\'helo\', mailCont=\'hello cm\', attch=\'33002|10\', validity=\'2024-12-28 00:01:01\'}',NULL,'2024-12-27 13:13:12',''),
	(1074,'DyKo','Gửi tệp đính kèm email',9,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[2], addressee=1, receiveId=\'143\', sender=\'helo \', mailTitle=\'helo\', mailCont=\'hello cm\', attch=\'33002|10\', validity=\'2024-12-28 00:01:01\'}','116.108.131.87','2024-12-27 13:13:17','越南|0|0|0|0'),
	(1075,'DyKo','Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[2], addressee=2, receiveId=\'\', sender=\'helo \', mailTitle=\'helo\', mailCont=\'hello cm\', attch=\'33002|10\', validity=\'2024-12-28 00:01:01\'}','116.108.131.87','2024-12-27 13:13:30','越南|0|0|0|0'),
	(1076,'DyKo','Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[2], addressee=2, receiveId=\'\', sender=\'helo alll\', mailTitle=\'helo all\', mailCont=\'hello cm\', attch=\'33002|10\', validity=\'2024-12-28 00:01:01\'}','116.108.131.87','2024-12-27 13:13:37','越南|0|0|0|0'),
	(1077,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[2], addressee=1, receiveId=\'143\', sender=\'eq\', mailTitle=\'qe\', mailCont=\'adad\', attch=\'33003|2\', validity=\'2024-12-27 12:51:00\'}','116.108.131.87','2024-12-27 13:49:38','越南|0|0|0|0'),
	(1078,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[2], addressee=1, receiveId=\'143\', sender=\'eqeq\', mailTitle=\'qeeq\', mailCont=\'adadeqe\', attch=\'33003|2\', validity=\'2024-12-27 12:51:00\'}','116.108.131.87','2024-12-27 13:50:00','越南|0|0|0|0'),
	(1079,'MrBird','Gửi tệp đính kèm email',151,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'55\', sender=\'TEST TIME\', mailTitle=\'TEST TIME\', mailCont=\'TEST TIME\', attch=\'30001|10\', validity=\'2024-12-27 13:33:00\'}',NULL,'2024-12-27 14:31:39',''),
	(1080,'MrBird','Gửi tệp đính kèm email',180,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'55\', sender=\'TEST TIME\', mailTitle=\'TEST TIME\', mailCont=\'TEST TIME\', attch=\'30001|10\', validity=\'2024-12-27 13:36:00\'}','116.108.131.87','2024-12-27 14:35:17','越南|0|0|0|0'),
	(1081,'MrBird','Gửi tệp đính kèm email',179,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'55\', sender=\'TEST TIME\', mailTitle=\'TEST TIME\', mailCont=\'TEST TIME\', attch=\'30001|10\', validity=\'2024-12-28 13:36:00\'}','116.108.131.87','2024-12-27 14:38:32','越南|0|0|0|0'),
	(1082,'MrBird','Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'55\', sender=\'TEST TIME\', mailTitle=\'TEST TIME\', mailCont=\'TEST TIME\', attch=\'30001|10\', validity=\'2024-12-28 13:36:00\'}','116.108.131.87','2024-12-27 14:45:34','越南|0|0|0|0'),
	(1083,'MrBird','Gửi tệp đính kèm email',11,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'55\', sender=\'TEST TIME\', mailTitle=\'TEST TIME\', mailCont=\'TEST TIME\', attch=\'30001|10\', validity=\'2024-12-28 13:36:00\'}','116.108.131.87','2024-12-27 14:49:27','越南|0|0|0|0'),
	(1084,'MrBird','Gửi tệp đính kèm email',9,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'55\', sender=\'TEST TIME\', mailTitle=\'TEST TIME\', mailCont=\'TEST TIME\', attch=\'30001|10\', validity=\'2024-12-30 13:36:00\'}','116.108.131.87','2024-12-27 14:56:49','越南|0|0|0|0'),
	(1085,'MrBird','Gửi tệp đính kèm email',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'55\', sender=\'TEST TIME 2\', mailTitle=\'TEST TIME 2\', mailCont=\'TEST TIME 2\', attch=\'30001|10\', validity=\'2024-12-30 13:36:00\'}','116.108.131.87','2024-12-27 14:57:02','越南|0|0|0|0'),
	(1086,'MrBird','Gửi tệp đính kèm email',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'55\', sender=\'TEST TIME 3\', mailTitle=\'TEST TIME 3\', mailCont=\'TEST TIME 3\', attch=\'30001|10\', validity=\'2024-12-31 13:36:00\'}','116.108.131.87','2024-12-27 14:57:20','越南|0|0|0|0'),
	(1087,'MrBird','Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'test\', mailTitle=\'test den chet \', mailCont=\'test den chet \', attch=\'33004|3\', validity=\'2024-12-27 14:05:00\'}','116.108.131.87','2024-12-27 15:00:42','越南|0|0|0|0'),
	(1088,'MrBird','Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'140\', sender=\'test\', mailTitle=\'Quà Top 1 Đua Top LC Tháng 11-2024\', mailCont=\'Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. \', attch=\'33004|3\', validity=\'2024-12-27 15:05:00\'}',NULL,'2024-12-27 15:06:10',''),
	(1089,'MrBird','Gửi tệp đính kèm email',8,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'test\', mailTitle=\'Quà Top 1 Đua Top LC Tháng 11-2024\', mailCont=\'Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. \', attch=\'33004|3\', validity=\'2024-12-28 15:05:00\'}','116.108.131.87','2024-12-27 15:39:17','越南|0|0|0|0'),
	(1090,'MrBird','Gửi tệp đính kèm email',17,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'test\', mailTitle=\'Quà Top 1 Đua Top LC Tháng 11-2024Quà Top 1 Đua To\', mailCont=\'Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. Tiểu Thi xin gửi Quà Top 1 Đua Top LC Tháng 11. Chúc bạn chơi game vui vẻ Để nhận GGold quà đua Top, Vui lòng liên hệ FANPAGE để gửi mã xác thực là: ABCD. \', attch=\'33004|3\', validity=\'2024-12-28 15:05:00\'}','116.108.131.87','2024-12-27 15:40:16','越南|0|0|0|0'),
	(1091,NULL,'Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'sdadad\', mailTitle=\'sdadadad\', mailCont=\'Công nghệ hiện đại đã thay đổi sâu sắc cách chúng ta sống, làm việc và tương tác. Trong vài thập kỷ qua, sự phát triển vượt bậc của Internet, trí tuệ nhân tạo (AI), và các thiết bị thông minh đã mở ra những cánh cửa mới cho con người, đồng thời đặt ra những thách thức không nhỏ. Internet không chỉ cung cấp một kho tàng thông tin khổng lồ, mà còn là công cụ giúp kết nối con người trên toàn cầu, vượt qua mọi giới hạn địa lý. Nhờ các nền tảng trực tuyến, những mối quan hệ cá nhân và chuyên nghiệp được thiết lập và duy trì dễ dàng hơn bao giờ hết. Tuy nhiên, cùng với đó là sự phụ thuộc ngày càng lớn vào công nghệ, dẫn đến nhiều vấn đề như mất cân bằng trong đời sống, giảm tương tác trực tiếp và sự xâm phạm quyền riêng tư. Trong lĩnh vực giáo dục, công nghệ cũng đang thay đổi cách thức dạy và học. Học sinh không còn chỉ học qua sách vở mà còn có thể tham gia các lớp học trực tuyến, làm việc nhóm từ xa hoặc sử dụng AI để giải quyết các bài toán phức tạp. Mặt khác, công nghệ y tế đã cứu sống hàng triệu người nhờ các phương pháp điều trị tiên tiến và thiết bị thông minh hỗ trợ theo dõi sức khỏe. Nhưng, những vấn đề như bảo mật thông tin cá nhân hay khoảng cách kỹ thuật số giữa các quốc gia vẫn còn đó. Việc cân bằng giữa tận dụng công nghệ và kiểm soát các tác động tiêu cực của nó là trách nhiệm không chỉ của các nhà phát triển mà còn của toàn xã hội. Sự tiến bộ không ngừng của công nghệ mang lại hy vọng về một tương lai tốt đẹp hơn, nhưng cũng đòi hỏi chúng ta phải có những bước đi cẩn trọng và đầy trách nhiệm.\', attch=\'33005|10\', validity=\'2024-12-29 00:00:00\'}','116.108.131.87','2024-12-27 17:21:13','越南|0|0|0|0'),
	(1092,NULL,'Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'sdadad\', mailTitle=\'sdadadad\', mailCont=\'Công nghệ hiện đại đã thay đổi sâu sắc cách chúng ta sống, làm việc và tương tác. Trong vài thập kỷ qua, sự phát triển vượt bậc của Internet, trí tuệ nhân tạo (AI), và các thiết bị thông minh đã mở ra những cánh cửa mới cho con người, đồng thời đặt ra những thách thức không nhỏ. Internet không chỉ cung cấp một kho tàng thông tin khổng lồ, mà còn là công cụ giúp kết nối con người trên toàn cầu, vượt qua mọi giới hạn địa lý. Nhờ các nền tảng trực tuyến, những mối quan hệ cá nhân và chuyên nghiệp được thiết lập và duy trì dễ dàng hơn bao giờ hết. Tuy nhiên, cùng với đó là sự phụ thuộc ngày càng lớn vào công nghệ, dẫn đến nhiều vấn đề như mất cân bằng trong đời sống, giảm tương tác trực tiếp và sự xâm phạm quyền riêng tư. Trong lĩnh vực giáo dục, công nghệ cũng đang thay đổi cách thức dạy và học. Học sinh không còn chỉ học qua sách vở mà còn có thể tham gia các lớp học trực tuyến, làm việc nhóm từ xa hoặc sử dụng AI để giải quyết các bài toán phức tạp. Mặt khác, công nghệ y tế đã cứu sống.\', attch=\'33005|10\', validity=\'2024-12-29 00:00:00\'}','116.108.131.87','2024-12-27 17:22:41','越南|0|0|0|0'),
	(1093,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'0123456789012345678901234567890123456789\', mailTitle=\'0123456789012345678901234567890123456789\', mailCont=\'Công nghệ hiện đại đã thay đổi sâu sắc cách chúng ta sống, làm việc và tương tác. Trong vài thập kỷ qua, sự phát triển vượt bậc của Internet, trí tuệ nhân tạo (AI), và các thiết bị thông minh đã mở ra những cánh cửa mới cho con người, đồng thời đặt ra những thách thức không nhỏ. Internet không chỉ cung cấp một kho tàng thông tin khổng lồ, mà còn là công cụ giúp kết nối con người trên toàn cầu, vượt qua mọi giới hạn địa lý. Nhờ các nền tảng trực tuyến, những mối quan hệ cá nhân và chuyên nghiệp được thiết lập và duy trì dễ dàng hơn bao giờ hết. Tuy nhiên, cùng với đó là sự phụ thuộc ngày càng lớn vào công nghệ, dẫn đến nhiều vấn đề như mất cân bằng trong đời sống, giảm tương tác trực tiếp và sự xâm phạm quyền riêng tư. Trong lĩnh vực giáo dục, công nghệ cũng đang thay đổi cách thức dạy và học. Học sinh không còn chỉ học qua sách vở mà còn có thể tham gia các lớp học trực tuyến, làm việc nhóm từ xa hoặc sử dụng AI để giải quyết các bài toán phức tạp. Mặt khác, công nghệ y tế đã cứu sống.\', attch=\'33005|10\', validity=\'2024-12-29 00:00:00\'}','116.108.131.87','2024-12-27 17:23:27','越南|0|0|0|0'),
	(1094,NULL,'Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'0123456789012345678901234567890123456789\', mailTitle=\'0123456789012345678901234567890123456789\', mailCont=\'Công nghệ hiện đại đã thay đổi sâu sắc cách chúng ta sống, làm việc và tương tác. Trong vài thập kỷ qua, sự phát triển vượt bậc của Internet, trí tuệ nhân tạo (AI), và các thiết bị thông minh đã mở ra những cánh cửa mới cho con người, đồng thời đặt ra những thách thức không nhỏ. Internet không chỉ cung cấp một kho tàng thông tin khổng lồ, mà còn là công cụ giúp kết nối con người trên toàn cầu, vượt qua mọi giới hạn địa lý. Nhờ các nền tảng trực tuyến, những mối quan hệ cá nhân và chuyên nghiệp được thiết lập và duy trì dễ dàng hơn bao giờ hết. Tuy nhiên, cùng với đó là sự phụ thuộc ngày càng lớn vào công nghệ, dẫn đến nhiều vấn đề như mất cân bằng trong đời sống, giảm tương tác trực tiếp và sự xâm phạm quyền riêng tư. Trong lĩnh vực giáo dục, công nghệ cũng đang thay đổi cách thức dạy và học. Học sinh không còn chỉ học qua sách vở mà còn có thể tham gia các lớp học trực tuyến, làm việc nhóm từ xa hoặc sử dụng AI để giải quyết các bài toán phức tạp. Mặt khác, công nghệ y tế đã cứu sống.\', attch=\'33005|10-33006|10-33007|10-33008|10-33009|10-33010|10-33011|10-33012|10-33013|10-33014|10-33015|10-33016|10-33017|10-33101|10-33102|10-33103|10\', validity=\'2024-12-29 00:00:00\'}','116.108.131.87','2024-12-27 17:39:38','越南|0|0|0|0'),
	(1095,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'32\', sender=\'0123456789012345678901234567890123456789\', mailTitle=\'0123456789012345678901234567890123456789\', mailCont=\'Công nghệ hiện đại đã thay đổi sâu sắc cách chúng ta sống, làm việc và tương tác. Trong vài thập kỷ qua, sự phát triển vượt bậc của Internet, trí tuệ nhân tạo (AI), và các thiết bị thông minh đã mở ra những cánh cửa mới cho con người, đồng thời đặt ra những thách thức không nhỏ. Internet không chỉ cung cấp một kho tàng thông tin khổng lồ, mà còn là công cụ giúp kết nối con người trên toàn cầu, vượt qua mọi giới hạn địa lý. Nhờ các nền tảng trực tuyến, những mối quan hệ cá nhân và chuyên nghiệp được thiết lập và duy trì dễ dàng hơn bao giờ hết. Tuy nhiên, cùng với đó là sự phụ thuộc ngày càng lớn vào công nghệ, dẫn đến nhiều vấn đề như mất cân bằng trong đời sống, giảm tương tác trực tiếp và sự xâm phạm quyền riêng tư. Trong lĩnh vực giáo dục, công nghệ cũng đang thay đổi cách thức dạy và học. Học sinh không còn chỉ học qua sách vở mà còn có thể tham gia các lớp học trực tuyến, làm việc nhóm từ xa hoặc sử dụng AI để giải quyết các bài toán phức tạp. Mặt khác, công nghệ y tế đã cứu sống.\', attch=\'33005|10-33006|10-33007|10-33008|10-33009|10-33010|10-33011|10-33012|10-33013|10-33014|10-33015|10-33016|10-33017|10-33101|10-33102|10-33103|10\', validity=\'2024-12-29 00:00:00\'}','116.108.131.87','2024-12-27 17:39:47','越南|0|0|0|0'),
	(1096,NULL,'Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'32\', sender=\'0123456789012345678901234567890123456789\', mailTitle=\'0123456789012345678901234567890123456789\', mailCont=\'Công nghệ hiện đại đã thay đổi sâu sắc cách chúng ta sống, làm việc và tương tác. Trong vài thập kỷ qua, sự phát triển vượt bậc của Internet, trí tuệ nhân tạo (AI), và các thiết bị thông minh đã mở ra những cánh cửa mới cho con người, đồng thời đặt ra những thách thức không nhỏ. Internet không chỉ cung cấp một kho tàng thông tin khổng lồ, mà còn là công cụ giúp kết nối con người trên toàn cầu, vượt qua mọi giới hạn địa lý. Nhờ các nền tảng trực tuyến, những mối quan hệ cá nhân và chuyên nghiệp được thiết lập và duy trì dễ dàng hơn bao giờ hết. Tuy nhiên, cùng với đó là sự phụ thuộc ngày càng lớn vào công nghệ, dẫn đến nhiều vấn đề như mất cân bằng trong đời sống, giảm tương tác trực tiếp và sự xâm phạm quyền riêng tư. Trong lĩnh vực giáo dục, công nghệ cũng đang thay đổi cách thức dạy và học. Học sinh không còn chỉ học qua sách vở mà còn có thể tham gia các lớp học trực tuyến, làm việc nhóm từ xa hoặc sử dụng AI để giải quyết các bài toán phức tạp. Mặt khác, công nghệ y tế đã cứu sống.\', attch=\'33005|10-33006|10-33007|10-33008|10-33009|10-33010|10-33011|10-33012|10-33013|10-33014|10-33015|10-33016|10-33017|10-33101|10-33102|10-33103|10\', validity=\'2024-12-29 00:00:00\'}','116.108.131.87','2024-12-27 17:41:04','越南|0|0|0|0'),
	(1097,'DyKo','Gửi tệp đính kèm email',32,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'\', sender=\'Admin nè người đẹp\', mailTitle=\'Gửi quá ok chưa\', mailCont=\'Giàu nên gửi 20 loại items\', attch=\'33001|10-33002|10-33003|10-33004|10-33005|10-33006|10-33007|10-33008|10-33009|10-33010|10-33011|10-33012|10-33013|10-33014|10-33015|10-33016|10-33017|10-33101|10-33102|10-33103|10\', validity=\'2024-12-31 00:00:00\'}',NULL,'2024-12-27 17:47:21',''),
	(1098,'DyKo','Gửi tệp đính kèm email',23,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=null, addressee=2, receiveId=\'\', sender=\'Test\', mailTitle=\'Gửi quà\', mailCont=\'Gửi 60 items khác nhau\', attch=\'33001|10-33002|10-33003|10-33004|10-33005|10-33006|10-33007|10-33008|10-33009|10-33010|10-33011|10-33012|10-33013|10-33014|10-33015|10-33016|10-33017|10-33101|10-33102|10-33103|10-33104|10-33105|10-33106|10-33107|10-33108|10-33109|10-33110|10-33111|10-33112|10-33113|10-33114|10-33115|10-33116|10-33117|10-33201|10-33202|10-33203|10-33204|10-33205|10-33206|10-33207|10-33208|10-33209|10-33210|10-33211|10-33212|10-33213|10-33214|10-33215|10-33216|10-33217|10-33301|10-33302|10-33303|10-33304|10-33305|10-33306|10-33307|10-33308|10-33309|10\', validity=\'2024-12-31 00:00:00\'}',NULL,'2024-12-27 17:54:16',''),
	(1099,'DyKo','Gửi tệp đính kèm email',10,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'\', sender=\'Test\', mailTitle=\'Gửi quà\', mailCont=\'Gửi 60 items khác nhau\', attch=\'33001|10-33002|10-33003|10-33004|10-33005|10-33006|10-33007|10-33008|10-33009|10-33010|10-33011|10-33012|10-33013|10-33014|10-33015|10-33016|10-33017|10-33101|10-33102|10-33103|10-33104|10-33105|10-33106|10-33107|10-33108|10-33109|10-33110|10-33111|10-33112|10-33113|10-33114|10-33115|10-33116|10-33117|10-33201|10-33202|10-33203|10-33204|10-33205|10-33206|10-33207|10-33208|10-33209|10-33210|10-33211|10-33212|10-33213|10-33214|10-33215|10-33216|10-33217|10-33301|10-33302|10-33303|10-33304|10-33305|10-33306|10-33307|10-33308|10-33309|10\', validity=\'2024-12-31 00:00:00\'}','116.108.131.87','2024-12-27 17:54:26','越南|0|0|0|0'),
	(1100,'DyKo','Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'\', sender=\'Test\', mailTitle=\'Gửi quà\', mailCont=\'Gửi 60 items khác nhau\', attch=\'33001|10-33002|10-33003|10-33004|10-33005|10-33006|10-33007|10-33008|10-33009|10-33010|10-33011|10-33012|10-33013|10-33014|10-33015|10-33016|10-33017|10-33101|10-33102|10-33103|10-33104|10-33105|10-33106|10-33107|10-33108|10-33109|10-33110|10-33111|10-33112|10-33113|10-33114|10-33115|10-33116|10-33117|10-33201|10-33202|10-33203|10-33204|10-33205|10-33206|10-33207|10-33208|10-33209|10-33210|10-33211|10-33212|10-33213|10-33214|10-33215|10-33216|10-33217|10-33301|10-33302|10-33303|10-33304|10-33305|10-33306|10-33307|10-33308|10-33309|10\', validity=\'2024-12-31 00:00:00\'}','116.108.131.87','2024-12-27 17:54:27','越南|0|0|0|0'),
	(1101,'MrBird','Gửi tệp đính kèm email',33,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'32\', sender=\'TEST 123\', mailTitle=\'TEST 123\', mailCont=\'TEST 123\', attch=\'0|1\', validity=\'2024-12-28 00:00:00\'}',NULL,'2024-12-27 18:31:31',''),
	(1102,'DyKo','Gửi tệp đính kèm email',21,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'\', sender=\'test\', mailTitle=\'Test\', mailCont=\'dada\', attch=\'33001|10\n33002|10\n33003|10\n33004|10\n33005|10\', validity=\'2024-12-31 00:00:00\'}',NULL,'2024-12-27 18:42:26',''),
	(1103,'DyKo','Gửi tệp đính kèm email',8,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'\', sender=\'test\', mailTitle=\'Test\', mailCont=\'dada\', attch=\'33001|10-33002|10-33003|10-33004|10-33005|10\', validity=\'2024-12-31 00:00:00\'}','116.108.131.87','2024-12-27 18:42:39','越南|0|0|0|0'),
	(1104,'DyKo','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'\', sender=\'test\', mailTitle=\'Test\', mailCont=\'dada\', attch=\'33001|10-33002|10-33003|10-33004|10-33005|10;33007|10\', validity=\'2024-12-31 00:00:00\'}','116.108.131.87','2024-12-27 18:43:14','越南|0|0|0|0'),
	(1105,'DyKo','Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'\', sender=\'test\', mailTitle=\'Test\', mailCont=\'dada\', attch=\'33001|10-33002|10-33003|10-33004|10-33005|10_33007|10\', validity=\'2024-12-31 00:00:00\'}','116.108.131.87','2024-12-27 18:43:18','越南|0|0|0|0'),
	(1106,'DyKo','Gửi tệp đính kèm email',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'\', sender=\'test\', mailTitle=\'Test\', mailCont=\'dada\', attch=\'33001|10-33002|10-33003|10-33004|10\n\', validity=\'2024-12-31 00:00:00\'}','116.108.131.87','2024-12-27 18:43:33','越南|0|0|0|0'),
	(1107,'MrBird','新增角色',18,'cc.mrbird.febs.system.controller.RoleController.addRole()',' role: \"Role(roleId=81, roleName=MrBird, remark=, createTime=Fri Dec 27 11:52:41 UTC 2024, modifyTime=null, menuIds=)\"',NULL,'2024-12-27 19:52:42',''),
	(1108,'MrBird','删除角色',40,'cc.mrbird.febs.system.controller.RoleController.deleteRoles()',' roleIds: \"81\"','116.108.131.87','2024-12-27 19:52:48','越南|0|0|0|0'),
	(1109,'MrBird','修改用户',61,'cc.mrbird.febs.system.controller.UserController.updateUser()',' user: \"User(userId=1, username=null, password=null, deptId=1, email=mrbird@qq.com, mobile=17788888888, status=1, createTime=null, modifyTime=Fri Dec 27 11:55:16 UTC 2024, lastLoginTime=null, sex=0, avatar=null, theme=null, isTab=null, description=aaaaa, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1,2,77,78,79,80, roleName=null, gsEntity=null, toSendGsIds=null)\"',NULL,'2024-12-27 19:55:17',''),
	(1110,'MrBird','Cấm tài khoản và tắt tiếng',146,'cc.mrbird.febs.system.controller.OperationController.fenghaoSend()',' fenghaoConfig: FenghaoConfig{opType=1, playerId=55, endTime=2024-12-28}',NULL,'2024-12-27 20:22:07',''),
	(1111,'MrBird','Cấm tài khoản và tắt tiếng',5,'cc.mrbird.febs.system.controller.OperationController.fenghaoSend()',' fenghaoConfig: FenghaoConfig{opType=2, playerId=55, endTime=2024-12-28}','116.108.131.87','2024-12-27 20:22:21','越南|0|0|0|0'),
	(1112,'DyKo','Cấm tài khoản và tắt tiếng',1,'cc.mrbird.febs.system.controller.OperationController.fenghaoSend()',' fenghaoConfig: FenghaoConfig{opType=1, playerId=140, endTime=2024-12-28}','116.108.131.87','2024-12-27 20:22:59','越南|0|0|0|0'),
	(1113,'DyKo','Cấm tài khoản và tắt tiếng',5,'cc.mrbird.febs.system.controller.OperationController.fenghaoSend()',' fenghaoConfig: FenghaoConfig{opType=1, playerId=140, endTime=2024-12-28}','116.108.131.87','2024-12-27 20:23:06','越南|0|0|0|0'),
	(1114,'DyKo','Cấm tài khoản và tắt tiếng',4,'cc.mrbird.febs.system.controller.OperationController.fenghaoSend()',' fenghaoConfig: FenghaoConfig{opType=2, playerId=140, endTime=2024-12-28}','116.108.131.87','2024-12-27 20:23:32','越南|0|0|0|0'),
	(1115,'MrBird','Gửi tệp đính kèm email',37,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'44\', sender=\'Test\', mailTitle=\'Test\', mailCont=\'Test\', attch=\'30001|10\', validity=\'2024-12-29 16:44:50\'}',NULL,'2024-12-29 17:44:51',''),
	(1116,'MrBird','Truy vấn thống kê máy chủ',29,'cc.mrbird.febs.system.controller.ServerStatsController.getServerStatsList()',' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" serverStats: \"ServerStats(serverId=null, serverName=null, onlineCount=null, totalCount=null, createTime=null)\"',NULL,'2024-12-29 20:12:29',''),
	(1117,'MrBird','Truy vấn thống kê máy chủ',6,'cc.mrbird.febs.system.controller.ServerStatsController.getServerStatsList()',' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" serverStats: \"ServerStats(serverId=null, serverName=null, onlineCount=null, totalCount=null, createTime=null)\"','116.108.131.87','2024-12-29 20:12:48','越南|0|0|0|0'),
	(1118,'MrBird','Gửi tệp đính kèm email',98,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'57\', sender=\'TESST\', mailTitle=\'TESST\', mailCont=\'TESST\', attch=\'30001|10\', validity=\'2024-12-31 11:07:42\'}',NULL,'2024-12-31 12:07:44',''),
	(1119,'MrBird','Gửi tệp đính kèm email',101,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'57\', sender=\'TEST TIME\', mailTitle=\'TEST TIME\', mailCont=\'TEST TIME\', attch=\'30001|2\', validity=\'2025-01-01 00:00:00\'}',NULL,'2024-12-31 12:23:17',''),
	(1120,'MrBird','Gửi tệp đính kèm email',89,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'57\', sender=\'Test\', mailTitle=\'Test\', mailCont=\'Test\', attch=\'30001|10\', validity=\'2025-01-01 00:00:00\'}',NULL,'2024-12-31 12:26:18',''),
	(1121,'MrBird','Gửi tệp đính kèm email',37,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=null, addressee=1, receiveId=\'57\', sender=\'test\', mailTitle=\'test\', mailCont=\'test\', attch=\'30001|10\', validity=\'2025-01-01 00:00:00\'}',NULL,'2024-12-31 12:29:17',''),
	(1122,'MrBird','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=null, addressee=1, receiveId=\'57\', sender=\'test\', mailTitle=\'test\', mailCont=\'test\', attch=\'30001|10\', validity=\'2025-01-01 00:00:00\'}','192.168.2.68','2024-12-31 12:29:33','内网IP|0|0|内网IP|内网IP'),
	(1123,'MrBird','Gửi tệp đính kèm email',263,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'57\', sender=\'test\', mailTitle=\'test\', mailCont=\'test\', attch=\'30001|10\', validity=\'2025-01-01 00:00:00\'}','192.168.2.68','2024-12-31 12:29:37','内网IP|0|0|内网IP|内网IP'),
	(1124,'MrBird','Gửi tệp đính kèm email',41,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'57\', sender=\'test\', mailTitle=\'test\', mailCont=\'test\', attch=\'30001|10\', validity=\'2025-01-02 00:00:00\'}','192.168.2.68','2024-12-31 12:29:53','内网IP|0|0|内网IP|内网IP'),
	(1125,'MrBird','Gửi tệp đính kèm email',82,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'57\', sender=\'test\', mailTitle=\'test\', mailCont=\'test\', attch=\'30001|10\', validity=\'2025-02-08 00:00:00\'}','192.168.2.68','2024-12-31 12:30:08','内网IP|0|0|内网IP|内网IP'),
	(1126,'MrBird','Gửi nạp tiền mô phỏng',173,'cc.mrbird.febs.system.controller.OperationController.fakepaySend()',' fakepayConfig: FakepayConfig(playerId=57, payMoney=100000, gsId=3)',NULL,'2024-12-31 17:10:20',''),
	(1127,'MrBird','Gửi nạp tiền mô phỏng',2,'cc.mrbird.febs.system.controller.OperationController.fakepaySend()',' fakepayConfig: FakepayConfig(playerId=57, payMoney=100000, gsId=3)','192.168.2.68','2024-12-31 17:11:07','内网IP|0|0|内网IP|内网IP'),
	(1128,'DyKo','Gửi nạp tiền mô phỏng',2,'cc.mrbird.febs.system.controller.OperationController.fakepaySend()',' fakepayConfig: FakepayConfig(playerId=68, payMoney=10000, gsId=3)','192.168.2.4','2024-12-31 17:15:01','内网IP|0|0|内网IP|内网IP'),
	(1129,'MrBird','Gửi cửa xoay',162,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=testttttttttttttt, repeatCount=1)',NULL,'2024-12-31 17:28:04',''),
	(1130,'MrBird','Gửi cửa xoay',9,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=testttttttttttttt, repeatCount=1)','192.168.2.68','2024-12-31 17:29:42','内网IP|0|0|内网IP|内网IP'),
	(1131,'MrBird','Gửi cửa xoay',153,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=testttttttttttttt, repeatCount=1)','192.168.2.68','2024-12-31 17:29:47','内网IP|0|0|内网IP|内网IP'),
	(1132,'MrBird','Gửi cửa xoay',1,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=testttttttttttttt, repeatCount=10)','192.168.2.68','2024-12-31 17:30:30','内网IP|0|0|内网IP|内网IP'),
	(1133,'MrBird','Gửi tệp đính kèm email',147,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'57\', sender=\'30015\', mailTitle=\'30015\', mailCont=\'30015\', attch=\'30015|100000\', validity=\'2025-01-01 00:00:00\'}','192.168.2.68','2024-12-31 18:19:37','内网IP|0|0|内网IP|内网IP'),
	(1134,'MrBird','Gửi cửa xoay',14,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=Hahahaa, repeatCount=1)',NULL,'2024-12-31 21:20:28',''),
	(1135,'MrBird','Gửi cửa xoay',180,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TESTTTTTTT, repeatCount=1)',NULL,'2024-12-31 21:23:16',''),
	(1136,'MrBird','Gửi cửa xoay',12,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TEST THANH TÚ NÈ, repeatCount=3)',NULL,'2024-12-31 21:34:51',''),
	(1137,'MrBird','Gửi cửa xoay',1,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TEST THANH TÚ NÈ, repeatCount=3)','192.168.2.68','2024-12-31 21:35:28','内网IP|0|0|内网IP|内网IP'),
	(1138,'MrBird','Gửi cửa xoay',1,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TEST THANH TÚ NÈ, repeatCount=3)','192.168.2.68','2024-12-31 21:35:35','内网IP|0|0|内网IP|内网IP'),
	(1139,'MrBird','Gửi thông báo chạy',219,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TEST NÈ, repeatCount=3)',NULL,'2024-12-31 21:47:13',''),
	(1140,'MrBird','Gửi thông báo chạy',2,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TEST NÈ, repeatCount=3)','192.168.2.68','2024-12-31 21:48:14','内网IP|0|0|内网IP|内网IP'),
	(1141,'MrBird','Gửi thông báo chạy',168,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TEST NÈ, repeatCount=3)',NULL,'2024-12-31 21:50:54',''),
	(1142,'MrBird','Gửi thông báo chạy',2,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TEST NÈ, repeatCount=3)','192.168.2.68','2024-12-31 21:53:01','内网IP|0|0|内网IP|内网IP'),
	(1143,'MrBird','Gửi thông báo chạy',190,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=abc, repeatCount=3)',NULL,'2024-12-31 21:57:34',''),
	(1144,'MrBird','Gửi thông báo chạy',5,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=abc, repeatCount=3)','192.168.2.68','2024-12-31 22:01:31','内网IP|0|0|内网IP|内网IP'),
	(1145,'MrBird','Gửi thông báo chạy',4,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=abcxyz, repeatCount=3)','192.168.2.68','2024-12-31 22:04:04','内网IP|0|0|内网IP|内网IP'),
	(1146,'MrBird','Gửi thông báo chạy',2,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=TEST THONG BAO, repeatCount=3)','192.168.2.68','2025-01-02 11:08:16','内网IP|0|0|内网IP|内网IP'),
	(1147,'MrBird','Gửi thông báo chạy',12,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=Test thông báo, repeatCount=5)',NULL,'2025-01-02 11:28:27',''),
	(1148,'MrBird','Gửi thông báo chạy',5,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=Test thông báo, repeatCount=3)','192.168.2.68','2025-01-02 11:33:51','内网IP|0|0|内网IP|内网IP'),
	(1149,'MrBird','Gửi thông báo chạy',2,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=Test thông báo, repeatCount=3)','192.168.2.68','2025-01-02 11:35:15','内网IP|0|0|内网IP|内网IP'),
	(1160,'DyKo','Gửi tệp đính kèm email',78,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'80\', sender=\'0123456789AĂÂBCDĐEÊGHIKL\', mailTitle=\'0123456789AĂÂBCDĐEÊGHIKL012345\', mailCont=\'0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCD\', attch=\'13001|1\', validity=\'2025-01-03 00:00:00\'}',NULL,'2025-01-02 18:53:31',''),
	(1161,'DyKo','Gửi tệp đính kèm email',43,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'80\', sender=\'0123456789AĂÂBCDĐEÊGHIKL\', mailTitle=\'0123456789AĂÂBCDĐEÊGHIKL012345\', mailCont=\'0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCD\', attch=\'13001|1\', validity=\'2025-01-03 00:00:00\'}','192.168.2.43','2025-01-02 18:53:39','内网IP|0|0|内网IP|内网IP'),
	(1162,'DyKo','Gửi tệp đính kèm email',85,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'68\', sender=\'0123456789AĂÂBCDĐEÊGHIKL\', mailTitle=\'0123456789AĂÂBCDĐEÊGHIKL012345\', mailCont=\'0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCD\', attch=\'13001|1\', validity=\'2025-01-03 00:00:00\'}','192.168.2.43','2025-01-02 18:54:20','内网IP|0|0|内网IP|内网IP'),
	(1163,'DyKo','Gửi tệp đính kèm email',86,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'68\', sender=\'0123456789AĂÂBCDĐEÊGHIKL\', mailTitle=\'0123456789AĂÂBCDĐEÊGHIKL012345\', mailCont=\'0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCDĐEÊGHIKL0123456789AĂÂBCD\', attch=\'13001|1\', validity=\'2025-01-03 00:00:00\'}','192.168.2.43','2025-01-02 18:54:24','内网IP|0|0|内网IP|内网IP'),
	(1164,'MrBird','新增菜单/按钮',344,'cc.mrbird.febs.system.controller.MenuController.addMenu()',' menu: \"Menu(menuId=207, parentId=179, menuName=Gói nạp, url=/op/recharge, perms=recharge:view, icon=layui-icon-Dollar-circle-fill, type=0, orderNum=3, createTime=Thu Jan 02 23:07:12 ICT 2025, modifyTime=null)\"','127.0.0.1','2025-01-03 00:07:12','内网IP|0|0|内网IP|内网IP'),
	(1165,'MrBird','新增菜单/按钮',265,'cc.mrbird.febs.system.controller.MenuController.addMenu()',' menu: \"Menu(menuId=208, parentId=179, menuName=Máy chủ, url=/op/server_list, perms=server:view, icon=layui-icon-sever-fill, type=0, orderNum=null, createTime=Tue Jan 07 13:29:08 ICT 2025, modifyTime=null)\"',NULL,'2025-01-07 14:29:08',''),
	(1166,'MrBird','Gửi nạp tiền mô phỏng',15,'cc.mrbird.febs.system.controller.OperationController.fakepaySend()',' fakepayConfig: FakepayConfig(playerId=122, payMoney=100000000, gsId=1)',NULL,'2025-01-08 14:42:40',''),
	(1167,'MrBird','Gửi tệp đính kèm email',95,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'122\', sender=\'CHO VÀNG\', mailTitle=\'CHO VÀNG\', mailCont=\'CHO VÀNG\', attch=\'38005|10000000000\', validity=\'2025-01-08 13:48:45\'}','192.168.2.68','2025-01-08 14:48:47','内网IP|0|0|内网IP|内网IP'),
	(1168,'DyKo','Gửi tệp đính kèm email',17,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101,121,130,128,126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}',NULL,'2025-01-08 16:55:42',''),
	(1169,'DyKo','Gửi tệp đính kèm email',14,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101,121,130,128,126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:55:47','内网IP|0|0|内网IP|内网IP'),
	(1170,'DyKo','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101,121,130,128,126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:55:50','内网IP|0|0|内网IP|内网IP'),
	(1171,'DyKo','Gửi tệp đính kèm email',81,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:56:14','内网IP|0|0|内网IP|内网IP'),
	(1172,'DyKo','Gửi tệp đính kèm email',42,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:57:12','内网IP|0|0|内网IP|内网IP'),
	(1173,'DyKo','Gửi tệp đính kèm email',86,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:58:34','内网IP|0|0|内网IP|内网IP'),
	(1174,'DyKo','Gửi tệp đính kèm email',87,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:58:38','内网IP|0|0|内网IP|内网IP'),
	(1175,'DyKo','Gửi tệp đính kèm email',67,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:58:38','内网IP|0|0|内网IP|内网IP'),
	(1176,'DyKo','Gửi tệp đính kèm email',80,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:58:38','内网IP|0|0|内网IP|内网IP'),
	(1177,'DyKo','Gửi tệp đính kèm email',41,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:58:39','内网IP|0|0|内网IP|内网IP'),
	(1178,'DyKo','Gửi tệp đính kèm email',45,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:58:39','内网IP|0|0|内网IP|内网IP'),
	(1179,'DyKo','Gửi tệp đính kèm email',47,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 16:58:39','内网IP|0|0|内网IP|内网IP'),
	(1180,'DyKo','Gửi tệp đính kèm email',114,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2, 3], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 17:03:54','内网IP|0|0|内网IP|内网IP'),
	(1181,'DyKo','Gửi tệp đính kèm email',41,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 17:11:15','内网IP|0|0|内网IP|内网IP'),
	(1182,'DyKo','Gửi tệp đính kèm email',40,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 17:11:16','内网IP|0|0|内网IP|内网IP'),
	(1183,'DyKo','Gửi tệp đính kèm email',73,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 17:11:16','内网IP|0|0|内网IP|内网IP'),
	(1184,'DyKo','Gửi tệp đính kèm email',38,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 17:11:16','内网IP|0|0|内网IP|内网IP'),
	(1185,'DyKo','Gửi tệp đính kèm email',77,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ\', mailCont=\'Quà Hỗ trợ\', attch=\'38005|100000-38007|1000-38008|1000-38011|1000-30003|1000\', validity=\'2025-01-10 00:00:00\'}','192.168.2.2','2025-01-08 17:11:17','内网IP|0|0|内网IP|内网IP'),
	(1186,'DyKo','Gửi tệp đính kèm email',128,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2, 3], addressee=2, receiveId=\'101\n121\n130\n128\n126\', sender=\'Vui Vui\', mailTitle=\'Quà Hỗ trợ lần 2\', mailCont=\'Quà Hỗ trợ lần 2\', attch=\'38005|100000-38007|1000-38008|1000\', validity=\'2025-01-10 00:00:00\'}',NULL,'2025-01-08 17:14:28',''),
	(1187,'MrBird','Gửi tệp đính kèm email',82,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'38006|100000000\', validity=\'2025-01-09 11:42:20\'}',NULL,'2025-01-09 12:42:23',''),
	(1188,'MrBird','Gửi tệp đính kèm email',42,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30015|100000000\', validity=\'2025-01-09 11:42:20\'}','192.168.2.68','2025-01-09 12:43:05','内网IP|0|0|内网IP|内网IP'),
	(1189,'MrBird','Gửi tệp đính kèm email',84,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|100000000\', validity=\'2025-01-09 11:42:20\'}','192.168.2.68','2025-01-09 12:43:33','内网IP|0|0|内网IP|内网IP'),
	(1190,'MrBird','Gửi nạp tiền mô phỏng',11,'cc.mrbird.febs.system.controller.OperationController.fakepaySend()',' fakepayConfig: FakepayConfig(playerId=121, payMoney=100000000, gsId=1)',NULL,'2025-01-09 17:07:43',''),
	(1191,'MrBird','Gửi tệp đính kèm email',46,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'TESST\', mailTitle=\'TESST\', mailCont=\'TESST\', attch=\'30001|10\', validity=\'2025-01-09 23:09:51\'}',NULL,'2025-01-10 00:09:52',''),
	(1192,'MrBird','Gửi tệp đính kèm email',5008,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1, 2, 3, 4], addressee=1, receiveId=\'90\', sender=\'TESST\', mailTitle=\'TESST\', mailCont=\'TESST\', attch=\'30001|10\', validity=\'2025-01-09 23:09:51\'}','171.232.191.44','2025-01-10 00:10:13','越南|0|0|0|0'),
	(1193,'MrBird','Gửi tệp đính kèm email',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'TESST\', mailTitle=\'TESST\', mailCont=\'TESST\', attch=\'30001|10\', validity=\'2025-01-09 23:09:51\'}','171.232.191.44','2025-01-10 00:10:58','越南|0|0|0|0'),
	(1194,'MrBird','Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'TESST\', mailTitle=\'TESST\', mailCont=\'TESST\', attch=\'30001|10\', validity=\'2025-01-09 23:11:19\'}','171.232.191.44','2025-01-10 00:11:19','越南|0|0|0|0'),
	(1195,'DyKo','Gửi tệp đính kèm email',26,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}',NULL,'2025-01-10 10:50:12',''),
	(1196,'DyKo','Gửi tệp đính kèm email',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:50:17','越南|0|0|0|0'),
	(1197,'MrBird','Gửi tệp đính kèm email',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'Test\', mailTitle=\'Test\', mailCont=\'Test\', attch=\'30001|10\', validity=\'2025-01-10 09:51:39\'}','171.232.191.44','2025-01-10 10:51:40','越南|0|0|0|0'),
	(1198,'DyKo','Gửi tệp đính kèm email',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:52:00','越南|0|0|0|0'),
	(1199,'MrBird','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'Test\', mailTitle=\'Test\', mailCont=\'Test\', attch=\'30001|10\', validity=\'2025-01-10 09:51:39\'}','171.232.191.44','2025-01-10 10:52:26','越南|0|0|0|0'),
	(1200,'DyKo','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\n133\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:52:56','越南|0|0|0|0'),
	(1201,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'133\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:53:55','越南|0|0|0|0'),
	(1202,'MrBird','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n121\n130\n128\n126\n133\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:54:14','越南|0|0|0|0'),
	(1203,'DyKo','Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101-133\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:54:39','越南|0|0|0|0'),
	(1204,'MrBird','Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101,133\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:54:49','越南|0|0|0|0'),
	(1205,'DyKo','Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\n133\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:54:59','越南|0|0|0|0'),
	(1206,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'101\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:56:30','越南|0|0|0|0'),
	(1207,'MrBird','Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'121\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:56:40','越南|0|0|0|0'),
	(1208,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'130\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:56:52','越南|0|0|0|0'),
	(1209,'MrBird','Gửi tệp đính kèm email',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'128\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:57:03','越南|0|0|0|0'),
	(1210,'DyKo','Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'126\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|100000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:57:13','越南|0|0|0|0'),
	(1211,'MrBird','Gửi tệp đính kèm email',101,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79\', sender=\'ADMIN\', mailTitle=\'test\', mailCont=\'test\', attch=\'30001|1000\', validity=\'2025-01-10 09:58:48\'}',NULL,'2025-01-10 10:58:50',''),
	(1212,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'Quà hỗ trợ\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ\', attch=\'38005|1000000\', validity=\'2025-01-11 00:00:00\'}','171.232.191.44','2025-01-10 10:59:50','越南|0|0|0|0'),
	(1213,'DyKo','Gửi tệp đính kèm email',22,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'Quà hỗ trợ\', mailTitle=\'tiêu tiêu\', mailCont=\'quà hỗ trợ\', attch=\'30015|1000000\', validity=\'2025-01-11 00:00:00\'}',NULL,'2025-01-10 11:16:54',''),
	(1214,'MrBird','Gửi tệp đính kèm email',26,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79,139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:19:02\'}',NULL,'2025-01-10 11:19:04',''),
	(1215,'MrBird','Gửi tệp đính kèm email',42,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79\n139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:19:02\'}',NULL,'2025-01-10 11:19:10',''),
	(1216,'MrBird','Gửi tệp đính kèm email',70,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:19:02\'}','192.168.2.68','2025-01-10 11:19:41','内网IP|0|0|内网IP|内网IP'),
	(1217,'MrBird','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79|139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:19:02\'}','192.168.2.68','2025-01-10 11:19:52','内网IP|0|0|内网IP|内网IP'),
	(1218,'MrBird','Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79,139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:19:02\'}','192.168.2.68','2025-01-10 11:20:00','内网IP|0|0|内网IP|内网IP'),
	(1219,'MrBird','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79,139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:19:02\'}','192.168.2.68','2025-01-10 11:20:02','内网IP|0|0|内网IP|内网IP'),
	(1220,'MrBird','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79,139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:19:02\'}','192.168.2.68','2025-01-10 11:20:07','内网IP|0|0|内网IP|内网IP'),
	(1221,'MrBird','Gửi tệp đính kèm email',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79,139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:19:02\'}','192.168.2.68','2025-01-10 11:20:30','内网IP|0|0|内网IP|内网IP'),
	(1222,'MrBird','Gửi tệp đính kèm email',21,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79;139\', sender=\'Test2\', mailTitle=\'test2\', mailCont=\'test2\', attch=\'30001|10\', validity=\'2025-01-10 10:23:32\'}',NULL,'2025-01-10 11:23:34',''),
	(1223,'MrBird','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79,139\', sender=\'Test2\', mailTitle=\'test2\', mailCont=\'test2\', attch=\'30001|10\', validity=\'2025-01-10 10:23:32\'}','192.168.2.68','2025-01-10 11:23:44','内网IP|0|0|内网IP|内网IP'),
	(1224,'MrBird','Gửi tệp đính kèm email',21,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79,139\', sender=\'test2\', mailTitle=\'test2\', mailCont=\'test2\', attch=\'30001|10\', validity=\'2025-01-10 10:25:20\'}',NULL,'2025-01-10 11:25:22',''),
	(1225,'MrBird','Gửi tệp đính kèm email',19,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79,139\', sender=\'TEST2\', mailTitle=\'TEST2\', mailCont=\'TEST2\', attch=\'30001|20\', validity=\'2025-01-10 10:29:22\'}',NULL,'2025-01-10 11:29:23',''),
	(1226,'MrBird','Gửi tệp đính kèm email',18,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79|139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:32:29\'}',NULL,'2025-01-10 11:32:30',''),
	(1227,'MrBird','Gửi tệp đính kèm email',173,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79\n139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:32:29\'}','192.168.2.68','2025-01-10 11:32:42','内网IP|0|0|内网IP|内网IP'),
	(1228,'MrBird','Gửi tệp đính kèm email',20,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79|139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:36:18\'}',NULL,'2025-01-10 11:36:19',''),
	(1229,'MrBird','Gửi tệp đính kèm email',155,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[3], addressee=1, receiveId=\'79\n139\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:36:18\'}','192.168.2.68','2025-01-10 11:36:23','内网IP|0|0|内网IP|内网IP'),
	(1230,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'133\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|100000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:37:37','越南|0|0|0|0'),
	(1231,'MrBird','Gửi tệp đính kèm email',104,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'TEST\', mailTitle=\'TEST\', mailCont=\'TEST\', attch=\'30001|10\', validity=\'2025-01-10 10:36:18\'}','192.168.2.68','2025-01-10 11:37:47','内网IP|0|0|内网IP|内网IP'),
	(1232,'DyKo','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|100000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:38:18','越南|0|0|0|0'),
	(1233,'DyKo','Gửi tệp đính kèm email',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'121\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|100000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:38:27','越南|0|0|0|0'),
	(1234,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'130\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|100000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:38:34','越南|0|0|0|0'),
	(1235,'DyKo','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'130\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|100000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:38:35','越南|0|0|0|0'),
	(1236,'DyKo','Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|10000000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:39:12','越南|0|0|0|0'),
	(1237,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'121\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|10000000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:39:20','越南|0|0|0|0'),
	(1238,'DyKo','Gửi tệp đính kèm email',24,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'134\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|10000000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:40:18','越南|0|0|0|0'),
	(1239,'DyKo','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'134\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|10000000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:40:19','越南|0|0|0|0'),
	(1240,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'130\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|10000000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:40:33','越南|0|0|0|0'),
	(1241,'DyKo','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'130\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|10000000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:40:34','越南|0|0|0|0'),
	(1242,'DyKo','Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'133\', sender=\'Test\', mailTitle=\'Quà hỗ trợ\', mailCont=\'Quà hỗ trợ \', attch=\'30015|1000000-38005|1000000-38006|10000000-38008|100000\', validity=\'2025-01-13 00:00:00\'}','171.232.191.44','2025-01-10 11:41:02','越南|0|0|0|0'),
	(1243,'DyKo','Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'79\', sender=\'HEHE\', mailTitle=\'HEHE\', mailCont=\'HEHE\', attch=\'30026|1000000000\', validity=\'2025-01-10 11:26:31\'}','171.232.191.44','2025-01-10 12:26:31','越南|0|0|0|0'),
	(1244,NULL,'Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'1\', mailTitle=\'1\', mailCont=\'1\', attch=\'30001|100000000\', validity=\'2025-01-10 13:33:34\'}','171.232.191.44','2025-01-10 14:33:41','越南|0|0|0|0'),
	(1245,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'1\', mailTitle=\'1\', mailCont=\'1\', attch=\'30001|10000000000\', validity=\'2025-01-10 13:33:34\'}','171.232.191.44','2025-01-10 14:34:01','越南|0|0|0|0'),
	(1246,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'1\', mailTitle=\'1\', mailCont=\'1\', attch=\'30001|1000000000\', validity=\'2025-01-10 13:33:34\'}','171.232.191.44','2025-01-10 14:34:27','越南|0|0|0|0'),
	(1247,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'1\', mailTitle=\'1\', mailCont=\'1\', attch=\'38006|1000000000\', validity=\'2025-01-10 13:33:34\'}','171.232.191.44','2025-01-10 14:38:37','越南|0|0|0|0'),
	(1248,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'1\', mailTitle=\'1\', mailCont=\'1\', attch=\'30015|1000000000\', validity=\'2025-01-10 13:33:34\'}','171.232.191.44','2025-01-10 14:40:09','越南|0|0|0|0'),
	(1249,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'90\', sender=\'1\', mailTitle=\'1\', mailCont=\'1\', attch=\'30026|1000000000\', validity=\'2025-01-10 13:33:34\'}','171.232.191.44','2025-01-10 14:40:56','越南|0|0|0|0'),
	(1250,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'121\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:25:55','越南|0|0|0|0'),
	(1251,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'121\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:25:59','越南|0|0|0|0'),
	(1252,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'134\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:07','越南|0|0|0|0'),
	(1253,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'134\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:07','越南|0|0|0|0'),
	(1254,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:14','越南|0|0|0|0'),
	(1255,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:14','越南|0|0|0|0'),
	(1256,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'128\n\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:20','越南|0|0|0|0'),
	(1257,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'128\n\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:21','越南|0|0|0|0'),
	(1258,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'133\n\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:25','越南|0|0|0|0'),
	(1259,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'133\n\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:26','越南|0|0|0|0'),
	(1260,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'103\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:29','越南|0|0|0|0'),
	(1261,NULL,'Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'103\', sender=\'tiêu tiêu\', mailTitle=\'Quà Hỗ Trợ\', mailCont=\'Quà Hỗ Trợ\', attch=\'30001|1000000-38005|1000000-38006|1000000-30015|1000000\', validity=\'2025-01-12 00:00:00\'}','171.232.191.44','2025-01-10 15:26:29','越南|0|0|0|0'),
	(1262,NULL,'Gửi thông báo chạy',0,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=Dịch vụ buff bẩn 100k tặng 50k đây , repeatCount=1)','171.232.191.44','2025-01-10 15:41:41','越南|0|0|0|0'),
	(1263,NULL,'Gửi thông báo chạy',0,'cc.mrbird.febs.system.controller.OperationController.subtitleSend()',' subtitleConfig: SubtitleConfig(subtitleContent=Dịch vụ buff bẩn 100k tặng 50k đây , repeatCount=1)','171.232.191.44','2025-01-10 15:41:54','越南|0|0|0|0'),
	(1264,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|0\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:34','越南|0|0|0|0'),
	(1265,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:38','越南|0|0|0|0'),
	(1266,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:38','越南|0|0|0|0'),
	(1267,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:39','越南|0|0|0|0'),
	(1268,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:39','越南|0|0|0|0'),
	(1269,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:39','越南|0|0|0|0'),
	(1270,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:39','越南|0|0|0|0'),
	(1271,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:39','越南|0|0|0|0'),
	(1272,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:39','越南|0|0|0|0'),
	(1273,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:40','越南|0|0|0|0'),
	(1274,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:40','越南|0|0|0|0'),
	(1275,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:40','越南|0|0|0|0'),
	(1276,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:40','越南|0|0|0|0'),
	(1277,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}',NULL,'2025-01-10 15:43:40',''),
	(1278,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:40','越南|0|0|0|0'),
	(1279,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'104\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:43:41','越南|0|0|0|0'),
	(1280,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|1\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:45:19','越南|0|0|0|0'),
	(1281,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'THANH TÚ ĐẸP TRAI\', mailTitle=\'THANH TÚ ĐẸP TRAI\', mailCont=\'THANH TÚ ĐẸP TRAI\', attch=\'30001|10000000\', validity=\'2025-01-10 14:43:34\'}','171.232.191.44','2025-01-10 15:45:43','越南|0|0|0|0'),
	(1282,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'38005|9999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:50:58','越南|0|0|0|0'),
	(1283,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'12048|1\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:56:20','越南|0|0|0|0'),
	(1284,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'12068|1\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:56:42','越南|0|0|0|0'),
	(1285,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'12088|1\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:56:54','越南|0|0|0|0'),
	(1286,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'17048|1\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:57:35','越南|0|0|0|0'),
	(1287,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'18008|1\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:57:52','越南|0|0|0|0'),
	(1288,NULL,'Gửi tệp đính kèm email',4,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:53','越南|0|0|0|0'),
	(1289,NULL,'Gửi tệp đính kèm email',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:57','越南|0|0|0|0'),
	(1290,NULL,'Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:57','越南|0|0|0|0'),
	(1291,NULL,'Gửi tệp đính kèm email',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:58','越南|0|0|0|0'),
	(1292,NULL,'Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:58','越南|0|0|0|0'),
	(1293,NULL,'Gửi tệp đính kèm email',9,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:58','越南|0|0|0|0'),
	(1294,NULL,'Gửi tệp đính kèm email',6,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:58','越南|0|0|0|0'),
	(1295,NULL,'Gửi tệp đính kèm email',8,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:58','越南|0|0|0|0'),
	(1296,NULL,'Gửi tệp đính kèm email',7,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:59','越南|0|0|0|0'),
	(1297,NULL,'Gửi tệp đính kèm email',8,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:59','越南|0|0|0|0'),
	(1298,NULL,'Gửi tệp đính kèm email',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'34446|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:58:59','越南|0|0|0|0'),
	(1299,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'33317|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 15:59:51','越南|0|0|0|0'),
	(1300,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'33327|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:00:03','越南|0|0|0|0'),
	(1301,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'33117|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:00:20','越南|0|0|0|0'),
	(1302,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'33017|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:01:08','越南|0|0|0|0'),
	(1303,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30026|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:02:03','越南|0|0|0|0'),
	(1304,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30017|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:02:26','越南|0|0|0|0'),
	(1305,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30017|9999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:03:23','越南|0|0|0|0'),
	(1306,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'38010|9999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:03:38','越南|0|0|0|0'),
	(1307,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30026|9999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:03:48','越南|0|0|0|0'),
	(1308,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'33217|9999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:05:33','越南|0|0|0|0'),
	(1309,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'33217|9999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:05:51','越南|0|0|0|0'),
	(1310,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'33217|10\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:06:40','越南|0|0|0|0'),
	(1311,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'38006|9999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:08:10','越南|0|0|0|0'),
	(1312,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'38006|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:08:32','越南|0|0|0|0'),
	(1313,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|9999999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:09','越南|0|0|0|0'),
	(1314,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|99999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:40','越南|0|0|0|0'),
	(1315,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:50','越南|0|0|0|0'),
	(1316,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:51','越南|0|0|0|0'),
	(1317,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:51','越南|0|0|0|0'),
	(1318,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:52','越南|0|0|0|0'),
	(1319,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:52','越南|0|0|0|0'),
	(1320,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:52','越南|0|0|0|0'),
	(1321,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:52','越南|0|0|0|0'),
	(1322,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:11:52','越南|0|0|0|0'),
	(1323,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30026|999999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:12:38','越南|0|0|0|0'),
	(1324,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|9999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:13:55','越南|0|0|0|0'),
	(1325,NULL,'Gửi tệp đính kèm email',1,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|9999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:13:55','越南|0|0|0|0'),
	(1326,NULL,'Gửi tệp đính kèm email',2,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'145\', sender=\'Thanh du\', mailTitle=\'gggg\', mailCont=\'ggg\', attch=\'30001|9999999\', validity=\'2025-01-10 00:00:00\'}','171.232.191.44','2025-01-10 16:16:49','越南|0|0|0|0');

/*!40000 ALTER TABLE `t_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_login_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_login_log`;

CREATE TABLE `t_login_log` (
  `ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `USERNAME` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `LOGIN_TIME` datetime NOT NULL COMMENT '登录时间',
  `LOCATION` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '登录地点',
  `IP` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'IP地址',
  `SYSTEM` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '操作系统',
  `BROWSER` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '浏览器',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='登录日志表';

LOCK TABLES `t_login_log` WRITE;
/*!40000 ALTER TABLE `t_login_log` DISABLE KEYS */;

INSERT INTO `t_login_log` (`ID`, `USERNAME`, `LOGIN_TIME`, `LOCATION`, `IP`, `SYSTEM`, `BROWSER`)
VALUES
	(96,'mrbird','2024-12-17 18:25:21','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(97,'mrbird','2024-12-17 18:28:35','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(98,'mrbird','2024-12-18 10:19:49','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(99,'mrbird','2024-12-18 14:19:27','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(100,'mrbird','2024-12-18 14:24:52','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(101,'mrbird','2024-12-19 10:57:18','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(102,'mrbird','2024-12-19 12:18:30','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(103,'mrbird','2024-12-19 12:28:36','内网IP|0|0|内网IP|内网IP','192.168.2.15','Mac OS X 10_15_7','Chrome 13'),
	(104,'mrbird','2024-12-19 12:29:39','内网IP|0|0|内网IP|内网IP','192.168.2.66','Windows 10','Chrome 13'),
	(105,'mrbird','2024-12-19 12:31:03','内网IP|0|0|内网IP|内网IP','192.168.2.16','Windows 10','Chrome 13'),
	(106,'mrbird','2024-12-19 14:39:26','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(107,'mrbird','2024-12-19 14:51:38','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(108,'Mrbird','2024-12-19 14:56:30','越南|0|0|0|0','113.185.40.225','Mac OS X) AppleW','Safari Version 18.1.1 '),
	(109,'Mrbird','2024-12-19 14:58:57','越南|0|0|0|0','113.185.40.225','Mac OS X) AppleW','Safari Version 18.1.1 '),
	(110,'mrbird','2024-12-19 14:59:43','越南|0|0|0|0','203.210.219.242','Windows 10','Chrome 13'),
	(111,'mrbird','2024-12-19 15:41:15','越南|0|0|0|0','14.177.150.251','Mac OS X 10_15_7','Chrome 13'),
	(112,'mrbird','2024-12-24 14:54:32','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(113,'mrbird','2024-12-25 10:37:00','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(114,'mrbird','2024-12-25 10:45:12','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(115,'mrbird','2024-12-25 10:50:21','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(116,'mrbird','2024-12-25 10:56:31','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(117,'DyKo','2024-12-25 11:01:46','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(118,'DyKo','2024-12-25 11:25:39','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(119,'mrbird','2024-12-25 17:15:01','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(120,'mrbird','2024-12-26 11:04:15','内网IP|0|0|内网IP|内网IP','192.168.2.6','Windows 10','Chrome 13'),
	(121,'mrbird','2024-12-26 15:05:35','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(122,'mrbird','2024-12-26 15:34:19','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(123,'mrbird','2024-12-26 15:49:24','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(124,'mrbird','2024-12-26 15:57:43','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(125,'mrbird','2024-12-26 16:22:59','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(126,'mrbird','2024-12-26 16:24:37','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(127,'Dyko','2024-12-26 17:13:11','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(128,'dyko','2024-12-26 17:19:07','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(129,'DyKo','2024-12-26 17:28:15','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(130,'DyKo','2024-12-27 13:10:52','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(131,'DyKo','2024-12-27 13:48:38','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(132,'dyko','2024-12-27 14:59:42','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(133,'mrbird','2024-12-27 17:19:44','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(134,'Dyko','2024-12-27 17:20:15','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(135,'DyKo','2024-12-27 17:45:13','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(136,'dyko','2024-12-27 17:50:55','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(137,'mrbird','2024-12-27 17:55:15','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(138,'mrbird','2024-12-27 17:57:40','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(139,'Dyko','2024-12-27 18:23:59','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(140,'dyko','2024-12-27 18:41:49','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(141,'Dyko','2024-12-27 19:29:43','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(142,'Dyko','2024-12-27 19:42:38','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(143,'Dyko','2024-12-27 21:31:53','越南|0|0|0|0','116.108.131.87','Mac OS X 10_15_7','Chrome 13'),
	(144,'mrbird','2024-12-27 22:59:18','越南|0|0|0|0','116.108.83.80','Windows 10','Chrome 13'),
	(145,'mrbird','2024-12-29 16:54:12','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(146,'mrbird','2024-12-29 18:49:42','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(147,'mrbird','2024-12-29 18:53:09','越南|0|0|0|0','116.108.131.87','Windows 10','Chrome 13'),
	(148,'mrbird','2024-12-31 12:05:25','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(149,'mrbird','2024-12-31 12:06:52','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(150,'mrbird','2024-12-31 12:22:43','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(151,'mrbird','2024-12-31 12:25:56','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(152,'mrbird','2024-12-31 12:56:49','内网IP|0|0|内网IP|内网IP','172.17.192.1','Windows 10','Chrome 13'),
	(153,'dyko','2024-12-31 17:11:54','内网IP|0|0|内网IP|内网IP','192.168.2.4','Mac OS X 10_15_7','Chrome 13'),
	(154,'mrbird','2024-12-31 20:53:22','内网IP|0|0|内网IP|内网IP','172.23.80.1','Windows 10','Chrome 13'),
	(155,'mrbird','2024-12-31 21:03:08','内网IP|0|0|内网IP|内网IP','172.23.80.1','Windows 10','Chrome 13'),
	(156,'mrbird','2024-12-31 21:16:24','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(157,'mrbird','2025-01-02 11:07:36','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(158,'mrbird','2025-01-02 11:27:07','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(159,'DyKo','2025-01-02 12:15:15','内网IP|0|0|内网IP|内网IP','192.168.2.43','Mac OS X 10_15_7','Chrome 13'),
	(160,'mrbird','2025-01-02 14:43:00','内网IP|0|0|内网IP|内网IP','127.0.0.1','Mac OS X 10_15_7','Chrome 13'),
	(161,'mrbird','2025-01-03 11:47:07','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(162,'mrbird','2025-01-06 10:52:14','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(163,'mrbird','2025-01-06 11:49:38','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(164,'mrbird','2025-01-06 14:33:14','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(165,'mrbird','2025-01-06 15:10:53','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(166,'mrbird','2025-01-06 15:22:36','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(167,'mrbird','2025-01-06 15:47:39','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(168,'DyKo','2025-01-07 11:51:45','内网IP|0|0|内网IP|内网IP','192.168.2.9','Mac OS X 10_15_7','Chrome 13'),
	(169,'mrbird','2025-01-07 14:25:15','内网IP|0|0|内网IP|内网IP','192.168.2.32','Windows 10','Chrome 13'),
	(170,'mrbird','2025-01-07 14:36:19','内网IP|0|0|内网IP|内网IP','192.168.2.32','Windows 10','Chrome 13'),
	(171,'mrbird','2025-01-07 17:30:26','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(172,'DyKo','2025-01-08 11:58:42','内网IP|0|0|内网IP|内网IP','192.168.2.2','Mac OS X 10_15_7','Chrome 13'),
	(173,'mrbird','2025-01-08 18:37:00','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(174,'mrbird','2025-01-09 12:36:24','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(175,'mrbird','2025-01-09 16:46:28','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(176,'mrbird','2025-01-09 17:07:03','内网IP|0|0|内网IP|内网IP','192.168.2.38','Windows 10','Chrome 13'),
	(177,'mrbird','2025-01-10 00:07:51','越南|0|0|0|0','116.108.83.80','Windows 10','Chrome 13'),
	(178,'mrbird','2025-01-10 00:09:20','越南|0|0|0|0','171.232.191.44','Windows 10','Chrome 13'),
	(179,'dyko','2025-01-10 10:47:07','越南|0|0|0|0','171.232.191.44','Mac OS X 10_15_7','Chrome 13'),
	(180,'mrbird','2025-01-10 10:47:09','越南|0|0|0|0','171.232.191.44','Windows 10','Chrome 13'),
	(181,'mrbird','2025-01-10 10:56:58','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(182,'mrbird','2025-01-10 11:10:04','内网IP|0|0|内网IP|内网IP','192.168.2.68','Windows 10','Chrome 13'),
	(183,'Dyko','2025-01-10 11:16:13','越南|0|0|0|0','171.232.191.44','Mac OS X 10_15_7','Chrome 13'),
	(184,'mrbird','2025-01-10 12:23:37','越南|0|0|0|0','171.232.191.44','Windows 10','Chrome 13'),
	(185,'mrbird','2025-01-10 14:33:07','越南|0|0|0|0','171.232.191.44','Windows 10','Chrome 13'),
	(186,'Dyko','2025-01-10 15:02:35','越南|0|0|0|0','171.232.191.44','Mac OS X 10_15_7','Chrome 13'),
	(187,'mrbird','2025-01-10 15:39:22','越南|0|0|0|0','171.232.191.44','Mac OS X 10_15_7','Chrome 13'),
	(188,'mrbird','2025-01-10 15:43:16','越南|0|0|0|0','171.232.191.44','Windows 10','Chrome 13'),
	(189,'mrbird','2025-01-10 15:47:36','越南|0|0|0|0','171.232.191.44','Windows 10','Chrome 13');

/*!40000 ALTER TABLE `t_login_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `MENU_ID` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `PARENT_ID` bigint NOT NULL COMMENT '上级菜单ID',
  `MENU_NAME` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `URL` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '菜单URL',
  `PERMS` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '权限标识',
  `ICON` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图标',
  `TYPE` char(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '类型 0菜单 1按钮',
  `ORDER_NUM` bigint DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='菜单表';

LOCK TABLES `t_menu` WRITE;
/*!40000 ALTER TABLE `t_menu` DISABLE KEYS */;

INSERT INTO `t_menu` (`MENU_ID`, `PARENT_ID`, `MENU_NAME`, `URL`, `PERMS`, `ICON`, `TYPE`, `ORDER_NUM`, `CREATE_TIME`, `MODIFY_TIME`)
VALUES
	(1,0,'Quản lý hệ thống','','','layui-icon-setting','0',3,'2017-12-27 16:39:07','2019-11-01 15:44:45'),
	(2,0,'Giám sát hệ thống','','','layui-icon-alert','0',4,'2017-12-27 16:45:51','2019-11-01 15:44:41'),
	(3,1,'Quản lý người dùng','/system/user','user:view','layui-icon-meh','0',1,'2017-12-27 16:47:13','2019-06-13 11:13:55'),
	(4,1,'Quản lý vai trò','/system/role','role:view','','0',2,'2017-12-27 16:48:09','2019-06-13 08:57:19'),
	(5,1,'Quản lý menu','/system/menu','menu:view','','0',3,'2017-12-27 16:48:57','2019-06-13 08:57:34'),
	(6,1,'Quản lý bộ phận','/system/dept','dept:view','','0',4,'2017-12-27 16:57:33','2019-06-14 19:56:00'),
	(8,2,'Người dùng trực tuyến','/monitor/online','online:view','','0',1,'2017-12-27 16:59:33','2019-06-13 14:30:31'),
	(10,2,'Nhật ký hệ thống','/monitor/log','log:view','','0',2,'2017-12-27 17:00:50','2019-06-13 14:30:37'),
	(11,3,'Thêm người dùng mới',NULL,'user:add',NULL,'1',NULL,'2017-12-27 17:02:58',NULL),
	(12,3,'Sửa đổi người dùng',NULL,'user:update',NULL,'1',NULL,'2017-12-27 17:04:07',NULL),
	(13,3,'Xóa người dùng',NULL,'user:delete',NULL,'1',NULL,'2017-12-27 17:04:58',NULL),
	(14,4,'Thêm vai trò mới',NULL,'role:add',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),
	(15,4,'Sửa đổi vai trò',NULL,'role:update',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),
	(16,4,'Xóa vai trò',NULL,'role:delete',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),
	(17,5,'Thực đơn mới',NULL,'menu:add',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),
	(18,5,'Sửa đổi menu',NULL,'menu:update',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),
	(19,5,'Xóa menu',NULL,'menu:delete',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),
	(20,6,'Thêm bộ phận mới',NULL,'dept:add',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),
	(21,6,'Sửa đổi bộ phận',NULL,'dept:update',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),
	(22,6,'Xóa bộ phận',NULL,'dept:delete',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),
	(23,8,'Kick người sử dụng',NULL,'user:kickout',NULL,'1',NULL,'2017-12-27 17:11:13',NULL),
	(24,10,'Xóa nhật ký',NULL,'log:delete',NULL,'1',NULL,'2017-12-27 17:11:45','2019-06-06 05:56:40'),
	(113,2,'Giám sát Redis','/monitor/redis/info','redis:view','','0',4,'2018-06-28 14:29:42','2019-06-13 14:30:45'),
	(114,2,'Thiết bị đầu cuối Redis','/monitor/redis/terminal','redis:terminal:view','','0',5,'2018-06-28 15:35:21','2019-06-13 14:30:54'),
	(115,0,'Các mô-đun khác','','','layui-icon-gift','0',6,'2019-05-27 10:18:07','2019-11-01 15:44:30'),
	(116,115,'Biểu đồ Apex','','',NULL,'0',2,'2019-05-27 10:21:35',NULL),
	(117,116,'Biểu đồ tuyến tính','/others/apex/line','apex:line:view',NULL,'0',1,'2019-05-27 10:24:49',NULL),
	(118,115,'Bản đồ','/others/map','map:view','','0',3,'2019-05-27 17:13:12','2019-06-12 15:33:00'),
	(119,116,'Biểu đồ khu vực','/others/apex/area','apex:area:view',NULL,'0',2,'2019-05-27 18:49:22',NULL),
	(120,116,'Biểu đồ cột','/others/apex/column','apex:column:view',NULL,'0',3,'2019-05-27 18:51:33',NULL),
	(121,116,'Biểu đồ radar','/others/apex/radar','apex:radar:view',NULL,'0',4,'2019-05-27 18:56:05',NULL),
	(122,116,'Biểu đồ thanh','/others/apex/bar','apex:bar:view',NULL,'0',5,'2019-05-27 18:57:02',NULL),
	(123,116,'Biểu đồ lai','/others/apex/mix','apex:mix:view','','0',6,'2019-05-27 18:58:04','2019-06-06 02:55:23'),
	(125,115,'Nhập khẩu và xuất khẩu','/others/eximport','others:eximport:view','','0',4,'2019-05-27 19:01:57','2019-06-13 01:20:14'),
	(126,132,'Biểu tượng hệ thống','/others/febs/icon','febs:icons:view','','0',4,'2019-05-27 19:03:18','2019-06-06 03:05:26'),
	(127,2,'Yêu cầu theo dõi','/monitor/httptrace','httptrace:view','','0',6,'2019-05-27 19:06:38','2019-06-13 14:36:43'),
	(128,2,'Thông tin hệ thống',NULL,NULL,NULL,'0',7,'2019-05-27 19:08:23',NULL),
	(129,128,'Thông tin JVM','/monitor/jvm','jvm:view','','0',1,'2019-05-27 19:08:50','2019-06-13 14:36:51'),
	(130,128,'Thông tin tomcat','/monitor/tomcat','tomcat:view','','0',2,'2019-05-27 19:09:26','2019-06-13 14:36:57'),
	(131,128,'Thông tin máy chủ','/monitor/server','server:view','','0',3,'2019-05-27 19:10:07','2019-06-13 14:37:04'),
	(132,115,'Thành phần FEBS','','',NULL,'0',1,'2019-05-27 19:13:54',NULL),
	(133,132,'Thành phần biểu mẫu','/others/febs/form','febs:form:view',NULL,'0',1,'2019-05-27 19:14:45',NULL),
	(134,132,'Công cụ FEBS','/others/febs/tools','febs:tools:view','','0',3,'2019-05-29 10:11:22','2019-06-12 13:21:27'),
	(135,132,'Kết hợp hình thức','/others/febs/form/group','febs:formgroup:view',NULL,'0',2,'2019-05-29 10:16:19',NULL),
	(136,2,'Nhật ký đăng nhập','/monitor/loginlog','loginlog:view','','0',3,'2019-05-29 15:56:15','2019-06-13 14:35:56'),
	(137,0,'Tạo mã','','','layui-icon-verticalright','0',5,'2019-06-03 15:35:58','2019-11-01 15:44:36'),
	(138,137,'Tạo cấu hình','/generator/configure','generator:configure:view',NULL,'0',1,'2019-06-03 15:38:36',NULL),
	(139,137,'Tạo mã','/generator/generator','generator:view','','0',2,'2019-06-03 15:39:15','2019-06-13 14:31:38'),
	(159,132,'Đặt lại mật khẩu','/others/febs/others','others:febs:others','','0',5,'2019-06-12 07:51:08','2019-06-12 07:51:40'),
	(160,3,'Đặt lại mật khẩu',NULL,'user:password:reset',NULL,'1',NULL,'2019-06-13 08:40:13',NULL),
	(161,3,'Xuất Excel',NULL,'user:export',NULL,'1',NULL,'2019-06-13 08:40:34',NULL),
	(162,4,'Xuất Excel',NULL,'role:export',NULL,'1',NULL,'2019-06-13 14:29:00','2019-06-13 14:29:11'),
	(163,5,'Xuất Excel',NULL,'menu:export',NULL,'1',NULL,'2019-06-13 14:29:32',NULL),
	(164,6,'Xuất Excel',NULL,'dept:export',NULL,'1',NULL,'2019-06-13 14:29:59',NULL),
	(165,138,'Sửa đổi cấu hình',NULL,'generator:configure:update',NULL,'1',NULL,'2019-06-13 14:32:09','2019-06-13 14:32:20'),
	(166,139,'Tạo mã',NULL,'generator:generate',NULL,'1',NULL,'2019-06-13 14:32:51',NULL),
	(167,125,'Tải xuống mẫu',NULL,'eximport:template',NULL,'1',NULL,'2019-06-13 14:33:37',NULL),
	(168,125,'Xuất Excel',NULL,'eximport:export',NULL,'1',NULL,'2019-06-13 14:33:57',NULL),
	(169,125,'Nhập Excel',NULL,'eximport:import',NULL,'1',NULL,'2019-06-13 14:34:19',NULL),
	(170,10,'Xuất Excel',NULL,'log:export',NULL,'1',NULL,'2019-06-13 14:34:55',NULL),
	(171,136,'Xóa nhật ký',NULL,'loginlog:delete',NULL,'1',NULL,'2019-06-13 14:35:27','2019-06-13 14:36:08'),
	(172,136,'Xuất Excel',NULL,'loginlog:export',NULL,'1',NULL,'2019-06-13 14:36:26',NULL),
	(175,2,'Tài liệu Swagger','/monitor/swagger','swagger:view','','0',8,'2019-08-18 17:25:36','2019-08-18 17:25:59'),
	(178,0,'Truy vấn hoạt động','','','layui-icon-linechart','0',1,'2019-11-01 15:43:54',NULL),
	(179,0,'Vận hành','','','layui-icon-edit-square','0',2,'2019-11-01 15:45:52',NULL),
	(180,178,'Thống kê thời gian thực','/op/active','active:view','layui-icon-check-circle','0',1,'2019-11-01 15:47:51','2019-11-01 15:47:58'),
	(181,178,'Lịch sử','/query/history','history:view','layui-icon-reloadtime','0',2,'2019-11-01 15:48:42',NULL),
	(182,179,'Cấu hình hoạt động','/op/activity','activity:view','layui-icon-wrench','0',1,'2019-11-01 15:49:58',NULL),
	(183,179,'Quản lý tài khoản user','/op/fenghao','fenghao:view','layui-icon-scissor','0',2,'2019-11-01 15:50:34',NULL),
	(184,182,'Thêm hoạt động mới',NULL,'activity:add',NULL,'1',NULL,'2019-11-01 16:09:02',NULL),
	(185,182,'Sửa đổi hoạt động',NULL,'activity:add',NULL,'1',NULL,'2019-11-01 16:09:40',NULL),
	(186,178,'Lịch sử','/query/history','history:view','layui-icon-reloadtime','0',2,'2024-12-19 11:14:04',NULL),
	(189,182,'Sửa event',NULL,'activity:add',NULL,'1',NULL,'2024-12-25 11:05:07',NULL),
	(191,179,'Gửi thư','/op/mail','mail:view','layui-icon-mail','0',3,'2024-12-26 08:22:04','2024-12-26 08:22:04'),
	(192,191,'Gửi thư',NULL,'mail:send',NULL,'1',NULL,'2024-12-26 08:22:04','2024-12-26 08:22:04'),
	(194,183,'Khóa tài khoản',NULL,'fenghao:send',NULL,'1',NULL,'2024-12-27 12:16:17',NULL),
	(197,179,'Nạp tiền giả lập','/op/fakepay','fakepay:view','layui-icon-rmb','0',3,'2024-12-31 09:06:38',NULL),
	(198,197,'Thực hiện nạp',NULL,'fakepay:send',NULL,'1',NULL,'2024-12-31 09:06:38',NULL),
	(199,179,'Thông báo chạy','/op/subtitle','subtitle:view','layui-icon-notice','0',4,'2024-12-31 09:21:44',NULL),
	(200,199,'Gửi thông báo',NULL,'subtitle:send',NULL,'1',NULL,'2024-12-31 09:21:44',NULL),
	(201,178,'Lịch sử chat','/query/chatContent','query:view','layui-icon-dialogue','0',3,'2025-01-02 06:19:40','2025-01-02 06:19:40'),
	(202,178,'Thông Tin User','/query/playerAllBase','query:view','layui-icon-user','0',4,'2025-01-02 06:57:09','2025-01-02 06:57:09'),
	(203,179,'Gói nạp','/op/recharge','recharge:view','layui-icon-Dollar-circle-fill','0',3,'2024-01-01 00:00:00',NULL),
	(204,203,'Thêm gói',NULL,'recharge:add',NULL,'1',NULL,'2024-01-01 00:00:00',NULL),
	(205,203,'Sửa gói',NULL,'recharge:update',NULL,'1',NULL,'2024-01-01 00:00:00',NULL),
	(206,203,'Xóa gói',NULL,'recharge:delete',NULL,'1',NULL,'2024-01-01 00:00:00',NULL),
	(208,179,'Quản lý máy chủ','/op/server_list','server:view','layui-icon-sever-fill','0',NULL,'2025-01-07 14:29:08',NULL);

/*!40000 ALTER TABLE `t_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_recharge_package
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_recharge_package`;

CREATE TABLE `t_recharge_package` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` varchar(50) NOT NULL COMMENT 'ID gói nạp trong game',
  `name` varchar(100) NOT NULL COMMENT 'Tên gói',
  `price` int NOT NULL COMMENT 'Giá (VNĐ)',
  `coins` int NOT NULL COMMENT 'Số xu',
  `bonus_coins` int DEFAULT '0' COMMENT 'Xu thưởng thêm',
  `first_charge` tinyint(1) DEFAULT '0' COMMENT 'Gói nạp lần đầu',
  `description` text COMMENT 'Mô tả',
  `active` tinyint(1) DEFAULT '1' COMMENT 'Trạng thái',
  `create_time` datetime NOT NULL COMMENT 'Thời gian tạo',
  `update_time` datetime NOT NULL COMMENT 'Thời gian cập nhật',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Bảng gói nạp';



# Dump of table t_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `ROLE_ID` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_NAME` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `REMARK` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ROLE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='角色表';

LOCK TABLES `t_role` WRITE;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;

INSERT INTO `t_role` (`ROLE_ID`, `ROLE_NAME`, `REMARK`, `CREATE_TIME`, `MODIFY_TIME`)
VALUES
	(1,'Quản trị viên hệ thống','Quản trị viên hệ thống, có mọi quyền điều hành ^_^','2019-06-14 16:23:11','2019-11-01 16:10:17'),
	(2,'Đăng ký tài khoản','Đăng ký tài khoản và có quyền xem, thêm người dùng mới (trừ người dùng mới) và xuất Excel','2019-06-14 16:00:15','2019-08-18 17:36:02'),
	(77,'Màn hình Redis','Chịu trách nhiệm về mô-đun Redis','2019-06-14 20:49:22',NULL),
	(78,'Giám sát hệ thống','Chịu trách nhiệm giám sát toàn bộ module hệ thống','2019-06-14 20:50:07',NULL),
	(79,'Đang chạy hàng loạt nhân sự','Chịu trách nhiệm lập kế hoạch nhiệm vụ và chạy mô-đun hàng loạt','2019-06-14 20:51:02',NULL),
	(80,'Nhà phát triển','Có quyền truy cập vào mô-đun tạo mã','2019-06-14 20:51:26',NULL);

/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_role_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_role_menu`;

CREATE TABLE `t_role_menu` (
  `ROLE_ID` bigint NOT NULL COMMENT '角色ID',
  `MENU_ID` bigint NOT NULL COMMENT '菜单/按钮ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='角色菜单关联表';

LOCK TABLES `t_role_menu` WRITE;
/*!40000 ALTER TABLE `t_role_menu` DISABLE KEYS */;

INSERT INTO `t_role_menu` (`ROLE_ID`, `MENU_ID`)
VALUES
	(77,2),
	(77,113),
	(77,114),
	(78,2),
	(78,8),
	(78,23),
	(78,10),
	(78,24),
	(78,170),
	(78,136),
	(78,171),
	(78,172),
	(78,113),
	(78,114),
	(78,127),
	(78,128),
	(78,129),
	(78,130),
	(78,131),
	(79,101),
	(79,102),
	(79,103),
	(79,104),
	(79,105),
	(79,106),
	(79,107),
	(79,108),
	(79,173),
	(79,109),
	(79,110),
	(79,174),
	(80,137),
	(80,138),
	(80,165),
	(80,139),
	(80,166),
	(2,1),
	(2,3),
	(2,161),
	(2,4),
	(2,14),
	(2,162),
	(2,5),
	(2,17),
	(2,163),
	(2,6),
	(2,20),
	(2,164),
	(2,2),
	(2,8),
	(2,10),
	(2,170),
	(2,136),
	(2,172),
	(2,113),
	(2,114),
	(2,127),
	(2,128),
	(2,129),
	(2,130),
	(2,131),
	(2,175),
	(2,101),
	(2,102),
	(2,173),
	(2,109),
	(2,174),
	(2,137),
	(2,138),
	(2,139),
	(2,115),
	(2,132),
	(2,133),
	(2,135),
	(2,134),
	(2,126),
	(2,159),
	(2,116),
	(2,117),
	(2,119),
	(2,120),
	(2,121),
	(2,122),
	(2,123),
	(2,118),
	(2,125),
	(2,167),
	(2,168),
	(2,169),
	(1,178),
	(1,180),
	(1,181),
	(1,179),
	(1,182),
	(1,184),
	(1,185),
	(1,183),
	(1,1),
	(1,3),
	(1,11),
	(1,12),
	(1,13),
	(1,160),
	(1,161),
	(1,4),
	(1,14),
	(1,15),
	(1,16),
	(1,162),
	(1,5),
	(1,17),
	(1,18),
	(1,19),
	(1,163),
	(1,6),
	(1,20),
	(1,21),
	(1,22),
	(1,164),
	(1,2),
	(1,8),
	(1,23),
	(1,10),
	(1,24),
	(1,170),
	(1,136),
	(1,171),
	(1,172),
	(1,113),
	(1,114),
	(1,127),
	(1,128),
	(1,129),
	(1,130),
	(1,131),
	(1,175),
	(1,137),
	(1,138),
	(1,165),
	(1,139),
	(1,166),
	(1,115),
	(1,132),
	(1,133),
	(1,135),
	(1,134),
	(1,126),
	(1,159),
	(1,116),
	(1,117),
	(1,119),
	(1,120),
	(1,121),
	(1,122),
	(1,123),
	(1,118),
	(1,125),
	(1,167),
	(1,168),
	(1,169),
	(1,190),
	(1,191),
	(1,192),
	(1,193),
	(1,179),
	(1,195),
	(1,195),
	(1,196),
	(1,196),
	(1,131),
	(1,197),
	(1,198),
	(1,195),
	(1,197),
	(1,198),
	(1,199),
	(1,200),
	(1,201),
	(1,202),
	(1,201),
	(1,202),
	(1,203),
	(1,203),
	(1,208),
	(1,209),
	(1,210),
	(1,211),
	(1,212),
	(1,213),
	(1,214),
	(1,215),
	(1,216),
	(1,217),
	(1,221),
	(1,222),
	(1,223),
	(1,224),
	(1,225);

/*!40000 ALTER TABLE `t_role_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `USER_ID` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERNAME` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `DEPT_ID` bigint DEFAULT NULL COMMENT '部门ID',
  `EMAIL` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
  `MOBILE` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系电话',
  `STATUS` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '状态 0锁定 1有效',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `LAST_LOGIN_TIME` datetime DEFAULT NULL COMMENT '最近访问时间',
  `SSEX` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `IS_TAB` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '是否开启tab，0关闭 1开启',
  `THEME` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '主题',
  `AVATAR` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '头像',
  `DESCRIPTION` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='用户表';

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;

INSERT INTO `t_user` (`USER_ID`, `USERNAME`, `PASSWORD`, `DEPT_ID`, `EMAIL`, `MOBILE`, `STATUS`, `CREATE_TIME`, `MODIFY_TIME`, `LAST_LOGIN_TIME`, `SSEX`, `IS_TAB`, `THEME`, `AVATAR`, `DESCRIPTION`)
VALUES
	(1,'MrBird','a4f18ee51f21294dc3ae8b7c5642035a',1,'mrbird@qq.com','17788888888','1','2019-06-14 20:39:22','2024-12-27 19:55:17','2025-01-10 15:47:38','0','0','black','1d22f3e41d284f50b2c8fc32e0788698.jpeg','aaaaa'),
	(2,'Scott','1d685729d113cfd03872f154939bee1c',10,'scott@gmail.com','17722222222','1','2019-06-14 20:55:53','2019-06-14 21:05:43','2019-10-31 22:05:35','0','1','black','gaOngJwsRYRaVAuXXcmB.png','我是scott。'),
	(3,'Reina','1461afff857c02afbfb768aa3771503d',4,'Reina@hotmail.com','17711111111','0','2019-06-14 21:07:38','2019-06-14 21:09:06','2019-06-14 21:08:26','1','1','black','5997fedcc7bd4cffbd350b40d1b5b987.jpg','由于公款私用，已被封禁。'),
	(4,'Micaela','9f2daa2c7bed6870fcbb5b9a51d6300e',10,'Micaela@163.com','17733333333','1','2019-06-14 21:10:13','2019-06-14 21:11:26','2019-10-31 22:16:40','0','0','white','20180414165909.jpg','我叫米克拉'),
	(5,'Jana','176679b77b3c3e352bd3b30ddc81083e',8,'Jana@126.com','17744444444','1','2019-06-14 21:12:16','2019-06-14 21:12:52','2019-06-14 21:12:32','1','1','white','20180414165821.jpg','大家好，我叫简娜'),
	(6,'Georgie','dffc683378cdaa015a0ee9554c532225',3,'Georgie@qq.com','17766666666','0','2019-06-14 21:15:09','2019-06-14 21:16:25','2019-06-14 21:16:11','2','0','black','BiazfanxmamNRoxxVxka.png','生产执行rm -rf *，账号封禁T T'),
	(7,'Margot','31379841b9f4bfde22b8b40471e9a6ce',9,'Margot@qq.com','13444444444','1','2019-06-14 21:17:53','2019-06-14 21:18:59','2019-06-14 21:18:07','1','1','white','20180414165834.jpg','大家好我叫玛戈'),
	(8,'DyKo','a4f18ee51f21294dc3ae8b7c5642035a',8,'','','1','2024-12-25 10:59:24',NULL,'2025-01-10 15:02:36','2','1','black','default.jpg','');

/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_user_role`;

CREATE TABLE `t_user_role` (
  `USER_ID` bigint NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='用户角色关联表';

LOCK TABLES `t_user_role` WRITE;
/*!40000 ALTER TABLE `t_user_role` DISABLE KEYS */;

INSERT INTO `t_user_role` (`USER_ID`, `ROLE_ID`)
VALUES
	(2,2),
	(3,77),
	(4,78),
	(5,79),
	(6,80),
	(7,78),
	(7,79),
	(7,80),
	(8,1),
	(8,2),
	(8,77),
	(8,78),
	(8,80),
	(8,79),
	(1,1),
	(1,2),
	(1,77),
	(1,78),
	(1,79),
	(1,80);

/*!40000 ALTER TABLE `t_user_role` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
