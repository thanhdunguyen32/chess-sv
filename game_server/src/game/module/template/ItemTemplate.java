package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbPropNormal.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemTemplate {

    private Integer id;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("QUALITY")
    private Integer QUALITY;

    @JsonProperty("ICON")
    private Integer ICON;

    @JsonProperty("INTRO")
    private String INTRO;

    @JsonProperty("HIDE")
    private Integer HIDE;

    @Override
    public String toString() {
        return "ItemTemplate{" +
                "id=" + id +
                ", NAME='" + NAME + '\'' +
                ", QUALITY=" + QUALITY +
                ", ICON=" + ICON +
                ", INTRO='" + INTRO + '\'' +
                ", HIDE=" + HIDE +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getICON() {
        return ICON;
    }

    public void setICON(Integer ICON) {
        this.ICON = ICON;
    }

    public String getINTRO() {
        return INTRO;
    }

    public void setINTRO(String INTRO) {
        this.INTRO = INTRO;
    }

    public Integer getHIDE() {
        return HIDE;
    }

    public void setHIDE(Integer HIDE) {
        this.HIDE = HIDE;
    }
}
