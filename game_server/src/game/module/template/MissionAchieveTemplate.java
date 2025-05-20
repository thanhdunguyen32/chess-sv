package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbMissionAchieve.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MissionAchieveTemplate {

    @JsonProperty("GETMARK")
    private Integer GETMARK;

    @JsonProperty("MISSION")
    private List<MissionAchieveTemplateMission> MISSION;

    public Integer getGETMARK() {
        return GETMARK;
    }

    public void setGETMARK(Integer GETMARK) {
        this.GETMARK = GETMARK;
    }

    public List<MissionAchieveTemplateMission> getMISSION() {
        return MISSION;
    }

    public void setMISSION(List<MissionAchieveTemplateMission> MISSION) {
        this.MISSION = MISSION;
    }

    @Override
    public String toString() {
        return "MissionAchieveTemplate{" +
                "GETMARK=" + GETMARK +
                ", MISSION=" + MISSION +
                '}';
    }

    public static final class MissionAchieveTemplateMission{
        @JsonProperty("ID")
        private Integer ID;

        @JsonProperty("NAME")
        private String NAME;

        @JsonProperty("PMARK")
        private String PMARK;

        @JsonProperty("EXID")
        private String EXID;

        @JsonProperty("CNUM")
        private Integer CNUM;

        @JsonProperty("GETMARK")
        private Integer GETMARK;

        @JsonProperty("GNUM")
        private Integer GNUM;

        @JsonProperty("REWARD")
        private List<RewardTemplateSimple> REWARD;

        @JsonProperty("NAME_VN")
        private String NAME_VN;

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

        public String getEXID() {
            return EXID;
        }

        public void setEXID(String EXID) {
            this.EXID = EXID;
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

        public Integer getGNUM() {
            return GNUM;
        }

        public void setGNUM(Integer GNUM) {
            this.GNUM = GNUM;
        }

        public List<RewardTemplateSimple> getREWARD() {
            return REWARD;
        }

        public void setREWARD(List<RewardTemplateSimple> REWARD) {
            this.REWARD = REWARD;
        }

        public String getNAME_VN() {
            return NAME_VN;
        }

        public void setNAME_VN(String NAME_VN) {
            this.NAME_VN = NAME_VN;
        }

        @Override
        public String toString() {
            return "MissionAchieveTemplateMission{" +
                    "ID=" + ID +
                    ", NAME='" + NAME + '\'' +
                    ", PMARK='" + PMARK + '\'' +
                    ", EXID='" + EXID + '\'' +
                    ", CNUM=" + CNUM +
                    ", GETMARK=" + GETMARK +
                    ", GNUM=" + GNUM +
                    ", REWARD=" + REWARD +
                    ", NAME_VN='" + NAME_VN + '\'' +
                    '}';
        }
    }

}
