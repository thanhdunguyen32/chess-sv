package game.module.spin.bean;

import java.util.Date;
import java.util.List;

public class SpinBean {

    private Integer id;

    private Integer playerId;

    private List<List<Integer>> rewardsNormal;

    private List<List<Integer>> rewardsAdvance;

    private List<Integer> scoreBuyNormal;

    private List<Integer> scoreBuyAdvance;

    private Date lastRefreshTimeNormal;

    private Date lastRefreshTimeAdvance;

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

    public List<List<Integer>> getRewardsNormal() {
        return rewardsNormal;
    }

    public void setRewardsNormal(List<List<Integer>> rewardsNormal) {
        this.rewardsNormal = rewardsNormal;
    }

    public List<List<Integer>> getRewardsAdvance() {
        return rewardsAdvance;
    }

    public void setRewardsAdvance(List<List<Integer>> rewardsAdvance) {
        this.rewardsAdvance = rewardsAdvance;
    }

    public Date getLastRefreshTimeNormal() {
        return lastRefreshTimeNormal;
    }

    public void setLastRefreshTimeNormal(Date lastRefreshTimeNormal) {
        this.lastRefreshTimeNormal = lastRefreshTimeNormal;
    }

    public Date getLastRefreshTimeAdvance() {
        return lastRefreshTimeAdvance;
    }

    public void setLastRefreshTimeAdvance(Date lastRefreshTimeAdvance) {
        this.lastRefreshTimeAdvance = lastRefreshTimeAdvance;
    }

    public List<Integer> getScoreBuyNormal() {
        return scoreBuyNormal;
    }

    public void setScoreBuyNormal(List<Integer> scoreBuyNormal) {
        this.scoreBuyNormal = scoreBuyNormal;
    }

    public List<Integer> getScoreBuyAdvance() {
        return scoreBuyAdvance;
    }

    public void setScoreBuyAdvance(List<Integer> scoreBuyAdvance) {
        this.scoreBuyAdvance = scoreBuyAdvance;
    }

    @Override
    public String toString() {
        return "SpinBean{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", rewardsNormal=" + rewardsNormal +
                ", scoreBuyNormal=" + scoreBuyNormal +
                ", rewardsAdvance=" + rewardsAdvance +
                ", scoreBuyAdvance=" + scoreBuyAdvance +
                ", lastRefreshTimeNormal=" + lastRefreshTimeNormal +
                ", lastRefreshTimeAdvance=" + lastRefreshTimeAdvance +
                '}';
    }
}
