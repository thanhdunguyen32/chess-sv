package game.module.template;

public class PvpSkillTemplate {
    private int skillId;
    private String name;
    private int damage;
    private int cooldown;
    private int range;
    
    public PvpSkillTemplate(int skillId) {
        this.skillId = skillId;
    }
    
    public int getSkillId() {
        return skillId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    public int getCooldown() {
        return cooldown;
    }
    
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
    
    public int getRange() {
        return range;
    }
    
    public void setRange(int range) {
        this.range = range;
    }
} 