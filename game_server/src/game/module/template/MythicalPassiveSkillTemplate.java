package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbMythicalPassiveSkill.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MythicalPassiveSkillTemplate {

    @JsonProperty("LEVEL")
    private Integer LEVEL;

    @JsonProperty("PROP")
    private List<KVTemplate> PROP;

    @JsonProperty("COST")
    private List<RewardTemplateSimple> COST;

    public Integer getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(Integer LEVEL) {
        this.LEVEL = LEVEL;
    }

    public List<KVTemplate> getPROP() {
        return PROP;
    }

    public void setPROP(List<KVTemplate> PROP) {
        this.PROP = PROP;
    }

    public List<RewardTemplateSimple> getCOST() {
        return COST;
    }

    public void setCOST(List<RewardTemplateSimple> COST) {
        this.COST = COST;
    }

    @Override
    public String toString() {
        return "MythicalPassiveSkillTemplate{" +
                "LEVEL=" + LEVEL +
                ", PROP=" + PROP +
                ", COST=" + COST +
                '}';
    }
}
