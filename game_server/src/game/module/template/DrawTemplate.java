package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "dbDraw.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawTemplate {

    @JsonProperty("TYPE")
    private Integer TYPE;

    @JsonProperty("TIMES")
    private Integer TIMES;

    @JsonProperty("SCORE")
    private Integer SCORE;

    @JsonProperty("COST")
    private List<RewardTemplateSimple> COST;

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }

    public Integer getTIMES() {
        return TIMES;
    }

    public void setTIMES(Integer TIMES) {
        this.TIMES = TIMES;
    }

    public Integer getSCORE() {
        return SCORE;
    }

    public void setSCORE(Integer SCORE) {
        this.SCORE = SCORE;
    }

    public List<RewardTemplateSimple> getCOST() {
        return COST;
    }

    public void setCOST(List<RewardTemplateSimple> COST) {
        this.COST = COST;
    }

    @Override
    public String toString() {
        return "DrawTemplate{" +
                "TYPE=" + TYPE +
                ", TIMES=" + TIMES +
                ", SCORE=" + SCORE +
                ", COST=" + COST +
                '}';
    }
}
