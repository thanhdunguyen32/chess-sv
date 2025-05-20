package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbLegionFDonation.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegionFDonationTemplate {

    @JsonProperty("CASTITEMS")
    private List<RewardTemplateSimple> CASTITEMS;

    @JsonProperty("ADDEXP")
    private Integer ADDEXP;

    public List<RewardTemplateSimple> getCASTITEMS() {
        return CASTITEMS;
    }

    public void setCASTITEMS(List<RewardTemplateSimple> CASTITEMS) {
        this.CASTITEMS = CASTITEMS;
    }

    public Integer getADDEXP() {
        return ADDEXP;
    }

    public void setADDEXP(Integer ADDEXP) {
        this.ADDEXP = ADDEXP;
    }

    @Override
    public String toString() {
        return "LegionFDonationTemplate{" +
                "CASTITEMS=" + CASTITEMS +
                ", ADDEXP=" + ADDEXP +
                '}';
    }
}
