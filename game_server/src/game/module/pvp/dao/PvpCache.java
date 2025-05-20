package game.module.pvp.dao;

import game.module.pvp.bean.PvpBean;
import game.module.pvp.bean.PvpPlayer;
import game.module.pvp.logic.PvpWeekRewardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class PvpCache {

    private static Logger logger = LoggerFactory.getLogger(PvpCache.class);

    static class SingletonHolder {
        static PvpCache instance = new PvpCache();
    }

    public static PvpCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private PvpBean pvpBean;

    /**
     * 初始化数据到缓存中
     */
    public void loadFromDb() {
        pvpBean = PvpDao.getInstance().getPvpBean();
        if (pvpBean == null) {
            pvpBean = new PvpBean();
            Map<Integer, Integer> pvpScore = new ConcurrentHashMap<>();
            pvpBean.setPvpScore(pvpScore);
            pvpBean.setPlayers(new ArrayList<>());
            //初始化
            PvpWeekRewardManager.getInstance().initPvpPlayers(pvpBean);
            PvpDao.getInstance().addPvpBean(pvpBean);
        } else if (pvpBean.getPvpScore() == null) {
            Map<Integer, Integer> pvpScore = new ConcurrentHashMap<>();
            pvpBean.setPvpScore(pvpScore);
            pvpBean.setPlayers(new ArrayList<>());
            //初始化
            PvpWeekRewardManager.getInstance().initPvpPlayers(pvpBean);
            PvpDao.getInstance().updatePvpBean(pvpBean);
        }
        //init player list
        Map<Integer, Integer> pvpScore = pvpBean.getPvpScore();
        List<Integer> playerList = new ArrayList<>(pvpScore.size());
        for (Map.Entry<Integer, Integer> aEntry : pvpScore.entrySet()) {
            playerList.add(aEntry.getKey());
        }
        //从大到小排列
        playerList.sort(Comparator.comparingInt(pvpScore::get).reversed());
        pvpBean.setPlayers(playerList);
        //rank map
        Map<Integer, Integer> myRankMap = new HashMap<>();
        for (int i = 0; i < playerList.size(); i++) {
            myRankMap.put(playerList.get(i), i);
        }
        pvpBean.setMyRankMap(myRankMap);
        //against players
        Map<Integer, PvpPlayer> againstPlayers = new HashMap<>();
        pvpBean.setPvpPlayerInfo(againstPlayers);
        logger.info("pvp loadFromDb success!");
    }

    public void save2Db() {
        PvpDao.getInstance().updatePvpBean(pvpBean);
    }

    public void setPvpBean(PvpBean apvpBean) {
        pvpBean = apvpBean;
    }

    public PvpBean getPvpBean() {
        return pvpBean;
    }

}
