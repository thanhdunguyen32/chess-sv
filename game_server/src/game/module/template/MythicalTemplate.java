package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbMythical.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MythicalTemplate {

    private Integer id;

    @JsonProperty("SKILL")
    private List<Integer> SKILL;

    @JsonProperty("PSKILL")
    private List<Integer> PSKILL;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getSKILL() {
        return SKILL;
    }

    public void setSKILL(List<Integer> SKILL) {
        this.SKILL = SKILL;
    }

    public List<Integer> getPSKILL() {
        return PSKILL;
    }

    public void setPSKILL(List<Integer> PSKILL) {
        this.PSKILL = PSKILL;
    }

    @Override
    public String toString() {
        return "MythicalTemplate{" +
                "id=" + id +
                ", SKILL=" + SKILL +
                ", PSKILL=" + PSKILL +
                '}';
    }
}
