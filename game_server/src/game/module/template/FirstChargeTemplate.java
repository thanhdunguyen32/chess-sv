package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbFirstCharge.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirstChargeTemplate {

    @JsonProperty("EXID")
    private String EXID;

    @JsonProperty("ITEMS")
    private List<RewardTemplateSimple> ITEMS;

    @Override
    public String toString() {
        return "FirstChargeTemplate{" +
                "EXID='" + EXID + '\'' +
                ", ITEMS=" + ITEMS +
                '}';
    }

    public String getEXID() {
        return EXID;
    }

    public void setEXID(String EXID) {
        this.EXID = EXID;
    }

    public List<RewardTemplateSimple> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateSimple> ITEMS) {
        this.ITEMS = ITEMS;
    }
}
