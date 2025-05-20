package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actCjxg1.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdCjxg1Template {

    private Integer viple;
    private List<RewardTemplateSimple> consume;
    private List<RewardTemplateSimple> items;
    private Integer buytime;

    public Integer getViple() {
        return viple;
    }

    public void setViple(Integer viple) {
        this.viple = viple;
    }

    public List<RewardTemplateSimple> getConsume() {
        return consume;
    }

    public void setConsume(List<RewardTemplateSimple> consume) {
        this.consume = consume;
    }

    public List<RewardTemplateSimple> getItems() {
        return items;
    }

    public void setItems(List<RewardTemplateSimple> items) {
        this.items = items;
    }

    public Integer getBuytime() {
        return buytime;
    }

    public void setBuytime(Integer buytime) {
        this.buytime = buytime;
    }

    @Override
    public String toString() {
        return "ZhdCjxg1Template{" +
                "viple=" + viple +
                ", consume=" + consume +
                ", items=" + items +
                ", buytime=" + buytime +
                '}';
    }
}
