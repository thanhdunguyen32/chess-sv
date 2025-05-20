package game.module.cross.bean;

public class CrossHeroEntity {
    private int heroId;
    private int playerId;
    private int serverId;
    private int level;
    private int hp;
    private int sp;
    private int attack;
    private int defense;
    private int speed;
    
    public CrossHeroEntity(int heroId, int playerId, int serverId) {
        this.heroId = heroId;
        this.playerId = playerId;
        this.serverId = serverId;
    }
    
    public int getHeroId() {
        return heroId;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public int getServerId() {
        return serverId;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getHp() {
        return hp;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public int getSp() {
        return sp;
    }
    
    public void setSp(int sp) {
        this.sp = sp;
    }
    
    public int getAttack() {
        return attack;
    }
    
    public void setAttack(int attack) {
        this.attack = attack;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public void setDefense(int defense) {
        this.defense = defense;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
} 