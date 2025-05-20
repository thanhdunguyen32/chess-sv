package game.module.hero.dao;

import game.common.DaoCommonHandler;
import game.db.DataSourceManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExclusive;
import lion.common.ProtostuffUtil;
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

public class GeneralDao {

    private static Logger logger = LoggerFactory.getLogger(GeneralDao.class);

    private DataSource dataSource = DataSourceManager.getInstance().getDataSource();

    static class SingletonHolder {

        static GeneralDao instance = new GeneralDao();
    }
    public static GeneralDao getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务--进度数据
     */
    private ResultSetHandler<GeneralBean> oneHeroHanlder = rs -> {
        GeneralBean heroBean = null;
        if (rs.next()) {
            heroBean = new GeneralBean();
            heroBean.setUuid(rs.getLong("uuid"));
            heroBean.setPlayerId(rs.getInt("player_id"));
            heroBean.setTemplateId(rs.getInt("template_id"));
            heroBean.setLevel(rs.getInt("level"));
            heroBean.setPclass(rs.getInt("class"));
            heroBean.setStar(rs.getInt("star"));
            heroBean.setOccu(rs.getInt("occu"));
            heroBean.setPower(rs.getInt("power"));
            String equip_str = rs.getString("equip");
            List<Integer> equip_list = SimpleTextConvert.decodeIntList(equip_str);
            heroBean.setEquip(equip_list);
            heroBean.setTreasure(rs.getInt("treasure"));
            String skill_str = rs.getString("skill");
            List<Integer> skill_list = SimpleTextConvert.decodeIntList(skill_str);
            heroBean.setSkill(skill_list);
            heroBean.setHp(rs.getInt("hp"));
            heroBean.setAtk(rs.getInt("atk"));
            heroBean.setDef(rs.getInt("def"));
            heroBean.setMdef(rs.getInt("mdef"));
            heroBean.setAtktime(rs.getFloat("atktime"));
            heroBean.setRange(rs.getInt("prange"));
            heroBean.setMsp(rs.getInt("msp"));
            heroBean.setPasp(rs.getInt("pasp"));
            heroBean.setPcri(rs.getInt("pcri"));
            heroBean.setPcrid(rs.getInt("pcrid"));
            heroBean.setPmdex(rs.getInt("pmdex"));
            heroBean.setPdam(rs.getInt("pdam"));
            heroBean.setPhp(rs.getInt("php"));
            heroBean.setPatk(rs.getInt("patk"));
            heroBean.setPdef(rs.getInt("pdef"));
            heroBean.setPmdef(rs.getInt("pmdef"));
            heroBean.setPpbs(rs.getInt("ppbs"));
            heroBean.setPmbs(rs.getInt("pmbs"));
            heroBean.setPefc(rs.getInt("pefc"));
            heroBean.setPpthr(rs.getInt("ppthr"));
            heroBean.setPatkdam(rs.getInt("patkdam"));
            heroBean.setPskidam(rs.getInt("pskidam"));
            heroBean.setPckatk(rs.getInt("pckatk"));
            heroBean.setPmthr(rs.getInt("pmthr"));
            heroBean.setPdex(rs.getInt("pdex"));
            heroBean.setPmsatk(rs.getInt("pmsatk"));
            heroBean.setPmps(rs.getInt("pmps"));
            heroBean.setPcd(rs.getInt("pcd"));
            heroBean.setTalent(SimpleTextConvert.decodeIntList(rs.getString("talent")));
            //
            byte[] dataPacks = rs.getBytes("exclusive");
            if (dataPacks != null) {
                GeneralExclusive generalExclusive = ProtostuffUtil.deserialize(dataPacks, GeneralExclusive.class);
                heroBean.setExclusive(generalExclusive);
            }
        }
        return heroBean;
    };

    private ResultSetHandler<List<GeneralBean>> multiBuildingProHandler = rs -> {
        List<GeneralBean> heroList = new ArrayList<>();
        while (rs.next()) {
            GeneralBean heroBean = new GeneralBean();
            heroBean.setUuid(rs.getLong("uuid"));
            heroBean.setPlayerId(rs.getInt("player_id"));
            heroBean.setTemplateId(rs.getInt("template_id"));
            heroBean.setLevel(rs.getInt("level"));
            heroBean.setPclass(rs.getInt("class"));
            heroBean.setStar(rs.getInt("star"));
            heroBean.setOccu(rs.getInt("occu"));
            heroBean.setPower(rs.getInt("power"));
            String equip_str = rs.getString("equip");
            List<Integer> equip_list = SimpleTextConvert.decodeIntList(equip_str);
            heroBean.setEquip(equip_list);
            heroBean.setTreasure(rs.getInt("treasure"));
            String skill_str = rs.getString("skill");
            List<Integer> skill_list = SimpleTextConvert.decodeIntList(skill_str);
            heroBean.setSkill(skill_list);
            heroBean.setHp(rs.getInt("hp"));
            heroBean.setAtk(rs.getInt("atk"));
            heroBean.setDef(rs.getInt("def"));
            heroBean.setMdef(rs.getInt("mdef"));
            heroBean.setAtktime(rs.getFloat("atktime"));
            heroBean.setRange(rs.getInt("prange"));
            heroBean.setMsp(rs.getInt("msp"));
            heroBean.setPasp(rs.getInt("pasp"));
            heroBean.setPcri(rs.getInt("pcri"));
            heroBean.setPcrid(rs.getInt("pcrid"));
            heroBean.setPmdex(rs.getInt("pmdex"));
            heroBean.setPdam(rs.getInt("pdam"));
            heroBean.setPhp(rs.getInt("php"));
            heroBean.setPatk(rs.getInt("patk"));
            heroBean.setPdef(rs.getInt("pdef"));
            heroBean.setPmdef(rs.getInt("pmdef"));
            heroBean.setPpbs(rs.getInt("ppbs"));
            heroBean.setPmbs(rs.getInt("pmbs"));
            heroBean.setPefc(rs.getInt("pefc"));
            heroBean.setPpthr(rs.getInt("ppthr"));
            heroBean.setPatkdam(rs.getInt("patkdam"));
            heroBean.setPskidam(rs.getInt("pskidam"));
            heroBean.setPckatk(rs.getInt("pckatk"));
            heroBean.setPmthr(rs.getInt("pmthr"));
            heroBean.setPdex(rs.getInt("pdex"));
            heroBean.setPmsatk(rs.getInt("pmsatk"));
            heroBean.setPmps(rs.getInt("pmps"));
            heroBean.setPcd(rs.getInt("pcd"));
            heroBean.setTalent(SimpleTextConvert.decodeIntList(rs.getString("talent")));
            //
            byte[] dataPacks = rs.getBytes("exclusive");
            if (dataPacks != null) {
                GeneralExclusive generalExclusive = ProtostuffUtil.deserialize(dataPacks, GeneralExclusive.class);
                heroBean.setExclusive(generalExclusive);
            }
            heroList.add(heroBean);
        }
        return heroList;
    };

    /**
     * 获得玩家的所有任务进度<br/>
     * 排序：已完成任务>未完成任务<br/>
     * 第一优先级相同的情况下按照id从小到大排序
     *
     * @param playerId
     * @return
     */
    public List<GeneralBean> getHeros(int playerId) {
        List<GeneralBean> ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from hero where player_id = ?",
                    multiBuildingProHandler, playerId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    /**
     * 添加一条任务进度
     *
     * @param heroBean
     * @return
     */
    public boolean addHeroBean(GeneralBean heroBean) {
        boolean addRet = false;
        QueryRunner runner = new QueryRunner(dataSource);
        Connection conn = null;

        String equip_str = SimpleTextConvert.encodeCollection(heroBean.getEquip());
        String skill_str = SimpleTextConvert.encodeCollection(heroBean.getSkill());
        String talent_str = SimpleTextConvert.encodeCollection(heroBean.getTalent());
        byte[] exclusive_b = ProtostuffUtil.serialize(heroBean.getExclusive());

        try {
            conn = dataSource.getConnection();
            int ret = runner.update(conn,
                    "insert into hero(uuid,player_id, template_id,level, class,star,occu,power,equip,treasure,skill,hp,atk,def,mdef,atktime,prange,msp,pasp," +
                            "pcri,pcrid,pmdex,pdam,php,patk,pdef,pmdef,ppbs,pmbs,pefc,ppthr,patkdam,pskidam,pckatk,pmthr,pdex,pmsatk,pmps,pcd,exclusive," +
                            "talent,create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())",
                    heroBean.getUuid(), heroBean.getPlayerId(), heroBean.getTemplateId(), heroBean.getLevel(), heroBean.getPclass(), heroBean.getStar(),
                    heroBean.getOccu(), heroBean.getPower(), equip_str, heroBean.getTreasure(), skill_str,heroBean.getHp(),heroBean.getAtk(),heroBean.getDef(),
                    heroBean.getMdef(),heroBean.getAtktime(),heroBean.getRange(),heroBean.getMsp(),heroBean.getPasp(),heroBean.getPcri(),heroBean.getPcrid(),
                    heroBean.getPmdex(),heroBean.getPdam(),heroBean.getPhp(),heroBean.getPatk(),heroBean.getPdef(),heroBean.getPmdef(),heroBean.getPpbs(),
                    heroBean.getPmbs(),heroBean.getPefc(),heroBean.getPpthr(),heroBean.getPatkdam(),heroBean.getPskidam(),heroBean.getPckatk(),heroBean.getPmthr(),
                    heroBean.getPdex(),heroBean.getPmsatk(),heroBean.getPmps(),heroBean.getPcd(),exclusive_b,talent_str);
            if (ret > 0) {
                addRet = true;
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
     * @param heroBean
     * @return
     */
    public void updateHero(GeneralBean heroBean) {
        String equip_str = SimpleTextConvert.encodeCollection(heroBean.getEquip());
        String skill_str = SimpleTextConvert.encodeCollection(heroBean.getSkill());
        String talent_str = SimpleTextConvert.encodeCollection(heroBean.getTalent());
        byte[] exclusive_b = ProtostuffUtil.serialize(heroBean.getExclusive());
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("update hero set template_id=?,level=?,class=?,star=?,occu=?,power=?,equip=?,treasure=?,skill=?,hp=?,atk=?,def=?,mdef=?,atktime=?," +
                            "prange=?,msp=?,pasp=?,pcri=?,pcrid=?,pmdex=?,pdam=?,php=?,patk=?,pdef=?,pmdef=?,ppbs=?,pmbs=?,pefc=?,ppthr=?,patkdam=?," +
                            "pskidam=?,pckatk=?,pmthr=?,pdex=?,pmsatk=?,pmps=?,pcd=?,exclusive=?,talent=? where uuid=?",
                    heroBean.getTemplateId(), heroBean.getLevel(), heroBean.getPclass(), heroBean.getStar(), heroBean.getOccu(), heroBean.getPower(), equip_str,
                    heroBean.getTreasure(), skill_str, heroBean.getHp(), heroBean.getAtk(), heroBean.getDef(), heroBean.getMdef(), heroBean.getAtktime(),
                    heroBean.getRange(), heroBean.getMsp(), heroBean.getPasp(), heroBean.getPcri(), heroBean.getPcrid(), heroBean.getPmdex(),heroBean.getPdam(),
                    heroBean.getPhp(),heroBean.getPatk(),heroBean.getPdef(), heroBean.getPmdef(), heroBean.getPpbs(), heroBean.getPmbs(), heroBean.getPefc(),
                    heroBean.getPpthr(),heroBean.getPatkdam(),heroBean.getPskidam(),heroBean.getPckatk(),heroBean.getPmthr(),heroBean.getPdex(),
                    heroBean.getPmsatk(), heroBean.getPmps(), heroBean.getPcd(), exclusive_b, talent_str, heroBean.getUuid());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    /**
     * 删除一条任务进度(任务完成且奖励已领取)
     *
     * @param heroId
     */
    public void removeHero(long heroId) {
        QueryRunner runner = new QueryRunner(dataSource);

        try {
            runner.update("delete from hero where uuid=?", heroId);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    public GeneralBean getHero1(Integer playerId, int heroTemplateId) {
        GeneralBean ret = null;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select * from hero where player_id = ? and template_id = ?", oneHeroHanlder,
                    playerId, heroTemplateId);
        } catch (SQLException e) {
            logger.error("", e);
        }

        return ret;
    }

    public int getMaxHeroId() {
        int ret = 0;
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ret = runner.query("select max(id) from hero", DaoCommonHandler.Integerhandler);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return ret;
    }

}
