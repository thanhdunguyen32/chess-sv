package game.module.activity.bean;

import java.util.Date;

public class ActivityXiangou {
    private Integer id;
    private Integer playerId;
    private Integer gstar;
    private Integer level;
    private Date endTime;
    private Integer buyCount;
    private Date lastBuyTime;

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

    public Integer getGstar() {
        return gstar;
    }

    public void setGstar(Integer gstar) {
        this.gstar = gstar;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Date getLastBuyTime() {
        return lastBuyTime;
    }

    public void setLastBuyTime(Date lastBuyTime) {
        this.lastBuyTime = lastBuyTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ActivityXiangou{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", gstar=" + gstar +
                ", level=" + level +
                ", endTime=" + endTime +
                ", buyCount=" + buyCount +
                ", lastBuyTime=" + lastBuyTime +
                '}';
    }
}
