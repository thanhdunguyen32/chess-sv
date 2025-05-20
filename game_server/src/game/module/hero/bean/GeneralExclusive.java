package game.module.hero.bean;

import java.util.List;
import java.util.Map;

public class GeneralExclusive {
    private Integer level;
    private List<Integer> skill;
    private List<Integer> skillPending;
    private Integer gsid;
    private Map<String,Integer> property;
    private Map<String,Integer> propertyPending;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Integer> getSkill() {
        return skill;
    }

    public void setSkill(List<Integer> skill) {
        this.skill = skill;
    }

    public Integer getGsid() {
        return gsid;
    }

    public void setGsid(Integer gsid) {
        this.gsid = gsid;
    }

    public Map<String, Integer> getProperty() {
        return property;
    }

    public void setProperty(Map<String, Integer> property) {
        this.property = property;
    }

    public List<Integer> getSkillPending() {
        return skillPending;
    }

    public void setSkillPending(List<Integer> skillPending) {
        this.skillPending = skillPending;
    }

    public Map<String, Integer> getPropertyPending() {
        return propertyPending;
    }

    public void setPropertyPending(Map<String, Integer> propertyPending) {
        this.propertyPending = propertyPending;
    }

    @Override
    public String toString() {
        return "GeneralExclusive{" +
                "level=" + level +
                ", skill=" + skill +
                ", skillPending=" + skillPending +
                ", gsid=" + gsid +
                ", property=" + property +
                ", propertyPending=" + propertyPending +
                '}';
    }
}
