package game.module.craft.bean;

import game.module.pvp.bean.PvpPlayer;

public class CraftRoomPlayer extends PvpPlayer {
    private int heroId;
    private int skillId;
    private boolean isReady;
    
    public CraftRoomPlayer(int playerId, int serverId) {
        super(playerId, serverId);
        this.isReady = false;
    }
    
    public int getHeroId() {
        return heroId;
    }
    
    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }
    
    public int getSkillId() {
        return skillId;
    }
    
    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }
    
    public boolean isReady() {
        return isReady;
    }
    
    public void setReady(boolean ready) {
        isReady = ready;
    }
} 