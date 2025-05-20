package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "myManor.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyManorTemplate {

    @JsonProperty("boss")
    private List<MyManorBossTemplate> boss;

    @JsonProperty("enemy")
    private List<List<Map<Integer, ChapterBattleTemplate>>> enemy;

    public List<MyManorBossTemplate> getBoss() {
        return boss;
    }

    public void setBoss(List<MyManorBossTemplate> boss) {
        this.boss = boss;
    }

    public List<List<Map<Integer, ChapterBattleTemplate>>> getEnemy() {
        return enemy;
    }

    public void setEnemy(List<List<Map<Integer, ChapterBattleTemplate>>> enemy) {
        this.enemy = enemy;
    }

    @Override
    public String toString() {
        return "MyManorTemplate{" +
                "boss=" + boss +
                ", enemy=" + enemy +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MyManorBossTemplate {

        @JsonProperty("bset")
        private Map<Integer, ChapterBattleTemplate> bset;
        @JsonProperty("maxhp")
        private Integer maxhp;
        @JsonProperty("bossid")
        private Integer bossid;

        public Map<Integer, ChapterBattleTemplate> getBset() {
            return bset;
        }

        public void setBset(Map<Integer, ChapterBattleTemplate> bset) {
            this.bset = bset;
        }

        public Integer getMaxhp() {
            return maxhp;
        }

        public void setMaxhp(Integer maxhp) {
            this.maxhp = maxhp;
        }

        public Integer getBossid() {
            return bossid;
        }

        public void setBossid(Integer bossid) {
            this.bossid = bossid;
        }

        @Override
        public String toString() {
            return "MyManorBossTemplate{" +
                    "bset=" + bset +
                    ", maxhp=" + maxhp +
                    ", bossid=" + bossid +
                    '}';
        }
    }
}
