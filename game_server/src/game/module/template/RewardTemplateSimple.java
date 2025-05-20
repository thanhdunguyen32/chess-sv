package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardTemplateSimple {

    @JsonProperty("GSID")
    private Integer GSID;

    @JsonProperty("COUNT")
    private Integer COUNT;

    public RewardTemplateSimple(Integer GSID, Integer COUNT) {
        this.GSID = GSID;
        this.COUNT = COUNT;
    }

    public RewardTemplateSimple() {
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

    @Override
    public String toString() {
        return "RewardTemplateSimple{" +
                "GSID=" + GSID +
                ", COUNT=" + COUNT +
                '}';
    }
}
