package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbKingStage.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KingStageTemplate {

    @JsonProperty("ID")
    private Integer ID;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("REWARDS")
    private List<RewardTemplateSimple> REWARDS;

    @Override
    public String toString() {
        return "KingStageTemplate{" +
                "ID=" + ID +
                ", STAR=" + STAR +
                ", NAME='" + NAME + '\'' +
                ", REWARDS=" + REWARDS +
                '}';
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getSTAR() {
        return STAR;
    }

    public void setSTAR(Integer STAR) {
        this.STAR = STAR;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public List<RewardTemplateSimple> getREWARDS() {
        return REWARDS;
    }

    public void setREWARDS(List<RewardTemplateSimple> REWARDS) {
        this.REWARDS = REWARDS;
    }
}
