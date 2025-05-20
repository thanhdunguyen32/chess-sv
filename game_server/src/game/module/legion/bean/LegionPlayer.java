package game.module.legion.bean;

import java.util.Date;
import java.util.Map;

public class LegionPlayer {

    private Integer playerId;

    private Integer donateSum;

    private Date lastDonateTime;

    private Date addTime;

    /**
     * 职位
     */
    private Integer pos;

    private Integer score;

    private Map<Integer,Integer> tech;

    private Map<Long,LegionMission> missions;

    private Date missionGetTime;
    // THÊM 2 trường lưu số lượt còn lại và 1 trường lưu ngày làm mới
    private int dailyDonationGold;   // số lượt còn lại cho donate bằng gold
    private int dailyDonationMoney;    // số lượt còn lại cho donate bằng money
    
    // Getters and setters cho các trường mới:
    public int getDailyDonationGold() {
        return dailyDonationGold;
    }
    public void setDailyDonationGold(int dailyDonationGold) {
        this.dailyDonationGold = dailyDonationGold;
    }
    public int getDailyDonationMoney() {
        return dailyDonationMoney;
    }
    public void setDailyDonationMoney(int dailyDonationMoney) {
        this.dailyDonationMoney = dailyDonationMoney;
    }
    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getDonateSum() {
        return donateSum;
    }

    public void setDonateSum(Integer donateSum) {
        this.donateSum = donateSum;
    }

    public Date getLastDonateTime() {
        return lastDonateTime;
    }

    public void setLastDonateTime(Date lastDonateTime) {
        this.lastDonateTime = lastDonateTime;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Map<Integer, Integer> getTech() {
        return tech;
    }

    public void setTech(Map<Integer, Integer> tech) {
        this.tech = tech;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Map<Long, LegionMission> getMissions() {
        return missions;
    }

    public void setMissions(Map<Long, LegionMission> missions) {
        this.missions = missions;
    }

    public Date getMissionGetTime() {
        return missionGetTime;
    }

    public void setMissionGetTime(Date missionGetTime) {
        this.missionGetTime = missionGetTime;
    }

    @Override
    public String toString() {
        return "LegionPlayer{" +
                "playerId=" + playerId +
                ", donateSum=" + donateSum +
                ", lastDonateTime=" + lastDonateTime +
                ", addTime=" + addTime +
                ", pos=" + pos +
                ", score=" + score +
                ", tech=" + tech +
                ", missions=" + missions +
                ", missionGetTime=" + missionGetTime +
                '}';
    }

    public static final class DbLegionPlayers{
        private Map<Integer,LegionPlayer> members;

        public Map<Integer, LegionPlayer> getMembers() {
            return members;
        }

        public void setMembers(Map<Integer, LegionPlayer> members) {
            this.members = members;
        }

        @Override
        public String toString() {
            return "DbLegionPlayers{" +
                    "members=" + members +
                    '}';
        }
    }
}
