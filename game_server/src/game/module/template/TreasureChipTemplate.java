package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbTreasChip.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TreasureChipTemplate {

    @JsonProperty("CTYPE")
    private Integer CTYPE;

    @JsonProperty("QUALITY")
    private Integer QUALITY;

    @JsonProperty("CHIP")
    private Integer CHIP;

    @JsonProperty("TCOND")
    private Integer TCOND;

    public Integer getCTYPE() {
        return CTYPE;
    }

    public void setCTYPE(Integer CTYPE) {
        this.CTYPE = CTYPE;
    }

    public Integer getCHIP() {
        return CHIP;
    }

    public void setCHIP(Integer CHIP) {
        this.CHIP = CHIP;
    }

    public Integer getTCOND() {
        return TCOND;
    }

    public void setTCOND(Integer TCOND) {
        this.TCOND = TCOND;
    }

    public Integer getQUALITY() {
        return QUALITY;
    }

    public void setQUALITY(Integer QUALITY) {
        this.QUALITY = QUALITY;
    }

    @Override
    public String toString() {
        return "TreasureChipTemplate{" +
                "CTYPE=" + CTYPE +
                ", QUALITY=" + QUALITY +
                ", CHIP=" + CHIP +
                ", TCOND=" + TCOND +
                '}';
    }
}
