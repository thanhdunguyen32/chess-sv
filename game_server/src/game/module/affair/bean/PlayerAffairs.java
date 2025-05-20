package game.module.affair.bean;

import java.util.Date;

public class PlayerAffairs {

    private Integer id;
    private Integer playerId;
    private Date lastVisitTime;
    private DbAffairs dbAffairs;

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

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public DbAffairs getDbAffairs() {
        return dbAffairs;
    }

    public void setDbAffairs(DbAffairs dbAffairs) {
        this.dbAffairs = dbAffairs;
    }

    @Override
    public String toString() {
        return "PlayerAffairs{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", lastVisitTime=" + lastVisitTime +
                ", dbAffairs=" + dbAffairs +
                '}';
    }
}
