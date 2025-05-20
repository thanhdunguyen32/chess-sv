package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbAffairs.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AffairsTemplate {

    @JsonProperty("ID")
    private Integer ID;

    @JsonProperty("TITLE")
    private String TITLE;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("COST")
    private Integer COST;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public Integer getSTAR() {
        return STAR;
    }

    public void setSTAR(Integer STAR) {
        this.STAR = STAR;
    }

    public Integer getCOST() {
        return COST;
    }

    public void setCOST(Integer COST) {
        this.COST = COST;
    }

    @Override
    public String toString() {
        return "AffairsTemplate{" +
                "ID=" + ID +
                ", TITLE='" + TITLE + '\'' +
                ", STAR=" + STAR +
                ", COST=" + COST +
                '}';
    }
}
