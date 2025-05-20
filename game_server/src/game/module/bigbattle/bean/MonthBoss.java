package game.module.bigbattle.bean;

import java.util.Date;
import java.util.List;

public class MonthBoss {

    private Integer id;

    private Integer playerId;

    private List<Integer> levelIndex;

    private List<Long> lastDamage;

    private List<Long> nowHp;

    private Date lastVisitTime;

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

    public List<Integer> getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(List<Integer> levelIndex) {
        this.levelIndex = levelIndex;
    }

    public List<Long> getLastDamage() {
        return lastDamage;
    }

    public void setLastDamage(List<Long> lastDamage) {
        this.lastDamage = lastDamage;
    }

    public List<Long> getNowHp() {
        return nowHp;
    }

    public void setNowHp(List<Long> nowHp) {
        this.nowHp = nowHp;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    @Override
    public String toString() {
        return "MonthBoss{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", levelIndex=" + levelIndex +
                ", lastDamage=" + lastDamage +
                ", nowHp=" + nowHp +
                ", lastVisitTime=" + lastVisitTime +
                '}';
    }
}
