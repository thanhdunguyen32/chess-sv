package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbEquip.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipTemplate {

    private Integer id;

    @JsonProperty("POS")
    private Integer POS;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("QUALITY")
    private Integer QUALITY;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("ICON")
    private Integer ICON;

    @JsonProperty("PRICE")
    private Integer PRICE;

    @JsonProperty("INTRO")
    private String INTRO;

    @JsonProperty("PROP")
    private List<KVTemplate> PROP;

    @JsonProperty("SETID")
    private Integer SETID;

    @Override
    public String toString() {
        return "EquipTemplate{" +
                "id=" + id +
                ", POS=" + POS +
                ", NAME='" + NAME + '\'' +
                ", QUALITY=" + QUALITY +
                ", STAR=" + STAR +
                ", ICON=" + ICON +
                ", PRICE=" + PRICE +
                ", INTRO='" + INTRO + '\'' +
                ", PROP=" + PROP +
                ", SETID=" + SETID +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPOS() {
        return POS;
    }

    public void setPOS(Integer POS) {
        this.POS = POS;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getQUALITY() {
        return QUALITY;
    }

    public void setQUALITY(Integer QUALITY) {
        this.QUALITY = QUALITY;
    }

    public Integer getSTAR() {
        return STAR;
    }

    public void setSTAR(Integer STAR) {
        this.STAR = STAR;
    }

    public Integer getICON() {
        return ICON;
    }

    public void setICON(Integer ICON) {
        this.ICON = ICON;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public void setPRICE(Integer PRICE) {
        this.PRICE = PRICE;
    }

    public String getINTRO() {
        return INTRO;
    }

    public void setINTRO(String INTRO) {
        this.INTRO = INTRO;
    }

    public List<KVTemplate> getPROP() {
        return PROP;
    }

    public void setPROP(List<KVTemplate> PROP) {
        this.PROP = PROP;
    }

    public Integer getSETID() {
        return SETID;
    }

    public void setSETID(Integer SETID) {
        this.SETID = SETID;
    }
}
