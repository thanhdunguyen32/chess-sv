alter table battle_formation add column `teampvp` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL;
alter table battle_formation add column `kingpvp1` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL;
alter table battle_formation add column `kingpvp2` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL;
alter table battle_formation add column `kingpvp3` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL;

/**
*   2020-12-14 王者演武
*/
CREATE TABLE `king_pvp` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) unsigned DEFAULT NULL,
  `stage` smallint(6) DEFAULT NULL,
  `star` smallint(6) DEFAULT NULL,
  `hstage` smallint(6) DEFAULT NULL,
  `promotion` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `locate` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE `system_blob` ADD COLUMN `rank_king_pvp` mediumblob NULL AFTER `rank_arena`;

/**
* 2020-12-28
*/
ALTER TABLE `season` ADD COLUMN `month_etime` datetime(0) NULL AFTER `pos`;