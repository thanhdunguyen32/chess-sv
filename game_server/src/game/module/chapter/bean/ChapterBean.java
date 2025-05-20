package game.module.chapter.bean;

import java.util.Date;

public class ChapterBean {

    private Integer id;
    private Integer playerId;
    private Integer maxMapId;
    private Date lastGainTime;

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

    public Integer getMaxMapId() {
        return maxMapId;
    }

    public void setMaxMapId(Integer maxMapId) {
        this.maxMapId = maxMapId;
    }

    public Date getLastGainTime() {
        return lastGainTime;
    }

    public void setLastGainTime(Date lastGainTime) {
        this.lastGainTime = lastGainTime;
    }
}
