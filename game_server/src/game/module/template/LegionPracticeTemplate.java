package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "dbLegionPractice.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegionPracticeTemplate {

    @JsonProperty("BOSS_INFO")
    private Map<Integer,LegionPracticeBossTemplate> BOSS_INFO;

    @JsonProperty("FIGHTING_COST")
    private List<Integer> FIGHTING_COST;

    @JsonProperty("LEGION_HARM_FIRST")
    private List<RewardTemplateSimple> LEGION_HARM_FIRST;

    @JsonProperty("LEGION_WORLD_REWARD")
    private List<LegionWorldRewardTemplate> LEGION_WORLD_REWARD;

    public Map<Integer, LegionPracticeBossTemplate> getBOSS_INFO() {
        return BOSS_INFO;
    }

    public void setBOSS_INFO(Map<Integer, LegionPracticeBossTemplate> BOSS_INFO) {
        this.BOSS_INFO = BOSS_INFO;
    }

    public List<Integer> getFIGHTING_COST() {
        return FIGHTING_COST;
    }

    public void setFIGHTING_COST(List<Integer> FIGHTING_COST) {
        this.FIGHTING_COST = FIGHTING_COST;
    }

    public List<RewardTemplateSimple> getLEGION_HARM_FIRST() {
        return LEGION_HARM_FIRST;
    }

    public void setLEGION_HARM_FIRST(List<RewardTemplateSimple> LEGION_HARM_FIRST) {
        this.LEGION_HARM_FIRST = LEGION_HARM_FIRST;
    }

    public List<LegionWorldRewardTemplate> getLEGION_WORLD_REWARD() {
        return LEGION_WORLD_REWARD;
    }

    public void setLEGION_WORLD_REWARD(List<LegionWorldRewardTemplate> LEGION_WORLD_REWARD) {
        this.LEGION_WORLD_REWARD = LEGION_WORLD_REWARD;
    }

    @Override
    public String toString() {
        return "LegionPracticeTemplate{" +
                "BOSS_INFO=" + BOSS_INFO +
                ", FIGHTING_COST=" + FIGHTING_COST +
                ", LEGION_HARM_FIRST=" + LEGION_HARM_FIRST +
                ", LEGION_WORLD_REWARD=" + LEGION_WORLD_REWARD +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class LegionPracticeBossTemplate{
        @JsonProperty("ID")
        private Integer ID;
        @JsonProperty("FIGHT_REWARD")
        private List<RewardTemplateSimple> FIGHT_REWARD;
        @JsonProperty("RANDOM_RATE")
        private List<RewardTemplateRandomRate> RANDOM_RATE;
        @JsonProperty("RANDOM_REWARD")
        private List<RewardTemplateChance> RANDOM_REWARD;

        public Integer getID() {
            return ID;
        }

        public void setID(Integer ID) {
            this.ID = ID;
        }

        public List<RewardTemplateSimple> getFIGHT_REWARD() {
            return FIGHT_REWARD;
        }

        public void setFIGHT_REWARD(List<RewardTemplateSimple> FIGHT_REWARD) {
            this.FIGHT_REWARD = FIGHT_REWARD;
        }

        public List<RewardTemplateRandomRate> getRANDOM_RATE() {
            return RANDOM_RATE;
        }

        public void setRANDOM_RATE(List<RewardTemplateRandomRate> RANDOM_RATE) {
            this.RANDOM_RATE = RANDOM_RATE;
        }

        public List<RewardTemplateChance> getRANDOM_REWARD() {
            return RANDOM_REWARD;
        }

        public void setRANDOM_REWARD(List<RewardTemplateChance> RANDOM_REWARD) {
            this.RANDOM_REWARD = RANDOM_REWARD;
        }

        @Override
        public String toString() {
            return "LegionPracticeBossTemplate{" +
                    "ID=" + ID +
                    ", FIGHT_REWARD=" + FIGHT_REWARD +
                    ", RANDOM_RATE=" + RANDOM_RATE +
                    ", RANDOM_REWARD=" + RANDOM_REWARD +
                    '}';
        }
    }

    public static final class LegionWorldRewardTemplate{
        @JsonProperty("MIN")
        private Integer MIN;
        @JsonProperty("MAX")
        private Integer MAX;
        @JsonProperty("ITEMS")
        private List<RewardTemplateSimple> ITEMS;

        public Integer getMIN() {
            return MIN;
        }

        public void setMIN(Integer MIN) {
            this.MIN = MIN;
        }

        public Integer getMAX() {
            return MAX;
        }

        public void setMAX(Integer MAX) {
            this.MAX = MAX;
        }

        public List<RewardTemplateSimple> getITEMS() {
            return ITEMS;
        }

        public void setITEMS(List<RewardTemplateSimple> ITEMS) {
            this.ITEMS = ITEMS;
        }

        @Override
        public String toString() {
            return "LegionWorldRewardTemplate{" +
                    "MIN=" + MIN +
                    ", MAX=" + MAX +
                    ", ITEMS=" + ITEMS +
                    '}';
        }
    }
}
