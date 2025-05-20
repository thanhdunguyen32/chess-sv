package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbChapter.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChapterTemplate {

    private Integer id;

    @JsonProperty("CITY")
    private Integer CITY;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("LIMIT")
    private Integer LIMIT;

    @JsonProperty("GTYPE")
    private Integer GTYPE;

    @JsonProperty("TYPE")
    private Integer TYPE;

    @JsonProperty("GENERAL")
    private Integer GENERAL;

    @JsonProperty("GLEVEL")
    private Integer GLEVEL;

    @JsonProperty("HANG")
    private List<RewardTemplateSimple> HANG;

    @JsonProperty("DROPS")
    private List<RewardTemplateSimple> DROPS;

    @JsonProperty("REWARDS")
    private List<RewardTemplateSimple> REWARDS;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCITY() {
        return CITY;
    }

    public void setCITY(Integer CITY) {
        this.CITY = CITY;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getLIMIT() {
        return LIMIT;
    }

    public void setLIMIT(Integer LIMIT) {
        this.LIMIT = LIMIT;
    }

    public Integer getGTYPE() {
        return GTYPE;
    }

    public void setGTYPE(Integer GTYPE) {
        this.GTYPE = GTYPE;
    }

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }

    public Integer getGENERAL() {
        return GENERAL;
    }

    public void setGENERAL(Integer GENERAL) {
        this.GENERAL = GENERAL;
    }

    public Integer getGLEVEL() {
        return GLEVEL;
    }

    public void setGLEVEL(Integer GLEVEL) {
        this.GLEVEL = GLEVEL;
    }

    public List<RewardTemplateSimple> getHANG() {
        return HANG;
    }

    public void setHANG(List<RewardTemplateSimple> HANG) {
        this.HANG = HANG;
    }

    public List<RewardTemplateSimple> getDROPS() {
        return DROPS;
    }

    public void setDROPS(List<RewardTemplateSimple> DROPS) {
        this.DROPS = DROPS;
    }

    public List<RewardTemplateSimple> getREWARDS() {
        return REWARDS;
    }

    public void setREWARDS(List<RewardTemplateSimple> REWARDS) {
        this.REWARDS = REWARDS;
    }

    @Override
    public String toString() {
        return "ChapterTemplate{" +
                "id=" + id +
                ", CITY=" + CITY +
                ", NAME='" + NAME + '\'' +
                ", LIMIT=" + LIMIT +
                ", GTYPE=" + GTYPE +
                ", TYPE=" + TYPE +
                ", GENERAL=" + GENERAL +
                ", GLEVEL=" + GLEVEL +
                ", HANG=" + HANG +
                ", DROPS=" + DROPS +
                ", REWARDS=" + REWARDS +
                '}';
    }
}
