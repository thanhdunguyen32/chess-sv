package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbEquipSet.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipSetTemplate {

    private Integer id;

    @JsonProperty("EQUIP")
    private List<Integer> EQUIP;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("PROPERTY")
    private List<KVTemplateNum> PROPERTY;

    @Override
    public String toString() {
        return "EquipSetTemplate{" +
                "id=" + id +
                ", EQUIP=" + EQUIP +
                ", NAME='" + NAME + '\'' +
                ", PROPERTY=" + PROPERTY +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getEQUIP() {
        return EQUIP;
    }

    public void setEQUIP(List<Integer> EQUIP) {
        this.EQUIP = EQUIP;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public List<KVTemplateNum> getPROPERTY() {
        return PROPERTY;
    }

    public void setPROPERTY(List<KVTemplateNum> PROPERTY) {
        this.PROPERTY = PROPERTY;
    }
}
