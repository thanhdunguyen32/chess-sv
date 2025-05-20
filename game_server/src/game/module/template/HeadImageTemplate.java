package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbHeadImage.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadImageTemplate {

    @JsonProperty("IMAGEID")
    private Integer IMAGEID;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("TIMELIMIT")
    private Integer TIMELIMIT;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("TYPE")
    private Integer TYPE;

    @Override
    public String toString() {
        return "HeadImageTemplate{" +
                "IMAGEID=" + IMAGEID +
                ", NAME='" + NAME + '\'' +
                ", TIMELIMIT=" + TIMELIMIT +
                ", STAR=" + STAR +
                ", TYPE=" + TYPE +
                '}';
    }

    public Integer getIMAGEID() {
        return IMAGEID;
    }

    public void setIMAGEID(Integer IMAGEID) {
        this.IMAGEID = IMAGEID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getTIMELIMIT() {
        return TIMELIMIT;
    }

    public void setTIMELIMIT(Integer TIMELIMIT) {
        this.TIMELIMIT = TIMELIMIT;
    }

    public Integer getSTAR() {
        return STAR;
    }

    public void setSTAR(Integer STAR) {
        this.STAR = STAR;
    }

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }
}
