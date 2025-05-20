package game.module.user.bean;

import java.util.List;

public class GoldBuy {

    private Integer id;
    private Integer playerId;
    private List<Long> buyTime;

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

    public List<Long> getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(List<Long> buyTime) {
        this.buyTime = buyTime;
    }

    @Override
    public String toString() {
        return "GoldBuy{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", buyTime=" + buyTime +
                '}';
    }
}
