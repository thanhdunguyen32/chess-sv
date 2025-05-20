package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbPvpDayReward.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PvpDayRewardTemplate {

    @JsonProperty("MAX")
    private Integer MAX;

    @JsonProperty("MIN")
    private Integer MIN;

    @JsonProperty("REWARDS")
    private List<RewardTemplateSimple> REWARDS;

    public Integer getMAX() {
        return MAX;
    }

    public void setMAX(Integer MAX) {
        this.MAX = MAX;
    }

    public Integer getMIN() {
        return MIN;
    }

    public void setMIN(Integer MIN) {
        this.MIN = MIN;
    }

    public List<RewardTemplateSimple> getREWARDS() {
        return REWARDS;
    }

    public void setREWARDS(List<RewardTemplateSimple> REWARDS) {
        this.REWARDS = REWARDS;
    }

    @Override
    public String toString() {
        return "PvpDayRewardTemplate{" +
                "MAX=" + MAX +
                ", MIN=" + MIN +
                ", REWARDS=" + REWARDS +
                '}';
    }
}
