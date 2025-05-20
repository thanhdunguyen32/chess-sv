package game.module.affair.bean;

import game.module.template.RewardTemplateSimple;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class AffairBean {

    private Long id;
    private Integer templateId;
    private Integer status;
    private List<Integer> cond;
    private RewardTemplateSimple reward;
    private Integer isLock;
    private Date startTime;
    private List<Long> onlineGenerals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<Integer> getCond() {
        return cond;
    }

    public void setCond(List<Integer> cond) {
        this.cond = cond;
    }

    public RewardTemplateSimple getReward() {
        return reward;
    }

    public void setReward(RewardTemplateSimple reward) {
        this.reward = reward;
    }

    public Integer getLock() {
        return isLock;
    }

    public void setLock(Integer lock) {
        isLock = lock;
    }

    public List<Long> getOnlineGenerals() {
        return onlineGenerals;
    }

    public void setOnlineGenerals(List<Long> onlineGenerals) {
        this.onlineGenerals = onlineGenerals;
    }

    @Override
    public String toString() {
        return "AffairBean{" +
                "id=" + id +
                ", templateId=" + templateId +
                ", status=" + status +
                ", cond=" + cond +
                ", reward=" + reward +
                ", isLock=" + isLock +
                ", startTime=" + startTime +
                ", onlineGenerals=" + onlineGenerals +
                '}';
    }
}
