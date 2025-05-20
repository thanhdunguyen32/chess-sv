package game.module.craft.bean;

import java.util.ArrayList;
import java.util.List;

public class CraftRoom {
    private int roomId;
    private List<CraftRoomPlayer> players;
    
    public CraftRoom(int roomId) {
        this.roomId = roomId;
        this.players = new ArrayList<>();
    }
    
    public int getRoomId() {
        return roomId;
    }
    
    public List<CraftRoomPlayer> getPlayers() {
        return players;
    }
    
    public void addPlayer(CraftRoomPlayer player) {
        players.add(player);
    }
    
    public void removePlayer(int playerId, int serverId) {
        players.removeIf(p -> p.getPlayerId() == playerId && p.getServerId() == serverId);
    }
    
    public CraftRoomPlayer getPlayer(int playerId, int serverId) {
        return players.stream()
                .filter(p -> p.getPlayerId() == playerId && p.getServerId() == serverId)
                .findFirst()
                .orElse(null);
    }
} 