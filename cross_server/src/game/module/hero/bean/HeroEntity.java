package game.module.hero.bean;

public class HeroEntity {
    private int heroId;
    private int level;
    private int hp;
    private int sp;
    
    public HeroEntity(int heroId) {
        this.heroId = heroId;
    }
    
    public int getHeroId() {
        return heroId;
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
} 