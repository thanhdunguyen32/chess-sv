package game.module.manor.bean;

import game.module.template.ChapterBattleTemplate;

public class DbBattleGeneral {

    private ChapterBattleTemplate chapterBattleTemplate;
    private Integer nowhp;
    private Integer maxhp;

    public ChapterBattleTemplate getChapterBattleTemplate() {
        return chapterBattleTemplate;
    }

    public void setChapterBattleTemplate(ChapterBattleTemplate chapterBattleTemplate) {
        this.chapterBattleTemplate = chapterBattleTemplate;
    }

    public Integer getNowhp() {
        return nowhp;
    }

    public void setNowhp(Integer nowhp) {
        this.nowhp = nowhp;
    }

    public Integer getMaxhp() {
        return maxhp;
    }

    public void setMaxhp(Integer maxhp) {
        this.maxhp = maxhp;
    }

    @Override
    public String toString() {
        return "DbBattleGeneral{" +
                "chapterBattleTemplate=" + chapterBattleTemplate +
                ", nowhp=" + nowhp +
                ", maxhp=" + maxhp +
                '}';
    }
}
