package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbExpedition.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpeditionTemplate {

    private Integer id;

    @JsonProperty("TYPE")
    private Integer TYPE;

    @JsonProperty("REWARD")
    private List<RewardTemplateSimple> REWARD;

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }

    public List<RewardTemplateSimple> getREWARD() {
        return REWARD;
    }

    public void setREWARD(List<RewardTemplateSimple> REWARD) {
        this.REWARD = REWARD;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ExpeditionTemplate{" +
                "id=" + id +
                ", TYPE=" + TYPE +
                ", REWARD=" + REWARD +
                '}';
    }
}
