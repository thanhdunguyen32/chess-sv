package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbExclusiveReset.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExclusiveResetTemplate {

    @JsonProperty("ITEMS")
    private List<RewardTemplateSimple> ITEMS;

    @JsonProperty("ATTR")
    private List<RewardTemplateSimple> ATTR;

    @JsonProperty("SKILL")
    private List<RewardTemplateSimple> SKILL;

    public List<RewardTemplateSimple> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateSimple> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public List<RewardTemplateSimple> getATTR() {
        return ATTR;
    }

    public void setATTR(List<RewardTemplateSimple> ATTR) {
        this.ATTR = ATTR;
    }

    public List<RewardTemplateSimple> getSKILL() {
        return SKILL;
    }

    public void setSKILL(List<RewardTemplateSimple> SKILL) {
        this.SKILL = SKILL;
    }

    @Override
    public String toString() {
        return "ExclusiveResetTemplate{" +
                "ITEMS=" + ITEMS +
                ", ATTR=" + ATTR +
                ", SKILL=" + SKILL +
                '}';
    }
}
