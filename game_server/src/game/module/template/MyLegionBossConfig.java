package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "myLegionBoss.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyLegionBossConfig {

    @JsonProperty("chapter")
    private Integer chapter;

    @JsonProperty("name")
    private String name;

    @JsonProperty("rewards")
    private List<RewardTemplateSimple> rewards;

    @JsonProperty("bset")
    private Map<Integer, ChapterBattleTemplate> bset;

    @JsonProperty("maxhp")
    private Long maxhp;

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RewardTemplateSimple> getRewards() {
        return rewards;
    }

    public void setRewards(List<RewardTemplateSimple> rewards) {
        this.rewards = rewards;
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

    @Override
    public String toString() {
        return "MyLegionBossConfig{" +
                "chapter=" + chapter +
                ", name='" + name + '\'' +
                ", rewards=" + rewards +
                ", bset=" + bset +
                ", maxhp=" + maxhp +
                '}';
    }

}
