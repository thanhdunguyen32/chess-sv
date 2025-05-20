package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.Map;

@ExcelTemplateAnn(file = "myFixAward.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixAwardTemplate {

    @JsonProperty("award")
    private Map<Integer, Integer> award;

    @JsonProperty("desc")
    private String desc;

    public Map<Integer, Integer> getAward() {
        return award;
    }

    public void setAward(Map<Integer, Integer> award) {
        this.award = award;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "FixAwardTemplate{" +
                ", award=" + award +
                ", desc='" + desc + '\'' +
                '}';
    }
}
