package game.module.legion.dao;

import game.module.legion.bean.LegionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LegionCache {

    private static Logger logger = LoggerFactory.getLogger(LegionCache.class);

    static class SingletonHolder {
        static LegionCache instance = new LegionCache();
    }

    public static LegionCache getInstance() {
        return SingletonHolder.instance;
    }

    private final Map<Long, LegionBean> legionCache = new ConcurrentHashMap<>();

    public void loadFromDb() {
        logger.info("legion loadFromDb!");
        List<LegionBean> legions = LegionDao.getInstance().getLegionBeans();
        legionCache.clear();
        for (LegionBean legionBean : legions) {
            legionCache.put(legionBean.getUuid(), legionBean);
        }
    }

    public LegionBean getLegionBean(long uuid) {
        return legionCache.get(uuid);
    }

    public void addLegionBean(LegionBean legionBean) {
        legionCache.put(legionBean.getUuid(), legionBean);
    }

    public void removeLegionBean(long uuid) {
        legionCache.remove(uuid);
    }

    public Collection<LegionBean> getLegionAll() {
        return legionCache.values();
    }

}
