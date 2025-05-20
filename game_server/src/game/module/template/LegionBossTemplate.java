package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbLegionBoss.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegionBossTemplate {

    @JsonProperty("BOSS")
    private List<LegionBoss1> BOSS;

    @JsonProperty("RANK")
    private List<LegionBossRank> RANK;

    @JsonProperty("COST")
    private List<LegionBossCost> COST;

    public List<LegionBoss1> getBOSS() {
        return BOSS;
    }

    public void setBOSS(List<LegionBoss1> BOSS) {
        this.BOSS = BOSS;
    }

    public List<LegionBossRank> getRANK() {
        return RANK;
    }

    public void setRANK(List<LegionBossRank> RANK) {
        this.RANK = RANK;
    }

    public List<LegionBossCost> getCOST() {
        return COST;
    }

    public void setCOST(List<LegionBossCost> COST) {
        this.COST = COST;
    }

    @Override
    public String toString() {
        return "LegionBossTemplate{" +
                "BOSS=" + BOSS +
                ", RANK=" + RANK +
                ", COST=" + COST +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class LegionBoss1 {

        @JsonProperty("ID")
        private Integer ID;

        @JsonProperty("NAME")
        private String NAME;

        @JsonProperty("ICON")
        private Integer ICON;

        @JsonProperty("LV")
        private Integer LV;

        @JsonProperty("REWARD")
        private List<RewardTemplateSimple> REWARD;

        @JsonProperty("BOSSHP")
        private Long BOSSHP;

        @JsonProperty("KREWARD")
        private List<RewardTemplateSimple> KREWARD;

        public Integer getID() {
            return ID;
        }

        public void setID(Integer ID) {
            this.ID = ID;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public Integer getICON() {
            return ICON;
        }

        public void setICON(Integer ICON) {
            this.ICON = ICON;
        }

        public Integer getLV() {
            return LV;
        }

        public void setLV(Integer LV) {
            this.LV = LV;
        }

        public List<RewardTemplateSimple> getREWARD() {
            return REWARD;
        }

        public void setREWARD(List<RewardTemplateSimple> REWARD) {
            this.REWARD = REWARD;
        }

        public Long getBOSSHP() {
            return BOSSHP;
        }

        public void setBOSSHP(Long BOSSHP) {
            this.BOSSHP = BOSSHP;
        }

        public List<RewardTemplateSimple> getKREWARD() {
            return KREWARD;
        }

        public void setKREWARD(List<RewardTemplateSimple> KREWARD) {
            this.KREWARD = KREWARD;
        }

        @Override
        public String toString() {
            return "LeginBoss1{" +
                    "ID=" + ID +
                    ", NAME='" + NAME + '\'' +
                    ", ICON=" + ICON +
                    ", LV=" + LV +
                    ", REWARD=" + REWARD +
                    ", BOSSHP=" + BOSSHP +
                    ", KREWARD=" + KREWARD +
                    '}';
        }
    }

    public static final class LegionBossRank{
        @JsonProperty("MAX")
        private Integer MAX;

        @JsonProperty("MIN")
        private Integer MIN;

        @JsonProperty("RAND")
        private Integer RAND;

        public Integer getMAX() {
            return MAX;
        }

        public void setMAX(Integer MAX) {
            this.MAX = MAX;
        }

        public Integer getMIN() {
            return MIN;
        }

        public void setMIN(Integer MIN) {
            this.MIN = MIN;
        }

        public Integer getRAND() {
            return RAND;
        }

        public void setRAND(Integer RAND) {
            this.RAND = RAND;
        }

        @Override
        public String toString() {
            return "LegionBossRank{" +
                    "MAX=" + MAX +
                    ", MIN=" + MIN +
                    ", RAND=" + RAND +
                    '}';
        }
    }

    public static final class LegionBossCost{
        @JsonProperty("NUM")
        private Integer NUM;

        @JsonProperty("COST")
        private Integer COST;

        public Integer getNUM() {
            return NUM;
        }

        public void setNUM(Integer NUM) {
            this.NUM = NUM;
        }

        public Integer getCOST() {
            return COST;
        }

        public void setCOST(Integer COST) {
            this.COST = COST;
        }

        @Override
        public String toString() {
            return "LegionBossCost{" +
                    "NUM=" + NUM +
                    ", COST=" + COST +
                    '}';
        }
    }
}
