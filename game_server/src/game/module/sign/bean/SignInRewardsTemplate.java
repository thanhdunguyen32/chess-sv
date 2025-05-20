package game.module.sign.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbSignReward.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInRewardsTemplate {

    @JsonProperty("GSID")
    private Integer GSID;

    @JsonProperty("COUNT")
    private Integer COUNT;

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
        return "SignInRewardsTemplate{" +
                "GSID=" + GSID +
                ", COUNT='" + COUNT + '\'' +
                '}';
    }

}
