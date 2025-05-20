package game.module.friend.bean;

import game.module.manor.bean.DbBattleGeneral;
import game.module.template.RewardTemplateSimple;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FriendBoss {
    private Integer id;
    private Integer playerId;
    private DbFriendBoss dbFriendBoss;

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

    public DbFriendBoss getDbFriendBoss() {
        return dbFriendBoss;
    }

    public void setDbFriendBoss(DbFriendBoss dbFriendBoss) {
        this.dbFriendBoss = dbFriendBoss;
    }

    @Override
    public String toString() {
        return "FriendBoss{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", dbFriendBoss=" + dbFriendBoss +
                '}';
    }

    public static final class DbFriendBoss{
        private Map<Integer,Long> playerHurm;
        private Integer id;
        private Integer gsid;
        private String name;
        private Integer level;
        private List<RewardTemplateSimple> rewards;
        private Date etime;
        private Long maxhp;
        private Long nowhp;
        private Map<Integer, DbBattleGeneral> formationHeros;

        public Map<Integer, Long> getPlayerHurm() {
            return playerHurm;
        }

        public void setPlayerHurm(Map<Integer, Long> playerHurm) {
            this.playerHurm = playerHurm;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getGsid() {
            return gsid;
        }

        public void setGsid(Integer gsid) {
            this.gsid = gsid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public List<RewardTemplateSimple> getRewards() {
            return rewards;
        }

        public void setRewards(List<RewardTemplateSimple> rewards) {
            this.rewards = rewards;
        }

        public Date getEtime() {
            return etime;
        }

        public void setEtime(Date etime) {
            this.etime = etime;
        }

        public Long getMaxhp() {
            return maxhp;
        }

        public void setMaxhp(Long maxhp) {
            this.maxhp = maxhp;
        }

        public Long getNowhp() {
            return nowhp;
        }

        public void setNowhp(Long nowhp) {
            this.nowhp = nowhp;
        }

        public Map<Integer, DbBattleGeneral> getFormationHeros() {
            return formationHeros;
        }

        public void setFormationHeros(Map<Integer, DbBattleGeneral> formationHeros) {
            this.formationHeros = formationHeros;
        }

        @Override
        public String toString() {
            return "DbFriendBoss{" +
                    "playerHurm=" + playerHurm +
                    ", id=" + id +
                    ", gsid=" + gsid +
                    ", name='" + name + '\'' +
                    ", level=" + level +
                    ", rewards=" + rewards +
                    ", etime=" + etime +
                    ", maxhp=" + maxhp +
                    ", nowhp=" + nowhp +
                    ", formationHeros=" + formationHeros +
                    '}';
        }
    }

}
