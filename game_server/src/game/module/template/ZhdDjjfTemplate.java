package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actCzlb.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdDjjfTemplate {

    private Integer looplimit;
    private Integer currentloop;
    private List<ZhdDjjfMission> missions;

    public Integer getLooplimit() {
        return looplimit;
    }

    public void setLooplimit(Integer looplimit) {
        this.looplimit = looplimit;
    }

    public Integer getCurrentloop() {
        return currentloop;
    }

    public void setCurrentloop(Integer currentloop) {
        this.currentloop = currentloop;
    }

    public List<ZhdDjjfMission> getMissions() {
        return missions;
    }

    public void setMissions(List<ZhdDjjfMission> missions) {
        this.missions = missions;
    }

    @Override
    public String toString() {
        return "ZhdDjjfTemplate{" +
                "looplimit=" + looplimit +
                ", currentloop=" + currentloop +
                ", missions=" + missions +
                '}';
    }

    public static final class ZhdDjjfMission{
        @JsonProperty("NUM")
        private Integer NUM;
        @JsonProperty("cur")
        private Integer cur;
        @JsonProperty("NAME")
        private String NAME;
        @JsonProperty("ITEMS")
        private List<RewardTemplateSimple> ITEMS;
        @JsonProperty("status")
        private Integer status;
        @JsonProperty("NAME_VN")
        private String NAME_VN;
        public Integer getNUM() {
            return NUM;
        }

        public void setNUM(Integer NUM) {
            this.NUM = NUM;
        }

        public Integer getCur() {
            return cur;
        }

        public void setCur(Integer cur) {
            this.cur = cur;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public List<RewardTemplateSimple> getITEMS() {
            return ITEMS;
        }

        public void setITEMS(List<RewardTemplateSimple> ITEMS) {
            this.ITEMS = ITEMS;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getNAME_VN() {
            return NAME_VN;
        }

        public void setNAME_VN(String NAME_VN) {
            this.NAME_VN = NAME_VN;
        }

        @Override
        public String toString() {
            return "ZhdDjjfMission{" +
                    "NUM=" + NUM +
                    ", cur=" + cur +
                    ", NAME='" + NAME + '\'' +
                    ", ITEMS=" + ITEMS +
                    ", status=" + status +
                    ", NAME_VN='" + NAME_VN + '\'' +
                    '}';
        }
    }
}
