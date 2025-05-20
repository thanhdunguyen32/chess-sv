package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actCzlb.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdCzlbTemplate {

    private Integer value;
    private Integer price;
    private List<RewardTemplateSimple> items;
    private Integer buytime;
    private Integer limit;
    private List<RewardTemplateSimple> special;
    private Integer exp;
    private String path;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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

    public Integer getBuytime() {
        return buytime;
    }

    public void setBuytime(Integer buytime) {
        this.buytime = buytime;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<RewardTemplateSimple> getSpecial() {
        return special;
    }

    public void setSpecial(List<RewardTemplateSimple> special) {
        this.special = special;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ZhdCzlbTemplate{" +
                "value=" + value +
                ", price=" + price +
                ", items=" + items +
                ", buytime=" + buytime +
                ", limit=" + limit +
                ", special=" + special +
                ", exp=" + exp +
                ", path='" + path + '\'' +
                '}';
    }
}
