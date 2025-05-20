package game.module.mine.bean;

import java.util.Map;

public class DBMineBattleRecord {
    private Integer targetPlayerId;
    private Boolean isPositive;
    private Integer isSuccess;
    private Integer minePoint;
    private Integer type;
    private Map<Integer,Integer> gains;
    private Long addTime;

    public Integer getTargetPlayerId() {
        return targetPlayerId;
    }

    public void setTargetPlayerId(Integer targetPlayerId) {
        this.targetPlayerId = targetPlayerId;
    }

    public Boolean getPositive() {
        return isPositive;
    }

    public void setPositive(Boolean positive) {
        isPositive = positive;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Integer getMinePoint() {
        return minePoint;
    }

    public void setMinePoint(Integer minePoint) {
        this.minePoint = minePoint;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<Integer, Integer> getGains() {
        return gains;
    }

    public void setGains(Map<Integer, Integer> gains) {
        this.gains = gains;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "DBMineBattleRecord{" +
                "targetPlayerId=" + targetPlayerId +
                ", isPositive=" + isPositive +
                ", isSuccess=" + isSuccess +
                ", minePoint=" + minePoint +
                ", type=" + type +
                ", gains=" + gains +
                ", addTime=" + addTime +
                '}';
    }
}
