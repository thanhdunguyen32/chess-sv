package game.module.rank.bean;

import java.util.List;

public class DbRankKingPvp {

    List<DbRankKingPvp.DbRankKingPvp1> rankItem;
    List<DBRankMyItem> rankMy;

    public List<DbRankKingPvp.DbRankKingPvp1> getRankItem() {
        return rankItem;
    }

    public void setRankItem(List<DbRankKingPvp.DbRankKingPvp1> rankItem) {
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

    public static final class DbRankKingPvp1 {
        private Integer playerId;
        private Integer rankChange;
        private Integer stage;
        private Integer star;

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

        public Integer getStage() {
            return stage;
        }

        public void setStage(Integer stage) {
            this.stage = stage;
        }

        public Integer getStar() {
            return star;
        }

        public void setStar(Integer star) {
            this.star = star;
        }

        @Override
        public String toString() {
            return "DbRankKingPvp1{" +
                    "playerId=" + playerId +
                    ", rankChange=" + rankChange +
                    ", stage=" + stage +
                    ", star=" + star +
                    '}';
        }
    }

}
