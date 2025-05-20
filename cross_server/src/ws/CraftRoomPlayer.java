package ws;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import game.entity.PlayingRole;

public class CraftRoomPlayer {
    private PlayingRole playingRole;
    private boolean downline;
    private Map<Integer, CraftHeroBean> heroInfoMap;
    private List<Integer> selectedHeros;
    private int playerId;
    private int serverId;
    
    public static class CraftHeroBean {
        private int hp;
        private int maxHp;
        private boolean die;
        
        public int getHp() {
            return hp;
        }
        
        public void setHp(int hp) {
            this.hp = hp;
        }
        
        public int getMaxHp() {
            return maxHp;
        }
        
        public void setMaxHp(int maxHp) {
            this.maxHp = maxHp;
        }
        
        public boolean isDie() {
            return die;
        }
        
        public void setDie(boolean die) {
            this.die = die;
        }
    }
    
    public CraftRoomPlayer(PlayingRole playingRole) {
        this.playingRole = playingRole;
        this.heroInfoMap = new ConcurrentHashMap<>();
        if (playingRole != null) {
            this.playerId = playingRole.getId();
            this.serverId = playingRole.getPlayerBean().getServerId();
        }
    }
    
    public PlayingRole getPlayingRole() {
        return playingRole;
    }
    
    public void setPlayingRole(PlayingRole playingRole) {
        this.playingRole = playingRole;
    }
    
    public boolean isDownline() {
        return downline;
    }
    
    public void setDownline(boolean downline) {
        this.downline = downline;
    }
    
    public Map<Integer, CraftHeroBean> getHeroInfoMap() {
        return heroInfoMap;
    }
    
    public List<Integer> getSelectedHeros() {
        return selectedHeros;
    }
    
    public void setSelectedHeros(List<Integer> selectedHeros) {
        this.selectedHeros = selectedHeros;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public int getServerId() {
        return serverId;
    }
} 