package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbDungeonChooseBuff.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DungeonChooseBuffTemplate {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("NATUREID")
    private String NATUREID;

    @JsonProperty("ADDITION")
    private Float ADDITION;

    @JsonProperty("LAST")
    private Integer LAST;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("TYPE")
    private Integer TYPE;

    @JsonProperty("CHAPERID")
    private Integer CHAPERID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNATUREID() {
        return NATUREID;
    }

    public void setNATUREID(String NATUREID) {
        this.NATUREID = NATUREID;
    }

    public Float getADDITION() {
        return ADDITION;
    }

    public void setADDITION(Float ADDITION) {
        this.ADDITION = ADDITION;
    }

    public Integer getLAST() {
        return LAST;
    }

    public void setLAST(Integer LAST) {
        this.LAST = LAST;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }

    public Integer getCHAPERID() {
        return CHAPERID;
    }

    public void setCHAPERID(Integer CHAPERID) {
        this.CHAPERID = CHAPERID;
    }

    @Override
    public String toString() {
        return "DungeonChooseBuffTemplate{" +
                "id=" + id +
                ", NATUREID='" + NATUREID + '\'' +
                ", ADDITION=" + ADDITION +
                ", LAST=" + LAST +
                ", NAME='" + NAME + '\'' +
                ", TYPE=" + TYPE +
                ", CHAPERID=" + CHAPERID +
                '}';
    }

}
