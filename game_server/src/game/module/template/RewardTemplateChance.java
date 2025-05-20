package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RewardTemplateChance {

    @JsonProperty("GSID")
    private Integer GSID;

    @JsonProperty("COUNT")
    private Integer COUNT;

    @JsonProperty("CHANCE")
    private Integer CHANCE;

    public RewardTemplateChance(Integer GSID, Integer COUNT, Integer CHANCE) {
        this.GSID = GSID;
        this.COUNT = COUNT;
        this.CHANCE = CHANCE;
    }

    public RewardTemplateChance() {
    }

    public Integer getGSID() {
        return GSID;
    }

    public void setGSID(Integer GSID) {
        this.GSID = GSID;
    }

    public Integer getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(Integer COUNT) {
        this.COUNT = COUNT;
    }

    public Integer getCHANCE() {
        return CHANCE;
    }

    public void setCHANCE(Integer CHANCE) {
        this.CHANCE = CHANCE;
    }

    @Override
    public String toString() {
        return "RewardTemplateChance{" +
                "GSID=" + GSID +
                ", COUNT=" + COUNT +
                ", CHANCE=" + CHANCE +
                '}';
    }
}
