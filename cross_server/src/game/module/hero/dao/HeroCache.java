package game.module.hero.dao;

import game.module.hero.bean.HeroEntity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeroCache {
    private static final HeroCache INSTANCE = new HeroCache();
    private Map<Integer, HeroEntity> heroMap;
    
    private HeroCache() {
        heroMap = new ConcurrentHashMap<>();
    }
    
    public static HeroCache getInstance() {
        return INSTANCE;
    }
    
    public HeroEntity getHero(int heroId) {
        return heroMap.get(heroId);
    }
    
    public void addHero(HeroEntity hero) {
        heroMap.put(hero.getHeroId(), hero);
    }
    
    public void removeHero(int heroId) {
        heroMap.remove(heroId);
    }
    
    public void clear() {
        heroMap.clear();
    }
} 