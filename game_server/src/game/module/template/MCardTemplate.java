package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "dbMCard.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MCardTemplate {

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("ETIME")
    private Integer ETIME;

    @JsonProperty("TYPR")
    private Integer TYPR;

    @JsonProperty("VALUE")
    private Integer VALUE;

    @JsonProperty("FIRST")
    private Integer FIRST;

    @JsonProperty("ARRACT")
    private List<Map<String,Integer>> ARRACT;

    @JsonProperty("ARRREW")
    private List<Map<String,Integer>> ARRREW;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getETIME() {
        return ETIME;
    }

    public void setETIME(Integer ETIME) {
        this.ETIME = ETIME;
    }

    public Integer getTYPR() {
        return TYPR;
    }

    public void setTYPR(Integer TYPR) {
        this.TYPR = TYPR;
    }

    public Integer getVALUE() {
        return VALUE;
    }

    public void setVALUE(Integer VALUE) {
        this.VALUE = VALUE;
    }

    public Integer getFIRST() {
        return FIRST;
    }

    public void setFIRST(Integer FIRST) {
        this.FIRST = FIRST;
    }

    public List<Map<String, Integer>> getARRACT() {
        return ARRACT;
    }

    public void setARRACT(List<Map<String, Integer>> ARRACT) {
        this.ARRACT = ARRACT;
    }

    public List<Map<String, Integer>> getARRREW() {
        return ARRREW;
    }

    public void setARRREW(List<Map<String, Integer>> ARRREW) {
        this.ARRREW = ARRREW;
    }

    @Override
    public String toString() {
        return "MCardTemplate{" +
                "NAME='" + NAME + '\'' +
                ", ETIME=" + ETIME +
                ", TYPR=" + TYPR +
                ", VALUE=" + VALUE +
                ", FIRST=" + FIRST +
                ", ARRACT=" + ARRACT +
                ", ARRREW=" + ARRREW +
                '}';
    }
}
