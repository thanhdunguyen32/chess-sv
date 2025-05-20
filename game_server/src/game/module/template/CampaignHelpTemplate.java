package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbCampaignHelp.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignHelpTemplate {

    @JsonProperty("THREECHOOSEONE")
    private List<Three1Template> THREECHOOSEONE;

    public List<Three1Template> getTHREECHOOSEONE() {
        return THREECHOOSEONE;
    }

    public void setTHREECHOOSEONE(List<Three1Template> THREECHOOSEONE) {
        this.THREECHOOSEONE = THREECHOOSEONE;
    }

    @Override
    public String toString() {
        return "CampaignHelpTemplate{" +
                "THREECHOOSEONE=" + THREECHOOSEONE +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Three1Template{

        @JsonProperty("MINMAP")
        private Integer MINMAP;

        @JsonProperty("REWARD")
        private RewardTemplateSimple REWARD;

        public Integer getMINMAP() {
            return MINMAP;
        }

        public void setMINMAP(Integer MINMAP) {
            this.MINMAP = MINMAP;
        }

        public RewardTemplateSimple getREWARD() {
            return REWARD;
        }

        public void setREWARD(RewardTemplateSimple REWARD) {
            this.REWARD = REWARD;
        }

        @Override
        public String toString() {
            return "Three1Template{" +
                    "MINMAP=" + MINMAP +
                    ", REWARD=" + REWARD +
                    '}';
        }
    }
}
