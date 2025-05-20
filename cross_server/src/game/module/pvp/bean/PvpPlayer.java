package game.module.pvp.bean;

public class PvpPlayer {
    public static enum PVP_BATTLE_STATUS {
        NONE,
        PREPARE,
        FIGHTING,
        END
    }
    
    private int playerId;
    private int serverId;
    private PVP_BATTLE_STATUS status;
    
    public PvpPlayer(int playerId, int serverId) {
        this.playerId = playerId;
        this.serverId = serverId;
        this.status = PVP_BATTLE_STATUS.NONE;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public int getServerId() {
        return serverId;
    }
    
    public PVP_BATTLE_STATUS getStatus() {
        return status;
    }
    
    public void setStatus(PVP_BATTLE_STATUS status) {
        this.status = status;
    }
} 