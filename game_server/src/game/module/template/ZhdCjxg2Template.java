package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actCjxg2.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdCjxg2Template {

    private Integer value;
    private Integer price;
    private List<RewardTemplateSimple> items;
    private Integer buytime;
    private String icon;
    private List<RewardTemplateSimple> special;
    private String bg1;
    private String bg2;
    private String hero;
    private String heroname;
    private String txbig;
    private String normal;
    private String check;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<RewardTemplateSimple> getSpecial() {
        return special;
    }

    public void setSpecial(List<RewardTemplateSimple> special) {
        this.special = special;
    }

    public String getBg1() {
        return bg1;
    }

    public void setBg1(String bg1) {
        this.bg1 = bg1;
    }

    public String getBg2() {
        return bg2;
    }

    public void setBg2(String bg2) {
        this.bg2 = bg2;
    }

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public String getHeroname() {
        return heroname;
    }

    public void setHeroname(String heroname) {
        this.heroname = heroname;
    }

    public String getTxbig() {
        return txbig;
    }

    public void setTxbig(String txbig) {
        this.txbig = txbig;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "ZhdCjxg2Template{" +
                "value=" + value +
                ", price=" + price +
                ", items=" + items +
                ", buytime=" + buytime +
                ", icon='" + icon + '\'' +
                ", special=" + special +
                ", bg1='" + bg1 + '\'' +
                ", bg2='" + bg2 + '\'' +
                ", hero='" + hero + '\'' +
                ", heroname='" + heroname + '\'' +
                ", txbig='" + txbig + '\'' +
                ", normal='" + normal + '\'' +
                ", check='" + check + '\'' +
                '}';
    }
}
