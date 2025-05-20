package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "myOccTask.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyOccTaskTemplate {

    @JsonProperty("0")
    private List<RewardTemplateSimple> config0;

    @JsonProperty("2")
    private OccTaskConfig2 config2;

    @JsonProperty("3")
    private OccTaskConfig3 config3;

    public List<RewardTemplateSimple> getConfig0() {
        return config0;
    }

    public void setConfig0(List<RewardTemplateSimple> config0) {
        this.config0 = config0;
    }

    public OccTaskConfig2 getConfig2() {
        return config2;
    }

    public void setConfig2(OccTaskConfig2 config2) {
        this.config2 = config2;
    }

    public OccTaskConfig3 getConfig3() {
        return config3;
    }

    public void setConfig3(OccTaskConfig3 config3) {
        this.config3 = config3;
    }

    @Override
    public String toString() {
        return "MyOccTaskTemplate{" +
                "config0=" + config0 +
                ", config2=" + config2 +
                ", config3=" + config3 +
                '}';
    }

    public static final class OccTaskConfig3{

        private RewardTemplateSimple refcost;

        @JsonProperty("REWARDS")
        private List<OccTaskRewardItem> REWARDS;

        private List<Integer> prewards;

        public RewardTemplateSimple getRefcost() {
            return refcost;
        }

        public void setRefcost(RewardTemplateSimple refcost) {
            this.refcost = refcost;
        }

        public List<OccTaskRewardItem> getREWARDS() {
            return REWARDS;
        }

        public void setREWARDS(List<OccTaskRewardItem> REWARDS) {
            this.REWARDS = REWARDS;
        }

        public List<Integer> getPrewards() {
            return prewards;
        }

        public void setPrewards(List<Integer> prewards) {
            this.prewards = prewards;
        }

        @Override
        public String toString() {
            return "OccTaskConfig3{" +
                    "refcost=" + refcost +
                    ", REWARDS=" + REWARDS +
                    ", prewards=" + prewards +
                    '}';
        }
    }

    public static final class OccTaskRewardItem{

        @JsonProperty("TYPE")
        private Integer TYPE;

        @JsonProperty("VALUE")
        private Integer VALUE;

        @JsonProperty("WEIGHT")
        private Integer WEIGHT;

        @JsonProperty("ID")
        private Integer ID;

        @JsonProperty("ITEMS")
        private List<RewardTemplateSimple> ITEMS;

        public Integer getTYPE() {
            return TYPE;
        }

        public void setTYPE(Integer TYPE) {
            this.TYPE = TYPE;
        }

        public Integer getVALUE() {
            return VALUE;
        }

        public void setVALUE(Integer VALUE) {
            this.VALUE = VALUE;
        }

        public Integer getWEIGHT() {
            return WEIGHT;
        }

        public void setWEIGHT(Integer WEIGHT) {
            this.WEIGHT = WEIGHT;
        }

        public Integer getID() {
            return ID;
        }

        public void setID(Integer ID) {
            this.ID = ID;
        }

        public List<RewardTemplateSimple> getITEMS() {
            return ITEMS;
        }

        public void setITEMS(List<RewardTemplateSimple> ITEMS) {
            this.ITEMS = ITEMS;
        }

        @Override
        public String toString() {
            return "OccTaskRewardItem{" +
                    "TYPE=" + TYPE +
                    ", VALUE=" + VALUE +
                    ", WEIGHT=" + WEIGHT +
                    ", ID=" + ID +
                    ", ITEMS=" + ITEMS +
                    '}';
        }
    }

    public static final class OccTaskConfig2{
        private List<OccTaskTemplate1> list;
        private List<RewardTemplateSimple> reward;

        public List<OccTaskTemplate1> getList() {
            return list;
        }

        public void setList(List<OccTaskTemplate1> list) {
            this.list = list;
        }

        public List<RewardTemplateSimple> getReward() {
            return reward;
        }

        public void setReward(List<RewardTemplateSimple> reward) {
            this.reward = reward;
        }

        @Override
        public String toString() {
            return "OccTaskConfig2{" +
                    "list=" + list +
                    ", reward=" + reward +
                    '}';
        }
    }

    public static final class OccTaskTemplate1{
        private Integer status;
        private List<RewardTemplateSimple> rewards;
        private String intro;
        private Integer mark;
        private Integer limit;
        private String page;
        private Integer num;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<RewardTemplateSimple> getRewards() {
            return rewards;
        }

        public void setRewards(List<RewardTemplateSimple> rewards) {
            this.rewards = rewards;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public Integer getMark() {
            return mark;
        }

        public void setMark(Integer mark) {
            this.mark = mark;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        @Override
        public String toString() {
            return "OccTaskTemplate1{" +
                    "status=" + status +
                    ", rewards=" + rewards +
                    ", intro='" + intro + '\'' +
                    ", mark=" + mark +
                    ", limit=" + limit +
                    ", page='" + page + '\'' +
                    ", num=" + num +
                    '}';
        }
    }

}
