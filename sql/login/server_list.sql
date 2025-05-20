# ************************************************************
# Sequel Ace SQL dump
# Version 20080
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# Host: 172.104.47.85 (MySQL 8.0.40)
# Database: gg04
# Generation Time: 2025-01-11 18:46:30 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table server_list
# ------------------------------------------------------------

DROP TABLE IF EXISTS `server_list`;

CREATE TABLE `server_list` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `port` int DEFAULT NULL,
  `port_ssl` int DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `lan_port` int DEFAULT NULL,
  `http_url` varchar(128) DEFAULT NULL,
  `is_ssl` tinyint(1) DEFAULT NULL,
  `url_ssl` varchar(255) DEFAULT NULL,
  `time_open` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3;

LOCK TABLES `server_list` WRITE;
/*!40000 ALTER TABLE `server_list` DISABLE KEYS */;

INSERT INTO `server_list` (`Id`, `name`, `ip`, `port`, `port_ssl`, `status`, `lan_port`, `http_url`, `is_ssl`, `url_ssl`, `time_open`)
VALUES
	(1,'Demo','172.104.47.85',10001,13001,1,12001,'http://172.104.47.85:11001',1,'qs3q-s1.goodgame.vn','2025-01-07 04:13:20'),
	(2,'Main','27.72.105.113',8801,8802,1,8804,'http://27.72.105.113:8803',0,'','2025-01-07 04:13:20'),
	(3,'Company','192.168.2.68',10001,13001,1,12001,'http://192.168.2.68:11001',0,'','2025-01-07 04:13:20');

/*!40000 ALTER TABLE `server_list` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
