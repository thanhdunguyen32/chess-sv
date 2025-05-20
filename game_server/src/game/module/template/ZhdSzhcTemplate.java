package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actSzhc.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdSzhcTemplate {

    private List<RewardTemplateSimple> consume;
    private List<RewardTemplateSimple> demand;
    private Integer buytime;

    public List<RewardTemplateSimple> getConsume() {
        return consume;
    }

    public void setConsume(List<RewardTemplateSimple> consume) {
        this.consume = consume;
    }

    public List<RewardTemplateSimple> getDemand() {
        return demand;
    }

    public void setDemand(List<RewardTemplateSimple> demand) {
        this.demand = demand;
    }

    public Integer getBuytime() {
        return buytime;
    }

    public void setBuytime(Integer buytime) {
        this.buytime = buytime;
    }

    @Override
    public String toString() {
        return "ZhdSzhcTemplate{" +
                "consume=" + consume +
                ", demand=" + demand +
                ", buytime=" + buytime +
                '}';
    }

}
