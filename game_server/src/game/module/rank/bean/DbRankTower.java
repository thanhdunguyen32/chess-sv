package game.module.rank.bean;

import java.util.List;

public class DbRankTower {

    List<DbRankTower.DbRankTower1> rankItem;
    List<DBRankMyItem> rankMy;

    public List<DbRankTower.DbRankTower1> getRankItem() {
        return rankItem;
    }

    public void setRankItem(List<DbRankTower.DbRankTower1> rankItem) {
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
        return "DbRankTower{" +
                "rankItem=" + rankItem +
                ", rankMy=" + rankMy +
                '}';
    }

    public static final class DbRankTower1 {
        private Integer playerId;
        private Integer rankChange;
        private Integer level;

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

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return "DbRankTower1{" +
                    "playerId=" + playerId +
                    ", rankChange=" + rankChange +
                    ", level=" + level +
                    '}';
        }
    }

}
