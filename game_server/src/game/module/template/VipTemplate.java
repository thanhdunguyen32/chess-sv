package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbVip.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VipTemplate {

    @JsonProperty("EXP")
    private Integer EXP;

    @JsonProperty("INTRO")
    private String INTRO;

    @JsonProperty("EXID")
    private String EXID;

    @JsonProperty("MARK")
    private Integer MARK;

    @JsonProperty("REWARD")
    private List<RewardTemplateSimple> REWARD;

    @JsonProperty("RIGHT")
    private VipTemplateRight RIGHT;

    @JsonProperty("BCARD")
    private List<RewardTemplateSimple> BCARD;

    public Integer getEXP() {
        return EXP;
    }

    public void setEXP(Integer EXP) {
        this.EXP = EXP;
    }

    public String getINTRO() {
        return INTRO;
    }

    public void setINTRO(String INTRO) {
        this.INTRO = INTRO;
    }

    public String getEXID() {
        return EXID;
    }

    public void setEXID(String EXID) {
        this.EXID = EXID;
    }

    public Integer getMARK() {
        return MARK;
    }

    public void setMARK(Integer MARK) {
        this.MARK = MARK;
    }

    public List<RewardTemplateSimple> getREWARD() {
        return REWARD;
    }

    public void setREWARD(List<RewardTemplateSimple> REWARD) {
        this.REWARD = REWARD;
    }

    public VipTemplateRight getRIGHT() {
        return RIGHT;
    }

    public void setRIGHT(VipTemplateRight RIGHT) {
        this.RIGHT = RIGHT;
    }

    public List<RewardTemplateSimple> getBCARD() {
        return BCARD;
    }

    public void setBCARD(List<RewardTemplateSimple> BCARD) {
        this.BCARD = BCARD;
    }

    @Override
    public String toString() {
        return "VipTemplate{" +
                "EXP=" + EXP +
                ", INTRO='" + INTRO + '\'' +
                ", EXID='" + EXID + '\'' +
                ", MARK=" + MARK +
                ", REWARD=" + REWARD +
                ", RIGHT=" + RIGHT +
                ", BCARD=" + BCARD +
                '}';
    }
}
