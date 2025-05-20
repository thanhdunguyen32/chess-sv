package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.Arrays;
import java.util.List;

@ExcelTemplateAnn(file = "dbBigConfig.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BigConfigTemplate {

    @JsonProperty("TYPE")
    private Integer TYPE;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("MAP")
    private int[] MAP;

    @JsonProperty("PASSID")
    private Integer PASSID;

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int[] getMAP() {
        return MAP;
    }

    public void setMAP(int[] MAP) {
        this.MAP = MAP;
    }

    public Integer getPASSID() {
        return PASSID;
    }

    public void setPASSID(Integer PASSID) {
        this.PASSID = PASSID;
    }

    @Override
    public String toString() {
        return "BigConfigTemplate{" +
                "TYPE=" + TYPE +
                ", NAME='" + NAME + '\'' +
                ", MAP=" + Arrays.toString(MAP) +
                ", PASSID=" + PASSID +
                '}';
    }
}
