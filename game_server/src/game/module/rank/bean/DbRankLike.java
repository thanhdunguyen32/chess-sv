package game.module.rank.bean;

import java.util.Map;
import java.util.Set;

public class DbRankLike {

    private Map<Integer,Integer> beLikeInfo;

    private Map<Integer,DbLikePlayerInfo> myLikeInfo;

    public Map<Integer, Integer> getBeLikeInfo() {
        return beLikeInfo;
    }

    public void setBeLikeInfo(Map<Integer, Integer> beLikeInfo) {
        this.beLikeInfo = beLikeInfo;
    }

    public Map<Integer, DbLikePlayerInfo> getMyLikeInfo() {
        return myLikeInfo;
    }

    public void setMyLikeInfo(Map<Integer, DbLikePlayerInfo> myLikeInfo) {
        this.myLikeInfo = myLikeInfo;
    }

    @Override
    public String toString() {
        return "DbRankLike{" +
                "beLikeInfo=" + beLikeInfo +
                ", myLikeInfo=" + myLikeInfo +
                '}';
    }

    public static final class DbLikePlayerInfo{
        private Integer playerId;
        private Long lastVisitTime;
        private Set<Integer> todayLikePlayers;

        public Integer getPlayerId() {
            return playerId;
        }

        public void setPlayerId(Integer playerId) {
            this.playerId = playerId;
        }

        public Long getLastVisitTime() {
            return lastVisitTime;
        }

        public void setLastVisitTime(Long lastVisitTime) {
            this.lastVisitTime = lastVisitTime;
        }

        public Set<Integer> getTodayLikePlayers() {
            return todayLikePlayers;
        }

        public void setTodayLikePlayers(Set<Integer> todayLikePlayers) {
            this.todayLikePlayers = todayLikePlayers;
        }

        @Override
        public String toString() {
            return "DbLikePlayerInfo{" +
                    "playerId=" + playerId +
                    ", lastVisitTime=" + lastVisitTime +
                    ", todayLikePlayers=" + todayLikePlayers +
                    '}';
        }
    }

}
