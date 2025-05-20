package ws;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CraftRoom {
    private int uuid;
    private Map<CraftPlayerInfo, CraftRoomPlayer> onlinePlayers;
    private List<CraftPlayerInfo> playerIdPair;
    private int[] dieCount;
    private BATTLE_STAUS battleStatus;
    private int selectHeroCount;
    
    public static enum BATTLE_STAUS {
        BATTLE_STAUS_NONE,
        BATTLE_STAUS_PROGRESS,
        BATTLE_STAUS_END
    }
    
    public CraftRoom() {
        this.onlinePlayers = new ConcurrentHashMap<>();
    }
    
    public int getUuid() {
        return uuid;
    }
    
    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
    
    public Map<CraftPlayerInfo, CraftRoomPlayer> getOnlinePlayers() {
        return onlinePlayers;
    }
    
    public List<CraftPlayerInfo> getPlayerIdPair() {
        return playerIdPair;
    }
    
    public void setPlayerIdPair(List<CraftPlayerInfo> playerIdPair) {
        this.playerIdPair = playerIdPair;
    }
    
    public int[] getDieCount() {
        return dieCount;
    }
    
    public void setDieCount(int[] dieCount) {
        this.dieCount = dieCount;
    }
    
    public BATTLE_STAUS getBattleStatus() {
        return battleStatus;
    }
    
    public void setBattleStatus(BATTLE_STAUS battleStatus) {
        this.battleStatus = battleStatus;
    }
    
    public int getSelectHeroCount() {
        return selectHeroCount;
    }
    
    public void setSelectHeroCount(int selectHeroCount) {
        this.selectHeroCount = selectHeroCount;
    }
} 