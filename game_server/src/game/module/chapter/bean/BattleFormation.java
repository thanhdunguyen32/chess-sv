package game.module.chapter.bean;

import java.util.Map;

public class BattleFormation {

    private Integer id;

    private Integer playerId;

    private Map<Integer, Integer> mythics;

    private Map<Integer, Long> normal;
    private Map<Integer, Long> pre1;
    private Map<Integer, Long> pre2;
    private Map<Integer, Long> pvpatt;
    private Map<Integer, Long> pvpdef;
    private Map<Integer, Long> tower;
    private Map<Integer, Long> expedition;
    private Map<Integer, Long> dungeon;
    private Map<Integer, Long> teampvp;
    private Map<Integer, Long> kingpvp1;
    private Map<Integer, Long> kingpvp2;
    private Map<Integer, Long> kingpvp3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Map<Integer, Long> getNormal() {
        return normal;
    }

    public void setNormal(Map<Integer, Long> normal) {
        this.normal = normal;
    }

    public Map<Integer, Long> getPre1() {
        return pre1;
    }

    public void setPre1(Map<Integer, Long> pre1) {
        this.pre1 = pre1;
    }

    public Map<Integer, Long> getPre2() {
        return pre2;
    }

    public void setPre2(Map<Integer, Long> pre2) {
        this.pre2 = pre2;
    }

    public Map<Integer, Long> getPvpatt() {
        return pvpatt;
    }

    public void setPvpatt(Map<Integer, Long> pvpatt) {
        this.pvpatt = pvpatt;
    }

    public Map<Integer, Long> getPvpdef() {
        return pvpdef;
    }

    public void setPvpdef(Map<Integer, Long> pvpdef) {
        this.pvpdef = pvpdef;
    }

    public Map<Integer, Long> getTower() {
        return tower;
    }

    public void setTower(Map<Integer, Long> tower) {
        this.tower = tower;
    }

    public Map<Integer, Integer> getMythics() {
        return mythics;
    }

    public void setMythics(Map<Integer, Integer> mythics) {
        this.mythics = mythics;
    }

    public Map<Integer, Long> getExpedition() {
        return expedition;
    }

    public void setExpedition(Map<Integer, Long> expedition) {
        this.expedition = expedition;
    }

    public Map<Integer, Long> getDungeon() {
        return dungeon;
    }

    public void setDungeon(Map<Integer, Long> dungeon) {
        this.dungeon = dungeon;
    }

    public Map<Integer, Long> getTeampvp() {
        return teampvp;
    }

    public void setTeampvp(Map<Integer, Long> teampvp) {
        this.teampvp = teampvp;
    }

    public Map<Integer, Long> getKingpvp1() {
        return kingpvp1;
    }

    public void setKingpvp1(Map<Integer, Long> kingpvp1) {
        this.kingpvp1 = kingpvp1;
    }

    public Map<Integer, Long> getKingpvp2() {
        return kingpvp2;
    }

    public void setKingpvp2(Map<Integer, Long> kingpvp2) {
        this.kingpvp2 = kingpvp2;
    }

    public Map<Integer, Long> getKingpvp3() {
        return kingpvp3;
    }

    public void setKingpvp3(Map<Integer, Long> kingpvp3) {
        this.kingpvp3 = kingpvp3;
    }

    @Override
    public String toString() {
        return "BattleFormation{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", mythics=" + mythics +
                ", normal=" + normal +
                ", pre1=" + pre1 +
                ", pre2=" + pre2 +
                ", pvpatt=" + pvpatt +
                ", pvpdef=" + pvpdef +
                ", tower=" + tower +
                ", expedition=" + expedition +
                ", dungeon=" + dungeon +
                ", teampvp=" + teampvp +
                ", kingpvp1=" + kingpvp1 +
                ", kingpvp2=" + kingpvp2 +
                ", kingpvp3=" + kingpvp3 +
                '}';
    }
}
