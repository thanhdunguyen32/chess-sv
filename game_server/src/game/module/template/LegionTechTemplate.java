package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "dbLegionTech.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegionTechTemplate {

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("OCCU")
    private Integer OCCU;

    @JsonProperty("COST")
    private LegionTechTemplateCost COST;

    @JsonProperty("PRE")
    private LegionTechTemplatePre PRE;

    @JsonProperty("ATTR")
    private Map<Integer, List<Integer>> ATTR;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getOCCU() {
        return OCCU;
    }

    public void setOCCU(Integer OCCU) {
        this.OCCU = OCCU;
    }

    public LegionTechTemplateCost getCOST() {
        return COST;
    }

    public void setCOST(LegionTechTemplateCost COST) {
        this.COST = COST;
    }

    public Map<Integer, List<Integer>> getATTR() {
        return ATTR;
    }

    public void setATTR(Map<Integer, List<Integer>> ATTR) {
        this.ATTR = ATTR;
    }

    public LegionTechTemplatePre getPRE() {
        return PRE;
    }

    public void setPRE(LegionTechTemplatePre PRE) {
        this.PRE = PRE;
    }

    @Override
    public String toString() {
        return "LegionTechTemplate{" +
                "NAME='" + NAME + '\'' +
                ", OCCU=" + OCCU +
                ", COST=" + COST +
                ", PRE=" + PRE +
                ", ATTR=" + ATTR +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class LegionTechTemplateCost {
        @JsonProperty("30020")
        private List<Integer> LegionCoin;
        @JsonProperty("38006")
        private List<Integer> goldCoin;

        public List<Integer> getLegionCoin() {
            return LegionCoin;
        }

        public void setLegionCoin(List<Integer> legionCoin) {
            LegionCoin = legionCoin;
        }

        public List<Integer> getGoldCoin() {
            return goldCoin;
        }

        public void setGoldCoin(List<Integer> goldCoin) {
            this.goldCoin = goldCoin;
        }

        @Override
        public String toString() {
            return "LegionTechTemplateCost{" +
                    "LegionCoin=" + LegionCoin +
                    ", goldCoin=" + goldCoin +
                    '}';
        }
    }

    public static final class LegionTechTemplatePre{
        @JsonProperty("ID")
        private Integer ID;
        @JsonProperty("LV")
        private Integer LV;

        public Integer getID() {
            return ID;
        }

        public void setID(Integer ID) {
            this.ID = ID;
        }

        public Integer getLV() {
            return LV;
        }

        public void setLV(Integer LV) {
            this.LV = LV;
        }

        @Override
        public String toString() {
            return "LegionTechTemplatePre{" +
                    "ID=" + ID +
                    ", LV=" + LV +
                    '}';
        }
    }

}
