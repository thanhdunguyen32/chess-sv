package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbGold.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldTemplate {

    @JsonProperty("REFRESH")
    private List<Integer> REFRESH;

    @JsonProperty("INFO")
    private List<GoldTemplateInfo> INFO;

    public List<Integer> getREFRESH() {
        return REFRESH;
    }

    public void setREFRESH(List<Integer> REFRESH) {
        this.REFRESH = REFRESH;
    }

    public List<GoldTemplateInfo> getINFO() {
        return INFO;
    }

    public void setINFO(List<GoldTemplateInfo> INFO) {
        this.INFO = INFO;
    }

    @Override
    public String toString() {
        return "GoldTemplate{" +
                "REFRESH=" + REFRESH +
                ", INFO=" + INFO +
                '}';
    }

    public static final class GoldTemplateInfo{
        @JsonProperty("BASE")
        private Integer BASE;

        @JsonProperty("ADD")
        private Integer ADD;

        @JsonProperty("COST")
        private Integer COST;

        @JsonProperty("MARK")
        private Integer MARK;

        @JsonProperty("LIMIT")
        private Integer LIMIT;

        public Integer getBASE() {
            return BASE;
        }

        public void setBASE(Integer BASE) {
            this.BASE = BASE;
        }

        public Integer getADD() {
            return ADD;
        }

        public void setADD(Integer ADD) {
            this.ADD = ADD;
        }

        public Integer getCOST() {
            return COST;
        }

        public void setCOST(Integer COST) {
            this.COST = COST;
        }

        public Integer getMARK() {
            return MARK;
        }

        public void setMARK(Integer MARK) {
            this.MARK = MARK;
        }

        public Integer getLIMIT() {
            return LIMIT;
        }

        public void setLIMIT(Integer LIMIT) {
            this.LIMIT = LIMIT;
        }

        @Override
        public String toString() {
            return "GoldTemplateInfo{" +
                    "BASE=" + BASE +
                    ", ADD=" + ADD +
                    ", COST=" + COST +
                    ", MARK=" + MARK +
                    ", LIMIT=" + LIMIT +
                    '}';
        }
    }
}
