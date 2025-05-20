package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbLegionMission.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegionMissionTemplate {

    @JsonProperty("ID")
    private Integer ID;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("TIMEMIN")
    private Integer TIMEMIN;

    @JsonProperty("YB")
    private Integer YB;

    @JsonProperty("ITEMS")
    private List<RewardTemplateRange> ITEMS;

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

    public Integer getSTAR() {
        return STAR;
    }

    public void setSTAR(Integer STAR) {
        this.STAR = STAR;
    }

    public Integer getTIMEMIN() {
        return TIMEMIN;
    }

    public void setTIMEMIN(Integer TIMEMIN) {
        this.TIMEMIN = TIMEMIN;
    }

    public List<RewardTemplateRange> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateRange> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public Integer getYB() {
        return YB;
    }

    public void setYB(Integer YB) {
        this.YB = YB;
    }

    @Override
    public String toString() {
        return "LegionMissionTemplate{" +
                "ID=" + ID +
                ", NAME='" + NAME + '\'' +
                ", STAR=" + STAR +
                ", TIMEMIN=" + TIMEMIN +
                ", YB=" + YB +
                ", ITEMS=" + ITEMS +
                '}';
    }
}
