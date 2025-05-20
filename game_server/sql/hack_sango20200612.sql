-- MySQL dump 10.13  Distrib 8.0.18, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: hack_sango
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
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `type` smallint(6) NOT NULL DEFAULT '2' COMMENT '1充值奖励 2消费反馈 3登录奖励 4答题活动 5冲级奖励 6奖励加倍 7钻石矿井',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `title` varchar(128) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `param` blob,
  `is_open` bit(1) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `affair`
--

DROP TABLE IF EXISTS `affair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `affair` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `template_id` int(10) unsigned DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `affair`
--

LOCK TABLES `affair` WRITE;
/*!40000 ALTER TABLE `affair` DISABLE KEYS */;
/*!40000 ALTER TABLE `affair` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `announce`
--

DROP TABLE IF EXISTS `announce`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announce` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announce`
--

LOCK TABLES `announce` WRITE;
/*!40000 ALTER TABLE `announce` DISABLE KEYS */;
/*!40000 ALTER TABLE `announce` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bag`
--

DROP TABLE IF EXISTS `bag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bag` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL DEFAULT '0',
  `buy_num` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bag`
--

LOCK TABLES `bag` WRITE;
/*!40000 ALTER TABLE `bag` DISABLE KEYS */;
INSERT INTO `bag` VALUES (1,1,2);
/*!40000 ALTER TABLE `bag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cdkey`
--

DROP TABLE IF EXISTS `cdkey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cdkey` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `platform` int(11) DEFAULT NULL COMMENT '激活码',
  `area` int(11) DEFAULT NULL COMMENT '区',
  `cdkey` varchar(200) DEFAULT NULL COMMENT '激活码',
  `player_id` int(11) NOT NULL COMMENT '领取玩家ID',
  `cdk_id` int(11) NOT NULL COMMENT '激活码礼包编号',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cdkey`
--

LOCK TABLES `cdkey` WRITE;
/*!40000 ALTER TABLE `cdkey` DISABLE KEYS */;
/*!40000 ALTER TABLE `cdkey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chapter`
--

DROP TABLE IF EXISTS `chapter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chapter` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `max_map_id` int(11) DEFAULT NULL,
  `last_reap_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chapter`
--

LOCK TABLES `chapter` WRITE;
/*!40000 ALTER TABLE `chapter` DISABLE KEYS */;
/*!40000 ALTER TABLE `chapter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned DEFAULT NULL,
  `friend_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_request`
--

DROP TABLE IF EXISTS `friend_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_request` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `request_player_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_request`
--

LOCK TABLES `friend_request` WRITE;
/*!40000 ALTER TABLE `friend_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `general_exchange`
--

DROP TABLE IF EXISTS `general_exchange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `general_exchange` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `old_general_uuid` bigint(20) DEFAULT NULL,
  `new_general_template_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_exchange`
--

LOCK TABLES `general_exchange` WRITE;
/*!40000 ALTER TABLE `general_exchange` DISABLE KEYS */;
/*!40000 ALTER TABLE `general_exchange` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gold_buy`
--

DROP TABLE IF EXISTS `gold_buy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gold_buy` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `buy_seconds` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gold_buy`
--

LOCK TABLES `gold_buy` WRITE;
/*!40000 ALTER TABLE `gold_buy` DISABLE KEYS */;
/*!40000 ALTER TABLE `gold_buy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hero`
--

DROP TABLE IF EXISTS `hero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hero` (
  `uuid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `player_id` int(11) unsigned DEFAULT NULL,
  `template_id` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `class` int(11) DEFAULT NULL,
  `star` int(11) DEFAULT NULL,
  `occu` int(11) DEFAULT NULL,
  `power` int(11) DEFAULT NULL,
  `equip` varchar(128) DEFAULT NULL,
  `treasure` int(11) DEFAULT NULL,
  `skill` varchar(128) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL,
  `atk` int(11) DEFAULT NULL,
  `def` int(11) DEFAULT NULL,
  `mdef` int(11) DEFAULT NULL,
  `atktime` float DEFAULT NULL,
  `range` int(11) DEFAULT NULL,
  `msp` int(11) DEFAULT NULL,
  `pcri` int(11) DEFAULT NULL,
  `pcrid` int(11) DEFAULT NULL,
  `pmdex` int(11) DEFAULT NULL,
  `pdam` int(11) DEFAULT NULL,
  `php` int(11) DEFAULT NULL,
  `patk` int(11) DEFAULT NULL,
  `ppbs` int(11) DEFAULT NULL,
  `pmbs` int(11) DEFAULT NULL,
  `pefc` int(11) DEFAULT NULL,
  `patkdam` int(11) DEFAULT NULL,
  `pskidam` int(11) DEFAULT NULL,
  `pckatk` int(11) DEFAULT NULL,
  `exclusive` blob,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hero`
--

LOCK TABLES `hero` WRITE;
/*!40000 ALTER TABLE `hero` DISABLE KEYS */;
/*!40000 ALTER TABLE `hero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) DEFAULT NULL,
  `template_id` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `gain_time` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `idx_player_id` (`player_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_item_go`
--

DROP TABLE IF EXISTS `log_item_go`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_item_go` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `module_type` int(11) NOT NULL DEFAULT '0' COMMENT '模块类型: 0其他模块, 1装备合成, 2商店购买, 3商店刷新, 4购买体力, 5购买技能点, 6过往之球, 7现在之球, 8未来之球, 9精英副本刷新',
  `item_id` int(11) NOT NULL COMMENT '道具类型',
  `change_value` int(11) NOT NULL COMMENT '变化值,负数为减少',
  `add_time` timestamp NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`Id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_item_go`
--

LOCK TABLES `log_item_go` WRITE;
/*!40000 ALTER TABLE `log_item_go` DISABLE KEYS */;
INSERT INTO `log_item_go` VALUES (22,1,55,0,10000,'2020-04-03 06:30:34'),(23,1,55,0,500,'2020-04-03 06:30:46'),(24,1,55,0,50,'2020-04-03 06:35:12');
/*!40000 ALTER TABLE `log_item_go` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_login`
--

DROP TABLE IF EXISTS `log_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_login` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) DEFAULT NULL,
  `login_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_login`
--

LOCK TABLES `log_login` WRITE;
/*!40000 ALTER TABLE `log_login` DISABLE KEYS */;
INSERT INTO `log_login` VALUES (137,1,'2020-04-02 15:04:26'),(138,1,'2020-04-03 06:16:47'),(139,1,'2020-04-03 06:30:27'),(140,1,'2020-04-03 06:30:57'),(141,1,'2020-04-03 06:33:28'),(142,1,'2020-04-03 06:34:04'),(143,1,'2020-04-03 06:35:02'),(144,1,'2020-04-06 08:39:56'),(145,1,'2020-04-07 05:56:51'),(146,1,'2020-04-07 07:04:22'),(147,1,'2020-04-09 12:48:01'),(148,1,'2020-04-09 14:26:58'),(149,1,'2020-04-16 06:17:52'),(150,1,'2020-04-16 06:18:10'),(151,1,'2020-04-16 06:20:28'),(152,1,'2020-04-16 07:58:38'),(153,1,'2020-04-16 07:59:12'),(154,1,'2020-04-16 08:00:10');
/*!40000 ALTER TABLE `log_login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_topup`
--

DROP TABLE IF EXISTS `log_topup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_topup` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `transaction_code` varchar(64) DEFAULT NULL,
  `add_point` int(11) DEFAULT NULL COMMENT '充值钻石',
  `reward_diamond` int(11) DEFAULT NULL COMMENT '奖励钻石',
  `player_id` int(11) DEFAULT NULL,
  `open_id` varchar(64) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_topup`
--

LOCK TABLES `log_topup` WRITE;
/*!40000 ALTER TABLE `log_topup` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_topup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_topup_feedback`
--

DROP TABLE IF EXISTS `log_topup_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_topup_feedback` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `transaction_code` varchar(64) DEFAULT NULL,
  `add_point` int(11) DEFAULT NULL COMMENT '充值钻石',
  `reward_diamond` int(11) DEFAULT NULL COMMENT '奖励钻石',
  `player_id` int(11) DEFAULT NULL,
  `open_id` varchar(64) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`Id`),
  KEY `open_id` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_topup_feedback`
--

LOCK TABLES `log_topup_feedback` WRITE;
/*!40000 ALTER TABLE `log_topup_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_topup_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_account`
--

DROP TABLE IF EXISTS `login_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_account` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(255) DEFAULT NULL,
  `pwd` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4664 DEFAULT CHARSET=utf8;
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
-- Table structure for table `mail`
--

DROP TABLE IF EXISTS `mail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mail` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '玩家唯一ID',
  `player_id` int(11) NOT NULL,
  `template_id` int(11) DEFAULT NULL COMMENT '邮件模版ID',
  `sender_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '发件人',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '内容',
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮件参数',
  `attachs` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '附件',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `type` tinyint(4) DEFAULT '0' COMMENT '系统邮件，玩家自身邮件等',
  `status` tinyint(4) DEFAULT NULL COMMENT '邮件是否已读：0否，1是，2附件已领取',
  PRIMARY KEY (`Id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mail`
--

LOCK TABLES `mail` WRITE;
/*!40000 ALTER TABLE `mail` DISABLE KEYS */;
/*!40000 ALTER TABLE `mail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mission_achieve`
--

DROP TABLE IF EXISTS `mission_achieve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mission_achieve` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `record` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `progress` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mission_achieve`
--

LOCK TABLES `mission_achieve` WRITE;
/*!40000 ALTER TABLE `mission_achieve` DISABLE KEYS */;
/*!40000 ALTER TABLE `mission_achieve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mission_daily`
--

DROP TABLE IF EXISTS `mission_daily`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mission_daily` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `mission_progress` blob,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mission_daily`
--

LOCK TABLES `mission_daily` WRITE;
/*!40000 ALTER TABLE `mission_daily` DISABLE KEYS */;
/*!40000 ALTER TABLE `mission_daily` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mythical_animal`
--

DROP TABLE IF EXISTS `mythical_animal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mythical_animal` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `template_id` int(11) NOT NULL,
  `level` int(11) unsigned DEFAULT NULL,
  `pclass` int(11) unsigned DEFAULT NULL,
  `passive_skills` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mythical_animal`
--

LOCK TABLES `mythical_animal` WRITE;
/*!40000 ALTER TABLE `mythical_animal` DISABLE KEYS */;
/*!40000 ALTER TABLE `mythical_animal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_id` varchar(128) NOT NULL COMMENT '玩家账号名',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  `gold` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL COMMENT '玩家等级',
  `level_exp` int(11) DEFAULT NULL COMMENT '等级经验',
  `vip_level` int(11) DEFAULT NULL,
  `vip_exp` int(11) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `icon_id` int(11) DEFAULT NULL,
  `head_id` int(11) DEFAULT NULL,
  `frame_id` int(11) DEFAULT NULL,
  `image_id` int(11) DEFAULT NULL,
  `power` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `downline_time` datetime DEFAULT NULL,
  `upgrade_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_head`
--

DROP TABLE IF EXISTS `player_head`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player_head` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `head_icons` varchar(1024) DEFAULT NULL,
  `head_frames` varchar(1024) DEFAULT NULL,
  `head_images` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_head`
--

LOCK TABLES `player_head` WRITE;
/*!40000 ALTER TABLE `player_head` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_head` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pub_draw`
--

DROP TABLE IF EXISTS `pub_draw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pub_draw` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `last_normal_time` datetime DEFAULT NULL,
  `last_advance_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pub_draw`
--

LOCK TABLES `pub_draw` WRITE;
/*!40000 ALTER TABLE `pub_draw` DISABLE KEYS */;
/*!40000 ALTER TABLE `pub_draw` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_list`
--

DROP TABLE IF EXISTS `server_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `server_list` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `port_ssl` int(11) DEFAULT NULL,
  `status` tinyint(3) DEFAULT NULL,
  `lan_port` int(11) DEFAULT NULL,
  `http_url` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_list`
--

LOCK TABLES `server_list` WRITE;
/*!40000 ALTER TABLE `server_list` DISABLE KEYS */;
INSERT INTO `server_list` VALUES (1,'开黑1区','127.0.0.1',10001,13001,1,12001,'http://127.0.0.1:11001');
/*!40000 ALTER TABLE `server_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shop` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `last_day_buy_time` datetime DEFAULT NULL,
  `day_buy_count` varchar(512) DEFAULT NULL,
  `decomp_buy_count` varchar(512) DEFAULT NULL,
  `star_buy_count` varchar(512) DEFAULT NULL,
  `legion_buy_count` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop`
--

LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sign_in`
--

DROP TABLE IF EXISTS `sign_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sign_in` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL DEFAULT '0',
  `sign_count` int(11) DEFAULT NULL,
  `last_sign_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sign_in`
--

LOCK TABLES `sign_in` WRITE;
/*!40000 ALTER TABLE `sign_in` DISABLE KEYS */;
/*!40000 ALTER TABLE `sign_in` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spin`
--

DROP TABLE IF EXISTS `spin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spin` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned NOT NULL,
  `rewards_normal` varchar(512) DEFAULT NULL,
  `rewards_advance` varchar(512) DEFAULT NULL,
  `score_buy_normal` varchar(255) DEFAULT NULL,
  `score_buy_advance` varchar(255) DEFAULT NULL,
  `last_refersh_time_normal` datetime DEFAULT NULL,
  `last_refersh_time_advance` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spin`
--

LOCK TABLES `spin` WRITE;
/*!40000 ALTER TABLE `spin` DISABLE KEYS */;
/*!40000 ALTER TABLE `spin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_blob`
--

DROP TABLE IF EXISTS `system_blob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_blob` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `arena_blob` mediumblob,
  `rank_player_level` mediumblob,
  `rank_tower` mediumblob COMMENT '玩家英雄数量',
  `rank_hero_stars` mediumblob COMMENT '英雄总星级',
  `rank_team_bf` mediumblob COMMENT '队伍战斗力排行',
  `rank_arena` mediumblob,
  `friend_help` blob COMMENT '好友互助频道',
  `qi_zhen_yi_bao_activity` blob COMMENT '奇珍异宝活动',
  `chong_zhi_bang_activity` blob,
  `xiao_fei_bang_activity` blob,
  `player_chat_visit` mediumblob COMMENT 'chat visit time info',
  `rank_like` mediumblob COMMENT '排行榜点赞信息',
  `friend_heart_send` mediumblob COMMENT '好友相互赠送红星',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_blob`
--

LOCK TABLES `system_blob` WRITE;
/*!40000 ALTER TABLE `system_blob` DISABLE KEYS */;
INSERT INTO `system_blob` VALUES (2,NULL,'',NULL,NULL,NULL,NULL,NULL,_binary '\0','','',_binary '#$',_binary '',_binary '');
/*!40000 ALTER TABLE `system_blob` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tcdk`
--

LOCK TABLES `tcdk` WRITE;
/*!40000 ALTER TABLE `tcdk` DISABLE KEYS */;
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

-- Dump completed on 2020-06-12 15:09:03
