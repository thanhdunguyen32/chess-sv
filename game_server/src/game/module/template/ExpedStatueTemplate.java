package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbExpedstatue.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpedStatueTemplate {

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("VAL")
    private Integer VAL;

    @JsonProperty("MAX")
    private Integer MAX;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getVAL() {
        return VAL;
    }

    public void setVAL(Integer VAL) {
        this.VAL = VAL;
    }

    public Integer getMAX() {
        return MAX;
    }

    public void setMAX(Integer MAX) {
        this.MAX = MAX;
    }

    @Override
    public String toString() {
        return "ExpedStatueTemplate{" +
                "NAME='" + NAME + '\'' +
                ", VAL=" + VAL +
                ", MAX=" + MAX +
                '}';
    }
}
