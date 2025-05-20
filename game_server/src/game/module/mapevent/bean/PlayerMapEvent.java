package game.module.mapevent.bean;

import java.util.Date;

public class PlayerMapEvent {

    private Integer id;
    private Integer playerId;
    private Date lastGenerateTime;
    private MapEvent.DBMapEvent dbMapEvent;

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

    public Date getLastGenerateTime() {
        return lastGenerateTime;
    }

    public void setLastGenerateTime(Date lastGenerateTime) {
        this.lastGenerateTime = lastGenerateTime;
    }

    public MapEvent.DBMapEvent getDbMapEvent() {
        return dbMapEvent;
    }

    public void setDbMapEvent(MapEvent.DBMapEvent dbMapEvent) {
        this.dbMapEvent = dbMapEvent;
    }

    @Override
    public String toString() {
        return "PlayerMapEvent{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", lastGenerateTime=" + lastGenerateTime +
                ", dbMapEvent=" + dbMapEvent +
                '}';
    }
}
