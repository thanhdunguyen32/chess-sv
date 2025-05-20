package game.module.activity.bean;

import java.util.Date;

public class ZhdBean {
    private String zhdName;
    private Date startTime;
    private Date endTime;

    public ZhdBean(String zhdName, Date startTime, Date endTime) {
        this.zhdName = zhdName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getZhdName() {
        return zhdName;
    }

    public void setZhdName(String zhdName) {
        this.zhdName = zhdName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
