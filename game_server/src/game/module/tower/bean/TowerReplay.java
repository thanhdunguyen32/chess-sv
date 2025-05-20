package game.module.tower.bean;

import ws.WsMessageBase;

import java.util.List;

public class TowerReplay {

    private Integer id;

    private Integer towerLevel;

    private DbTowerReplay dbTowerReplay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTowerLevel() {
        return towerLevel;
    }

    public void setTowerLevel(Integer towerLevel) {
        this.towerLevel = towerLevel;
    }

    public DbTowerReplay getDbTowerReplay() {
        return dbTowerReplay;
    }

    public void setDbTowerReplay(DbTowerReplay dbTowerReplay) {
        this.dbTowerReplay = dbTowerReplay;
    }

    @Override
    public String toString() {
        return "TowerReplay{" +
                "id=" + id +
                ", towerLevel=" + towerLevel +
                ", dbTowerReplay=" + dbTowerReplay +
                '}';
    }

    public static final class DbTowerReplay{
        private List<WsMessageBase.IOPvpRecord> records;

        public List<WsMessageBase.IOPvpRecord> getRecords() {
            return records;
        }

        public void setRecords(List<WsMessageBase.IOPvpRecord> records) {
            this.records = records;
        }

        @Override
        public String toString() {
            return "DbTowerReplay{" +
                    "records=" + records +
                    '}';
        }
    }
}
