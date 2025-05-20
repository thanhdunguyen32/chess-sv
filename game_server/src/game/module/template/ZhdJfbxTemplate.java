package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actJfbx.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdJfbxTemplate {

    private List<ZhdJfbxBox> box;
    private List<ZhdJfbxEvent> event;

    public List<ZhdJfbxBox> getBox() {
        return box;
    }

    public void setBox(List<ZhdJfbxBox> box) {
        this.box = box;
    }

    public List<ZhdJfbxEvent> getEvent() {
        return event;
    }

    public void setEvent(List<ZhdJfbxEvent> event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "ZhdJfbxTemplate{" +
                "box=" + box +
                ", event=" + event +
                '}';
    }

    public static final class ZhdJfbxBox{
        @JsonProperty("SCORE")
        private Integer SCORE;
        @JsonProperty("state")
        private Integer state;
        @JsonProperty("REWARD")
        private List<RewardTemplateSimple> REWARD;

        public Integer getSCORE() {
            return SCORE;
        }

        public void setSCORE(Integer SCORE) {
            this.SCORE = SCORE;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public List<RewardTemplateSimple> getREWARD() {
            return REWARD;
        }

        public void setREWARD(List<RewardTemplateSimple> REWARD) {
            this.REWARD = REWARD;
        }

        @Override
        public String toString() {
            return "ZhdJfbxBox{" +
                    "SCORE=" + SCORE +
                    ", state=" + state +
                    ", REWARD=" + REWARD +
                    '}';
        }
    }

    public static final class ZhdJfbxEvent{
        @JsonProperty("intro")
        private String intro;
        @JsonProperty("MARK")
        private Integer MARK;
        @JsonProperty("LIMIT")
        private Integer LIMIT;

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public Integer getMARK() {
            return MARK;
        }

        public void setMARK(Integer MARK) {
            this.MARK = MARK;
        }

        public Integer getLIMIT() {
            return LIMIT;
        }

        public void setLIMIT(Integer LIMIT) {
            this.LIMIT = LIMIT;
        }

        @Override
        public String toString() {
            return "ZhdJfbxEvent{" +
                    "intro='" + intro + '\'' +
                    ", MARK=" + MARK +
                    ", LIMIT=" + LIMIT +
                    '}';
        }
    }
}
