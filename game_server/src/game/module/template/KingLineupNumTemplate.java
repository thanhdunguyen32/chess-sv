package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbKingLineupNum.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KingLineupNumTemplate {

    @JsonProperty("STAGE")
    private Integer STAGE;

    @JsonProperty("LINEUPNUM")
    private Integer LINEUPNUM;

    @Override
    public String toString() {
        return "KingLineupNumTemplate{" +
                "STAGE=" + STAGE +
                ", LINEUPNUM=" + LINEUPNUM +
                '}';
    }

    public Integer getSTAGE() {
        return STAGE;
    }

    public void setSTAGE(Integer STAGE) {
        this.STAGE = STAGE;
    }

    public Integer getLINEUPNUM() {
        return LINEUPNUM;
    }

    public void setLINEUPNUM(Integer LINEUPNUM) {
        this.LINEUPNUM = LINEUPNUM;
    }
}
