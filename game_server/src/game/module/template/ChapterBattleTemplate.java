package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbChapterBattle.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChapterBattleTemplate {

    @JsonProperty("gsid")
    private Integer gsid;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("class")
    private Integer pclass;

    @JsonProperty("hpcover")
    private Integer hpcover;

    @JsonProperty("exhp")
    private Float exhp;

    @JsonProperty("exatk")
    private Float exatk;

    public ChapterBattleTemplate() {
    }

    public ChapterBattleTemplate(Integer gsid, Integer level, Integer pclass, Integer hpcover, Float exhp, Float exatk) {
        this.gsid = gsid;
        this.level = level;
        this.pclass = pclass;
        this.hpcover = hpcover;
        this.exhp = exhp;
        this.exatk = exatk;
    }

    public Integer getGsid() {
        return gsid;
    }

    public void setGsid(Integer gsid) {
        this.gsid = gsid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPclass() {
        return pclass;
    }

    public void setPclass(Integer pclass) {
        this.pclass = pclass;
    }

    public Float getExhp() {
        return exhp;
    }

    public void setExhp(Float exhp) {
        this.exhp = exhp;
    }

    public Float getExatk() {
        return exatk;
    }

    public void setExatk(Float exatk) {
        this.exatk = exatk;
    }

    public Integer getHpcover() {
        return hpcover;
    }

    public void setHpcover(Integer hpcover) {
        this.hpcover = hpcover;
    }

    @Override
    public String toString() {
        return "ChapterBattleTemplate{" +
                "gsid=" + gsid +
                ", level=" + level +
                ", pclass=" + pclass +
                ", hpcover=" + hpcover +
                ", exhp=" + exhp +
                ", exatk=" + exatk +
                '}';
    }
}
