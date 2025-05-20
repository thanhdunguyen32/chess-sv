package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "myLegion.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyLegionTemplate {

    @JsonProperty("levelConfig")
    private List<MyLegionLevelConfig> levelConfig;

    @JsonProperty("donateCoin")
    private Integer donateCoin;

    @JsonProperty("donateExp")
    private Integer donateExp;

    public List<MyLegionLevelConfig> getLevelConfig() {
        return levelConfig;
    }

    public void setLevelConfig(List<MyLegionLevelConfig> levelConfig) {
        this.levelConfig = levelConfig;
    }

    public Integer getDonateCoin() {
        return donateCoin;
    }

    public void setDonateCoin(Integer donateCoin) {
        this.donateCoin = donateCoin;
    }

    public Integer getDonateExp() {
        return donateExp;
    }

    public void setDonateExp(Integer donateExp) {
        this.donateExp = donateExp;
    }

    @Override
    public String toString() {
        return "MyLegionTemplate{" +
                "levelConfig=" + levelConfig +
                ", donateCoin=" + donateCoin +
                ", donateExp=" + donateExp +
                '}';
    }

    public static final class MyLegionLevelConfig {
        private Integer pnum;
        private Integer exp;

        public Integer getPnum() {
            return pnum;
        }

        public void setPnum(Integer pnum) {
            this.pnum = pnum;
        }

        public Integer getExp() {
            return exp;
        }

        public void setExp(Integer exp) {
            this.exp = exp;
        }

        @Override
        public String toString() {
            return "MyLegionLevelConfig{" +
                    "pnum=" + pnum +
                    ", exp=" + exp +
                    '}';
        }
    }
}
