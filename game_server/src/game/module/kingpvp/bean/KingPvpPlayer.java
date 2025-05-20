package game.module.kingpvp.bean;

import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.DbBattleset;

import java.util.List;
import java.util.Map;

public class KingPvpPlayer {

    private String rname;

    private Integer level;

    private Integer iconid;

    private Integer headid;

    private Integer frameid;

    private Integer power;

    Map<Integer, BattlePlayerBase> battlePlayerMap;

    private List<DbBattleset> dbBattlesetList;

    private Integer id;

    private Integer hide;

    private Integer vip;

    private long createTime;

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

    public List<DbBattleset> getDbBattlesetList() {
        return dbBattlesetList;
    }

    public void setDbBattlesetList(List<DbBattleset> dbBattlesetList) {
        this.dbBattlesetList = dbBattlesetList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "KingPvpPlayer{" +
                "rname='" + rname + '\'' +
                ", level=" + level +
                ", iconid=" + iconid +
                ", headid=" + headid +
                ", frameid=" + frameid +
                ", power=" + power +
                ", battlePlayerMap=" + battlePlayerMap +
                ", dbBattlesetList=" + dbBattlesetList +
                ", id=" + id +
                ", hide=" + hide +
                ", vip=" + vip +
                ", createTime=" + createTime +
                '}';
    }

}
