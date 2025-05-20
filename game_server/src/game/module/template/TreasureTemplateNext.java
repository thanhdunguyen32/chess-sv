package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TreasureTemplateNext {

    @JsonProperty("EXP")
    private Integer EXP;

    @JsonProperty("ID")
    private Integer ID;

    public Integer getEXP() {
        return EXP;
    }

    public void setEXP(Integer EXP) {
        this.EXP = EXP;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "TreasureTemplateNext{" +
                "EXP=" + EXP +
                ", ID=" + ID +
                '}';
    }
}
