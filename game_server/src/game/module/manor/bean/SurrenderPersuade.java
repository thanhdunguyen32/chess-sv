package game.module.manor.bean;

import java.util.Map;

public class SurrenderPersuade {
    private Integer id;
    private Integer playerId;
    private Map<Integer, Integer> surrendermap;

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

    public Map<Integer, Integer> getSurrendermap() {
        return surrendermap;
    }

    public void setSurrendermap(Map<Integer, Integer> surrendermap) {
        this.surrendermap = surrendermap;
    }

    @Override
    public String toString() {
        return "SurrenderPersuade{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", surrendermap=" + surrendermap +
                '}';
    }

}
