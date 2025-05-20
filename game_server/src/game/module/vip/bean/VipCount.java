package game.module.vip.bean;

import java.util.Date;
import java.util.Set;

public class VipCount {

    private Integer id;
    private Integer playerId;
    private Integer actCount;
    private Date lastActTime;

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

    public Integer getActCount() {
        return actCount;
    }

    public void setActCount(Integer actCount) {
        this.actCount = actCount;
    }

    public Date getLastActTime() {
        return lastActTime;
    }

    public void setLastActTime(Date lastActTime) {
        this.lastActTime = lastActTime;
    }

    @Override
    public String toString() {
        return "VipCount{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", actCount=" + actCount +
                ", lastActTime=" + lastActTime +
                '}';
    }
}
