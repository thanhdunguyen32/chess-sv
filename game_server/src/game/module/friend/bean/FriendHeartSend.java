package game.module.friend.bean;

import java.util.Date;
import java.util.Map;

public class FriendHeartSend {

    private Map<String,HeartSendItem> hearSendInfo;

    public Map<String, HeartSendItem> getHearSendInfo() {
        return hearSendInfo;
    }

    public void setHearSendInfo(Map<String, HeartSendItem> hearSendInfo) {
        this.hearSendInfo = hearSendInfo;
    }

    @Override
    public String toString() {
        return "FriendHeartSend{" +
                "hearSendInfo=" + hearSendInfo +
                '}';
    }

    public static final class HeartSendItem{
        private Date sendTime;
        private Boolean isGet;

        public Date getSendTime() {
            return sendTime;
        }

        public void setSendTime(Date sendTime) {
            this.sendTime = sendTime;
        }

        public Boolean getGet() {
            return isGet;
        }

        public void setGet(Boolean get) {
            isGet = get;
        }

        @Override
        public String toString() {
            return "HeartSendItem{" +
                    "sendTime=" + sendTime +
                    ", isGet=" + isGet +
                    '}';
        }
    }
}
