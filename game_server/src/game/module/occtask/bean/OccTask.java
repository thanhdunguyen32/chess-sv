package game.module.occtask.bean;

import java.util.Map;

public class OccTask {

    private Integer id;
    private Integer playerId;
    private Integer index;
    private Integer occtype;
    private Map<Integer, Integer> taskStatus;
    private Integer rewardId;

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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getOcctype() {
        return occtype;
    }

    public void setOcctype(Integer occtype) {
        this.occtype = occtype;
    }

    public Integer getRewardId() {
        return rewardId;
    }

    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }

    public Map<Integer, Integer> getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Map<Integer, Integer> taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "OccTask{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", index=" + index +
                ", occtype=" + occtype +
                ", rewardId=" + rewardId +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
