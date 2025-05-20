package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HeroTemplateGrowth {

    @JsonProperty("HP")
    private Float HP;

    @JsonProperty("ATK")
    private Float ATK;

    @JsonProperty("DEF")
    private Float DEF;

    @JsonProperty("MDEF")
    private Float MDEF;

    public Float getHP() {
        return HP;
    }

    public void setHP(Float HP) {
        this.HP = HP;
    }

    public Float getATK() {
        return ATK;
    }

    public void setATK(Float ATK) {
        this.ATK = ATK;
    }

    public Float getDEF() {
        return DEF;
    }

    public void setDEF(Float DEF) {
        this.DEF = DEF;
    }

    public Float getMDEF() {
        return MDEF;
    }

    public void setMDEF(Float MDEF) {
        this.MDEF = MDEF;
    }

    @Override
    public String toString() {
        return "HeroGrowthTemplateConfig{" +
                "HP=" + HP +
                ", ATK=" + ATK +
                ", DEF=" + DEF +
                ", MDEF=" + MDEF +
                '}';
    }
}
