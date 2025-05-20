package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbOfficialItems.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficialItemsTemplate {

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("ATTR")
    private List<KVTemplate> ATTR;

    @JsonProperty("REWARD")
    private List<RewardTemplateSimple> REWARD;

    @JsonProperty("SALARY")
    private List<RewardTemplateSimple> SALARY;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public List<KVTemplate> getATTR() {
        return ATTR;
    }

    public void setATTR(List<KVTemplate> ATTR) {
        this.ATTR = ATTR;
    }

    public List<RewardTemplateSimple> getREWARD() {
        return REWARD;
    }

    public void setREWARD(List<RewardTemplateSimple> REWARD) {
        this.REWARD = REWARD;
    }

    public List<RewardTemplateSimple> getSALARY() {
        return SALARY;
    }

    public void setSALARY(List<RewardTemplateSimple> SALARY) {
        this.SALARY = SALARY;
    }

    @Override
    public String toString() {
        return "OfficialItemsTemplate{" +
                "NAME='" + NAME + '\'' +
                ", ATTR=" + ATTR +
                ", REWARD=" + REWARD +
                ", SALARY=" + SALARY +
                '}';
    }
}
