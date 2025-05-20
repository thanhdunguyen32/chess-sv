package game.module.offline.logic;

import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationDao;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.GuozhanPlayerDao;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralDao;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalDao;
import game.module.offline.bean.PlayerBaseBean;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerOfflineManager {

    private static Logger logger = LoggerFactory.getLogger(PlayerOfflineManager.class);

    static class SingletonHolder {
        static PlayerOfflineManager instance = new PlayerOfflineManager();
    }

    public static PlayerOfflineManager getInstance() {
        return SingletonHolder.instance;
    }

    private Map<Integer, PlayerBaseBean> playerOfflineMap = new ConcurrentHashMap<>();
    private Map<Integer, BattleFormation> battleFormationCache = new ConcurrentHashMap<>();
    private Map<Integer, Map<Long, GeneralBean>> generalCache = new ConcurrentHashMap<>();
    private Map<Integer, Map<Integer, MythicalAnimal>> mythicalCache = new ConcurrentHashMap<>();

    public void init() {
        List<PlayerBaseBean> playerBeans = PlayerDao.getInstance().getPlayersForOffline();
        logger.info("offline players,size={}", playerBeans.size());
        for (PlayerBaseBean playerBean : playerBeans) {
            playerOfflineMap.put(playerBean.getId(), playerBean);
        }
        //get battle formation
        Set<Integer> ids = playerOfflineMap.keySet();
        if (ids.size() == 0) {
            return;
        }
        String playerIdAll = StringUtils.join(ids, ",");
        battleFormationCache = BattleFormationDao.getInstance().getBattleFormations(playerIdAll);
        //general all
        for (PlayerBaseBean playerBean : playerBeans) {
            int playerId = playerBean.getId();
            List<GeneralBean> generalList = GeneralDao.getInstance().getHeros(playerId);
            Map<Long, GeneralBean> generalBeanMap = generalCache.computeIfAbsent(playerId, k -> new HashMap<>());
            for (GeneralBean heroEntity : generalList) {
                generalBeanMap.put(heroEntity.getUuid(), heroEntity);
            }
        }
        //mythical all
        for (PlayerBaseBean playerBean : playerBeans) {
            int playerId = playerBean.getId();
            List<MythicalAnimal> mythicalList = MythicalAnimalDao.getInstance().getPlayerMythicalAnimal(playerId);
            Map<Integer, MythicalAnimal> mythicalMap = mythicalCache.computeIfAbsent(playerId, k -> new HashMap<>());
            for (MythicalAnimal mythicalAnimal : mythicalList) {
                mythicalMap.put(mythicalAnimal.getTemplateId(), mythicalAnimal);
            }
        }
        //国战国家id和官职
        Map<Integer, Integer> nationIdAll = GuozhanPlayerDao.getInstance().getGuoZhanNationAll(playerIdAll);
        if(nationIdAll != null) {
            for (Map.Entry<Integer, Integer> aEntry : nationIdAll.entrySet()) {
                int playerId = aEntry.getKey();
                PlayerBaseBean playerOfflineCache = playerOfflineMap.get(playerId);
                if (playerOfflineCache != null) {
                    playerOfflineCache.setNationId(aEntry.getValue());
                }
            }
        }
    }

    public void loadFromDb(Integer playerId) {
        if (playerId == null) {
            logger.warn("Attempted to load player data with null playerId");
            return;
        }

        PlayerBean playerBean = PlayerDao.getInstance().getPlayerById(playerId);
        if (playerBean == null) {
            logger.warn("No player found for playerId: {}", playerId);
            return;
        }

        // Store player in offline map
        playerOfflineMap.put(playerBean.getId(), playerBean);

        try {
            // Get battle formation
            BattleFormation battleFormation = BattleFormationDao.getInstance().getBattleFormation(playerId);
            if (battleFormation != null) {
                battleFormationCache.put(playerId, battleFormation);
            }

            // Load generals
            List<GeneralBean> generalList = GeneralDao.getInstance().getHeros(playerId);
            if (generalList != null && !generalList.isEmpty()) {
                Map<Long, GeneralBean> generalBeanMap = generalCache.computeIfAbsent(playerId, k -> new HashMap<>());
                for (GeneralBean heroEntity : generalList) {
                    if (heroEntity != null && heroEntity.getUuid() != null) {
                        generalBeanMap.put(heroEntity.getUuid(), heroEntity);
                    }
                }
            }

            // Load mythical animals
            List<MythicalAnimal> mythicalList = MythicalAnimalDao.getInstance().getPlayerMythicalAnimal(playerId);
            if (mythicalList != null && !mythicalList.isEmpty()) {
                Map<Integer, MythicalAnimal> mythicalMap = mythicalCache.computeIfAbsent(playerId, k -> new HashMap<>());
                for (MythicalAnimal mythicalAnimal : mythicalList) {
                    if (mythicalAnimal != null && mythicalAnimal.getTemplateId() != null) {
                        mythicalMap.put(mythicalAnimal.getTemplateId(), mythicalAnimal);
                    }
                }
            }

            // Load nation data
            GuozhanPlayer guozhanPlayer = GuozhanPlayerDao.getInstance().getPlayerGuozhanPlayer(playerId);
            playerBean.setNationId(guozhanPlayer != null ? guozhanPlayer.getNation() : 0);

        } catch (Exception e) {
            logger.error("Error loading data for playerId: " + playerId, e);
            // Clean up any partial data that may have been stored
            playerOfflineMap.remove(playerId);
            battleFormationCache.remove(playerId);
            generalCache.remove(playerId);
            mythicalCache.remove(playerId);
            throw e; // Re-throw to maintain original behavior
        }
    }

    public void clear() {
        playerOfflineMap.clear();
        battleFormationCache.clear();
        generalCache.clear();
        mythicalCache.clear();
    }

    public PlayerBaseBean getPlayerOfflineCache(int playerId) {
        return playerOfflineMap.get(playerId);
    }

    public PlayerBaseBean getPlayerOfflineCache(String name) {
        for (PlayerBaseBean offlineCache : playerOfflineMap.values()) {
            if (offlineCache.getName().equals(name)) {
                return offlineCache;
            }
        }
        return null;
    }

    public long getPlayerOfflineCacheSize() {
        return playerOfflineMap.size();
    }

    public boolean checkExist(Integer playerId) {
        return playerOfflineMap.get(playerId) != null;
    }

    public void updatePlayerOfflineCache(int playerId, PlayerBaseBean cache) {
        playerOfflineMap.put(playerId, cache);
    }

    public void updateNationId(int playerId, GuozhanPlayer guozhanPlayer){
        PlayerBaseBean pbb = playerOfflineMap.get(playerId);
        if(pbb != null) {
            if(guozhanPlayer != null){
                pbb.setNationId(guozhanPlayer.getNation());
            }else {
                pbb.setNationId(0);
            }
        }
    }

    public void updateBattleFormation(int playerId, BattleFormation battleFormation) {
        battleFormationCache.put(playerId, battleFormation);
    }

    public BattleFormation getBattleFormation(int playerId) {
        BattleFormation formation = battleFormationCache.get(playerId);
        if (formation == null) {
            logger.info("Battle formation not found in cache for player {}, loading from DB", playerId);
            formation = BattleFormationDao.getInstance().getBattleFormation(playerId);
            if (formation != null) {
                battleFormationCache.put(playerId, formation);
            } else {
                logger.warn("Battle formation not found in DB for player {}", playerId);
            }
        }
        return formation;
    }

    public void updateGenerals(int playerId, Map<Long, GeneralBean> generalMap) {
        generalCache.put(playerId, generalMap);
    }

    public Map<Long, GeneralBean> getGeneralAll(int playerId){
        return generalCache.get(playerId);
    }

    public void updateMythical(int playerId, Map<Integer, MythicalAnimal> generalMap) {
        mythicalCache.put(playerId, generalMap);
    }

    public Map<Integer, MythicalAnimal> getMythicalAll(int playerId){
        return mythicalCache.get(playerId);
    }

    public Collection<PlayerBaseBean> getAll() {
        return playerOfflineMap.values();
    }

}
