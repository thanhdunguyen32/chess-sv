package game.module.shop.bean;

import java.util.Date;
import java.util.Map;

public class ShopBean {

    private Integer id;
    private Integer playerId;
    private Map<Integer, Integer> dayBuyCount;
    private Date lastDayBuyTime;
    private Map<Integer, Integer> decompBuyCount;
    private Map<Integer, Integer> starBuyCount;
    private Map<Integer, Integer> legionBuyCount;

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

    public Map<Integer, Integer> getDayBuyCount() {
        return dayBuyCount;
    }

    public void setDayBuyCount(Map<Integer, Integer> dayBuyCount) {
        this.dayBuyCount = dayBuyCount;
    }

    public Date getLastDayBuyTime() {
        return lastDayBuyTime;
    }

    public void setLastDayBuyTime(Date lastDayBuyTime) {
        this.lastDayBuyTime = lastDayBuyTime;
    }

    public Map<Integer, Integer> getDecompBuyCount() {
        return decompBuyCount;
    }

    public void setDecompBuyCount(Map<Integer, Integer> decompBuyCount) {
        this.decompBuyCount = decompBuyCount;
    }

    public Map<Integer, Integer> getStarBuyCount() {
        return starBuyCount;
    }

    public void setStarBuyCount(Map<Integer, Integer> starBuyCount) {
        this.starBuyCount = starBuyCount;
    }

    public Map<Integer, Integer> getLegionBuyCount() {
        return legionBuyCount;
    }

    public void setLegionBuyCount(Map<Integer, Integer> legionBuyCount) {
        this.legionBuyCount = legionBuyCount;
    }

    @Override
    public String toString() {
        return "ShopBean{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", dayBuyCount=" + dayBuyCount +
                ", lastDayBuyTime=" + lastDayBuyTime +
                ", decompBuyCount=" + decompBuyCount +
                ", starBuyCount=" + starBuyCount +
                ", legionBuyCount=" + legionBuyCount +
                '}';
    }
}
