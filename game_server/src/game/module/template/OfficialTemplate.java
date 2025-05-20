package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbOfficial.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficialTemplate {

    @JsonProperty("ID")
    private Integer ID;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("PMARK")
    private String PMARK;

    @JsonProperty("CNUM")
    private Integer CNUM;

    @JsonProperty("GETMARK")
    private Integer GETMARK;

    @JsonProperty("REWARD")
    private List<RewardTemplateSimple> REWARD;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPMARK() {
        return PMARK;
    }

    public void setPMARK(String PMARK) {
        this.PMARK = PMARK;
    }

    public Integer getCNUM() {
        return CNUM;
    }

    public void setCNUM(Integer CNUM) {
        this.CNUM = CNUM;
    }

    public Integer getGETMARK() {
        return GETMARK;
    }

    public void setGETMARK(Integer GETMARK) {
        this.GETMARK = GETMARK;
    }

    public List<RewardTemplateSimple> getREWARD() {
        return REWARD;
    }

    public void setREWARD(List<RewardTemplateSimple> REWARD) {
        this.REWARD = REWARD;
    }

    @Override
    public String toString() {
        return "OfficialTemplate{" +
                "ID=" + ID +
                ", NAME='" + NAME + '\'' +
                ", PMARK=" + PMARK +
                ", CNUM=" + CNUM +
                ", GETMARK=" + GETMARK +
                ", REWARD=" + REWARD +
                '}';
    }
}
