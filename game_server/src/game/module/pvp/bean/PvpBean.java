package game.module.pvp.bean;

import java.util.List;
import java.util.Map;

public class PvpBean {

    private Map<Integer,Integer> pvpScore;

    private List<Integer> players;

    private Map<Integer,Integer> myRankMap;

    private Map<Integer,PvpPlayer> pvpPlayerInfo;

    public Map<Integer, Integer> getPvpScore() {
        return pvpScore;
    }

    public void setPvpScore(Map<Integer, Integer> pvpScore) {
        this.pvpScore = pvpScore;
    }

    public List<Integer> getPlayers() {
        return players;
    }

    public void setPlayers(List<Integer> players) {
        this.players = players;
    }

    public Map<Integer, Integer> getMyRankMap() {
        return myRankMap;
    }

    public void setMyRankMap(Map<Integer, Integer> myRankMap) {
        this.myRankMap = myRankMap;
    }

    public Map<Integer, PvpPlayer> getPvpPlayerInfo() {
        return pvpPlayerInfo;
    }

    public void setPvpPlayerInfo(Map<Integer, PvpPlayer> pvpPlayerInfo) {
        this.pvpPlayerInfo = pvpPlayerInfo;
    }

    @Override
    public String toString() {
        return "PvpBean{" +
                "pvpScore=" + pvpScore +
                ", players=" + players +
                ", myRankMap=" + myRankMap +
                ", pvpPlayerInfo=" + pvpPlayerInfo +
                '}';
    }
}
