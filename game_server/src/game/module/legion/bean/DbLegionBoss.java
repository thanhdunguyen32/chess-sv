package game.module.legion.bean;

import java.util.List;
import java.util.Map;

public class DbLegionBoss {
    private Integer chapterIndex;
    private List<LegionBossDamage> records;
    private Long nowhp;

    public Integer getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(Integer chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public List<LegionBossDamage> getRecords() {
        return records;
    }

    public void setRecords(List<LegionBossDamage> records) {
        this.records = records;
    }

    public Long getNowhp() {
        return nowhp;
    }

    public void setNowhp(Long nowhp) {
        this.nowhp = nowhp;
    }

    @Override
    public String toString() {
        return "DbLegionBoss{" +
                "chapterIndex=" + chapterIndex +
                ", records=" + records +
                ", nowhp=" + nowhp +
                '}';
    }

    public static final class LegionBossDamage {
        private Map<Integer, Long> damageList;
        private Map<Integer, Long> lastDamageMap;

        public Map<Integer, Long> getDamageList() {
            return damageList;
        }

        public void setDamageList(Map<Integer, Long> damageList) {
            this.damageList = damageList;
        }

        public Map<Integer, Long> getLastDamageMap() {
            return lastDamageMap;
        }

        public void setLastDamageMap(Map<Integer, Long> lastDamageMap) {
            this.lastDamageMap = lastDamageMap;
        }

        @Override
        public String toString() {
            return "LegionBossDamage{" +
                    "damageList=" + damageList +
                    ", lastDamageMap=" + lastDamageMap +
                    '}';
        }
    }
}
