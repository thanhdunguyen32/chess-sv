package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbMythicalActiveSkill.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MythicalActiveSkillTemplate {

    @JsonProperty("LEVEL")
    private Integer LEVEL;

    private Integer VALUE;

    public Integer getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(Integer LEVEL) {
        this.LEVEL = LEVEL;
    }

    public Integer getVALUE() {
        return VALUE;
    }

    public void setVALUE(Integer VALUE) {
        this.VALUE = VALUE;
    }

    @Override
    public String toString() {
        return "MythicalActiveSkillTemplate{" +
                "LEVEL=" + LEVEL +
                ", VALUE=" + VALUE +
                '}';
    }
}
