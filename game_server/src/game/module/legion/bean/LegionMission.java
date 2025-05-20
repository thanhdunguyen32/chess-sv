package game.module.legion.bean;

import java.util.Date;

public class LegionMission {
    private Integer id;
    private Date endTime;
    private Date startTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "LegionMission{" +
                "id=" + id +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                '}';
    }
}
