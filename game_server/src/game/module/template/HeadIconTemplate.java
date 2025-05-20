package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbHeadIcon.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadIconTemplate {

    @JsonProperty("HEADID")
    private Integer HEADID;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("TIMELIMIT")
    private Integer TIMELIMIT;


    @Override
    public String toString() {
        return "HeadIconTemplate{" +
                "HEADID=" + HEADID +
                ", NAME='" + NAME + '\'' +
                ", TIMELIMIT=" + TIMELIMIT +
                '}';
    }

    public Integer getHEADID() {
        return HEADID;
    }

    public void setHEADID(Integer HEADID) {
        this.HEADID = HEADID;
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
}
