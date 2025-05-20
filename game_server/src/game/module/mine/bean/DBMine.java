package game.module.mine.bean;

import java.util.List;
import java.util.Map;

public class DBMine {

    private List<DBMineLevel> levels;
    private Map<Integer, DBMinePlayer> players;

    public List<DBMineLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<DBMineLevel> levels) {
        this.levels = levels;
    }

    public Map<Integer, DBMinePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, DBMinePlayer> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "DBMine{" +
                "levels=" + levels +
                ", players=" + players +
                '}';
    }
}
