package game.module.rank.bean;

import java.util.List;

public class DbRankArena {

    List<DbRankArena1> rankItem;
    List<DBRankMyItem> rankMy;

    public List<DbRankArena1> getRankItem() {
        return rankItem;
    }

    public void setRankItem(List<DbRankArena1> rankItem) {
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
        return "DbRankArea{" +
                "rankItem=" + rankItem +
                ", rankMy=" + rankMy +
                '}';
    }

    public static final class DbRankArena1 {
        private Integer playerId;
        private Integer rankChange;
        private Integer score;

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

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "DbRankArena1{" +
                    "playerId=" + playerId +
                    ", rankChange=" + rankChange +
                    ", score=" + score +
                    '}';
        }
    }
}
