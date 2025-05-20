package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbRBox.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RBoxTemplate {

    private Integer id;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("QUALITY")
    private Integer QUALITY;

    @JsonProperty("ITEMS")
    private List<RewardTemplateChance> ITEMS;

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

    public List<RewardTemplateChance> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateChance> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RBoxTemplate{" +
                "id=" + id +
                ", NAME='" + NAME + '\'' +
                ", QUALITY=" + QUALITY +
                ", ITEMS=" + ITEMS +
                '}';
    }
}
