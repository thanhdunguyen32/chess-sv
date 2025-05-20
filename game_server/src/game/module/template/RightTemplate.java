package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

@ExcelTemplateAnn(file = "dbRight.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RightTemplate {

    @JsonProperty("PID")
    private String PID;

    @JsonProperty("DAY")
    private Integer DAY;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("COIN")
    private String COIN;

    @JsonProperty("NUM")
    private Integer NUM;

    @JsonProperty("REWARD")
    private List<RewardTemplateSimple> REWARD;

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public Integer getDAY() {
        return DAY;
    }

    public void setDAY(Integer DAY) {
        this.DAY = DAY;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCOIN() {
        return COIN;
    }

    public void setCOIN(String COIN) {
        this.COIN = COIN;
    }

    public Integer getNUM() {
        return NUM;
    }

    public void setNUM(Integer NUM) {
        this.NUM = NUM;
    }

    public List<RewardTemplateSimple> getREWARD() {
        return REWARD;
    }

    public void setREWARD(List<RewardTemplateSimple> REWARD) {
        this.REWARD = REWARD;
    }

    @Override
    public String toString() {
        return "RightTemplate{" +
                "PID='" + PID + '\'' +
                ", DAY=" + DAY +
                ", NAME='" + NAME + '\'' +
                ", COIN='" + COIN + '\'' +
                ", NUM=" + NUM +
                ", REWARD=" + REWARD +
                '}';
    }
}
