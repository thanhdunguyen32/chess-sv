-- MySQL dump 10.13  Distrib 5.6.39, for Linux (x86_64)
--
-- Host: localhost    Database: idle_login
-- ------------------------------------------------------
-- Server version	5.6.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `announce`
--

DROP TABLE IF EXISTS `announce`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `announce` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announce`
--

LOCK TABLES `announce` WRITE;
/*!40000 ALTER TABLE `announce` DISABLE KEYS */;
INSERT INTO `announce` VALUES (1,'欢迎各位勇士！','关注微信公众号: buluojuweihui (部落居委会全拼),添加客服QQ3004958430,官方交流1群901297677,官方交流2群7210452 25,即可领取取专属礼包和参加福利活动\n本游戏是一款卡牌放置游戏!自然,邪灵、死亡、奥术 、光明、黑暗6个不同阵营,每位英雄都有属于自己的专属个性技能,不同阵营的克制与塔配,千百种组合策略等你来发现!'),(2,'净化游戏环境声明','亲爱的勇士:\n为保障各位勇士的游戏体验,为大家提供健康的游戏环境,官方运营团队会持续强化对污染游戏环境、破坏游戏公平的行为予以监督和惩罚。我们将会以禁言 、封号、公会封禁、角色强制改名、公会强制改名等形式对破坏游戏环境的行为进行严厉惩处!\n另外,现有大量虚假欺诈广告,其形式包括但不限于 (进群送礼包2加微信加Q群3充值折扣返大额资源 4各种转账汇款信息等等,请勿贪图便宜以免上当受骗,一切充值行为请在游戏内官方充值系统进行。\n游戏官方运营团队 2018年10月19日'),(3,'情人节活动公告','亲爱的勇士们,情人节活动将于2月14日0点上线啦!\n活动一:掉落活动(开服未满7天的区不参与)活动时间: 2月14日0点-2月16日23点59分活动内容:活动期间收集玫瑰花可以兑换超多奖励!\n活动二:兑换活动(开服未满7天的区不参与)活动时间： 2月14日0点-2月17日23点59分活动内容:情人节期间收集玫瑰可以兑换超多奖励!'),(4,'VIP福利中心','亲爱的各位部落勇士,为感谢广大玩家对游戏长久以来的支持, 目前我们为【VIP3及以上】的用户提供专属客服服务。不仅享受vip制度当中的种种贴心服务和福利,还能获得一手游戏资讯!请联系QQ客服安安: 3004958430,: 2851315862,查询您的专属客服！！！');
/*!40000 ALTER TABLE `announce` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_account`
--

DROP TABLE IF EXISTS `login_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login_account` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(255) DEFAULT NULL,
  `pwd` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4772 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_account`
--

LOCK TABLES `login_account` WRITE;
/*!40000 ALTER TABLE `login_account` DISABLE KEYS */;
INSERT INTO `login_account` VALUES (4564,'1','c4ca4238a0b923820dcc509a6f75849b'),(4565,'2','c4ca4238a0b923820dcc509a6f75849b'),(4566,'3','c4ca4238a0b923820dcc509a6f75849b'),(4567,'4','c4ca4238a0b923820dcc509a6f75849b'),(4568,'5','c4ca4238a0b923820dcc509a6f75849b'),(4569,'6','c4ca4238a0b923820dcc509a6f75849b'),(4570,'7','c4ca4238a0b923820dcc509a6f75849b'),(4571,'8','c4ca4238a0b923820dcc509a6f75849b'),(4572,'9','c4ca4238a0b923820dcc509a6f75849b'),(4573,'10','c4ca4238a0b923820dcc509a6f75849b'),(4574,'11','c4ca4238a0b923820dcc509a6f75849b'),(4575,'12','c4ca4238a0b923820dcc509a6f75849b'),(4576,'13','c4ca4238a0b923820dcc509a6f75849b'),(4577,'14','c4ca4238a0b923820dcc509a6f75849b'),(4578,'15','c4ca4238a0b923820dcc509a6f75849b'),(4579,'16','c4ca4238a0b923820dcc509a6f75849b'),(4580,'17','c4ca4238a0b923820dcc509a6f75849b'),(4581,'18','c4ca4238a0b923820dcc509a6f75849b'),(4582,'19','c4ca4238a0b923820dcc509a6f75849b'),(4583,'20','c4ca4238a0b923820dcc509a6f75849b'),(4584,'21','c4ca4238a0b923820dcc509a6f75849b'),(4585,'22','c4ca4238a0b923820dcc509a6f75849b'),(4586,'23','c4ca4238a0b923820dcc509a6f75849b'),(4587,'24','c4ca4238a0b923820dcc509a6f75849b'),(4588,'25','c4ca4238a0b923820dcc509a6f75849b'),(4589,'26','c4ca4238a0b923820dcc509a6f75849b'),(4590,'27','c4ca4238a0b923820dcc509a6f75849b'),(4591,'28','c4ca4238a0b923820dcc509a6f75849b'),(4592,'29','c4ca4238a0b923820dcc509a6f75849b'),(4593,'30','c4ca4238a0b923820dcc509a6f75849b'),(4594,'31','c4ca4238a0b923820dcc509a6f75849b'),(4595,'32','c4ca4238a0b923820dcc509a6f75849b'),(4596,'33','c4ca4238a0b923820dcc509a6f75849b'),(4597,'34','c4ca4238a0b923820dcc509a6f75849b'),(4598,'35','c4ca4238a0b923820dcc509a6f75849b'),(4599,'36','c4ca4238a0b923820dcc509a6f75849b'),(4600,'37','c4ca4238a0b923820dcc509a6f75849b'),(4601,'38','c4ca4238a0b923820dcc509a6f75849b'),(4602,'39','c4ca4238a0b923820dcc509a6f75849b'),(4603,'40','c4ca4238a0b923820dcc509a6f75849b'),(4604,'41','c4ca4238a0b923820dcc509a6f75849b'),(4605,'42','c4ca4238a0b923820dcc509a6f75849b'),(4606,'43','c4ca4238a0b923820dcc509a6f75849b'),(4607,'44','c4ca4238a0b923820dcc509a6f75849b'),(4608,'45','c4ca4238a0b923820dcc509a6f75849b'),(4609,'46','c4ca4238a0b923820dcc509a6f75849b'),(4610,'47','c4ca4238a0b923820dcc509a6f75849b'),(4611,'48','c4ca4238a0b923820dcc509a6f75849b'),(4612,'49','c4ca4238a0b923820dcc509a6f75849b'),(4613,'50','c4ca4238a0b923820dcc509a6f75849b'),(4614,'51','c4ca4238a0b923820dcc509a6f75849b'),(4615,'52','c4ca4238a0b923820dcc509a6f75849b'),(4616,'53','c4ca4238a0b923820dcc509a6f75849b'),(4617,'54','c4ca4238a0b923820dcc509a6f75849b'),(4618,'55','c4ca4238a0b923820dcc509a6f75849b'),(4619,'56','c4ca4238a0b923820dcc509a6f75849b'),(4620,'57','c4ca4238a0b923820dcc509a6f75849b'),(4621,'58','c4ca4238a0b923820dcc509a6f75849b'),(4622,'59','c4ca4238a0b923820dcc509a6f75849b'),(4623,'60','c4ca4238a0b923820dcc509a6f75849b'),(4624,'61','c4ca4238a0b923820dcc509a6f75849b'),(4625,'62','c4ca4238a0b923820dcc509a6f75849b'),(4626,'63','c4ca4238a0b923820dcc509a6f75849b'),(4627,'64','c4ca4238a0b923820dcc509a6f75849b'),(4628,'65','c4ca4238a0b923820dcc509a6f75849b'),(4629,'66','c4ca4238a0b923820dcc509a6f75849b'),(4630,'67','c4ca4238a0b923820dcc509a6f75849b'),(4631,'68','c4ca4238a0b923820dcc509a6f75849b'),(4632,'69','c4ca4238a0b923820dcc509a6f75849b'),(4633,'70','c4ca4238a0b923820dcc509a6f75849b'),(4634,'71','c4ca4238a0b923820dcc509a6f75849b'),(4635,'72','c4ca4238a0b923820dcc509a6f75849b'),(4636,'73','c4ca4238a0b923820dcc509a6f75849b'),(4637,'74','c4ca4238a0b923820dcc509a6f75849b'),(4638,'75','c4ca4238a0b923820dcc509a6f75849b'),(4639,'76','c4ca4238a0b923820dcc509a6f75849b'),(4640,'77','c4ca4238a0b923820dcc509a6f75849b'),(4641,'78','c4ca4238a0b923820dcc509a6f75849b'),(4642,'79','c4ca4238a0b923820dcc509a6f75849b'),(4643,'80','c4ca4238a0b923820dcc509a6f75849b'),(4644,'81','c4ca4238a0b923820dcc509a6f75849b'),(4645,'82','c4ca4238a0b923820dcc509a6f75849b'),(4646,'83','c4ca4238a0b923820dcc509a6f75849b'),(4647,'84','c4ca4238a0b923820dcc509a6f75849b'),(4648,'85','c4ca4238a0b923820dcc509a6f75849b'),(4649,'86','c4ca4238a0b923820dcc509a6f75849b'),(4650,'87','c4ca4238a0b923820dcc509a6f75849b'),(4651,'88','c4ca4238a0b923820dcc509a6f75849b'),(4652,'89','c4ca4238a0b923820dcc509a6f75849b'),(4653,'90','c4ca4238a0b923820dcc509a6f75849b'),(4654,'91','c4ca4238a0b923820dcc509a6f75849b'),(4655,'92','c4ca4238a0b923820dcc509a6f75849b'),(4656,'93','c4ca4238a0b923820dcc509a6f75849b'),(4657,'94','c4ca4238a0b923820dcc509a6f75849b'),(4658,'95','c4ca4238a0b923820dcc509a6f75849b'),(4659,'96','c4ca4238a0b923820dcc509a6f75849b'),(4660,'97','c4ca4238a0b923820dcc509a6f75849b'),(4661,'98','c4ca4238a0b923820dcc509a6f75849b'),(4662,'99','c4ca4238a0b923820dcc509a6f75849b'),(4663,'100','c4ca4238a0b923820dcc509a6f75849b');
/*!40000 ALTER TABLE `login_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_list`
--

DROP TABLE IF EXISTS `server_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server_list` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `port_nossl` int(11) DEFAULT NULL,
  `status` tinyint(3) DEFAULT NULL,
  `lan_port` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_list`
--

LOCK TABLES `server_list` WRITE;
/*!40000 ALTER TABLE `server_list` DISABLE KEYS */;
INSERT INTO `server_list` VALUES (1,'开天辟地','101.132.179.124',15001,18001,1,17001),(2,'测试2区','101.132.179.124',15001,18001,0,17001),(3,'测试3区','101.132.179.124',15001,18001,2,17001),(4,'测试4区','101.132.179.124',15001,18001,1,17001),(5,'测试5区','101.132.179.124',15001,18001,1,17001),(6,'测试6区','101.132.179.124',15001,18001,1,17001),(7,'测试7区','101.132.179.124',15001,18001,1,17001),(8,'测试8区','101.132.179.124',15001,18001,1,17001),(9,'测试9区','101.132.179.124',15001,18001,1,17001),(10,'测试10区','101.132.179.124',15001,18001,1,17001),(11,'测试11区','101.132.179.124',15001,18001,1,17001),(12,'测试12区','101.132.179.124',15001,18001,1,17001);
/*!40000 ALTER TABLE `server_list` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-22 18:21:18
