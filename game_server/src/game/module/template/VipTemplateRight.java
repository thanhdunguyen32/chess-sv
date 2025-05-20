package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VipTemplateRight {

    @JsonProperty("BAG")
    private Integer BAG;

    @JsonProperty("GOLD")
    private Integer GOLD;

    @JsonProperty("HANG_GOLD")
    private Integer HANG_GOLD;

    @JsonProperty("HANG_EXP")
    private Integer HANG_EXP;

    @JsonProperty("HANG_GEXP")
    private Integer HANG_GEXP;

    @JsonProperty("HANG_TIME")
    private Integer HANG_TIME;

    @JsonProperty("MISSION")
    private Integer MISSION;

    @JsonProperty("BIGBATTLE")
    private Integer BIGBATTLE;

    @JsonProperty("SHOP")
    private Integer SHOP;

    @JsonProperty("PUB")
    private Integer PUB;

    @JsonProperty("MANOR")
    private Integer MANOR;

    public Integer getBAG() {
        return BAG;
    }

    public void setBAG(Integer BAG) {
        this.BAG = BAG;
    }

    public Integer getGOLD() {
        return GOLD;
    }

    public void setGOLD(Integer GOLD) {
        this.GOLD = GOLD;
    }

    public Integer getHANG_GOLD() {
        return HANG_GOLD;
    }

    public void setHANG_GOLD(Integer HANG_GOLD) {
        this.HANG_GOLD = HANG_GOLD;
    }

    public Integer getHANG_EXP() {
        return HANG_EXP;
    }

    public void setHANG_EXP(Integer HANG_EXP) {
        this.HANG_EXP = HANG_EXP;
    }

    public Integer getHANG_GEXP() {
        return HANG_GEXP;
    }

    public void setHANG_GEXP(Integer HANG_GEXP) {
        this.HANG_GEXP = HANG_GEXP;
    }

    public Integer getHANG_TIME() {
        return HANG_TIME;
    }

    public void setHANG_TIME(Integer HANG_TIME) {
        this.HANG_TIME = HANG_TIME;
    }

    public Integer getMISSION() {
        return MISSION;
    }

    public void setMISSION(Integer MISSION) {
        this.MISSION = MISSION;
    }

    public Integer getBIGBATTLE() {
        return BIGBATTLE;
    }

    public void setBIGBATTLE(Integer BIGBATTLE) {
        this.BIGBATTLE = BIGBATTLE;
    }

    public Integer getSHOP() {
        return SHOP;
    }

    public void setSHOP(Integer SHOP) {
        this.SHOP = SHOP;
    }

    public Integer getPUB() {
        return PUB;
    }

    public void setPUB(Integer PUB) {
        this.PUB = PUB;
    }

    public Integer getMANOR() {
        return MANOR;
    }

    public void setMANOR(Integer MANOR) {
        this.MANOR = MANOR;
    }

    @Override
    public String toString() {
        return "VipTemplateRight{" +
                "BAG=" + BAG +
                ", GOLD=" + GOLD +
                ", HANG_GOLD=" + HANG_GOLD +
                ", HANG_EXP=" + HANG_EXP +
                ", HANG_GEXP=" + HANG_GEXP +
                ", HANG_TIME=" + HANG_TIME +
                ", MISSION=" + MISSION +
                ", BIGBATTLE=" + BIGBATTLE +
                ", SHOP=" + SHOP +
                ", PUB=" + PUB +
                ", MANOR=" + MANOR +
                '}';
    }
}
