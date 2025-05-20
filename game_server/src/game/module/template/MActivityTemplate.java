package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbMActivityAffairs.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MActivityTemplate {

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("PROCESS")
    private Integer PROCESS;

    @JsonProperty("NUM")
    private Integer NUM;

    @JsonProperty("RMARK")
    private Integer RMARK;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("ITEMS")
    private List<RewardTemplateSimple> ITEMS;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getPROCESS() {
        return PROCESS;
    }

    public void setPROCESS(Integer PROCESS) {
        this.PROCESS = PROCESS;
    }

    public Integer getNUM() {
        return NUM;
    }

    public void setNUM(Integer NUM) {
        this.NUM = NUM;
    }

    public Integer getRMARK() {
        return RMARK;
    }

    public void setRMARK(Integer RMARK) {
        this.RMARK = RMARK;
    }

    public List<RewardTemplateSimple> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateSimple> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public Integer getSTAR() {
        return STAR;
    }

    public void setSTAR(Integer STAR) {
        this.STAR = STAR;
    }

    @Override
    public String toString() {
        return "MActivityTemplate{" +
                "NAME='" + NAME + '\'' +
                ", PROCESS=" + PROCESS +
                ", NUM=" + NUM +
                ", RMARK=" + RMARK +
                ", STAR=" + STAR +
                ", ITEMS=" + ITEMS +
                '}';
    }
}
