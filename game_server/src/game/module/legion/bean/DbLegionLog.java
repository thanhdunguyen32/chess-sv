package game.module.legion.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DbLegionLog {
    private List<LegionLog> logs;

    public List<LegionLog> getLogs() {
        return logs;
    }

    public void setLogs(List<LegionLog> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "DbLegionLog{" +
                "logs=" + logs +
                '}';
    }

    public static final class LegionLog {
        private List<String> params;
        private String event;
        private Date ceateTime;

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public Date getCeateTime() {
            return ceateTime;
        }

        public void setCeateTime(Date ceateTime) {
            this.ceateTime = ceateTime;
        }

        @Override
        public String toString() {
            return "DbLegionLog{" +
                    "params=" + params +
                    ", event='" + event + '\'' +
                    ", ceateTime=" + ceateTime +
                    '}';
        }
    }
}
