package game.module.pay.bean;

import java.util.Map;

public class LibaoBuy {

    private Integer id;

    private Integer playerId;

    private Map<String,Integer> buyCount;

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

    public Map<String, Integer> getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Map<String, Integer> buyCount) {
        this.buyCount = buyCount;
    }

    @Override
    public String toString() {
        return "LibaoBuy{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", buyCount=" + buyCount +
                '}';
    }
}
