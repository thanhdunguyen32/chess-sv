package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.Arrays;

@ExcelTemplateAnn(file = "myFormationRobot.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyFormationRobotTemplate {

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("gsid")
    private int[] gsid;

    @JsonProperty("class")
    private Integer pclass;

    @JsonProperty("exhp")
    private Float exhp;

    @JsonProperty("exatk")
    private Float exatk;

    @JsonProperty("online_num")
    private Integer online_num;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public int[] getGsid() {
        return gsid;
    }

    public void setGsid(int[] gsid) {
        this.gsid = gsid;
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

    public Integer getOnline_num() {
        return online_num;
    }

    public void setOnline_num(Integer online_num) {
        this.online_num = online_num;
    }

    @Override
    public String toString() {
        return "MyFormationRobotTemplate{" +
                "level=" + level +
                ", gsid=" + Arrays.toString(gsid) +
                ", pclass=" + pclass +
                ", exhp=" + exhp +
                ", exatk=" + exatk +
                ", online_num=" + online_num +
                '}';
    }

}
