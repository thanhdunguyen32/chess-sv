package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "myAffairs.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AffairRewardTemplate {

    private Integer id;

    private List<List<Integer>> reward;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<List<Integer>> getReward() {
        return reward;
    }

    public void setReward(List<List<Integer>> reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "AffairRewardTemplate{" +
                "id=" + id +
                ", reward=" + reward +
                '}';
    }
}
