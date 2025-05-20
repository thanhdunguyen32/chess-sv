-- MySQL dump 10.13  Distrib 8.0.18, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: febs_hack
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `daily_stat`
--

DROP TABLE IF EXISTS `daily_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `daily_stat` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `new_player` int(11) DEFAULT NULL,
  `active_player` int(11) DEFAULT NULL,
  `new_pay` int(11) DEFAULT NULL,
  `pay_count` int(11) DEFAULT NULL,
  `pay_sum` int(11) DEFAULT NULL,
  `yesterday_retain` float DEFAULT NULL,
  `stat_time` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_stat`
--

LOCK TABLES `daily_stat` WRITE;
/*!40000 ALTER TABLE `daily_stat` DISABLE KEYS */;
INSERT INTO `daily_stat` VALUES (1,1833,1833,75,75,2718,0,'2019-12-17 23:59:55'),(2,889,1018,38,43,1624,7.03764,'2019-12-18 23:59:55'),(3,734,854,26,31,590,6.86164,'2019-12-19 23:59:55'),(4,483,571,15,22,883,5.31335,'2019-12-20 23:59:55'),(5,507,589,10,15,931,6.21118,'2019-12-21 23:59:55');
/*!40000 ALTER TABLE `daily_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gs_list`
--

DROP TABLE IF EXISTS `gs_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gs_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zone_id` int(11) DEFAULT NULL COMMENT '区号',
  `name` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gs_list`
--

LOCK TABLES `gs_list` WRITE;
/*!40000 ALTER TABLE `gs_list` DISABLE KEYS */;
INSERT INTO `gs_list` VALUES (1,1,'hack1区','db.xxgames.com',12001);
/*!40000 ALTER TABLE `gs_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_dept`
--

DROP TABLE IF EXISTS `t_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dept` (
  `DEPT_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级部门ID',
  `DEPT_NAME` varchar(100) NOT NULL COMMENT '部门名称',
  `ORDER_NUM` bigint(20) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`DEPT_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_dept`
--

LOCK TABLES `t_dept` WRITE;
/*!40000 ALTER TABLE `t_dept` DISABLE KEYS */;
INSERT INTO `t_dept` VALUES (1,0,'开发部',1,'2019-06-14 20:56:41',NULL),(11,0,'运营部',2,'2019-11-10 17:12:32',NULL);
/*!40000 ALTER TABLE `t_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_eximport`
--

DROP TABLE IF EXISTS `t_eximport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_eximport` (
  `FIELD1` varchar(20) NOT NULL COMMENT '字段1',
  `FIELD2` int(11) NOT NULL COMMENT '字段2',
  `FIELD3` varchar(100) NOT NULL COMMENT '字段3',
  `CREATE_TIME` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='Excel导入导出测试';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_eximport`
--

LOCK TABLES `t_eximport` WRITE;
/*!40000 ALTER TABLE `t_eximport` DISABLE KEYS */;
INSERT INTO `t_eximport` VALUES ('字段1',1,'mrbird0@gmail.com','2019-06-13 03:14:06'),('字段1',2,'mrbird1@gmail.com','2019-06-13 03:14:06'),('字段1',3,'mrbird2@gmail.com','2019-06-13 03:14:06'),('字段1',4,'mrbird3@gmail.com','2019-06-13 03:14:06'),('字段1',5,'mrbird4@gmail.com','2019-06-13 03:14:06'),('字段1',6,'mrbird5@gmail.com','2019-06-13 03:14:06'),('字段1',7,'mrbird6@gmail.com','2019-06-13 03:14:06'),('字段1',8,'mrbird7@gmail.com','2019-06-13 03:14:06'),('字段1',9,'mrbird8@gmail.com','2019-06-13 03:14:06'),('字段1',10,'mrbird9@gmail.com','2019-06-13 03:14:06'),('字段1',11,'mrbird10@gmail.com','2019-06-13 03:14:06'),('字段1',12,'mrbird11@gmail.com','2019-06-13 03:14:06'),('字段1',13,'mrbird12@gmail.com','2019-06-13 03:14:06'),('字段1',14,'mrbird13@gmail.com','2019-06-13 03:14:06'),('字段1',15,'mrbird14@gmail.com','2019-06-13 03:14:06'),('字段1',16,'mrbird15@gmail.com','2019-06-13 03:14:06'),('字段1',17,'mrbird16@gmail.com','2019-06-13 03:14:06'),('字段1',18,'mrbird17@gmail.com','2019-06-13 03:14:06'),('字段1',19,'mrbird18@gmail.com','2019-06-13 03:14:06'),('字段1',20,'mrbird19@gmail.com','2019-06-13 03:14:06');
/*!40000 ALTER TABLE `t_eximport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_generator_config`
--

DROP TABLE IF EXISTS `t_generator_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_generator_config` (
  `id` int(11) NOT NULL COMMENT '主键',
  `author` varchar(20) NOT NULL COMMENT '作者',
  `base_package` varchar(50) NOT NULL COMMENT '基础包名',
  `entity_package` varchar(20) NOT NULL COMMENT 'entity文件存放路径',
  `mapper_package` varchar(20) NOT NULL COMMENT 'mapper文件存放路径',
  `mapper_xml_package` varchar(20) NOT NULL COMMENT 'mapper xml文件存放路径',
  `service_package` varchar(20) NOT NULL COMMENT 'servcie文件存放路径',
  `service_impl_package` varchar(20) NOT NULL COMMENT 'serviceImpl文件存放路径',
  `controller_package` varchar(20) NOT NULL COMMENT 'controller文件存放路径',
  `is_trim` char(1) NOT NULL COMMENT '是否去除前缀 1是 0否',
  `trim_value` varchar(10) DEFAULT NULL COMMENT '前缀内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='代码生成配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_generator_config`
--

LOCK TABLES `t_generator_config` WRITE;
/*!40000 ALTER TABLE `t_generator_config` DISABLE KEYS */;
INSERT INTO `t_generator_config` VALUES (1,'MrBird','cc.mrbird.febs.gen','entity','mapper','mapper','service','service.impl','controller','1','t_');
/*!40000 ALTER TABLE `t_generator_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_job`
--

DROP TABLE IF EXISTS `t_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_job` (
  `JOB_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `BEAN_NAME` varchar(50) NOT NULL COMMENT 'spring bean名称',
  `METHOD_NAME` varchar(50) NOT NULL COMMENT '方法名',
  `PARAMS` varchar(50) DEFAULT NULL COMMENT '参数',
  `CRON_EXPRESSION` varchar(20) NOT NULL COMMENT 'cron表达式',
  `STATUS` char(2) NOT NULL COMMENT '任务状态  0：正常  1：暂停',
  `REMARK` varchar(50) DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`JOB_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='定时任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_job`
--

LOCK TABLES `t_job` WRITE;
/*!40000 ALTER TABLE `t_job` DISABLE KEYS */;
INSERT INTO `t_job` VALUES (1,'testTask','test','mrbird','0/1 * * * * ?','1','有参任务调度测试~~','2018-02-24 16:26:14'),(2,'testTask','test1',NULL,'0/10 * * * * ?','1','无参任务调度测试','2018-02-24 17:06:23'),(3,'testTask','test','hello world','0/1 * * * * ?','1','有参任务调度测试,每隔一秒触发','2018-02-26 09:28:26'),(11,'testTask','test2',NULL,'0/5 * * * * ?','1','测试异常','2018-02-26 11:15:30');
/*!40000 ALTER TABLE `t_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_job_log`
--

DROP TABLE IF EXISTS `t_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_job_log` (
  `LOG_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `JOB_ID` bigint(20) NOT NULL COMMENT '任务id',
  `BEAN_NAME` varchar(100) NOT NULL COMMENT 'spring bean名称',
  `METHOD_NAME` varchar(100) NOT NULL COMMENT '方法名',
  `PARAMS` varchar(200) DEFAULT NULL COMMENT '参数',
  `STATUS` char(2) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `ERROR` text COMMENT '失败信息',
  `TIMES` decimal(11,0) DEFAULT NULL COMMENT '耗时(单位：毫秒)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`LOG_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='调度日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_job_log`
--

LOCK TABLES `t_job_log` WRITE;
/*!40000 ALTER TABLE `t_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_log`
--

DROP TABLE IF EXISTS `t_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '操作用户',
  `OPERATION` text COMMENT '操作内容',
  `TIME` decimal(11,0) DEFAULT NULL COMMENT '耗时',
  `METHOD` text COMMENT '操作方法',
  `PARAMS` text COMMENT '方法参数',
  `IP` varchar(64) DEFAULT NULL COMMENT '操作者IP',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `location` varchar(50) DEFAULT NULL COMMENT '操作地点',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
INSERT INTO `t_log` VALUES (1,'MrBird','更新公告',32,'cc.mrbird.febs.system.controller.OperationController.updateGonggao()',' gonggaoConfig: GonggaoConfig(gonggaoContent=1, lsHostName=cszj-bt.xxgames.com, lsLanPort=9101)',NULL,'2019-11-29 13:28:25',''),(2,'MrBird','更新公告',6,'cc.mrbird.febs.system.controller.OperationController.updateGonggao()',' gonggaoConfig: GonggaoConfig(gonggaoContent=【官方公告】\n\n亲爱的玩家：\n      欢迎来到《众神纷争bt》大世界！在这里你将与众神一起抵挡异族入侵，挑战天空之塔！更有类MOBA的决斗玩法~赶紧来体验吧！, lsHostName=cszj-bt.xxgames.com, lsLanPort=9101)',NULL,'2019-11-29 13:28:37',''),(3,'MrBird','更新公告',4,'cc.mrbird.febs.system.controller.OperationController.updateGonggao()',' gonggaoConfig: GonggaoConfig(gonggaoContent=【官方公告】\n\n亲爱的玩家：\n      欢迎来到《众神纷争bt》大世界！在这里你将与众神一起抵挡异族入侵，挑战天空之塔！更有类MOBA的决斗玩法~赶紧来体验吧！, lsHostName=cszj-bt.xxgames.com, lsLanPort=9101)',NULL,'2019-11-29 14:45:46',''),(4,'MrBird','生成CDK',20,'cc.mrbird.febs.system.controller.OperationController.cdkGenerate()',' cnt: \"1\" area: \"1\" awardId: \"12120\" cdkName: \"新手礼包\" isReuse: \"1\"','116.237.204.24','2019-12-11 20:26:33','中国|华东|上海市|上海市|电信'),(5,'MrBird','生成CDK',10,'cc.mrbird.febs.system.controller.OperationController.cdkGenerate()',' cnt: \"1\" area: \"2\" awardId: \"12121\" cdkName: \"VIP礼包\" isReuse: \"1\"',NULL,'2019-12-11 20:26:44',''),(6,'MrBird','生成CDK',9,'cc.mrbird.febs.system.controller.OperationController.cdkGenerate()',' cnt: \"1\" area: \"3\" awardId: \"12122\" cdkName: \"诸神礼包\" isReuse: \"1\"',NULL,'2019-12-11 20:26:53',''),(7,'MrBird','删除用户',42,'cc.mrbird.febs.system.controller.UserController.deleteUsers()',' userIds: \"3\"',NULL,'2019-12-11 21:05:29',''),(8,'MrBird','删除用户',11,'cc.mrbird.febs.system.controller.UserController.deleteUsers()',' userIds: \"8,9,10\"','116.237.204.24','2019-12-11 21:05:41','中国|华东|上海市|上海市|电信'),(9,'MrBird','新增用户',35,'cc.mrbird.febs.system.controller.UserController.addUser()',' user: \"User(userId=11, username=shenshi01, password=68209e68dd5eb81c158069767e232e55, deptId=11, email=, mobile=, status=1, createTime=Wed Dec 11 21:05:55 CST 2019, modifyTime=null, lastLoginTime=null, sex=2, avatar=default.jpg, theme=black, isTab=1, description=, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, gsEntity=null, toSendGsIds=null)\"',NULL,'2019-12-11 21:05:56',''),(10,'MrBird','新增用户',13,'cc.mrbird.febs.system.controller.UserController.addUser()',' user: \"User(userId=12, username=shenshi02, password=4703dc3e1d7362f77239651695ea9b4e, deptId=11, email=, mobile=, status=1, createTime=Wed Dec 11 21:06:07 CST 2019, modifyTime=null, lastLoginTime=null, sex=2, avatar=default.jpg, theme=black, isTab=1, description=, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, gsEntity=null, toSendGsIds=null)\"',NULL,'2019-12-11 21:06:08',''),(11,'shenshi02','更新公告',32,'cc.mrbird.febs.system.controller.OperationController.updateGonggao()',' gonggaoConfig: GonggaoConfig(gonggaoContent=11, lsHostName=cszj-bt.xxgames.com, lsLanPort=9101)',NULL,'2019-12-11 21:17:02',''),(12,'MrBird','发送邮件附件',17,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'3\', sender=\'111\', mailTitle=\'11\', mailCont=\'12321\', attch=\'11140018|2-11140019|5\', validity=30}',NULL,'2019-12-12 09:56:43',''),(13,'shenshi01','发送邮件附件',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=null, addressee=1, receiveId=\'1\', sender=\'12\', mailTitle=\'2\', mailCont=\'3\', attch=\'11100002|12\', validity=30}',NULL,'2019-12-12 10:10:53',''),(14,'shenshi01','发送邮件附件',9,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'1\', sender=\'12\', mailTitle=\'2\', mailCont=\'3\', attch=\'11100002|12\', validity=30}',NULL,'2019-12-12 10:10:58',''),(15,'shenshi01','发送邮件附件',5,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'6\', sender=\'33\', mailTitle=\'33\', mailCont=\'333\', attch=\'11140010|10\', validity=30}',NULL,'2019-12-12 11:45:59',''),(16,'shenshi01','发送邮件附件',3,'cc.mrbird.febs.system.controller.OperationController.mailSend()',' mailParams: MailParams{selected_gs=[1], addressee=1, receiveId=\'6\', sender=\'33\', mailTitle=\'33\', mailCont=\'333\', attch=\'22020005|2-22020003|2\', validity=30}','113.66.0.124','2019-12-12 12:00:25','中国|华南|广东省|广州市|电信'),(17,'MrBird','发送模拟充值',6,'cc.mrbird.febs.system.controller.OperationController.fakepaySend()',' fakepayConfig: FakepayConfig(playerId=1, payMoney=10, gsId=1)',NULL,'2020-11-19 13:58:20',''),(18,'MrBird','更新公告',7,'cc.mrbird.febs.system.controller.OperationController.updateGonggao()',' gonggaoConfig: GonggaoConfig(gonggaoContent=, lsHostName=db.xxgames.com, lsLanPort=8101)',NULL,'2020-11-19 14:09:38',''),(19,'MrBird','更新公告',10,'cc.mrbird.febs.system.controller.OperationController.updateGonggao()',' gonggaoConfig: GonggaoConfig(gonggaoContent=《挂机自走三国》现在已开启公测，感谢您对我们游戏的支持和喜爱！\n\n充值途径只能通过官方充值，没有任何第三方代充途径。为了自身帐号的安全，请各位主公不要相信任何非官方充值渠道，不要掉入不法分子的圈套，谨防上当。\n\n同时，建议游客登录的玩家绑定手机号，改成正式账号登录，如遇游戏中遇到登录或其他任何问题，请及时联系客服。*/*新服动态：\n\n*/*【版本更新内容】\n\n*/*本周活动内容：点将周活动上线；\n\n【超值礼包】\n活动期间，购买节日礼包可获得大量奖励，每档礼包限购5次；\n【超值珍品】\n活动期间，购买礼包可获得大量奖励，每个礼包限购1次；\n【点将积分】\n活动期间，点将1次获得1积分，积分达到指定目标后可获得丰厚奖励； \n【点将任务】\n活动期间，完成指定认为可获得对应奖励（点将台中置换所得武将不参与获得）；\n【神秘宝箱】\n活动期间，通过点将，置换，购买礼包获得积分，积分达到目标可获得丰厚奖励；, lsHostName=db.xxgames.com, lsLanPort=8101)','192.168.1.5','2020-11-19 14:10:59','内网IP|0|0|内网IP|内网IP');
/*!40000 ALTER TABLE `t_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_login_log`
--

DROP TABLE IF EXISTS `t_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_login_log` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户名',
  `LOGIN_TIME` datetime NOT NULL COMMENT '登录时间',
  `LOCATION` varchar(50) DEFAULT NULL COMMENT '登录地点',
  `IP` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `SYSTEM` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `BROWSER` varchar(50) DEFAULT NULL COMMENT '浏览器',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_login_log`
--

LOCK TABLES `t_login_log` WRITE;
/*!40000 ALTER TABLE `t_login_log` DISABLE KEYS */;
INSERT INTO `t_login_log` VALUES (1,'mrbird','2019-11-29 13:26:15','中国|华东|上海市|上海市|电信','101.93.125.92','Windows 10','Chrome 69'),(2,'mrbird','2019-12-05 15:30:21','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(3,'mrbird','2019-12-06 16:33:03','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(4,'mrbird','2019-12-11 20:22:25','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(5,'shenshi01','2019-12-11 21:06:21','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(6,'mrbird','2019-12-11 21:10:03','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(7,'shenshi02','2019-12-11 21:14:23','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(8,'shenshi02','2019-12-11 21:16:46','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(9,'mrbird','2019-12-11 22:17:02','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(10,'shenshi01','2019-12-12 10:03:27','中国|华南|广东省|广州市|电信','113.66.0.124','Windows 10','Chrome 57'),(11,'mrbird','2019-12-16 15:53:06','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(12,'mrbird','2019-12-16 21:10:35','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(13,'shenshi01','2019-12-17 09:26:08','中国|华南|广东省|广州市|电信','59.41.75.58','Windows 10','Chrome 57'),(14,'shenshi01','2019-12-17 11:59:01','中国|华南|广东省|广州市|电信','113.65.131.131','Windows 10','Chrome 79'),(15,'shenshi01','2019-12-17 12:11:20','中国|华南|广东省|广州市|电信','113.65.152.235','Windows 10','Chrome 70'),(16,'mrbird','2019-12-17 12:26:42','中国|华东|上海市|上海市|电信','101.84.190.225','Linux','Chrome 70'),(17,'mrbird','2019-12-17 22:58:23','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(18,'shenshi01','2019-12-18 11:11:16','中国|华南|广东省|广州市|电信','113.65.152.235','Windows 10','Chrome 70'),(19,'mrbird','2019-12-18 11:33:56','中国|华东|上海市|上海市|电信','116.237.204.24','Mac OS X 10_14_6','Safari Version 12.1.2 '),(20,'mrbird','2019-12-18 16:17:01','中国|华东|上海市|上海市|电信','116.237.204.24','Linux','Chrome 70'),(21,'mrbird','2019-12-18 16:49:27','中国|华东|上海市|上海市|电信','116.237.204.24','Mac OS X) AppleW','Safari Version 12.1.2 '),(22,'shenshi01','2019-12-18 20:30:45','中国|华南|广东省|广州市|电信','113.65.152.235','Windows 10','Chrome 70'),(23,'shenshi01','2019-12-19 10:56:58','中国|华南|广东省|广州市|电信','59.41.118.255','Windows 10','Chrome 79'),(24,'mrbird','2019-12-19 13:16:45','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(25,'shenshi01','2019-12-19 13:41:26','中国|华南|广东省|广州市|电信','59.41.118.255','Windows 10','Chrome 79'),(26,'mrbird','2019-12-19 20:32:43','中国|华东|上海市|上海市|电信','116.237.204.24','Linux','Chrome 70'),(27,'shenshi01','2019-12-19 20:43:41','中国|华南|广东省|广州市|电信','59.41.118.255','Windows 10','Chrome 79'),(28,'shenshi01','2019-12-20 11:52:02','中国|华南|广东省|广州市|电信','113.109.20.205','Windows 10','Chrome 70'),(29,'shenshi01','2019-12-20 16:08:06','中国|华南|广东省|广州市|电信','113.109.20.205','Windows 10','Chrome 70'),(30,'shenshi01','2019-12-20 18:00:13','中国|华南|广东省|广州市|电信','113.109.20.205','Windows 10','Chrome 70'),(31,'mrbird','2019-12-20 22:04:57','中国|华东|上海市|上海市|电信','116.237.204.24','Linux','Chrome 70'),(32,'shenshi01','2019-12-21 10:38:08','中国|华南|广东省|广州市|鹏博士','118.196.67.84','Windows 10','Chrome 63'),(33,'mrbird','2019-12-21 22:46:36','中国|华东|上海市|上海市|电信','116.237.204.24','Linux','Chrome 70'),(34,'shenshi01','2019-12-22 10:29:16','中国|华南|广东省|广州市|电信','113.109.110.38','Windows 10','Chrome 76'),(35,'shenshi01','2019-12-22 14:53:18','中国|华南|广东省|广州市|电信','113.109.110.38','Windows 10','Chrome 76'),(36,'mrbird','2019-12-22 14:54:26','中国|华东|上海市|上海市|电信','116.237.204.24','Windows 10','Chrome 69'),(37,'mrbird','2020-10-10 10:44:29','内网IP|0|0|内网IP|内网IP','192.168.1.5','Windows 10','Chrome 70'),(38,'mrbird','2020-10-10 11:15:38','内网IP|0|0|内网IP|内网IP','192.168.1.5','Windows 10','Chrome 70'),(39,'mrbird','2020-11-01 20:04:13','中国|华东|上海市|上海市|电信','101.93.124.2','Mac OS X 10_14_6','Safari Version 12.1.2 '),(40,'mrbird','2020-11-19 13:53:39','内网IP|0|0|内网IP|内网IP','192.168.1.5','Windows 10','Chrome 70');
/*!40000 ALTER TABLE `t_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_menu`
--

DROP TABLE IF EXISTS `t_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_menu` (
  `MENU_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `MENU_NAME` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `URL` varchar(50) DEFAULT NULL COMMENT '菜单URL',
  `PERMS` text COMMENT '权限标识',
  `ICON` varchar(50) DEFAULT NULL COMMENT '图标',
  `TYPE` char(2) NOT NULL COMMENT '类型 0菜单 1按钮',
  `ORDER_NUM` bigint(20) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_menu`
--

LOCK TABLES `t_menu` WRITE;
/*!40000 ALTER TABLE `t_menu` DISABLE KEYS */;
INSERT INTO `t_menu` VALUES (1,0,'系统管理','','','layui-icon-setting','0',3,'2017-12-27 16:39:07','2019-11-01 15:44:45'),(2,0,'系统监控','','','layui-icon-alert','0',4,'2017-12-27 16:45:51','2019-11-01 15:44:41'),(3,1,'用户管理','/system/user','user:view','layui-icon-meh','0',1,'2017-12-27 16:47:13','2019-06-13 11:13:55'),(4,1,'角色管理','/system/role','role:view','','0',2,'2017-12-27 16:48:09','2019-06-13 08:57:19'),(5,1,'菜单管理','/system/menu','menu:view','','0',3,'2017-12-27 16:48:57','2019-06-13 08:57:34'),(6,1,'部门管理','/system/dept','dept:view','','0',4,'2017-12-27 16:57:33','2019-06-14 19:56:00'),(8,2,'在线用户','/monitor/online','online:view','','0',1,'2017-12-27 16:59:33','2019-06-13 14:30:31'),(10,2,'系统日志','/monitor/log','log:view','','0',2,'2017-12-27 17:00:50','2019-06-13 14:30:37'),(11,3,'新增用户',NULL,'user:add',NULL,'1',NULL,'2017-12-27 17:02:58',NULL),(12,3,'修改用户',NULL,'user:update',NULL,'1',NULL,'2017-12-27 17:04:07',NULL),(13,3,'删除用户',NULL,'user:delete',NULL,'1',NULL,'2017-12-27 17:04:58',NULL),(14,4,'新增角色',NULL,'role:add',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),(15,4,'修改角色',NULL,'role:update',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),(16,4,'删除角色',NULL,'role:delete',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),(17,5,'新增菜单',NULL,'menu:add',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),(18,5,'修改菜单',NULL,'menu:update',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),(19,5,'删除菜单',NULL,'menu:delete',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),(20,6,'新增部门',NULL,'dept:add',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),(21,6,'修改部门',NULL,'dept:update',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),(22,6,'删除部门',NULL,'dept:delete',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),(23,8,'踢出用户',NULL,'user:kickout',NULL,'1',NULL,'2017-12-27 17:11:13',NULL),(24,10,'删除日志',NULL,'log:delete',NULL,'1',NULL,'2017-12-27 17:11:45','2019-06-06 05:56:40'),(113,2,'Redis监控','/monitor/redis/info','redis:view','','0',4,'2018-06-28 14:29:42','2019-06-13 14:30:45'),(114,2,'Redis终端','/monitor/redis/terminal','redis:terminal:view','','0',5,'2018-06-28 15:35:21','2019-06-13 14:30:54'),(115,0,'其他模块','','','layui-icon-gift','0',6,'2019-05-27 10:18:07','2019-11-01 15:44:30'),(116,115,'Apex图表','','',NULL,'0',2,'2019-05-27 10:21:35',NULL),(117,116,'线性图表','/others/apex/line','apex:line:view',NULL,'0',1,'2019-05-27 10:24:49',NULL),(118,115,'高德地图','/others/map','map:view','','0',3,'2019-05-27 17:13:12','2019-06-12 15:33:00'),(119,116,'面积图表','/others/apex/area','apex:area:view',NULL,'0',2,'2019-05-27 18:49:22',NULL),(120,116,'柱形图表','/others/apex/column','apex:column:view',NULL,'0',3,'2019-05-27 18:51:33',NULL),(121,116,'雷达图表','/others/apex/radar','apex:radar:view',NULL,'0',4,'2019-05-27 18:56:05',NULL),(122,116,'条形图表','/others/apex/bar','apex:bar:view',NULL,'0',5,'2019-05-27 18:57:02',NULL),(123,116,'混合图表','/others/apex/mix','apex:mix:view','','0',6,'2019-05-27 18:58:04','2019-06-06 02:55:23'),(125,115,'导入导出','/others/eximport','others:eximport:view','','0',4,'2019-05-27 19:01:57','2019-06-13 01:20:14'),(126,132,'系统图标','/others/febs/icon','febs:icons:view','','0',4,'2019-05-27 19:03:18','2019-06-06 03:05:26'),(127,2,'请求追踪','/monitor/httptrace','httptrace:view','','0',6,'2019-05-27 19:06:38','2019-06-13 14:36:43'),(128,2,'系统信息',NULL,NULL,NULL,'0',7,'2019-05-27 19:08:23',NULL),(129,128,'JVM信息','/monitor/jvm','jvm:view','','0',1,'2019-05-27 19:08:50','2019-06-13 14:36:51'),(130,128,'Tomcat信息','/monitor/tomcat','tomcat:view','','0',2,'2019-05-27 19:09:26','2019-06-13 14:36:57'),(131,128,'服务器信息','/monitor/server','server:view','','0',3,'2019-05-27 19:10:07','2019-06-13 14:37:04'),(132,115,'FEBS组件','','',NULL,'0',1,'2019-05-27 19:13:54',NULL),(133,132,'表单组件','/others/febs/form','febs:form:view',NULL,'0',1,'2019-05-27 19:14:45',NULL),(134,132,'FEBS工具','/others/febs/tools','febs:tools:view','','0',3,'2019-05-29 10:11:22','2019-06-12 13:21:27'),(135,132,'表单组合','/others/febs/form/group','febs:formgroup:view',NULL,'0',2,'2019-05-29 10:16:19',NULL),(136,2,'登录日志','/monitor/loginlog','loginlog:view','','0',3,'2019-05-29 15:56:15','2019-06-13 14:35:56'),(137,0,'代码生成','','','layui-icon-verticalright','0',5,'2019-06-03 15:35:58','2019-11-01 15:44:36'),(138,137,'生成配置','/generator/configure','generator:configure:view',NULL,'0',1,'2019-06-03 15:38:36',NULL),(139,137,'代码生成','/generator/generator','generator:view','','0',2,'2019-06-03 15:39:15','2019-06-13 14:31:38'),(159,132,'其他组件','/others/febs/others','others:febs:others','','0',5,'2019-06-12 07:51:08','2019-06-12 07:51:40'),(160,3,'密码重置',NULL,'user:password:reset',NULL,'1',NULL,'2019-06-13 08:40:13',NULL),(161,3,'导出Excel',NULL,'user:export',NULL,'1',NULL,'2019-06-13 08:40:34',NULL),(162,4,'导出Excel',NULL,'role:export',NULL,'1',NULL,'2019-06-13 14:29:00','2019-06-13 14:29:11'),(163,5,'导出Excel',NULL,'menu:export',NULL,'1',NULL,'2019-06-13 14:29:32',NULL),(164,6,'导出Excel',NULL,'dept:export',NULL,'1',NULL,'2019-06-13 14:29:59',NULL),(165,138,'修改配置',NULL,'generator:configure:update',NULL,'1',NULL,'2019-06-13 14:32:09','2019-06-13 14:32:20'),(166,139,'生成代码',NULL,'generator:generate',NULL,'1',NULL,'2019-06-13 14:32:51',NULL),(167,125,'模板下载',NULL,'eximport:template',NULL,'1',NULL,'2019-06-13 14:33:37',NULL),(168,125,'导出Excel',NULL,'eximport:export',NULL,'1',NULL,'2019-06-13 14:33:57',NULL),(169,125,'导入Excel',NULL,'eximport:import',NULL,'1',NULL,'2019-06-13 14:34:19',NULL),(170,10,'导出Excel',NULL,'log:export',NULL,'1',NULL,'2019-06-13 14:34:55',NULL),(171,136,'删除日志',NULL,'loginlog:delete',NULL,'1',NULL,'2019-06-13 14:35:27','2019-06-13 14:36:08'),(172,136,'导出Excel',NULL,'loginlog:export',NULL,'1',NULL,'2019-06-13 14:36:26',NULL),(175,2,'Swagger文档','/monitor/swagger','swagger:view','','0',8,'2019-08-18 17:25:36','2019-08-18 17:25:59'),(178,0,'运营查询','','','layui-icon-linechart','0',1,'2019-11-01 15:43:54',NULL),(179,0,'操作','','','layui-icon-edit-square','0',2,'2019-11-01 15:45:52',NULL),(181,178,'历史记录','/query/daily_stat','query:view','','0',1,'2019-11-01 15:48:42','2019-11-05 13:03:25'),(182,179,'活动配置','/op/activity','activity:view','','0',1,'2019-11-01 15:49:58','2019-11-05 13:09:26'),(183,179,'封号','/op/fenghao','fenghao:view','','0',2,'2019-11-01 15:50:34','2019-11-05 13:09:32'),(184,182,'编辑活动',NULL,'activity:update',NULL,'1',NULL,'2019-11-01 16:09:02','2019-11-06 12:21:07'),(185,182,'清除数据',NULL,'activity:reset',NULL,'1',NULL,'2019-11-01 16:09:40','2019-11-06 12:21:28'),(186,179,'登录公告','/op/gonggao','gonggao:view','','0',6,'2019-11-03 13:22:51','2019-11-05 13:06:58'),(187,186,'更新',NULL,'gonggao:update',NULL,'1',NULL,'2019-11-03 15:14:38',NULL),(188,178,'充值总排行','/query/payRankAll','query:view','','0',2,'2019-11-05 12:56:17','2019-11-06 09:48:58'),(189,178,'充值日排行','/query/dailyTopPay','query:view','','0',3,'2019-11-05 12:58:20',NULL),(190,178,'所有充值记录','/query/payLogAll','query:view','','0',4,'2019-11-05 13:01:07',NULL),(191,178,'聊天内容','/query/chatContent','query:view','','0',5,'2019-11-05 13:02:00',NULL),(192,178,'道具流向日志','/query/logItemGo','query:view','','0',6,'2019-11-05 13:02:23',NULL),(193,178,'所有玩家信息','/query/playerAllBase','query:view','','0',7,'2019-11-05 13:02:50',NULL),(194,179,'禁言','/op/jinyan','jinyan:view','','0',3,'2019-11-05 13:06:19','2019-11-06 10:30:44'),(195,179,'踢下线','/op/kick','kick:view','','0',4,'2019-11-05 13:06:48',NULL),(196,179,'走马灯','/op/subtitle','subtitle:view','','0',5,'2019-11-05 13:07:17',NULL),(197,179,'邮件','/op/mail','mail:view','','0',7,'2019-11-05 13:07:55',NULL),(198,179,'模拟充值','/op/fakepay','fakepay:view','','0',8,'2019-11-05 13:08:23',NULL),(199,179,'激活码','/op/cdk','cdk:view','','0',9,'2019-11-05 13:08:53',NULL),(201,196,'发送',NULL,'subtitle:send',NULL,'1',NULL,'2019-11-05 13:49:00',NULL),(202,183,'发送',NULL,'fenghao:send',NULL,'1',NULL,'2019-11-06 11:21:34',NULL),(203,195,'发送',NULL,'kick:send',NULL,'1',NULL,'2019-11-06 11:55:00',NULL),(204,198,'发送',NULL,'fakepay:send',NULL,'1',NULL,'2019-11-07 09:39:49',NULL),(205,197,'发送',NULL,'mail:send',NULL,'1',NULL,'2019-11-07 09:40:05',NULL),(206,199,'生成',NULL,'cdk:generate',NULL,'1',NULL,'2019-11-07 15:05:58',NULL);
/*!40000 ALTER TABLE `t_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_role`
--

DROP TABLE IF EXISTS `t_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_role` (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_NAME` varchar(100) NOT NULL COMMENT '角色名称',
  `REMARK` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ROLE_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role`
--

LOCK TABLES `t_role` WRITE;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` VALUES (1,'系统管理员','系统管理员，拥有所有操作权限 ^_^','2019-06-14 16:23:11','2019-11-07 15:06:08'),(2,'发行运营','拥有运营查询和部分运维操作权限','2019-06-14 16:00:15','2019-11-10 17:10:31');
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_role_menu`
--

DROP TABLE IF EXISTS `t_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_role_menu` (
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  `MENU_ID` bigint(20) NOT NULL COMMENT '菜单/按钮ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role_menu`
--

LOCK TABLES `t_role_menu` WRITE;
/*!40000 ALTER TABLE `t_role_menu` DISABLE KEYS */;
INSERT INTO `t_role_menu` VALUES (1,178),(1,181),(1,188),(1,189),(1,190),(1,191),(1,192),(1,193),(1,179),(1,182),(1,184),(1,185),(1,183),(1,202),(1,194),(1,195),(1,203),(1,196),(1,201),(1,186),(1,187),(1,197),(1,205),(1,198),(1,204),(1,199),(1,206),(1,1),(1,3),(1,11),(1,12),(1,13),(1,160),(1,161),(1,4),(1,14),(1,15),(1,16),(1,162),(1,5),(1,17),(1,18),(1,19),(1,163),(1,6),(1,20),(1,21),(1,22),(1,164),(1,2),(1,8),(1,23),(1,10),(1,24),(1,170),(1,136),(1,171),(1,172),(1,113),(1,114),(1,127),(1,128),(1,129),(1,130),(1,131),(1,175),(1,137),(1,138),(1,165),(1,139),(1,166),(1,115),(1,132),(1,133),(1,135),(1,134),(1,126),(1,159),(1,116),(1,117),(1,119),(1,120),(1,121),(1,122),(1,123),(1,118),(1,125),(1,167),(1,168),(1,169),(2,178),(2,181),(2,188),(2,189),(2,190),(2,191),(2,192),(2,193),(2,179),(2,183),(2,202),(2,194),(2,195),(2,203),(2,196),(2,201),(2,186),(2,187),(2,197),(2,205),(2,198),(2,204),(2,3),(2,161),(2,4),(2,14),(2,162),(2,5),(2,17),(2,163),(2,6),(2,20),(2,164),(2,8),(2,10),(2,170),(2,136),(2,172),(2,113),(2,114),(2,127),(2,128),(2,129),(2,130),(2,131),(2,175),(2,138),(2,139),(2,132),(2,133),(2,135),(2,134),(2,126),(2,159),(2,116),(2,117),(2,119),(2,120),(2,121),(2,122),(2,123),(2,118),(2,125),(2,167),(2,168),(2,169);
/*!40000 ALTER TABLE `t_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(128) NOT NULL COMMENT '密码',
  `DEPT_ID` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `EMAIL` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `STATUS` char(1) NOT NULL COMMENT '状态 0锁定 1有效',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `LAST_LOGIN_TIME` datetime DEFAULT NULL COMMENT '最近访问时间',
  `SSEX` char(1) DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `IS_TAB` char(1) DEFAULT NULL COMMENT '是否开启tab，0关闭 1开启',
  `THEME` varchar(10) DEFAULT NULL COMMENT '主题',
  `AVATAR` varchar(100) DEFAULT NULL COMMENT '头像',
  `DESCRIPTION` varchar(100) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'MrBird','a4f18ee51f21294dc3ae8b7c5642035a',1,'mrbird@qq.com','17788888888','1','2019-06-14 20:39:22','2019-11-01 10:49:06','2020-11-19 14:11:46','0','0','black','1d22f3e41d284f50b2c8fc32e0788698.jpeg','我是帅比作者。'),(11,'shenshi01','68209e68dd5eb81c158069767e232e55',11,'','','1','2019-12-11 21:05:56','2019-12-11 21:07:59','2019-12-22 14:55:03','2','0','black','default.jpg',''),(12,'shenshi02','4703dc3e1d7362f77239651695ea9b4e',11,'','','1','2019-12-11 21:06:08','2019-12-11 21:17:30','2019-12-11 22:16:52','2','0','black','default.jpg','');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user_role`
--

DROP TABLE IF EXISTS `t_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user_role` (
  `USER_ID` bigint(20) NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_role`
--

LOCK TABLES `t_user_role` WRITE;
/*!40000 ALTER TABLE `t_user_role` DISABLE KEYS */;
INSERT INTO `t_user_role` VALUES (1,1),(11,2),(12,2);
/*!40000 ALTER TABLE `t_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tcdk`
--

DROP TABLE IF EXISTS `tcdk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tcdk` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform` int(11) DEFAULT NULL COMMENT '平台：1腾讯、2新浪',
  `area` int(11) DEFAULT NULL COMMENT '区',
  `cdk` varchar(200) NOT NULL COMMENT '激活码',
  `cdk_name` varchar(200) DEFAULT NULL COMMENT '编码名称：新手礼包',
  `award_id` int(11) DEFAULT '0' COMMENT '兑奖ID（奖品ID）',
  `reuse` int(11) DEFAULT '0' COMMENT '重复领取',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '起始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cdk` (`cdk`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tcdk`
--

LOCK TABLES `tcdk` WRITE;
/*!40000 ALTER TABLE `tcdk` DISABLE KEYS */;
INSERT INTO `tcdk` VALUES (1,1,1,'QevRm7gEfycyQzW','新手礼包',12107,1,NULL,NULL),(2,1,2,'I6lyvsY5M5tDmCI','豪华礼包',12108,1,NULL,NULL),(3,1,3,'EZ3bhsuqrHKNmjm','至尊礼包',12109,1,NULL,NULL),(4,1,1,'YzQNw1zEL4YsTyj','新手礼包',12120,1,NULL,NULL),(5,1,2,'nLafkKxEiXjSWxA','VIP礼包',12121,1,NULL,NULL),(6,1,3,'oncYBexuMUhQau4','诸神礼包',12122,1,NULL,NULL);
/*!40000 ALTER TABLE `tcdk` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-24  7:23:27
