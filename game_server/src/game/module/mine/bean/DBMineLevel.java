package game.module.mine.bean;

import java.util.List;

public class DBMineLevel {
    private List<DBMinePoint> minePoints;

    public List<DBMinePoint> getMinePoints() {
        return minePoints;
    }

    public void setMinePoints(List<DBMinePoint> minePoints) {
        this.minePoints = minePoints;
    }

    @Override
    public String toString() {
        return "DBMineLevel{" +
                "minePoints=" + minePoints +
                '}';
    }
}
