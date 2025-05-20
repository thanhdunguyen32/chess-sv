package game.module.cross.bean;

public class CrossBossBegin {
    private int bossId;
    private int playerId;
    private int serverId;
    private long startTime;
    
    public CrossBossBegin(int bossId, int playerId, int serverId) {
        this.bossId = bossId;
        this.playerId = playerId;
        this.serverId = serverId;
        this.startTime = System.currentTimeMillis();
    }
    
    public int getBossId() {
        return bossId;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public int getServerId() {
        return serverId;
    }
    
    public long getStartTime() {
        return startTime;
    }
} 