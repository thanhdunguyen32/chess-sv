package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbOnlineGift.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnlineGiftTemplate {

    @JsonProperty("GIVE_SIGN")
    private Integer GIVE_SIGN;

    @JsonProperty("REWARD")
    private List<OnlineGiftTemplateReward> REWARD;

    public Integer getGIVE_SIGN() {
        return GIVE_SIGN;
    }

    public void setGIVE_SIGN(Integer GIVE_SIGN) {
        this.GIVE_SIGN = GIVE_SIGN;
    }

    public List<OnlineGiftTemplateReward> getREWARD() {
        return REWARD;
    }

    public void setREWARD(List<OnlineGiftTemplateReward> REWARD) {
        this.REWARD = REWARD;
    }

    @Override
    public String toString() {
        return "OnlineGiftTemplate{" +
                "GIVE_SIGN=" + GIVE_SIGN +
                ", REWARD=" + REWARD +
                '}';
    }

    public static final class OnlineGiftTemplateReward {
        @JsonProperty("TIME")
        private Integer TIME;

        @JsonProperty("REWARD")
        private List<RewardTemplateSimple> REWARD;

        public Integer getTIME() {
            return TIME;
        }

        public void setTIME(Integer TIME) {
            this.TIME = TIME;
        }

        public List<RewardTemplateSimple> getREWARD() {
            return REWARD;
        }

        public void setREWARD(List<RewardTemplateSimple> REWARD) {
            this.REWARD = REWARD;
        }

        @Override
        public String toString() {
            return "OnlineGiftTemplateReward{" +
                    "TIME=" + TIME +
                    ", REWARD=" + REWARD +
                    '}';
        }
    }
}
