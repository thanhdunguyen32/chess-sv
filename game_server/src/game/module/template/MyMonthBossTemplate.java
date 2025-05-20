package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "myMonthBoss.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyMonthBossTemplate {

    @JsonProperty("general")
    private Integer general;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("bset")
    private Map<Integer,ChapterBattleTemplate> bset;

    @JsonProperty("maxhp")
    private Long maxhp;

    @JsonProperty("rewards")
    private List<RewardTemplateSimple> rewards;

    public Integer getGeneral() {
        return general;
    }

    public void setGeneral(Integer general) {
        this.general = general;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Map<Integer, ChapterBattleTemplate> getBset() {
        return bset;
    }

    public void setBset(Map<Integer, ChapterBattleTemplate> bset) {
        this.bset = bset;
    }

    public Long getMaxhp() {
        return maxhp;
    }

    public void setMaxhp(Long maxhp) {
        this.maxhp = maxhp;
    }

    public List<RewardTemplateSimple> getRewards() {
        return rewards;
    }

    public void setRewards(List<RewardTemplateSimple> rewards) {
        this.rewards = rewards;
    }

    @Override
    public String toString() {
        return "MyMonthBossTemplate{" +
                "general=" + general +
                ", level=" + level +
                ", bset=" + bset +
                ", maxhp=" + maxhp +
                ", rewards=" + rewards +
                '}';
    }

}
