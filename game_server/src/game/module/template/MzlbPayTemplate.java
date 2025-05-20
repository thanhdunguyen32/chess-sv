package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "myMzlb.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MzlbPayTemplate {

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("buytime")
    private Integer buytime;

    @JsonProperty("items")
    private List<RewardTemplateSimple> items;

    @Override
    public String toString() {
        return "MzlbPayTemplate{" +
                "price=" + price +
                ", buytime=" + buytime +
                ", items=" + items +
                '}';
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getBuytime() {
        return buytime;
    }

    public void setBuytime(Integer buytime) {
        this.buytime = buytime;
    }

    public List<RewardTemplateSimple> getItems() {
        return items;
    }

    public void setItems(List<RewardTemplateSimple> items) {
        this.items = items;
    }
}
