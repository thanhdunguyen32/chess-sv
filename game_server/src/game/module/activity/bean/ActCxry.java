package game.module.activity.bean;

import java.util.List;
import java.util.Map;

public class ActCxry {
    private Integer id;
    private Integer playerId;
    private Integer num;
    private Map<Integer,Integer> wishGenerals;

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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Map<Integer,Integer> getWishGenerals() {
        return wishGenerals;
    }

    public void setWishGenerals(Map<Integer,Integer> wishGenerals) {
        this.wishGenerals = wishGenerals;
    }

    @Override
    public String toString() {
        return "ActCxry{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", num=" + num +
                ", wishGenerals=" + wishGenerals +
                '}';
    }
}
