package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RewardTemplateRange {

    @JsonProperty("GSID")
    private Integer GSID;

    @JsonProperty("COUNT")
    private List<Integer> COUNT;

    public RewardTemplateRange(Integer GSID, List<Integer> COUNT) {
        this.GSID = GSID;
        this.COUNT = COUNT;
    }

    public RewardTemplateRange() {
    }

    public Integer getGSID() {
        return GSID;
    }

    public void setGSID(Integer GSID) {
        this.GSID = GSID;
    }

    public List<Integer> getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(List<Integer> COUNT) {
        this.COUNT = COUNT;
    }

    @Override
    public String toString() {
        return "RewardTemplateRange{" +
                "GSID=" + GSID +
                ", COUNT=" + COUNT +
                '}';
    }
}
