package game.module.chat.bean;

import java.util.List;
import java.util.Map;

public class DbChat {
    private Map<Integer,Long> publicVisit;
    private Map<Integer,Long> legionVisit;
    private List<DbChatItem> chatPublic;
    private Map<Long,DbChatLegion> chatLegion;

    public Map<Integer, Long> getPublicVisit() {
        return publicVisit;
    }

    public void setPublicVisit(Map<Integer, Long> publicVisit) {
        this.publicVisit = publicVisit;
    }

    public Map<Integer, Long> getLegionVisit() {
        return legionVisit;
    }

    public void setLegionVisit(Map<Integer, Long> legionVisit) {
        this.legionVisit = legionVisit;
    }

    public List<DbChatItem> getChatPublic() {
        return chatPublic;
    }

    public void setChatPublic(List<DbChatItem> chatPublic) {
        this.chatPublic = chatPublic;
    }
    
    public Map<Long, DbChatLegion> getChatLegion() {
        return chatLegion;
    }
    public void setChatLegion(Map<Long, DbChatLegion> chatLegion) {
        this.chatLegion = chatLegion;
    }

    @Override
    public String toString() {
        return "DbChat{" +
                "publicVisit=" + publicVisit +
                ", legionVisit=" + legionVisit +
                ", chatPublic=" + chatPublic +
                ", chatLegion=" + chatLegion +
                '}';
    }

    public static final class DbChatLegion{
        private List<DbChatItem> chatItem;

        public List<DbChatItem> getChatItem() {
            return chatItem;
        }

        public void setChatItem(List<DbChatItem> chatItem) {
            this.chatItem = chatItem;
        }

        @Override
        public String toString() {
            return "DbChatLegion{" +
                    "chatItem=" + chatItem +
                    '}';
        }
    }

    public static final class DbChatItem{
        private Integer msgtype;
        private String senderid;
        private Integer rid;
        private String rname;
        private Integer iconid;
        private Integer headid;
        private Integer frameid;
        private Integer level;
        private Integer serverid;
        private Integer vip;
        private String content;
        private Long videoId;
        private Long send_time;
        private Integer id;

        @Override
        public String toString() {
            return "DbChatItem{" +
                    "msgtype=" + msgtype +
                    ", senderid='" + senderid + '\'' +
                    ", rid=" + rid +
                    ", rname='" + rname + '\'' +
                    ", iconid=" + iconid +
                    ", headid=" + headid +
                    ", frameid=" + frameid +
                    ", level=" + level +
                    ", serverid=" + serverid +
                    ", vip=" + vip +
                    ", content='" + content + '\'' +
                    ", videoId=" + videoId +
                    ", send_time=" + send_time +
                    ", id=" + id +
                    '}';
        }

        public Integer getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(Integer msgtype) {
            this.msgtype = msgtype;
        }

        public String getSenderid() {
            return senderid;
        }

        public void setSenderid(String senderid) {
            this.senderid = senderid;
        }

        public Integer getRid() {
            return rid;
        }

        public void setRid(Integer rid) {
            this.rid = rid;
        }

        public String getRname() {
            return rname;
        }

        public void setRname(String rname) {
            this.rname = rname;
        }

        public Integer getIconid() {
            return iconid;
        }

        public void setIconid(Integer iconid) {
            this.iconid = iconid;
        }

        public Integer getHeadid() {
            return headid;
        }

        public void setHeadid(Integer headid) {
            this.headid = headid;
        }

        public Integer getFrameid() {
            return frameid;
        }

        public void setFrameid(Integer frameid) {
            this.frameid = frameid;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Integer getVip() {
            return vip;
        }

        public void setVip(Integer vip) {
            this.vip = vip;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getVideoId() {
            return videoId;
        }

        public void setVideoId(Long videoId) {
            this.videoId = videoId;
        }

        public Long getSend_time() {
            return send_time;
        }

        public void setSend_time(Long send_time) {
            this.send_time = send_time;
        }

        public Integer getServerid() {
            return serverid;
        }

        public void setServerid(Integer serverid) {
            this.serverid = serverid;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
