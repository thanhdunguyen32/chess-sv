package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "myDungeonShop.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyDungeonShopTemplate {

    @JsonProperty("chapter")
    private Integer chapter;

    @JsonProperty("node")
    private Integer node;

    @JsonProperty("disc")
    private Integer disc;

    @JsonProperty("item")
    private List<RewardTemplateSimple> item;

    @JsonProperty("consume")
    private List<RewardTemplateSimple> consume;

    @JsonProperty("quality")
    private Integer quality;

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

    public Integer getDisc() {
        return disc;
    }

    public void setDisc(Integer disc) {
        this.disc = disc;
    }

    public List<RewardTemplateSimple> getItem() {
        return item;
    }

    public void setItem(List<RewardTemplateSimple> item) {
        this.item = item;
    }

    public List<RewardTemplateSimple> getConsume() {
        return consume;
    }

    public void setConsume(List<RewardTemplateSimple> consume) {
        this.consume = consume;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "MyDungeonShopTemplate{" +
                "chapter=" + chapter +
                ", node=" + node +
                ", disc=" + disc +
                ", item=" + item +
                ", consume=" + consume +
                ", quality=" + quality +
                '}';
    }

}
