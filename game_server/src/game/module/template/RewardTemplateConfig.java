package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardTemplateConfig {

    @JsonProperty("GSID")
    private Integer GSID;

    @JsonProperty("COUNT")
    private Integer COUNT;

    @JsonProperty("PARAM")
    private Integer PARAM;

    @Override
    public String toString() {
        return "RewardTemplateConfig{" +
                "GSID=" + GSID +
                ", COUNT=" + COUNT +
                ", PARAM=" + PARAM +
                '}';
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

    public Integer getPARAM() {
        return PARAM;
    }

    public void setPARAM(Integer PARAM) {
        this.PARAM = PARAM;
    }
}
