package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actTnqw.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdTnqwTemplate {

    private List<ZhdTnqwEvent> event;
    private List<ZhdTnqwBosslist> bosslist;

    public List<ZhdTnqwEvent> getEvent() {
        return event;
    }

    public void setEvent(List<ZhdTnqwEvent> event) {
        this.event = event;
    }

    public List<ZhdTnqwBosslist> getBosslist() {
        return bosslist;
    }

    public void setBosslist(List<ZhdTnqwBosslist> bosslist) {
        this.bosslist = bosslist;
    }

    @Override
    public String toString() {
        return "ZhdTnqwTemplate{" +
                "event=" + event +
                ", bosslist=" + bosslist +
                '}';
    }

    public static final class ZhdTnqwEvent{
        private Integer mark;
        private Integer limit;
        private String intro;

        public Integer getMark() {
            return mark;
        }

        public void setMark(Integer mark) {
            this.mark = mark;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        @Override
        public String toString() {
            return "ZhdTnqwEvent{" +
                    "mark=" + mark +
                    ", limit=" + limit +
                    ", intro='" + intro + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ZhdTnqwBosslist{
        private Integer status;
        private List<String> rewardgsids;
        private Integer actscore;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<String> getRewardgsids() {
            return rewardgsids;
        }

        public void setRewardgsids(List<String> rewardgsids) {
            this.rewardgsids = rewardgsids;
        }

        public Integer getActscore() {
            return actscore;
        }

        public void setActscore(Integer actscore) {
            this.actscore = actscore;
        }

        @Override
        public String toString() {
            return "ZhdTnqwBosslist{" +
                    "status=" + status +
                    ", rewardgsids=" + rewardgsids +
                    ", actscore=" + actscore +
                    '}';
        }
    }
}
