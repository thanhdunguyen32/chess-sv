package game.module.pvp.dao;

import game.module.template.PvpSkillTemplate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PvpSkillTemplateCache {
    private static final PvpSkillTemplateCache INSTANCE = new PvpSkillTemplateCache();
    private Map<Integer, PvpSkillTemplate> skillTemplateMap;
    
    private PvpSkillTemplateCache() {
        skillTemplateMap = new ConcurrentHashMap<>();
    }
    
    public static PvpSkillTemplateCache getInstance() {
        return INSTANCE;
    }
    
    public PvpSkillTemplate getSkillTemplate(int skillId) {
        return skillTemplateMap.get(skillId);
    }
    
    public void addSkillTemplate(PvpSkillTemplate template) {
        skillTemplateMap.put(template.getSkillId(), template);
    }
    
    public void removeSkillTemplate(int skillId) {
        skillTemplateMap.remove(skillId);
    }
    
    public void clear() {
        skillTemplateMap.clear();
    }
} 