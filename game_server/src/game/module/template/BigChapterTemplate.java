package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbBigChapter.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BigChapterTemplate {

    private Integer id;

    @JsonProperty("TYPE")
    private Integer TYPE;

    @JsonProperty("LOCK")
    private Integer LOCK;

    @JsonProperty("GENERAL")
    private Integer GENERAL;

    @JsonProperty("GLEVEL")
    private Integer GLEVEL;

    @JsonProperty("RATIO")
    private Integer RATIO;

    @JsonProperty("TIMES")
    private Integer TIMES;

    @JsonProperty("PASSID")
    private Integer PASSID;

    @JsonProperty("PASSCOUNT")
    private Integer PASSCOUNT;

    @JsonProperty("REWARDS")
    private List<RewardTemplateSimple> REWARDS;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }

    public Integer getLOCK() {
        return LOCK;
    }

    public void setLOCK(Integer LOCK) {
        this.LOCK = LOCK;
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

    public Integer getRATIO() {
        return RATIO;
    }

    public void setRATIO(Integer RATIO) {
        this.RATIO = RATIO;
    }

    public Integer getTIMES() {
        return TIMES;
    }

    public void setTIMES(Integer TIMES) {
        this.TIMES = TIMES;
    }

    public Integer getPASSID() {
        return PASSID;
    }

    public void setPASSID(Integer PASSID) {
        this.PASSID = PASSID;
    }

    public Integer getPASSCOUNT() {
        return PASSCOUNT;
    }

    public void setPASSCOUNT(Integer PASSCOUNT) {
        this.PASSCOUNT = PASSCOUNT;
    }

    public List<RewardTemplateSimple> getREWARDS() {
        return REWARDS;
    }

    public void setREWARDS(List<RewardTemplateSimple> REWARDS) {
        this.REWARDS = REWARDS;
    }

    @Override
    public String toString() {
        return "BigChapterTemplate{" +
                "id=" + id +
                ", TYPE=" + TYPE +
                ", LOCK=" + LOCK +
                ", GENERAL=" + GENERAL +
                ", GLEVEL=" + GLEVEL +
                ", RATIO=" + RATIO +
                ", TIMES=" + TIMES +
                ", PASSID=" + PASSID +
                ", PASSCOUNT=" + PASSCOUNT +
                ", REWARDS=" + REWARDS +
                '}';
    }
}
