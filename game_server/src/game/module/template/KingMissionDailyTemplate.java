package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbKingMissionDaily.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KingMissionDailyTemplate {

    @JsonProperty("PMARK")
    private Integer PMARK;

    @JsonProperty("CNUM")
    private Integer CNUM;

    @JsonProperty("GETMARK")
    private Integer GETMARK;

    @JsonProperty("REWARD")
    private List<RewardTemplateConfig> REWARD;

    @Override
    public String toString() {
        return "KingMissionDailyTemplate{" +
                "PMARK=" + PMARK +
                ", CNUM=" + CNUM +
                ", GETMARK=" + GETMARK +
                ", REWARD=" + REWARD +
                '}';
    }

    public Integer getPMARK() {
        return PMARK;
    }

    public void setPMARK(Integer PMARK) {
        this.PMARK = PMARK;
    }

    public Integer getCNUM() {
        return CNUM;
    }

    public void setCNUM(Integer CNUM) {
        this.CNUM = CNUM;
    }

    public Integer getGETMARK() {
        return GETMARK;
    }

    public void setGETMARK(Integer GETMARK) {
        this.GETMARK = GETMARK;
    }

    public List<RewardTemplateConfig> getREWARD() {
        return REWARD;
    }

    public void setREWARD(List<RewardTemplateConfig> REWARD) {
        this.REWARD = REWARD;
    }
}
