package game.module.mine.bean;

import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.DbBattleset;

import java.util.Map;

public class DBMinePoint {
    private Integer playerId;
    private String rname;

    private Integer level;

    private Integer iconid;

    private Integer headid;

    private Integer frameid;

    private Integer power;
    private Long finishTime;
    /**
     * 机器人阵容
     */
    Map<Integer, BattlePlayerBase> battlePlayerMap;
    /**
     * 玩家防守阵容
     */
    private DbBattleset dbBattleset;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
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

    @Override
    public String toString() {
        return "DBMinePoint{" +
                "playerId=" + playerId +
                ", rname='" + rname + '\'' +
                ", level=" + level +
                ", iconid=" + iconid +
                ", headid=" + headid +
                ", frameid=" + frameid +
                ", power=" + power +
                ", finishTime=" + finishTime +
                ", battlePlayerMap=" + battlePlayerMap +
                ", dbBattleset=" + dbBattleset +
                '}';
    }
}
