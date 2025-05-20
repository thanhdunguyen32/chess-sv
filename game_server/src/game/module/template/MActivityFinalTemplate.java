package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "dbMActivityFinal.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MActivityFinalTemplate {

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("CHK")
    private List<Object> CHK;

    @JsonProperty("RMARK")
    private Map<String,Integer> RMARK;

    @JsonProperty("ITEMS")
    private Map<String,List<RewardTemplateSimple>> ITEMS;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public List<Object> getCHK() {
        return CHK;
    }

    public void setCHK(List<Object> CHK) {
        this.CHK = CHK;
    }

    public Map<String, Integer> getRMARK() {
        return RMARK;
    }

    public void setRMARK(Map<String, Integer> RMARK) {
        this.RMARK = RMARK;
    }

    public Map<String, List<RewardTemplateSimple>> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(Map<String, List<RewardTemplateSimple>> ITEMS) {
        this.ITEMS = ITEMS;
    }

    @Override
    public String toString() {
        return "MActivityFinalTemplate{" +
                "NAME='" + NAME + '\'' +
                ", CHK=" + CHK +
                ", RMARK=" + RMARK +
                ", ITEMS=" + ITEMS +
                '}';
    }
}
