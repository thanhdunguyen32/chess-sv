package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbLegionFLevel.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegionFLevelTemplate {

    @JsonProperty("EXP")
    private Integer EXP;

    @JsonProperty("MNUM")
    private Integer MNUM;

    @JsonProperty("RADD")
    private Integer RADD;

    public Integer getEXP() {
        return EXP;
    }

    public void setEXP(Integer EXP) {
        this.EXP = EXP;
    }

    public Integer getMNUM() {
        return MNUM;
    }

    public void setMNUM(Integer MNUM) {
        this.MNUM = MNUM;
    }

    public Integer getRADD() {
        return RADD;
    }

    public void setRADD(Integer RADD) {
        this.RADD = RADD;
    }

    @Override
    public String toString() {
        return "LegionFLevelTemplate{" +
                "EXP=" + EXP +
                ", MNUM=" + MNUM +
                ", RADD=" + RADD +
                '}';
    }
}
