package cc.mrbird.febs.system.entity;

public class UserInfo {
    public static final class UserInfoRequest {
        private String username;
        private Integer serverID;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Integer getServerID() {
            return serverID;
        }

        public void setServerID(Integer serverID) {
            this.serverID = serverID;
        }

        @Override
        public String toString() {
            return "UserInfoRequest [username=" + username + ", serverID=" + serverID + "]";
        }
    }
}