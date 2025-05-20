package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbTowerAchieve.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TowerAchieveTemplate {

    @JsonProperty("GETMARK")
    private Integer GETMARK;

    @JsonProperty("ACHIEVE")
    private List<TowerAchieve1> ACHIEVE;

    public Integer getGETMARK() {
        return GETMARK;
    }

    public void setGETMARK(Integer GETMARK) {
        this.GETMARK = GETMARK;
    }

    public List<TowerAchieve1> getACHIEVE() {
        return ACHIEVE;
    }

    public void setACHIEVE(List<TowerAchieve1> ACHIEVE) {
        this.ACHIEVE = ACHIEVE;
    }

    @Override
    public String toString() {
        return "TowerAchieveTemplate{" +
                "GETMARK=" + GETMARK +
                ", ACHIEVE=" + ACHIEVE +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class TowerAchieve1 {
        
        @JsonProperty("TOWER")
        private Integer TOWER;

        @JsonProperty("REWARDS")
        private List<RewardTemplateSimple> REWARDS;

        public Integer getTOWER() {
            return TOWER;
        }

        public void setTOWER(Integer TOWER) {
            this.TOWER = TOWER;
        }

        public List<RewardTemplateSimple> getREWARDS() {
            return REWARDS;
        }

        public void setREWARDS(List<RewardTemplateSimple> REWARDS) {
            this.REWARDS = REWARDS;
        }

        @Override
        public String toString() {
            return "TowerAchieve1{" +
                    "TOWER=" + TOWER +
                    ", REWARDS=" + REWARDS +
                    '}';
        }
    }

}
