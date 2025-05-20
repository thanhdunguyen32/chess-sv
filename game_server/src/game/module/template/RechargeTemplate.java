package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "dbRecharge.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RechargeTemplate {

    @JsonProperty("PID")
    private String PID;

    @JsonProperty("PRICE")
    private Integer PRICE;

    @JsonProperty("FIRST")
    private Integer FIRST;

    @JsonProperty("VIPEXP")
    private Integer VIPEXP;

    @JsonProperty("YB")
    private Integer YB;

    @JsonProperty("SIGN")
    private Integer SIGN;

    @JsonProperty("EXYB")
    private Integer EXYB;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public void setPRICE(Integer PRICE) {
        this.PRICE = PRICE;
    }

    public Integer getVIPEXP() {
        return VIPEXP;
    }

    public void setVIPEXP(Integer VIPEXP) {
        this.VIPEXP = VIPEXP;
    }

    public Integer getYB() {
        return YB;
    }

    public void setYB(Integer YB) {
        this.YB = YB;
    }

    public Integer getSIGN() {
        return SIGN;
    }

    public void setSIGN(Integer SIGN) {
        this.SIGN = SIGN;
    }

    public Integer getEXYB() {
        return EXYB;
    }

    public void setEXYB(Integer EXYB) {
        this.EXYB = EXYB;
    }

    public Integer getFIRST() {
        return FIRST;
    }

    public void setFIRST(Integer FIRST) {
        this.FIRST = FIRST;
    }
}
