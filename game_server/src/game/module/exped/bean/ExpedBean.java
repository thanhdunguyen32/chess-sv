package game.module.exped.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExpedBean {

    private Integer id;

    private Integer playerId;

    private Map<Long,Integer> myHp;

    private Map<Integer,Integer> enemyHp;

    private List<Integer> wishCount;

    private ExpedPlayer checkpointEnemy;

    private Date lastResetTime;

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

    public Map<Long, Integer> getMyHp() {
        return myHp;
    }

    public void setMyHp(Map<Long, Integer> myHp) {
        this.myHp = myHp;
    }

    public List<Integer> getWishCount() {
        return wishCount;
    }

    public void setWishCount(List<Integer> wishCount) {
        this.wishCount = wishCount;
    }

    public ExpedPlayer getCheckpointEnemy() {
        return checkpointEnemy;
    }

    public void setCheckpointEnemy(ExpedPlayer checkpointEnemy) {
        this.checkpointEnemy = checkpointEnemy;
    }

    public Date getLastResetTime() {
        return lastResetTime;
    }

    public void setLastResetTime(Date lastResetTime) {
        this.lastResetTime = lastResetTime;
    }

    public Map<Integer, Integer> getEnemyHp() {
        return enemyHp;
    }

    public void setEnemyHp(Map<Integer, Integer> enemyHp) {
        this.enemyHp = enemyHp;
    }

    @Override
    public String toString() {
        return "ExpedBean{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", hps=" + myHp +
                ", wishCount=" + wishCount +
                ", checkpointEnemy=" + checkpointEnemy +
                ", lastResetTime=" + lastResetTime +
                '}';
    }
}
