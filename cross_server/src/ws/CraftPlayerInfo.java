package ws;

public class CraftPlayerInfo {
    private int serverId;
    private int playerId;
    
    public CraftPlayerInfo(int serverId, int playerId) {
        this.serverId = serverId;
        this.playerId = playerId;
    }
    
    public int getServerId() {
        return serverId;
    }
    
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + playerId;
        result = prime * result + serverId;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CraftPlayerInfo other = (CraftPlayerInfo) obj;
        if (playerId != other.playerId)
            return false;
        if (serverId != other.serverId)
            return false;
        return true;
    }
} 