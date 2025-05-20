
ALTER TABLE `t_character_dayincome_count`
ADD COLUMN `f_id`  int(11) NULL AUTO_INCREMENT,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`f_id`);

ALTER TABLE `t_character_dujie`
ADD COLUMN `f_id`  int(11) NULL AUTO_INCREMENT,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`f_id`),
ADD KEY `idx_characterid` (`f_character_id`);

ALTER TABLE `t_character_buff`
ADD COLUMN `f_id`  int(11) NULL AUTO_INCREMENT,
ADD PRIMARY KEY (`f_id`);

ALTER TABLE `t_character_goods_dc`
ADD COLUMN `f_id` int(11) NULL AUTO_INCREMENT,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`f_id`),
ADD KEY `idx_characterid` (`f_character_id`);

ALTER TABLE `t_character_onhoor_config`
ADD COLUMN `f_id` int(11) NULL AUTO_INCREMENT,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`f_id`),
ADD KEY `idx_characterid` (`f_character_id`);

ALTER TABLE `t_instance_daystat`
ADD COLUMN `f_id` int(11) NULL AUTO_INCREMENT,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`f_id`),
ADD KEY `idx_characterid` (`characterId`,`instanceModelId`,`statdate`);

ALTER TABLE `t_monster_lastkill`
ADD COLUMN `f_id` int(11) NULL AUTO_INCREMENT,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`f_id`),
ADD KEY `idx_characterid` (`f_modelid`,`f_lineid`);

ALTER TABLE `t_character_goods`
ADD COLUMN `f_base_id`  int(11) NULL AUTO_INCREMENT AFTER `f_gs`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`f_base_id`),
ADD INDEX `idx_id` (`f_id`) ;

ALTER TABLE `t_character_task`
ADD COLUMN `f_base_id`  int(11) NULL AUTO_INCREMENT AFTER `f_targetMountUpgrade`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`f_base_id`),
ADD INDEX `idx_id` (`f_id`) ;


