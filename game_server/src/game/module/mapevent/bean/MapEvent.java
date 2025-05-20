package game.module.mapevent.bean;

import java.util.Date;
import java.util.Map;

public class MapEvent {
    private Integer cityId;
    private Integer type;
    private Integer eid;
    private Date endTime;

    public MapEvent(Integer cityId, Integer type, Integer eid, Date endTime) {
        this.cityId = cityId;
        this.type = type;
        this.eid = eid;
        this.endTime = endTime;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "MapEvent{" +
                ", cityId=" + cityId +
                ", type=" + type +
                ", eid=" + eid +
                ", endTime=" + endTime +
                '}';
    }

    public static final class DBMapEvent {
        private Map<Integer, MapEvent> events;

        public Map<Integer, MapEvent> getEvents() {
            return events;
        }

        public void setEvents(Map<Integer, MapEvent> events) {
            this.events = events;
        }

        @Override
        public String toString() {
            return "DBMapEvent{" +
                    "events=" + events +
                    '}';
        }
    }
}
