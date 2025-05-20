package game.module.rank.bean;

import java.util.List;

public class DbRankDungeon {

    List<DbRankDungeon.DbRankDungeon1> rankItem;
    List<DBRankMyItem> rankMy;

    public List<DbRankDungeon.DbRankDungeon1> getRankItem() {
        return rankItem;
    }

    public void setRankItem(List<DbRankDungeon.DbRankDungeon1> rankItem) {
        this.rankItem = rankItem;
    }

    public List<DBRankMyItem> getRankMy() {
        return rankMy;
    }

    public void setRankMy(List<DBRankMyItem> rankMy) {
        this.rankMy = rankMy;
    }

    @Override
    public String toString() {
        return "DbRankDungeon{" +
                "rankItem=" + rankItem +
                ", rankMy=" + rankMy +
                '}';
    }

    public static final class DbRankDungeon1 {
        private Integer playerId;
        private Integer rankChange;
        private Integer chapter;
        private Integer node;

        public Integer getPlayerId() {
            return playerId;
        }

        public void setPlayerId(Integer playerId) {
            this.playerId = playerId;
        }

        public Integer getRankChange() {
            return rankChange;
        }

        public void setRankChange(Integer rankChange) {
            this.rankChange = rankChange;
        }

        public Integer getChapter() {
            return chapter;
        }

        public void setChapter(Integer chapter) {
            this.chapter = chapter;
        }

        public Integer getNode() {
            return node;
        }

        public void setNode(Integer node) {
            this.node = node;
        }

        @Override
        public String toString() {
            return "DbRankDungeon1{" +
                    "playerId=" + playerId +
                    ", rankChange=" + rankChange +
                    ", chapter=" + chapter +
                    ", node=" + node +
                    '}';
        }
    }

}
