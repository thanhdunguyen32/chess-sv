package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbCity.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityTemplate {

    private Integer id;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("CAMPID")
    private Integer CAMPID;

    @JsonProperty("GENERAL")
    private Integer GENERAL;

    @JsonProperty("EID")
    private List<Integer> EID;

    @JsonProperty("MAP")
    private List<Integer> MAP;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getCAMPID() {
        return CAMPID;
    }

    public void setCAMPID(Integer CAMPID) {
        this.CAMPID = CAMPID;
    }

    public Integer getGENERAL() {
        return GENERAL;
    }

    public void setGENERAL(Integer GENERAL) {
        this.GENERAL = GENERAL;
    }

    public List<Integer> getEID() {
        return EID;
    }

    public void setEID(List<Integer> EID) {
        this.EID = EID;
    }

    public List<Integer> getMAP() {
        return MAP;
    }

    public void setMAP(List<Integer> MAP) {
        this.MAP = MAP;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CityTemplate{" +
                "id=" + id +
                ", NAME='" + NAME + '\'' +
                ", CAMPID=" + CAMPID +
                ", GENERAL=" + GENERAL +
                ", EID=" + EID +
                ", MAP=" + MAP +
                '}';
    }
}
