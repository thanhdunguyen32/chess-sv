package ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import game.util.concurrent.OrderedEventRunnable;

public class CraftManager {
    private static CraftManager instance = new CraftManager();
    private Map<Integer, CraftRoom> craftRooms = new ConcurrentHashMap<>();
    
    private CraftManager() {}
    
    public static CraftManager getInstance() {
        return instance;
    }
    
    public CraftRoom getCraftRoom(int roomId) {
        return craftRooms.get(roomId);
    }
    
    public void execute(OrderedEventRunnable runnable) {
        runnable.run();
    }
    
    public void remove(int roomId) {
        craftRooms.remove(roomId);
    }
    
    public void removeSchedule(int roomId) {
        // TODO: Implement schedule removal
    }
    
    public void removeCraftRoom(int roomId) {
        craftRooms.remove(roomId);
    }
    
    public void addCraftRoom(int roomId, CraftRoom room) {
        craftRooms.put(roomId, room);
    }
} 