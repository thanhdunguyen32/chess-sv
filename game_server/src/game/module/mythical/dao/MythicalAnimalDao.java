package game.module.mythical.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.mythical.bean.MythicalAnimal;
import lion.common.SimpleTextConvert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MythicalAnimalDao {

    private static Logger logger = LoggerFactory.getLogger(MythicalAnimalDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {
        static MythicalAnimalDao instance = new MythicalAnimalDao();
    }

    public static MythicalAnimalDao getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务--进度数据
     */
    private ResultSetHandler<List<MythicalAnimal>> multiMissionProHandler = rs -> {
        List<MythicalAnimal> mythicalAnimals = new ArrayList<>();
        while (rs.next()) {
            MythicalAnimal mythicalAnimal = new MythicalAnimal();
            mythicalAnimal.setId(rs.getInt("id"));
            mythicalAnimal.setPlayerId(rs.getInt("player_id"));
            mythicalAnimal.setTemplateId(rs.getInt("template_id"));
            mythicalAnimal.setLevel(rs.getInt("level"));
            mythicalAnimal.setPclass(rs.getInt("pclass"));
            //
            String passive_skill_str = rs.getString("passive_skills");
            List<Integer> passiveSkillList = SimpleTextConvert.decodeIntList(passive_skill_str);
            mythicalAnimal.setPassiveSkills(passiveSkillList);
            mythicalAnimals.add(mythicalAnimal);
        }
        return mythicalAnimals;
    };

    /**
     * 获得玩家的所有任务进度<br/>
     * 排序：已完成任务>未完成任务<br/>
     * 第一优先级相同的情况下按照id从小到大排序
     *
     * @param playerId
     * @return
     */
    public List<MythicalAnimal> getPlayerMythicalAnimal(int playerId) {
        List<MythicalAnimal> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from mythical_animal where player_id = ?",
                    multiMissionProHandler, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param mythicalAnimal
     * @return
     */
    public boolean addMythicalAnimal(MythicalAnimal mythicalAnimal) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        List<Integer> passiveSkills = mythicalAnimal.getPassiveSkills();
        String passiveSkillStr = SimpleTextConvert.encodeCollection(passiveSkills);
        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into mythical_animal(player_id, template_id, level, pclass, passive_skills) values(?,?,?,?,?)",
                    mythicalAnimal.getPlayerId(), mythicalAnimal.getTemplateId(), mythicalAnimal.getLevel(), mythicalAnimal.getPclass(), passiveSkillStr);
            if (ret > 0) {
                addRet = true;
            }
            if (addRet) {
                int theId = runner.query(conn, "SELECT LAST_INSERT_ID();", DaoCommonHandler.Integerhandler);
                mythicalAnimal.setId(theId);
            }

        } catch (SQLException e) {
            logger.error("", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return addRet;
    }

    /**
     * 更新一条任务进度
     *
     * @param mythicalAnimal
     * @return
     */
    public void updateMythicalAnimal(MythicalAnimal mythicalAnimal) {
        QueryRunner runner = new QueryRunner(dataSource);
        List<Integer> passiveSkills = mythicalAnimal.getPassiveSkills();
        String passiveSkillStr = SimpleTextConvert.encodeCollection(passiveSkills);
        try {
            runner.update(
                    "update mythical_animal set level=?, pclass=?,passive_skills=? where id=?",
                    mythicalAnimal.getLevel(), mythicalAnimal.getPclass(), passiveSkillStr, mythicalAnimal.getId());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     * 删除一条任务进度(任务完成且奖励已领取)
     *
     * @param taskId
     */
    public void removeMythicalAnimal(int taskId) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("delete from mythical_animal where id=?", taskId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
