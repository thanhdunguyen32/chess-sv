package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.Arrays;
import java.util.List;

@ExcelTemplateAnn(file = "myDungeon.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyDungeonTemplate {

    @JsonProperty("shops")
    private List<MyDungeonTemplateShop> shops;

    @JsonProperty("steps")
    private List<MyDungeonTemplateStep> steps;

    public List<MyDungeonTemplateShop> getShops() {
        return shops;
    }

    public void setShops(List<MyDungeonTemplateShop> shops) {
        this.shops = shops;
    }

    public List<MyDungeonTemplateStep> getSteps() {
        return steps;
    }

    public void setSteps(List<MyDungeonTemplateStep> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "MyDungeonTemplate{" +
                "shops=" + shops +
                ", steps=" + steps +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MyDungeonTemplateShop {

        private Integer disc;
        private List<RewardTemplateSimple> item;
        private List<RewardTemplateSimple> consume;

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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MyDungeonTemplateStep {

        private List<RewardTemplateSimple> reward;
        private int[] soldier;
        private int[] elite;
        private int[] boss;
        private List<RewardTemplateSimpleBox> box;

        public List<RewardTemplateSimple> getReward() {
            return reward;
        }

        public void setReward(List<RewardTemplateSimple> reward) {
            this.reward = reward;
        }

        public int[] getSoldier() {
            return soldier;
        }

        public void setSoldier(int[] soldier) {
            this.soldier = soldier;
        }

        public int[] getElite() {
            return elite;
        }

        public void setElite(int[] elite) {
            this.elite = elite;
        }

        public int[] getBoss() {
            return boss;
        }

        public void setBoss(int[] boss) {
            this.boss = boss;
        }

        public List<RewardTemplateSimpleBox> getBox() {
            return box;
        }

        public void setBox(List<RewardTemplateSimpleBox> box) {
            this.box = box;
        }

        @Override
        public String toString() {
            return "MyDungeonTemplateStep{" +
                    "reward=" + reward +
                    ", soldier=" + Arrays.toString(soldier) +
                    ", elite=" + Arrays.toString(elite) +
                    ", boss=" + Arrays.toString(boss) +
                    ", box=" + box +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class RewardTemplateSimpleBox {
        private Integer id;
        private Integer quality;
        private List<RewardTemplateRange> goods;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getQuality() {
            return quality;
        }

        public void setQuality(Integer quality) {
            this.quality = quality;
        }

        public List<RewardTemplateRange> getGoods() {
            return goods;
        }

        public void setGoods(List<RewardTemplateRange> goods) {
            this.goods = goods;
        }

        @Override
        public String toString() {
            return "RewardTemplateSimpleBox{" +
                    "id=" + id +
                    ", quality=" + quality +
                    ", goods=" + goods +
                    '}';
        }
    }

}
