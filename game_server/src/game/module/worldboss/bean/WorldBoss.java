package game.module.worldboss.bean;

import java.util.Map;

public class WorldBoss {

    private Map<Integer, Long> playerLastDamage;

    private Map<Integer, Long> playerDamageSum;

    private Map<Long, Long> legionDamageSum;

    private Boolean isReward;

    public Map<Integer, Long> getPlayerLastDamage() {
        return playerLastDamage;
    }

    public void setPlayerLastDamage(Map<Integer, Long> playerLastDamage) {
        this.playerLastDamage = playerLastDamage;
    }

    public Map<Integer, Long> getPlayerDamageSum() {
        return playerDamageSum;
    }

    public void setPlayerDamageSum(Map<Integer, Long> playerDamageSum) {
        this.playerDamageSum = playerDamageSum;
    }

    public Map<Long, Long> getLegionDamageSum() {
        return legionDamageSum;
    }

    public void setLegionDamageSum(Map<Long, Long> legionDamageSum) {
        this.legionDamageSum = legionDamageSum;
    }

    public Boolean getReward() {
        return isReward;
    }

    public void setReward(Boolean reward) {
        isReward = reward;
    }

    @Override
    public String toString() {
        return "WorldBoss{" +
                "playerLastDamage=" + playerLastDamage +
                ", playerDamageSum=" + playerDamageSum +
                ", legionDamageSum=" + legionDamageSum +
                ", isReward=" + isReward +
                '}';
    }
}
