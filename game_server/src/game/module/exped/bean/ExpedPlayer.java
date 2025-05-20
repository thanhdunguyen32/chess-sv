package game.module.exped.bean;

import game.module.battle.dao.BattlePlayer;
import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.DbBattleset;
import game.module.hero.bean.GeneralBean;

import java.util.List;
import java.util.Map;

public class ExpedPlayer {

    private String rname;

    private Integer level;

    private Integer iconid;

    private Integer headid;

    private Integer frameid;

    private Integer power;

    Map<Integer, BattlePlayerBase> battlePlayerMap;

    private DbBattleset dbBattleset;

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIconid() {
        return iconid;
    }

    public void setIconid(Integer iconid) {
        this.iconid = iconid;
    }

    public Integer getHeadid() {
        return headid;
    }

    public void setHeadid(Integer headid) {
        this.headid = headid;
    }

    public Integer getFrameid() {
        return frameid;
    }

    public void setFrameid(Integer frameid) {
        this.frameid = frameid;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Map<Integer, BattlePlayerBase> getBattlePlayerMap() {
        return battlePlayerMap;
    }

    public void setBattlePlayerMap(Map<Integer, BattlePlayerBase> battlePlayerMap) {
        this.battlePlayerMap = battlePlayerMap;
    }

    public DbBattleset getDbBattleset() {
        return dbBattleset;
    }

    public void setDbBattleset(DbBattleset dbBattleset) {
        this.dbBattleset = dbBattleset;
    }

    @Override
    public String toString() {
        return "ExpedPlayer{" +
                "rname='" + rname + '\'' +
                ", level=" + level +
                ", iconid=" + iconid +
                ", headid=" + headid +
                ", frameid=" + frameid +
                ", power=" + power +
                ", battlePlayerMap=" + battlePlayerMap +
                ", dbBattleset=" + dbBattleset +
                '}';
    }

}
