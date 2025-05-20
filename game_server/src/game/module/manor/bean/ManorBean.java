package game.module.manor.bean;

import game.module.template.RewardTemplateSimple;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ManorBean {

    private Integer id;
    private Integer playerId;
    private Integer level;
    private DbManorBuilding manorBuilding;
    private Date gainFoodTime;
    private DbManorField manorField;
    private Integer tmpFriendId;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public DbManorBuilding getManorBuilding() {
        return manorBuilding;
    }

    public void setManorBuilding(DbManorBuilding manorBuilding) {
        this.manorBuilding = manorBuilding;
    }

    public Date getGainFoodTime() {
        return gainFoodTime;
    }

    public void setGainFoodTime(Date gainFoodTime) {
        this.gainFoodTime = gainFoodTime;
    }

    public Integer getTmpFriendId() {
        return tmpFriendId;
    }

    public void setTmpFriendId(Integer tmpFriendId) {
        this.tmpFriendId = tmpFriendId;
    }

    public DbManorField getManorField() {
        return manorField;
    }

    public void setManorField(DbManorField manorField) {
        this.manorField = manorField;
    }

    @Override
    public String toString() {
        return "ManorBean{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", level=" + level +
                ", manorBuilding=" + manorBuilding +
                ", gainFoodTime=" + gainFoodTime +
                ", manorField=" + manorField +
                ", tmpFriendId=" + tmpFriendId +
                '}';
    }

    public static final class DbManorBuilding {
        private Map<Integer, DbManorBuilding1> buildings;

        public Map<Integer, DbManorBuilding1> getBuildings() {
            return buildings;
        }

        public void setBuildings(Map<Integer, DbManorBuilding1> buildings) {
            this.buildings = buildings;
        }

        @Override
        public String toString() {
            return "DbManorBuilding{" +
                    "buildings=" + buildings +
                    '}';
        }
    }

    public static final class DbManorField {
        private Date bossRefreshTime;
        private Date enemyRefreshTime;
        private List<RewardTemplateSimple> farmRewards;
        private Integer mop;
        private Integer bossid;
        private Integer bossState;
        private Integer bossLastDamage;
        private Integer bossMaxHp;
        private Integer bossNowHp;
        private Map<Integer, DbBattleGeneral> bossFormationHeros;
        private List<DbManorEnemy> enemys;
        private Date farmEndTime;

        public Date getBossRefreshTime() {
            return bossRefreshTime;
        }

        public void setBossRefreshTime(Date bossRefreshTime) {
            this.bossRefreshTime = bossRefreshTime;
        }

        public Date getEnemyRefreshTime() {
            return enemyRefreshTime;
        }

        public void setEnemyRefreshTime(Date enemyRefreshTime) {
            this.enemyRefreshTime = enemyRefreshTime;
        }

        public List<RewardTemplateSimple> getFarmRewards() {
            return farmRewards;
        }

        public void setFarmRewards(List<RewardTemplateSimple> farmRewards) {
            this.farmRewards = farmRewards;
        }

        public Integer getMop() {
            return mop;
        }

        public void setMop(Integer mop) {
            this.mop = mop;
        }

        public Integer getBossState() {
            return bossState;
        }

        public void setBossState(Integer bossState) {
            this.bossState = bossState;
        }

        public Integer getBossLastDamage() {
            return bossLastDamage;
        }

        public void setBossLastDamage(Integer bossLastDamage) {
            this.bossLastDamage = bossLastDamage;
        }

        public Integer getBossNowHp() {
            return bossNowHp;
        }

        public void setBossNowHp(Integer bossNowHp) {
            this.bossNowHp = bossNowHp;
        }

        public List<DbManorEnemy> getEnemys() {
            return enemys;
        }

        public void setEnemys(List<DbManorEnemy> enemys) {
            this.enemys = enemys;
        }

        public Map<Integer, DbBattleGeneral> getBossFormationHeros() {
            return bossFormationHeros;
        }

        public void setBossFormationHeros(Map<Integer, DbBattleGeneral> bossFormationHeros) {
            this.bossFormationHeros = bossFormationHeros;
        }

        public Integer getBossMaxHp() {
            return bossMaxHp;
        }

        public void setBossMaxHp(Integer bossMaxHp) {
            this.bossMaxHp = bossMaxHp;
        }

        public Integer getBossid() {
            return bossid;
        }

        public void setBossid(Integer bossid) {
            this.bossid = bossid;
        }

        public Date getFarmEndTime() {
            return farmEndTime;
        }

        public void setFarmEndTime(Date farmEndTime) {
            this.farmEndTime = farmEndTime;
        }

        @Override
        public String toString() {
            return "DbManorField{" +
                    "bossRefreshTime=" + bossRefreshTime +
                    ", enemyRefreshTime=" + enemyRefreshTime +
                    ", farmRewards=" + farmRewards +
                    ", mop=" + mop +
                    ", bossid=" + bossid +
                    ", bossState=" + bossState +
                    ", bossLastDamage=" + bossLastDamage +
                    ", bossMaxHp=" + bossMaxHp +
                    ", bossNowHp=" + bossNowHp +
                    ", bossFormationHeros=" + bossFormationHeros +
                    ", enemys=" + enemys +
                    ", farmEndTime=" + farmEndTime +
                    '}';
        }
    }

    public static final class DbManorEnemy {
        private List<RewardTemplateSimple> boxItem;
        private Integer id;
        private Map<Integer, DbBattleGeneral> formationHeros;
        private Boolean hasBoxOpen;

        public List<RewardTemplateSimple> getBoxItem() {
            return boxItem;
        }

        public void setBoxItem(List<RewardTemplateSimple> boxItem) {
            this.boxItem = boxItem;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Map<Integer, DbBattleGeneral> getFormationHeros() {
            return formationHeros;
        }

        public void setFormationHeros(Map<Integer, DbBattleGeneral> formationHeros) {
            this.formationHeros = formationHeros;
        }

        public Boolean getHasBoxOpen() {
            return hasBoxOpen;
        }

        public void setHasBoxOpen(Boolean hasBoxOpen) {
            this.hasBoxOpen = hasBoxOpen;
        }
    }

    public static final class DbManorBuilding1 {
        private Integer level;
        private Long rid;
        private Integer id;
        private Integer type;
        private Integer pos;
        private Long lastGain;

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Long getRid() {
            return rid;
        }

        public void setRid(Long rid) {
            this.rid = rid;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getPos() {
            return pos;
        }

        public void setPos(Integer pos) {
            this.pos = pos;
        }

        public Long getLastGain() {
            return lastGain;
        }

        public void setLastGain(Long lastGain) {
            this.lastGain = lastGain;
        }

        @Override
        public String toString() {
            return "DbManorBuilding{" +
                    "level=" + level +
                    ", rid=" + rid +
                    ", id=" + id +
                    ", type=" + type +
                    ", pos=" + pos +
                    ", lastGain=" + lastGain +
                    '}';
        }
    }
}
