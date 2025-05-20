package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "dbMActivityGp.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MActivityGpTemplate {

    @JsonProperty("LEVEL")
    private Integer LEVEL;

    @JsonProperty("YBNUM")
    private Integer YBNUM;

    public Integer getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(Integer LEVEL) {
        this.LEVEL = LEVEL;
    }

    public Integer getYBNUM() {
        return YBNUM;
    }

    public void setYBNUM(Integer YBNUM) {
        this.YBNUM = YBNUM;
    }

    @Override
    public String toString() {
        return "MActivityGpTemplate{" +
                "LEVEL=" + LEVEL +
                ", YBNUM=" + YBNUM +
                '}';
    }
}
