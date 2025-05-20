package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardTemplateRandomRate {

    @JsonProperty("CHANCE")
    private Integer CHANCE;

    @JsonProperty("NUM")
    private Integer NUM;

    public RewardTemplateRandomRate() {
    }

    public RewardTemplateRandomRate(Integer CHANCE, Integer NUM) {
        this.CHANCE = CHANCE;
        this.NUM = NUM;
    }

    public Integer getCHANCE() {
        return CHANCE;
    }

    public void setCHANCE(Integer CHANCE) {
        this.CHANCE = CHANCE;
    }

    public Integer getNUM() {
        return NUM;
    }

    public void setNUM(Integer NUM) {
        this.NUM = NUM;
    }

    @Override
    public String toString() {
        return "RewardTemplateRandomRate{" +
                "CHANCE=" + CHANCE +
                ", NUM=" + NUM +
                '}';
    }
}
