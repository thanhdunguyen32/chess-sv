package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbMythicalClass.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MythicalClassTemplate {

    @JsonProperty("MAXLV")
    private Integer MAXLV;

    @JsonProperty("PSK")
    private List<Integer> PSK;

    @JsonProperty("ITEMS")
    private List<RewardTemplateSimple> ITEMS;

    public Integer getMAXLV() {
        return MAXLV;
    }

    public void setMAXLV(Integer MAXLV) {
        this.MAXLV = MAXLV;
    }

    public List<Integer> getPSK() {
        return PSK;
    }

    public void setPSK(List<Integer> PSK) {
        this.PSK = PSK;
    }

    public List<RewardTemplateSimple> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateSimple> ITEMS) {
        this.ITEMS = ITEMS;
    }

    @Override
    public String toString() {
        return "MythicalClassTemplate{" +
                "MAXLV=" + MAXLV +
                ", PSK=" + PSK +
                ", ITEMS=" + ITEMS +
                '}';
    }
}
