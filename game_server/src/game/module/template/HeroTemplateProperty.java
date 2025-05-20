package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HeroTemplateProperty {

    @JsonProperty("HP")
    private Float HP;

    @JsonProperty("ATK")
    private Float ATK;

    @JsonProperty("DEF")
    private Float DEF;

    @JsonProperty("MDEF")
    private Float MDEF;

    @JsonProperty("ATKTIME")
    private Float ATKTIME;

    @JsonProperty("RANGE")
    private Integer RANGE;

    @JsonProperty("MSP")
    private Integer MSP;

    @JsonProperty("PCRI")
    private Integer PCRI;

    @JsonProperty("PCRID")
    private Integer PCRID;

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

    public Float getATKTIME() {
        return ATKTIME;
    }

    public void setATKTIME(Float ATKTIME) {
        this.ATKTIME = ATKTIME;
    }

    public Integer getRANGE() {
        return RANGE;
    }

    public void setRANGE(Integer RANGE) {
        this.RANGE = RANGE;
    }

    public Integer getMSP() {
        return MSP;
    }

    public void setMSP(Integer MSP) {
        this.MSP = MSP;
    }

    public Integer getPCRI() {
        return PCRI;
    }

    public void setPCRI(Integer PCRI) {
        this.PCRI = PCRI;
    }

    public Integer getPCRID() {
        return PCRID;
    }

    public void setPCRID(Integer PCRID) {
        this.PCRID = PCRID;
    }

    @Override
    public String toString() {
        return "HeroPropertyTemplateConfig{" +
                "HP=" + HP +
                ", ATK=" + ATK +
                ", DEF=" + DEF +
                ", MDEF=" + MDEF +
                ", ATKTIME=" + ATKTIME +
                ", RANGE=" + RANGE +
                ", MSP=" + MSP +
                ", PCRI=" + PCRI +
                ", PCRID=" + PCRID +
                '}';
    }
}
