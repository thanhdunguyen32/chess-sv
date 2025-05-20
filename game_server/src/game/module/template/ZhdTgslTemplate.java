package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "actTgsl.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdTgslTemplate {

    private ZhdTgslTemplateBoss boss;

    private List<List<Integer>> reward;

    public ZhdTgslTemplateBoss getBoss() {
        return boss;
    }

    public void setBoss(ZhdTgslTemplateBoss boss) {
        this.boss = boss;
    }

    public List<List<Integer>> getReward() {
        return reward;
    }

    public void setReward(List<List<Integer>> reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "ZhdTgslTemplate{" +
                "boss=" + boss +
                ", reward=" + reward +
                '}';
    }

    public static final class ZhdTgslTemplateBoss{
        private Integer gsid;
        private String name;
        private Integer mark;
        private List<Integer> skill;
        private List<Integer> challrewards;
        private Integer level;
        private Long hp;
        private String name_vn;
        private Map<Integer, ChapterBattleTemplate> bset;

        public Integer getGsid() {
            return gsid;
        }

        public void setGsid(Integer gsid) {
            this.gsid = gsid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getMark() {
            return mark;
        }

        public void setMark(Integer mark) {
            this.mark = mark;
        }

        public List<Integer> getSkill() {
            return skill;
        }

        public void setSkill(List<Integer> skill) {
            this.skill = skill;
        }

        public List<Integer> getChallrewards() {
            return challrewards;
        }

        public void setChallrewards(List<Integer> challrewards) {
            this.challrewards = challrewards;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Long getHp() {
            return hp;
        }

        public void setHp(Long hp) {
            this.hp = hp;
        }

        public Map<Integer, ChapterBattleTemplate> getBset() {
            return bset;
        }

        public void setBset(Map<Integer, ChapterBattleTemplate> bset) {
            this.bset = bset;
        }

        public String getName_vn() {
            return name_vn;
        }

        public void setName_vn(String name_vn) {
            this.name_vn = name_vn;
        }

        @Override
        public String toString() {
            return "ZhdTgslTemplateBoss{" +
                    "gsid=" + gsid +
                    ", name='" + name + '\'' +
                    ", mark=" + mark +
                    ", skill=" + skill +
                    ", challrewards=" + challrewards +
                    ", level=" + level +
                    ", hp=" + hp +
                    ", bset=" + bset +
                    ", name_vn='" + name_vn + '\'' +
                    '}';
        }
    }

}
