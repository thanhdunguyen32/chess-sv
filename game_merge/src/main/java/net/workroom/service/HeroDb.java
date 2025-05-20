package net.workroom.service;

import game.module.friend.bean.FriendBoss;
import game.module.friend.bean.FriendExplore;
import game.module.legion.bean.DbLegionBoss;
import game.module.legion.bean.LegionPlayer;
import lion.common.ProtostuffUtil;
import lion.common.SimpleTextConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class HeroDb extends BaseDbOperation {

    private static Logger logger = LoggerFactory.getLogger(HeroDb.class);

    @PostConstruct
    public void init() {
        //
        excludeTableNames.add("id_relations");
        excludeTableNames.add("activity");
        excludeTableNames.add("announce");
        excludeTableNames.add("cdkey");
        excludeTableNames.add("delete_players");
        excludeTableNames.add("topup_feedback");
        excludeTableNames.add("log_item_go");
        excludeTableNames.add("log_login");
        excludeTableNames.add("server_list");
        excludeTableNames.add("system_blob");
        excludeTableNames.add("tcdk");
        excludeTableNames.add("guozhan");
        excludeTableNames.add("mine");
        excludeTableNames.add("pvp");
        excludeTableNames.add("season");
        excludeTableNames.add("server_has_role");
        excludeTableNames.add("tower_replay");
        excludeTableNames.add("world_boss");
    }

    public void mergeOneDb(String targetDbName, String sourceDbName) {
        this.setTargetDbName(targetDbName);
        parseOneDb(sourceDbName);
    }

    /**
     * 去掉唯一索引
     */
    private void dropKeys() {
        logger.info("drop indexs");
        jdbcTemplate.execute("use " + getTargetDbName());
        jdbcTemplate.execute("alter table activity_battle drop KEY player_id");
        jdbcTemplate.execute("alter table bag drop KEY player_id");
        jdbcTemplate.execute("alter table friend drop KEY player_id");
        jdbcTemplate.execute("alter table guild_self drop KEY player_id");
        jdbcTemplate.execute("alter table invite drop KEY player_id");
        jdbcTemplate.execute("alter table item drop KEY idx_template_player_id");
        jdbcTemplate.execute("alter table overcome drop KEY player_id");
        jdbcTemplate.execute("alter table pub drop KEY player_id");
        jdbcTemplate.execute("alter table stage drop KEY idx_player_id");
        jdbcTemplate.execute("alter table store drop KEY player_id");
        jdbcTemplate.execute("alter table teamskill drop KEY player_id");
    }

    private void addKeys() {
        logger.info("add indexs");
        jdbcTemplate.execute("use " + getTargetDbName());
        // jdbcTemplate.execute("alter table arena add KEY `player_id`
        // (`player_id`)");
        jdbcTemplate.execute("alter table activity_battle add KEY `player_id` (`player_id`)");
        jdbcTemplate.execute("alter table bag add KEY `player_id` (`player_id`)");
        jdbcTemplate.execute("alter table friend add KEY `player_id` (`player_id`,`friend_id`)");
        jdbcTemplate.execute("alter table guild_self add KEY `player_id` (`player_id`)");
        jdbcTemplate.execute("alter table invite add KEY `player_id` (`player_id`)");
        jdbcTemplate.execute("alter table item add KEY `idx_template_player_id` (`template_id`,`player_id`)");
        jdbcTemplate.execute("alter table overcome add KEY `player_id` (`player_id`)");
        jdbcTemplate.execute("alter table pub add KEY `player_id` (`player_id`)");
        jdbcTemplate.execute("alter table stage add KEY `idx_player_id` (`player_id`)");
        jdbcTemplate.execute("alter table store add KEY `player_id` (`player_id`)");
        jdbcTemplate.execute("alter table teamskill add KEY `player_id` (`player_id`)");
    }

    public void parseOneDb(String sourceDbName) {
        logger.info("处理一个database的合服操作：source={}", sourceDbName);
        clearTmpTable();
//		dropKeys();
        // specialParseBefore();
        initTableMaxId();
        // returnFactionFlagCopper(sourceDbName);
        copyTblToTargetDb(sourceDbName);
        // specialParseAfter();
        createIdRelations();
        Map<Integer, Integer> playerIdMap = mapId(sourceDbName, "player");
//		mapId(sourceDbName, "guild");
//		addKeys();
        parseHeroId("act_cxry", "player_id");
        parseHeroId("act_mjbg", "player_id");
        parseHeroId("act_tnqw", "player_id");
        parseHeroId("activity_xiangou", "player_id");
        parseHeroId("affair", "player_id");
        parseHeroId("battle_formation", "player_id");
        parseHeroId("chapter", "player_id");
        parseHeroId("charge", "player_id");
        parseHeroId("dungeon", "player_id");
        parseHeroId("exped", "player_id");
        parseHeroId("friend", "player_id");
        parseHeroId("friend", "friend_id");
        parseHeroId("friend_boss", "player_id");
        parseHeroId("friend_explore", "player_id");
        parseHeroId("friend_request", "player_id");
        parseHeroId("friend_request", "request_player_id");
        parseHeroId("general_exchange", "player_id");
        parseHeroId("gold_buy", "player_id");
        parseHeroId("guozhan_self", "player_id");
        parseHeroId("hero", "player_id");
        parseHeroId("item", "player_id");
        parseHeroId("king_pvp", "player_id");
        //legion
        parseHeroId("legion", "ceo_id");
        parseHeroId("libao_buy", "player_id");
        parseHeroId("log_topup", "player_id");
        parseHeroId("mail", "player_id");
        parseHeroId("manor", "player_id");
        parseHeroId("map_event", "player_id");
        parseHeroId("mission_achieve", "player_id");
        parseHeroId("mission_daily", "player_id");
        parseHeroId("month_boss", "player_id");
        parseHeroId("mythical_animal", "player_id");
        parseHeroId("occ_task", "player_id");
        parseHeroId("online_gift", "player_id");
        parseHeroId("player_head", "player_id");
        parseHeroId("player_hide", "player_id");
        parseHeroId("player_other", "player_id");
        parseHeroId("player_server_prop", "player_id");
        parseHeroId("power_formation", "player_id");
        parseHeroId("pub_draw", "player_id");
        parseHeroId("pvp_record", "player_id");
        parseHeroId("secret", "player_id");
        parseHeroId("shop", "player_id");
        parseHeroId("sign_in", "player_id");
        parseHeroId("spin", "player_id");
        parseHeroId("surrender", "player_id");
        resetGuoZhanNation();
        //好友boss
        parseFriendBoss(playerIdMap);
        //好友探索
        parseFriendExplore(playerIdMap);
        // 处理公会内部信息
        parseLegion(playerIdMap);
    }

    private void parseFriendExplore(Map<Integer, Integer> playerIdMap) {
        logger.info("pase friend_explore blob");
        Integer tableMaxId = this.tableMaxId.get("friend_explore");
        String sql = "select id,chapter from friend_explore where id > " + tableMaxId;
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> aFriendExplore : mapList) {
            long tblId = (Long) aFriendExplore.get("id");
            if (aFriendExplore.get("chapter") == null) {
                continue;
            }
            byte[] chapterBytes = (byte[]) aFriendExplore.get("chapter");
            FriendExplore.DbFriendChapter dbFriendChapter = ProtostuffUtil.deserialize(chapterBytes, FriendExplore.DbFriendChapter.class);
            if (dbFriendChapter != null) {
                for (FriendExplore.DbFriendChapter1 dbFriendChapter1 : dbFriendChapter.getChapters().values()) {
                    List<Integer> friends = dbFriendChapter1.getFriends();
                    if (friends != null) {
                        List<Integer> newFriends = new ArrayList<>();
                        dbFriendChapter1.setFriends(newFriends);
                        for (int aFriendId : friends) {
                            Integer newPlayerId = playerIdMap.get(aFriendId);
                            if (newPlayerId != null) {
                                newFriends.add(newPlayerId);
                            }
                        }
                        friends.clear();
                    }
                }
            }
            chapterBytes = ProtostuffUtil.serialize(dbFriendChapter);
            //update db
            jdbcTemplate.update("update friend_explore set chapter = ? where id = ?", chapterBytes, tblId);
        }
    }

    private void parseFriendBoss(Map<Integer, Integer> playerIdMap) {
        logger.info("pase friend_boss blob");
        Integer tableMaxId = this.tableMaxId.get("friend_boss");
        String sql = "select id,boss from friend_boss where id > " + tableMaxId;
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> afriendboss : mapList) {
            long legionId = (Long) afriendboss.get("id");
            if (afriendboss.get("boss") == null) {
                continue;
            }
            byte[] bossByte = (byte[]) afriendboss.get("boss");
            FriendBoss.DbFriendBoss dbFriendBoss = ProtostuffUtil.deserialize(bossByte, FriendBoss.DbFriendBoss.class);
            Map<Integer, Long> playerHurmList = dbFriendBoss.getPlayerHurm();
            if (playerHurmList != null) {
                Map<Integer, Long> newPlayerHurm = new HashMap<>();
                dbFriendBoss.setPlayerHurm(newPlayerHurm);
                Set<Integer> keys = new HashSet<>(playerHurmList.keySet());
                for (int oldPlayerId : keys) {
                    Long aLong = playerHurmList.get(oldPlayerId);
                    Integer newPlayerId = playerIdMap.get(oldPlayerId);
                    if (newPlayerId != null) {
                        newPlayerHurm.put(newPlayerId, aLong);
                    }
                }
                playerHurmList.clear();
            }
            bossByte = ProtostuffUtil.serialize(dbFriendBoss);
            //update db
            jdbcTemplate.update("update friend_boss set boss = ? where id = ?", bossByte, legionId);
        }
    }

    private void parseLegion(Map<Integer, Integer> playerIdMap) {
        logger.info("pase legion blob");
        Integer legionMaxId = tableMaxId.get("legion");
        String sql = "select id,members,boss,apply_players from legion where id > " + legionMaxId;
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> alegion : mapList) {
            long legionId = (Long) alegion.get("id");
            //成员
            byte[] membersByte = (byte[]) alegion.get("members");
            if (membersByte != null && membersByte.length > 0) {
                LegionPlayer.DbLegionPlayers dbLegionPlayers = ProtostuffUtil.deserialize(membersByte, LegionPlayer.DbLegionPlayers.class);
                Map<Integer, LegionPlayer> legionMembers = dbLegionPlayers.getMembers();
                if (legionMembers != null) {
                    Map<Integer, LegionPlayer> newMembers = new HashMap<>();
                    dbLegionPlayers.setMembers(newMembers);
                    Set<Integer> keys = new HashSet<>(legionMembers.keySet());
                    for (int oldPlayerId : keys) {
                        LegionPlayer legionPlayer = legionMembers.get(oldPlayerId);
                        Integer newPlayerId = playerIdMap.get(oldPlayerId);
                        if (newPlayerId != null) {
                            legionPlayer.setPlayerId(newPlayerId);
                            newMembers.put(newPlayerId, legionPlayer);
                        }
                    }
                    legionMembers.clear();
                }
                membersByte = ProtostuffUtil.serialize(dbLegionPlayers);
            }
            //boss
            byte[] bossByte = null;
            if (alegion.get("boss") != null) {
                bossByte = (byte[]) alegion.get("boss");
                DbLegionBoss dbLegionBoss = ProtostuffUtil.deserialize(bossByte, DbLegionBoss.class);
                if (dbLegionBoss != null && dbLegionBoss.getRecords() != null) {
                    List<DbLegionBoss.LegionBossDamage> records = dbLegionBoss.getRecords();
                    for (DbLegionBoss.LegionBossDamage legionBossDamage : records) {
                        //damage list
                        Map<Integer, Long> damageList = legionBossDamage.getDamageList();
                        if (damageList != null) {
                            Map<Integer, Long> newDamageList = new HashMap<>();
                            legionBossDamage.setDamageList(newDamageList);
                            Set<Integer> keys = new HashSet<>(damageList.keySet());
                            for (int oldPlayerId : keys) {
                                Long aLong = damageList.get(oldPlayerId);
                                Integer newPlayerId = playerIdMap.get(oldPlayerId);
                                if (newPlayerId != null) {
                                    newDamageList.put(newPlayerId, aLong);
                                }
                            }
                            damageList.clear();
                        }
                        //last damage
                        Map<Integer, Long> lastDamageMap = legionBossDamage.getLastDamageMap();
                        if (lastDamageMap != null) {
                            Map<Integer, Long> newLastDamage = new HashMap<>();
                            legionBossDamage.setLastDamageMap(newLastDamage);
                            Set<Integer> keys = new HashSet<>(lastDamageMap.keySet());
                            for (int oldPlayerId : keys) {
                                Long aInt = lastDamageMap.get(oldPlayerId);
                                Integer newPlayerId = playerIdMap.get(oldPlayerId);
                                if (newPlayerId != null) {
                                    newLastDamage.put(newPlayerId, aInt);
                                }
                            }
                            lastDamageMap.clear();
                        }
                    }
                }
                bossByte = ProtostuffUtil.serialize(dbLegionBoss);
            }
            //申请人
            String apply_players = "";
            if (alegion.get("apply_players") != null) {
                apply_players = (String) alegion.get("apply_players");
                Map<Integer, Long> applyPlayerMap = SimpleTextConvert.decodeIntLongMap(apply_players);
                if (applyPlayerMap != null) {
                    Map<Integer, Long> newApplyPlayers = new HashMap<>();
                    Set<Integer> keys = new HashSet<>(applyPlayerMap.keySet());
                    for (int oldPlayerId : keys) {
                        Long aval = applyPlayerMap.get(oldPlayerId);
                        Integer newPlayerId = playerIdMap.get(oldPlayerId);
                        if (newPlayerId != null) {
                            newApplyPlayers.put(newPlayerId, aval);
                        }
                    }
                    applyPlayerMap.clear();
                    apply_players = SimpleTextConvert.encodeMap(newApplyPlayers);
                }
            }
            //update db
            jdbcTemplate.update("update legion set members = ?,boss=?,apply_players=? where id = ?", membersByte, bossByte, apply_players, legionId);
        }
    }

    private void resetGuoZhanNation() {
        logger.info("重置国战玩家的属国信息");
        String sql = "update guozhan_self set nation = 0,nation_change_time = null";
        jdbcTemplate.execute(sql);
    }

    private void clearGuildHireInfo() {
        logger.info("清除公会的雇用信息");
        String sql = "update guild_self set hire_hero = null";
        jdbcTemplate.execute(sql);
    }

    private void specialParseAfter() {
        logger.info("parse t_hero_mail#f_id remove AUTO_INCREMENT");
        jdbcTemplate.execute("ALTER TABLE `t_hero_mail` MODIFY COLUMN `f_id` int(11) NOT NULL FIRST");
    }

    private void specialParseBefore() {
        logger.info("parse t_hero_mail#f_id add AUTO_INCREMENT");
        jdbcTemplate.execute("ALTER TABLE `t_hero_mail` MODIFY COLUMN `f_id` int(11) NOT NULL AUTO_INCREMENT FIRST");
    }

    /**
     * 返还帮旗资金
     */
    private void returnFactionFlagCopper(String sourceDbName) {
        logger.info("return faction flag copper");
        String sql = String.format(
                "update %s.t_faction as tFaction,(SELECT f_faction_id,count(*) as fCount from %s.t_scene_bangqi GROUP BY f_faction_id) as tSceneBangqi set " +
                        "tFaction.f_copper = tFaction.f_copper + 10000000*tSceneBangqi.fCount where tFaction.f_id = tSceneBangqi.f_faction_id",
                sourceDbName, sourceDbName);
        jdbcTemplate.execute(sql);
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
        String tmpTbl = "CREATE TABLE `id_relations` (`f_id` int(11) NOT NULL AUTO_INCREMENT, `old_id` int(11) NOT NULL DEFAULT '0', `new_id` int(11) NOT " +
                "NULL DEFAULT '0',"
                + " `tbl_name` varchar(255) NOT NULL DEFAULT '', PRIMARY KEY (`f_id`),KEY `id_and_name` (`old_id`,`tbl_name`)) ENGINE=HEAP DEFAULT " +
                "CHARSET=utf8;";
        jdbcTemplate.execute(tmpTbl);
    }

    protected void parseHeroId(String tblName, String heroFieldName) {
        logger.info("update heroId:tbl={}", tblName);
        String idFieldName = "id";
        String sql = String.format(
                "update %s,id_relations set %s.%s = id_relations.new_id where %s.%s > ? and %s.%s = id_relations.old_id and id_relations.tbl_name = ?",
                tblName, tblName, heroFieldName, tblName, idFieldName, tblName, heroFieldName);
        // logger.info("update heroId:sql={}",sql);
        jdbcTemplate.update(sql, tableMaxId.get(tblName), "player");
    }

    protected void parseGuildId(String tblName, String heroFieldName) {
        logger.info("update guildId:tbl={}", tblName);
        String idFieldName = "Id";
        String sql = String.format(
                "update %s,id_relations set %s.%s = id_relations.new_id where %s.%s > ? and %s.%s = id_relations.old_id and id_relations.tbl_name = ?",
                tblName, tblName, heroFieldName, tblName, idFieldName, tblName, heroFieldName);
        jdbcTemplate.update(sql, tableMaxId.get(tblName), "guild");
    }

    private void parseFactionId(String tblName, String heroFieldName) {
        logger.info("update factionId:tbl={}", tblName);
        String sql = String.format(
                "update %s,id_relations set %s.%s = id_relations.new_id where %s.f_id > ? and %s.%s = id_relations.old_id and id_relations.tbl_name = ?",
                tblName, tblName, heroFieldName, tblName, tblName, heroFieldName);
        jdbcTemplate.update(sql, tableMaxId.get(tblName), "t_faction");
    }

    private void parseHorseId(String tblName, String heroFieldName) {
        logger.info("update horseId:tbl={}", tblName);
        String idFieldName = "f_id";
        if (tblName.equals("t_hero_goods") || tblName.equals("t_hero_task")) {
            idFieldName = "f_base_id";
        }
        String sql = String.format(
                "update %s,id_relations set %s.%s = id_relations.new_id where %s.%s > ? and %s.%s = id_relations.old_id and id_relations.tbl_name = ?",
                tblName, tblName, heroFieldName, tblName, idFieldName, tblName, heroFieldName);
        jdbcTemplate.update(sql, tableMaxId.get(tblName), "t_hero_horse");
    }

    private void parseSkillHorseId(String tblName, String heroFieldName) {
        logger.info("update skill horseId:tbl={}", tblName);
        String idFieldName = "f_id";
        String sql = String.format(
                "update %s,id_relations set %s.%s = id_relations.new_id where %s.%s > ? and %s.%s = id_relations.old_id and id_relations.tbl_name = ? and %s" +
                        ".f_skilltype=1",
                tblName, tblName, heroFieldName, tblName, idFieldName, tblName, heroFieldName, tblName);
        jdbcTemplate.update(sql, tableMaxId.get(tblName), "t_hero_horse");
    }

    public String getTargetDbName() {
        return targetDbName;
    }

    public void setTargetDbName(String targetDbName) {
        this.targetDbName = targetDbName;
    }

}
