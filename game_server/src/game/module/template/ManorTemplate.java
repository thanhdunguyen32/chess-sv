package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "dbManor.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManorTemplate {

    @JsonProperty("HOME")
    private List<ManorHomeTemplate> HOME;

    @JsonProperty("UP")
    private Map<Integer, ManorUpTemplate> UP;

    @JsonProperty("MAX")
    private Map<Integer, ManorMaxTemplate> MAX;

    @JsonProperty("ENEMY")
    private List<ManorEnemyTemplate> ENEMY;

    @JsonProperty("BOSS")
    private List<ManorEnemyTemplate> BOSS;

    @JsonProperty("FIGHT")
    private Map<Integer, Integer> FIGHT;

    public List<ManorHomeTemplate> getHOME() {
        return HOME;
    }

    public void setHOME(List<ManorHomeTemplate> HOME) {
        this.HOME = HOME;
    }

    public Map<Integer, ManorUpTemplate> getUP() {
        return UP;
    }

    public void setUP(Map<Integer, ManorUpTemplate> UP) {
        this.UP = UP;
    }

    public Map<Integer, ManorMaxTemplate> getMAX() {
        return MAX;
    }

    public void setMAX(Map<Integer, ManorMaxTemplate> MAX) {
        this.MAX = MAX;
    }

    public List<ManorEnemyTemplate> getENEMY() {
        return ENEMY;
    }

    public void setENEMY(List<ManorEnemyTemplate> ENEMY) {
        this.ENEMY = ENEMY;
    }

    public List<ManorEnemyTemplate> getBOSS() {
        return BOSS;
    }

    public void setBOSS(List<ManorEnemyTemplate> BOSS) {
        this.BOSS = BOSS;
    }

    public Map<Integer, Integer> getFIGHT() {
        return FIGHT;
    }

    public void setFIGHT(Map<Integer, Integer> FIGHT) {
        this.FIGHT = FIGHT;
    }

    @Override
    public String toString() {
        return "ManorTemplate{" +
                "HOME=" + HOME +
                ", UP=" + UP +
                ", MAX=" + MAX +
                ", ENEMY=" + ENEMY +
                ", BOSS=" + BOSS +
                ", FIGHT=" + FIGHT +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ManorHomeTemplate {
        @JsonProperty("LV")
        private Integer LV;

        @JsonProperty("COUNT")
        private Integer COUNT;

        @JsonProperty("UPMAX")
        private Integer UPMAX;

        @JsonProperty("UPITEM")
        private List<RewardTemplateSimple> UPITEM;

        @JsonProperty("POS")
        private Map<Integer, List<Integer>> POS;

        public Integer getLV() {
            return LV;
        }

        public void setLV(Integer LV) {
            this.LV = LV;
        }

        public Integer getCOUNT() {
            return COUNT;
        }

        public void setCOUNT(Integer COUNT) {
            this.COUNT = COUNT;
        }

        public Integer getUPMAX() {
            return UPMAX;
        }

        public void setUPMAX(Integer UPMAX) {
            this.UPMAX = UPMAX;
        }

        public List<RewardTemplateSimple> getUPITEM() {
            return UPITEM;
        }

        public void setUPITEM(List<RewardTemplateSimple> UPITEM) {
            this.UPITEM = UPITEM;
        }

        public Map<Integer, List<Integer>> getPOS() {
            return POS;
        }

        public void setPOS(Map<Integer, List<Integer>> POS) {
            this.POS = POS;
        }

        @Override
        public String toString() {
            return "ManorHomeTemplate{" +
                    "LV=" + LV +
                    ", COUNT=" + COUNT +
                    ", UPMAX=" + UPMAX +
                    ", UPITEM=" + UPITEM +
                    ", POS=" + POS +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ManorUpTemplate {
        @JsonProperty("NAME")
        private String NAME;

        @JsonProperty("INFO")
        private List<ManorUpCostTemplate> INFO;

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public List<ManorUpCostTemplate> getINFO() {
            return INFO;
        }

        public void setINFO(List<ManorUpCostTemplate> INFO) {
            this.INFO = INFO;
        }

        @Override
        public String toString() {
            return "ManorUpTemplate{" +
                    "NAME='" + NAME + '\'' +
                    ", INFO=" + INFO +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ManorUpCostTemplate {

        @JsonProperty("COST")
        private List<RewardTemplateSimple> COST;

        @JsonProperty("ATT")
        private List<IdNumTemplate> ATT;

        @JsonProperty("RES")
        private List<ManorResTemplate> RES;

        public List<IdNumTemplate> getATT() {
            return ATT;
        }

        public void setATT(List<IdNumTemplate> ATT) {
            this.ATT = ATT;
        }

        public List<RewardTemplateSimple> getCOST() {
            return COST;
        }

        public void setCOST(List<RewardTemplateSimple> COST) {
            this.COST = COST;
        }

        public List<ManorResTemplate> getRES() {
            return RES;
        }

        public void setRES(List<ManorResTemplate> RES) {
            this.RES = RES;
        }

        @Override
        public String toString() {
            return "ManorUpCostTemplate{" +
                    "COST=" + COST +
                    ", ATT=" + ATT +
                    ", RES=" + RES +
                    '}';
        }
    }

    public static final class ManorResTemplate{
        @JsonProperty("ID")
        private Integer ID;

        @JsonProperty("TIME")
        private Integer TIME;

        @JsonProperty("COUNT")
        private Integer COUNT;

        @JsonProperty("MAX")
        private Integer MAX;

        public Integer getID() {
            return ID;
        }

        public void setID(Integer ID) {
            this.ID = ID;
        }

        public Integer getTIME() {
            return TIME;
        }

        public void setTIME(Integer TIME) {
            this.TIME = TIME;
        }

        public Integer getCOUNT() {
            return COUNT;
        }

        public void setCOUNT(Integer COUNT) {
            this.COUNT = COUNT;
        }

        public Integer getMAX() {
            return MAX;
        }

        public void setMAX(Integer MAX) {
            this.MAX = MAX;
        }

        @Override
        public String toString() {
            return "ManorResTemplate{" +
                    "ID=" + ID +
                    ", TIME=" + TIME +
                    ", COUNT=" + COUNT +
                    ", MAX=" + MAX +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ManorMaxTemplate {

        @JsonProperty("TYPE")
        private Integer TYPE;

        @JsonProperty("MAX")
        private Integer MAX;

        @JsonProperty("NAME")
        private String NAME;

        public Integer getTYPE() {
            return TYPE;
        }

        public void setTYPE(Integer TYPE) {
            this.TYPE = TYPE;
        }

        public Integer getMAX() {
            return MAX;
        }

        public void setMAX(Integer MAX) {
            this.MAX = MAX;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        @Override
        public String toString() {
            return "ManorMaxTemplate{" +
                    "TYPE=" + TYPE +
                    ", MAX=" + MAX +
                    ", NAME='" + NAME + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ManorEnemyTemplate {
        @JsonProperty("LV")
        private Integer LV;

        @JsonProperty("REWARD")
        private List<RewardTemplateSimple> REWARD;

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

        @Override
        public String toString() {
            return "ManorEnemyTemplate{" +
                    "LV=" + LV +
                    ", REWARD=" + REWARD +
                    '}';
        }
    }

    public static final class IdNumTemplate{
        @JsonProperty("ID")
        private String ID;
        @JsonProperty("NUM")
        private Integer NUM;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public Integer getNUM() {
            return NUM;
        }

        public void setNUM(Integer NUM) {
            this.NUM = NUM;
        }

        @Override
        public String toString() {
            return "IdNumTemplate{" +
                    "ID=" + ID +
                    ", NUM=" + NUM +
                    '}';
        }
    }

}
