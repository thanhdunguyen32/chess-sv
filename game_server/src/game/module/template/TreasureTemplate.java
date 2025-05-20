package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbTreasure.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TreasureTemplate {

    private Integer id;

    @JsonProperty("EXP")
    private Integer EXP;

    @JsonProperty("PRICE")
    private Integer PRICE;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("QUALITY")
    private Integer QUALITY;

    @JsonProperty("STAR")
    private Integer STAR;

    @JsonProperty("ICON")
    private Integer ICON;

    @JsonProperty("PROP")
    private List<KVTemplate> PROP;

    @JsonProperty("SKILL")
    private List<Integer> SKILL;

    @JsonProperty("NEXT")
    private TreasureTemplateNext NEXT;

    public Integer getEXP() {
        return EXP;
    }

    public void setEXP(Integer EXP) {
        this.EXP = EXP;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public void setPRICE(Integer PRICE) {
        this.PRICE = PRICE;
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

    public List<KVTemplate> getPROP() {
        return PROP;
    }

    public void setPROP(List<KVTemplate> PROP) {
        this.PROP = PROP;
    }

    public List<Integer> getSKILL() {
        return SKILL;
    }

    public void setSKILL(List<Integer> SKILL) {
        this.SKILL = SKILL;
    }

    public TreasureTemplateNext getNEXT() {
        return NEXT;
    }

    public void setNEXT(TreasureTemplateNext NEXT) {
        this.NEXT = NEXT;
    }

    @Override
    public String toString() {
        return "TreasureTemplate{" +
                "id=" + id +
                ", EXP=" + EXP +
                ", PRICE=" + PRICE +
                ", NAME='" + NAME + '\'' +
                ", QUALITY=" + QUALITY +
                ", STAR=" + STAR +
                ", ICON=" + ICON +
                ", PROP=" + PROP +
                ", SKILL=" + SKILL +
                ", NEXT=" + NEXT +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
