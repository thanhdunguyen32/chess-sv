package game.module.mythical.bean;

import java.util.List;

public class MythicalAnimal {
    private Integer id;
    private Integer playerId;
    private Integer templateId;
    private Integer level;
    private Integer pclass;
    private List<Integer> passiveSkills;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPclass() {
        return pclass;
    }

    public void setPclass(Integer pclass) {
        this.pclass = pclass;
    }

    public List<Integer> getPassiveSkills() {
        return passiveSkills;
    }

    public void setPassiveSkills(List<Integer> passiveSkills) {
        this.passiveSkills = passiveSkills;
    }

    @Override
    public String toString() {
        return "MythicalAnimal{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", templateId=" + templateId +
                ", level=" + level +
                ", pclass=" + pclass +
                ", passiveSkills=" + passiveSkills +
                '}';
    }
}
