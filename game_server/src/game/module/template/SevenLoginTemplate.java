package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbSevenLogin.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SevenLoginTemplate {

    @JsonProperty("LOGINSIGN")
    private Integer LOGINSIGN;

    @JsonProperty("REWARD")
    private List<SevenLoginRewardTemplate> REWARD;

    @Override
    public String toString() {
        return "SevenLoginTemplate{" +
                "LOGINSIGN='" + LOGINSIGN + '\'' +
                ", REWARD=" + REWARD +
                '}';
    }

    public Integer getLOGINSIGN() {
        return LOGINSIGN;
    }

    public void setLOGINSIGN(Integer LOGINSIGN) {
        this.LOGINSIGN = LOGINSIGN;
    }

    public List<SevenLoginRewardTemplate> getREWARD() {
        return REWARD;
    }

    public void setREWARD(List<SevenLoginRewardTemplate> REWARD) {
        this.REWARD = REWARD;
    }

    public static final class SevenLoginRewardTemplate{
        @JsonProperty("EXID")
        private String EXID;
        @JsonProperty("GETSIGN")
        private Integer GETSIGN;
        @JsonProperty("REWARD")
        private List<RewardTemplateSimple> REWARD;

        public String getEXID() {
            return EXID;
        }

        public void setEXID(String EXID) {
            this.EXID = EXID;
        }

        public Integer getGETSIGN() {
            return GETSIGN;
        }

        public void setGETSIGN(Integer GETSIGN) {
            this.GETSIGN = GETSIGN;
        }

        public List<RewardTemplateSimple> getREWARD() {
            return REWARD;
        }

        public void setREWARD(List<RewardTemplateSimple> REWARD) {
            this.REWARD = REWARD;
        }

        @Override
        public String toString() {
            return "SevenLoginRewardTemplate{" +
                    "EXID='" + EXID + '\'' +
                    ", GETSIGN=" + GETSIGN +
                    ", REWARD=" + REWARD +
                    '}';
        }
    }

}
