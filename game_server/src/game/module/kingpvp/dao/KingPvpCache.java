package game.module.kingpvp.dao;

import game.module.kingpvp.bean.KingPvp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KingPvpCache {

    private static Logger logger = LoggerFactory.getLogger(KingPvpCache.class);

    static class SingletonHolder {
        static KingPvpCache instance = new KingPvpCache();
    }

    public static KingPvpCache getInstance() {
        return SingletonHolder.instance;
    }

    private final Map<Integer, KingPvp> kingPvpCache = new ConcurrentHashMap<>();

    private List<List<KingPvp>> kingPvpRank = new ArrayList<>(26);

    public void loadFromDb() {
        logger.info("kingPvp loadFromDb!");
        for (int i = 0; i < 26; i++) {
            kingPvpRank.add(new ArrayList<>());
        }
        List<KingPvp> kingPvpes = KingPvpDao.getInstance().getKingPvp();
        kingPvpCache.clear();
        for (KingPvp kingPvpBean : kingPvpes) {
            kingPvpCache.put(kingPvpBean.getPlayerId(), kingPvpBean);
            Integer stage = kingPvpBean.getStage();
            if (stage > 0) {
                int rankIndex = getIndexByStage(stage);
                kingPvpRank.get(rankIndex).add(kingPvpBean);
            }
        }
        logger.info("kingPvp init loadFromDb finish!size={}", kingPvpes.size());
    }

    public KingPvp getKingPvp(int playerId) {
        return kingPvpCache.get(playerId);
    }

    public void addKingPvp(KingPvp kingPvpBean) {
        kingPvpCache.put(kingPvpBean.getPlayerId(), kingPvpBean);
    }

    public void removeKingPvp(int playerId) {
        kingPvpCache.remove(playerId);
    }

    public Collection<KingPvp> getKingPvpAll() {
        return kingPvpCache.values();
    }

    public int getIndexByStage(int stage) {
        return stage / 100 + stage % 100 - 1;
    }

    public List<KingPvp> getRankList(int rankIndex) {
        return kingPvpRank.get(rankIndex);
    }

    public void addRankList(int rankIndex, List<KingPvp> rankList) {
        kingPvpRank.set(rankIndex,rankList);
    }

    public void clearAll() {
        kingPvpCache.clear();
        for (int i = 0; i < 26; i++) {
            List<KingPvp> kingPvps = kingPvpRank.get(i);
            if (kingPvps != null) {
                kingPvps.clear();
            }
        }
    }

}
