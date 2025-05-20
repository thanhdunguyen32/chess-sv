package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbTalentInfo.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalentInfoTemplate {

    private Integer id;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("LOCK")
    private Integer LOCK;

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

    public Integer getLOCK() {
        return LOCK;
    }

    public void setLOCK(Integer LOCK) {
        this.LOCK = LOCK;
    }

    @Override
    public String toString() {
        return "TalentInfoTemplate{" +
                "id=" + id +
                ", NAME='" + NAME + '\'' +
                ", LOCK=" + LOCK +
                '}';
    }
}
