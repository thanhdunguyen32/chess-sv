package game.module.mission.bean;

import java.util.Date;
import java.util.Map;

public class MissionDaily {

    private Integer id;
    private Integer playerId;
    private Date updateTime;

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MissionDaily{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", updateTime=" + updateTime +
                '}';
    }
}
