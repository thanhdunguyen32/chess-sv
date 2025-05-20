package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbExclusive.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExclusiveTemplate {

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("QUALITY")
    private Integer QUALITY;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("PARR")
    private List<Integer> PARR;

    @JsonProperty("SARR")
    private List<Integer> SARR;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getQUALITY() {
        return QUALITY;
    }

    public void setQUALITY(Integer QUALITY) {
        this.QUALITY = QUALITY;
    }

    public Integer getSTAR() {
        return STAR;
    }

    public void setSTAR(Integer STAR) {
        this.STAR = STAR;
    }

    public List<Integer> getPARR() {
        return PARR;
    }

    public void setPARR(List<Integer> PARR) {
        this.PARR = PARR;
    }

    public List<Integer> getSARR() {
        return SARR;
    }

    public void setSARR(List<Integer> SARR) {
        this.SARR = SARR;
    }

    @Override
    public String toString() {
        return "ExclusiveTemplate{" +
                "NAME='" + NAME + '\'' +
                ", QUALITY=" + QUALITY +
                ", STAR=" + STAR +
                ", PARR=" + PARR +
                ", SARR=" + SARR +
                '}';
    }
}
