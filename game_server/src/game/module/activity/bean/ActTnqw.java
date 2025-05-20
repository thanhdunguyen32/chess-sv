package game.module.activity.bean;

import java.util.Map;

public class ActTnqw {
    private Integer id;
    private Integer playerId;
    private Map<Integer,Long> nowHp;
    private Map<Integer,Long> lastDamage;

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

    public Map<Integer, Long> getNowHp() {
        return nowHp;
    }

    public void setNowHp(Map<Integer, Long> nowHp) {
        this.nowHp = nowHp;
    }

    public Map<Integer, Long> getLastDamage() {
        return lastDamage;
    }

    public void setLastDamage(Map<Integer, Long> lastDamage) {
        this.lastDamage = lastDamage;
    }

    @Override
    public String toString() {
        return "ActTnqw{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", nowHp=" + nowHp +
                ", lastDamage=" + lastDamage +
                '}';
    }
}
