package game.module.mine.bean;

import java.util.List;

public class DBMinePlayer {
    private List<Integer> ownMinePoint;
    private List<Integer> gains;
    private List<DBMineBattleRecord> battleRecord;

    public List<Integer> getOwnMinePoint() {
        return ownMinePoint;
    }

    public void setOwnMinePoint(List<Integer> ownMinePoint) {
        this.ownMinePoint = ownMinePoint;
    }

    public List<Integer> getGains() {
        return gains;
    }

    public void setGains(List<Integer> gains) {
        this.gains = gains;
    }

    public List<DBMineBattleRecord> getBattleRecord() {
        return battleRecord;
    }

    public void setBattleRecord(List<DBMineBattleRecord> battleRecord) {
        this.battleRecord = battleRecord;
    }

    @Override
    public String toString() {
        return "DBMinePlayer{" +
                "ownMinePoint=" + ownMinePoint +
                ", gains=" + gains +
                ", battleRecord=" + battleRecord +
                '}';
    }
}
