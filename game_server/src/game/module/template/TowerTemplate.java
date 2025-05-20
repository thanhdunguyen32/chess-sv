package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.Arrays;
import java.util.List;

@ExcelTemplateAnn(file = "dbTower.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TowerTemplate {

    @JsonProperty("GENERAL")
    private Integer GENERAL;

    @JsonProperty("GLEVEL")
    private Integer GLEVEL;

    @JsonProperty("REWARDS")
    private List<RewardTemplateSimple> REWARDS;

    public Integer getGENERAL() {
        return GENERAL;
    }

    public void setGENERAL(Integer GENERAL) {
        this.GENERAL = GENERAL;
    }

    public Integer getGLEVEL() {
        return GLEVEL;
    }

    public void setGLEVEL(Integer GLEVEL) {
        this.GLEVEL = GLEVEL;
    }

    public List<RewardTemplateSimple> getREWARDS() {
        return REWARDS;
    }

    public void setREWARDS(List<RewardTemplateSimple> REWARDS) {
        this.REWARDS = REWARDS;
    }

    @Override
    public String toString() {
        return "TowerTemplate{" +
                "GENERAL=" + GENERAL +
                ", GLEVEL=" + GLEVEL +
                ", REWARDS=" + REWARDS +
                '}';
    }
}
