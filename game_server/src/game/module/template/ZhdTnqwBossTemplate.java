package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "actTnqwBoss.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdTnqwBossTemplate {

    private String name;
    private Integer gsid;
    private Integer level;
    private List<RewardTemplateSimple> challrewards;
    private List<RewardTemplateSimple> killrewards;
    private Long maxhp;
    private Map<Integer, ChapterBattleTemplate> bset;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGsid() {
        return gsid;
    }

    public void setGsid(Integer gsid) {
        this.gsid = gsid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<RewardTemplateSimple> getChallrewards() {
        return challrewards;
    }

    public void setChallrewards(List<RewardTemplateSimple> challrewards) {
        this.challrewards = challrewards;
    }

    public List<RewardTemplateSimple> getKillrewards() {
        return killrewards;
    }

    public void setKillrewards(List<RewardTemplateSimple> killrewards) {
        this.killrewards = killrewards;
    }

    public Long getMaxhp() {
        return maxhp;
    }

    public void setMaxhp(Long maxhp) {
        this.maxhp = maxhp;
    }

    public Map<Integer, ChapterBattleTemplate> getBset() {
        return bset;
    }

    public void setBset(Map<Integer, ChapterBattleTemplate> bset) {
        this.bset = bset;
    }

    @Override
    public String toString() {
        return "ZhdTnqwBossTemplate{" +
                "name='" + name + '\'' +
                ", gsid=" + gsid +
                ", level=" + level +
                ", challrewards=" + challrewards +
                ", killrewards=" + killrewards +
                ", maxhp=" + maxhp +
                ", bset=" + bset +
                '}';
    }

}
