package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbGeneralChip.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralChipTemplate {

    private Integer id;

    @JsonProperty("CTYPE")
    private Integer CTYPE;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("CHIP")
    private Integer CHIP;

    @JsonProperty("GCOND")
    private Object GCOND;

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

    public Object getGCOND() {
        return GCOND;
    }

    public void setGCOND(Object GCOND) {
        this.GCOND = GCOND;
    }

    public Integer getSTAR() {
        return STAR;
    }

    public void setSTAR(Integer STAR) {
        this.STAR = STAR;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GeneralChipTemplate{" +
                "id=" + id +
                ", CTYPE=" + CTYPE +
                ", STAR=" + STAR +
                ", CHIP=" + CHIP +
                ", GCOND=" + GCOND +
                '}';
    }
}
