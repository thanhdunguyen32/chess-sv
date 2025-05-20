package game.module.pvp.bean;

import ws.WsMessageBase;

import java.util.List;

public class PvpRecord {

    private Integer id;

    private Integer playerId;

    private DbPvpRecord dbPvpRecord;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public DbPvpRecord getDbPvpRecord() {
        return dbPvpRecord;
    }

    public void setDbPvpRecord(DbPvpRecord dbPvpRecord) {
        this.dbPvpRecord = dbPvpRecord;
    }

    @Override
    public String toString() {
        return "PvpRecord{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", dbPvpRecord=" + dbPvpRecord +
                '}';
    }

    public static final class DbPvpRecord {
        private List<WsMessageBase.IOPvpRecord> records;

        public List<WsMessageBase.IOPvpRecord> getRecords() {
            return records;
        }

        public void setRecords(List<WsMessageBase.IOPvpRecord> records) {
            this.records = records;
        }

        @Override
        public String toString() {
            return "DbPvpRecord{" +
                    "records=" + records +
                    '}';
        }
    }
}
