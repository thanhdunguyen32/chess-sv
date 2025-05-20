package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "myXiangou.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyXiangouTemplate {

    private Integer level;

    private Integer gstar;

    private Integer price;

    private List<RewardTemplateSimple> items;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getGstar() {
        return gstar;
    }

    public void setGstar(Integer gstar) {
        this.gstar = gstar;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<RewardTemplateSimple> getItems() {
        return items;
    }

    public void setItems(List<RewardTemplateSimple> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "MyXiangouTemplate{" +
                "level=" + level +
                ", gstar=" + gstar +
                ", price=" + price +
                ", items=" + items +
                '}';
    }

}
