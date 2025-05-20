package game.module.activity.bean;

import java.util.Map;

public class ActMjbg {
    private Integer id;
    private Integer playerId;
    private Integer index;
    private Map<Integer,Integer> boxOpen;
    private Map<Integer,Integer> rewardOpen;
    private Integer bigRewardIndex;
    private Boolean bigRewardGet;

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

    public Map<Integer, Integer> getBoxOpen() {
        return boxOpen;
    }

    public void setBoxOpen(Map<Integer, Integer> boxOpen) {
        this.boxOpen = boxOpen;
    }

    public Map<Integer, Integer> getRewardOpen() {
        return rewardOpen;
    }

    public void setRewardOpen(Map<Integer, Integer> rewardOpen) {
        this.rewardOpen = rewardOpen;
    }

    public Integer getBigRewardIndex() {
        return bigRewardIndex;
    }

    public void setBigRewardIndex(Integer bigRewardIndex) {
        this.bigRewardIndex = bigRewardIndex;
    }

    public Boolean getBigRewardGet() {
        return bigRewardGet;
    }

    public void setBigRewardGet(Boolean bigRewardGet) {
        this.bigRewardGet = bigRewardGet;
    }

    @Override
    public String toString() {
        return "ActMjbg{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", index=" + index +
                ", boxOpen=" + boxOpen +
                ", rewardOpen=" + rewardOpen +
                ", bigRewardIndex=" + bigRewardIndex +
                ", bigRewardGet=" + bigRewardGet +
                '}';
    }
}
