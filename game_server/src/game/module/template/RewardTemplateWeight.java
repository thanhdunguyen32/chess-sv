package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RewardTemplateWeight {

    @JsonProperty("GSID")
    private Integer GSID;

    @JsonProperty("COUNT")
    private Integer COUNT;

    @JsonProperty("W")
    private Integer W;

    public RewardTemplateWeight(Integer GSID, Integer COUNT, Integer W) {
        this.GSID = GSID;
        this.COUNT = COUNT;
        this.W = W;
    }

    public RewardTemplateWeight() {
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

    public Integer getW() {
        return W;
    }

    public void setW(Integer w) {
        W = w;
    }

    @Override
    public String toString() {
        return "RewardTemplateWeight{" +
                "GSID=" + GSID +
                ", COUNT=" + COUNT +
                ", W=" + W +
                '}';
    }
}
