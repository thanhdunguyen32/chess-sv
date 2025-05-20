package game.module.chapter.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.chapter.bean.BattleFormation;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BattleFormationDao {

    private static final Logger logger = LoggerFactory.getLogger(BattleFormationDao.class);

    private final DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static BattleFormationDao instance = new BattleFormationDao();
    }

    public static BattleFormationDao getInstance() {
        return SingletonHolder.instance;
    }

    private ResultSetHandler<BattleFormation> multiBattleFormationHanlder = rs -> {
        BattleFormation battleFormation = null;
        if (rs.next()) {
            battleFormation = new BattleFormation();
            battleFormation.setId(rs.getInt("id"));
            battleFormation.setPlayerId(rs.getInt("player_id"));
            String mythics_str = rs.getString("mythics");
            Map<Integer, Integer> mythicsMap = SimpleTextConvert.decodeIntMap(mythics_str);
            battleFormation.setMythics(mythicsMap);
            String normal_str = rs.getString("normal");
            Map<Integer, Long> normalMap = SimpleTextConvert.decodeIntLongMap(normal_str);
            battleFormation.setNormal(normalMap);
            String pre1_str = rs.getString("pre1");
            Map<Integer, Long> pre1Map = SimpleTextConvert.decodeIntLongMap(pre1_str);
            battleFormation.setPre1(pre1Map);
            String pre2_str = rs.getString("pre2");
            Map<Integer, Long> pre2Map = SimpleTextConvert.decodeIntLongMap(pre2_str);
            battleFormation.setPre2(pre2Map);
            String pvpatt_str = rs.getString("pvpatt");
            Map<Integer, Long> pvpattMap = SimpleTextConvert.decodeIntLongMap(pvpatt_str);
            battleFormation.setPvpatt(pvpattMap);
            String pvpdef_str = rs.getString("pvpdef");
            Map<Integer, Long> pvpdefMap = SimpleTextConvert.decodeIntLongMap(pvpdef_str);
            battleFormation.setPvpdef(pvpdefMap);
            String tower_str = rs.getString("tower");
            Map<Integer, Long> towerMap = SimpleTextConvert.decodeIntLongMap(tower_str);
            battleFormation.setTower(towerMap);
            String expedition_str = rs.getString("expedition");
            Map<Integer, Long> expeditionMap = SimpleTextConvert.decodeIntLongMap(expedition_str);
            battleFormation.setExpedition(expeditionMap);
            String dungeon_str = rs.getString("dungeon");
            Map<Integer, Long> dungeonMap = SimpleTextConvert.decodeIntLongMap(dungeon_str);
            battleFormation.setDungeon(dungeonMap);
            String teampvp_str = rs.getString("teampvp");
            Map<Integer, Long> teampvpMap = SimpleTextConvert.decodeIntLongMap(teampvp_str);
            battleFormation.setTeampvp(teampvpMap);
            String kingpvp1_str = rs.getString("kingpvp1");
            Map<Integer, Long> kingpvp1Map = SimpleTextConvert.decodeIntLongMap(kingpvp1_str);
            battleFormation.setKingpvp1(kingpvp1Map);
            String kingpvp2_str = rs.getString("kingpvp2");
            Map<Integer, Long> kingpvp2Map = SimpleTextConvert.decodeIntLongMap(kingpvp2_str);
            battleFormation.setKingpvp2(kingpvp2Map);
            String kingpvp3_str = rs.getString("kingpvp3");
            Map<Integer, Long> kingpvp3Map = SimpleTextConvert.decodeIntLongMap(kingpvp3_str);
            battleFormation.setKingpvp3(kingpvp3Map);
        }
        return battleFormation;
    };

    /**
     * @param playerId
     * @return
     */
    public BattleFormation getBattleFormation(int playerId) {
        BattleFormation ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from battle_formation where player_id = ?",
                    multiBattleFormationHanlder, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param battleFormation
     * @return
     */
    public boolean addBattleFormation(BattleFormation battleFormation) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
        String mythics_s = SimpleTextConvert.encodeMap(mythicsMap);
        Map<Integer, Long> battleFormationNormal = battleFormation.getNormal();
        String battleFormationNormal_s = SimpleTextConvert.encodeMap(battleFormationNormal);
        Map<Integer, Long> battleFormationPre1 = battleFormation.getPre1();
        String battleFormationPre1_s = SimpleTextConvert.encodeMap(battleFormationPre1);
        Map<Integer, Long> battleFormationPre2 = battleFormation.getPre2();
        String battleFormationPre2_s = SimpleTextConvert.encodeMap(battleFormationPre2);
        Map<Integer, Long> battleFormationPvpatt = battleFormation.getPvpatt();
        String battleFormationPvpatt_s = SimpleTextConvert.encodeMap(battleFormationPvpatt);
        Map<Integer, Long> battleFormationPvpdef = battleFormation.getPvpdef();
        String battleFormationPvpdef_s = SimpleTextConvert.encodeMap(battleFormationPvpdef);
        Map<Integer, Long> battleFormationTower = battleFormation.getTower();
        String battleFormationTower_s = SimpleTextConvert.encodeMap(battleFormationTower);
        Map<Integer, Long> battleFormationExpedition = battleFormation.getExpedition();
        String battleFormationExpedition_s = SimpleTextConvert.encodeMap(battleFormationExpedition);
        Map<Integer, Long> battleFormationDungeon = battleFormation.getDungeon();
        String battleFormationDungeon_s = SimpleTextConvert.encodeMap(battleFormationDungeon);
        Map<Integer, Long> battleFormationTeampvp = battleFormation.getTeampvp();
        String battleFormationTeampvp_s = SimpleTextConvert.encodeMap(battleFormationTeampvp);
        Map<Integer, Long> battleFormationKingpvp1 = battleFormation.getKingpvp1();
        String battleFormationKingpvp1_s = SimpleTextConvert.encodeMap(battleFormationKingpvp1);
        Map<Integer, Long> battleFormationKingpvp2 = battleFormation.getKingpvp2();
        String battleFormationKingpvp2_s = SimpleTextConvert.encodeMap(battleFormationKingpvp2);
        Map<Integer, Long> battleFormationKingpvp3 = battleFormation.getKingpvp3();
        String battleFormationKingpvp3_s = SimpleTextConvert.encodeMap(battleFormationKingpvp3);

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into battle_formation(player_id,mythics,normal,pre1,pre2,pvpatt,pvpdef,tower,expedition,dungeon,teampvp,kingpvp1,kingpvp2," +
                            "kingpvp3) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    battleFormation.getPlayerId(), mythics_s, battleFormationNormal_s, battleFormationPre1_s, battleFormationPre2_s,
                    battleFormationPvpatt_s, battleFormationPvpdef_s, battleFormationTower_s, battleFormationExpedition_s, battleFormationDungeon_s,
                    battleFormationTeampvp_s, battleFormationKingpvp1_s, battleFormationKingpvp2_s, battleFormationKingpvp3_s);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                battleFormation.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    /**
     * @param battleFormation
     */
    public void updateBattleFormation(BattleFormation battleFormation) {
        QueryRunner runner = new QueryRunner(dataSource);

        Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
        String mythics_s = SimpleTextConvert.encodeMap(mythicsMap);
        Map<Integer, Long> battleFormationNormal = battleFormation.getNormal();
        String battleFormationNormal_s = SimpleTextConvert.encodeMap(battleFormationNormal);
        Map<Integer, Long> battleFormationPre1 = battleFormation.getPre1();
        String battleFormationPre1_s = SimpleTextConvert.encodeMap(battleFormationPre1);
        Map<Integer, Long> battleFormationPre2 = battleFormation.getPre2();
        String battleFormationPre2_s = SimpleTextConvert.encodeMap(battleFormationPre2);
        Map<Integer, Long> battleFormationPvpatt = battleFormation.getPvpatt();
        String battleFormationPvpatt_s = SimpleTextConvert.encodeMap(battleFormationPvpatt);
        Map<Integer, Long> battleFormationPvpdef = battleFormation.getPvpdef();
        String battleFormationPvpdef_s = SimpleTextConvert.encodeMap(battleFormationPvpdef);
        Map<Integer, Long> battleFormationTower = battleFormation.getTower();
        String battleFormationTower_s = SimpleTextConvert.encodeMap(battleFormationTower);
        Map<Integer, Long> battleFormationExpedition = battleFormation.getExpedition();
        String battleFormationExpedition_s = SimpleTextConvert.encodeMap(battleFormationExpedition);
        Map<Integer, Long> battleFormationDungeon = battleFormation.getDungeon();
        String battleFormationDungeon_s = SimpleTextConvert.encodeMap(battleFormationDungeon);
        Map<Integer, Long> battleFormationTeampvp = battleFormation.getTeampvp();
        String battleFormationTeampvp_s = SimpleTextConvert.encodeMap(battleFormationTeampvp);
        Map<Integer, Long> battleFormationKingpvp1 = battleFormation.getKingpvp1();
        String battleFormationKingpvp1_s = SimpleTextConvert.encodeMap(battleFormationKingpvp1);
        Map<Integer, Long> battleFormationKingpvp2 = battleFormation.getKingpvp2();
        String battleFormationKingpvp2_s = SimpleTextConvert.encodeMap(battleFormationKingpvp2);
        Map<Integer, Long> battleFormationKingpvp3 = battleFormation.getKingpvp3();
        String battleFormationKingpvp3_s = SimpleTextConvert.encodeMap(battleFormationKingpvp3);

        try {
            runner.update(
                    "update battle_formation set mythics=?,normal=?,pre1=?,pre2=?,pvpatt=?,pvpdef=?,tower=?,expedition=?,dungeon=?,teampvp=?,kingpvp1=?," +
                            "kingpvp2=?,kingpvp3=? where id=?",
                    mythics_s, battleFormationNormal_s, battleFormationPre1_s, battleFormationPre2_s, battleFormationPvpatt_s,
                    battleFormationPvpdef_s, battleFormationTower_s, battleFormationExpedition_s, battleFormationDungeon_s, battleFormationTeampvp_s,
                    battleFormationKingpvp1_s, battleFormationKingpvp2_s, battleFormationKingpvp3_s, battleFormation.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public void removeBattleFormation(int playerBattleFormationsId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from battle_formation where id=?", playerBattleFormationsId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public Map<Integer, BattleFormation> getBattleFormations(String playerIdAll) {
        ResultSetHandler<Map<Integer, BattleFormation>> stageFormationAllHandler = rs -> {
            Map<Integer, BattleFormation> bfMap = new ConcurrentHashMap<>();
            while (rs.next()) {
                BattleFormation battleFormation = new BattleFormation();
                int playerId = rs.getInt("player_id");
                battleFormation.setPlayerId(playerId);
                String mythics_str = rs.getString("mythics");
                Map<Integer, Integer> mythicsMap = SimpleTextConvert.decodeIntMap(mythics_str);
                battleFormation.setMythics(mythicsMap);
                String normal_str = rs.getString("normal");
                Map<Integer, Long> normalMap = SimpleTextConvert.decodeIntLongMap(normal_str);
                battleFormation.setNormal(normalMap);
                String pre1_str = rs.getString("pre1");
                Map<Integer, Long> pre1Map = SimpleTextConvert.decodeIntLongMap(pre1_str);
                battleFormation.setPre1(pre1Map);
                String pre2_str = rs.getString("pre2");
                Map<Integer, Long> pre2Map = SimpleTextConvert.decodeIntLongMap(pre2_str);
                battleFormation.setPre2(pre2Map);
                String pvpatt_str = rs.getString("pvpatt");
                Map<Integer, Long> pvpattMap = SimpleTextConvert.decodeIntLongMap(pvpatt_str);
                battleFormation.setPvpatt(pvpattMap);
                String pvpdef_str = rs.getString("pvpdef");
                Map<Integer, Long> pvpdefMap = SimpleTextConvert.decodeIntLongMap(pvpdef_str);
                battleFormation.setPvpdef(pvpdefMap);
                String tower_str = rs.getString("tower");
                Map<Integer, Long> towerMap = SimpleTextConvert.decodeIntLongMap(tower_str);
                battleFormation.setTower(towerMap);
                String expedition_str = rs.getString("expedition");
                Map<Integer, Long> expeditionMap = SimpleTextConvert.decodeIntLongMap(expedition_str);
                battleFormation.setExpedition(expeditionMap);
                String dungeon_str = rs.getString("dungeon");
                Map<Integer, Long> dungeonMap = SimpleTextConvert.decodeIntLongMap(dungeon_str);
                battleFormation.setDungeon(dungeonMap);
                String teampvp_str = rs.getString("teampvp");
                Map<Integer, Long> teampvpMap = SimpleTextConvert.decodeIntLongMap(teampvp_str);
                battleFormation.setTeampvp(teampvpMap);
                String kingpvp1_str = rs.getString("kingpvp1");
                Map<Integer, Long> kingpvp1Map = SimpleTextConvert.decodeIntLongMap(kingpvp1_str);
                battleFormation.setKingpvp1(kingpvp1Map);
                String kingpvp2_str = rs.getString("kingpvp2");
                Map<Integer, Long> kingpvp2Map = SimpleTextConvert.decodeIntLongMap(kingpvp2_str);
                battleFormation.setKingpvp2(kingpvp2Map);
                String kingpvp3_str = rs.getString("kingpvp3");
                Map<Integer, Long> kingpvp3Map = SimpleTextConvert.decodeIntLongMap(kingpvp3_str);
                battleFormation.setKingpvp3(kingpvp3Map);
                bfMap.put(playerId, battleFormation);
            }
            return bfMap;
        };

        Map<Integer, BattleFormation> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("SELECT * from battle_formation where player_id in (" + playerIdAll + ")",
                    stageFormationAllHandler);
        } catch (SQLException e) {
            logger.error("", e);
            return ret;
        }
        return ret;
    }

    public List<Integer> getPvpPlayers(){
        List<Integer> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select player_id from battle_formation where pvpdef is not null or pvpatt is not null",
                    DaoCommonHandler.IntegerListHandler);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

}
