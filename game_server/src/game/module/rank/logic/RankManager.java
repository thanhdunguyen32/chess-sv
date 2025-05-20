package game.module.rank.logic;

import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.dao.DungeonDao;
import game.module.item.logic.ItemConstants;
import game.module.kingpvp.bean.KingPvp;
import game.module.kingpvp.dao.KingPvpDao;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pvp.bean.PvpBean;
import game.module.pvp.dao.PvpCache;
import game.module.rank.bean.*;
import game.module.rank.dao.RankCache;
import game.module.user.bean.PlayerBean;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerDao;
import game.module.user.dao.PlayerServerPropDao;
import lion.common.BeanTool;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager implements Job {

    private static Logger logger = LoggerFactory.getLogger(RankManager.class);

    public static final int RANK_SIZE = 50;

    public static final int RANK_SIZE_SMALL = 10;

    public static final int MY_RANK_SIZE = 5000;

    static class SingletonHolder {
        static RankManager instance = new RankManager();
    }

    public static RankManager getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("rank job execute!");
        long startTime = System.currentTimeMillis();
        rankPlayerLevel();
        rankTeamBattleForce();
        rankHeroStar();
        rankTower();
        rankPvp();
        rankDungeon();
        rankKingPvp();
        // save to db
        RankCache.getInstance().save();
        logger.info("rank job success!cost={}ms", System.currentTimeMillis() - startTime);
    }

    private void rankPlayerLevel() {
        List<PlayerBean> newRankList = PlayerDao.getInstance().rankPlayerLevel();
        List<DBPlayerLevelRank1> oldRankList = RankCache.getInstance().getPlayerLevelRanks();
        List<DBPlayerLevelRank1> newRankListCache = new ArrayList<DBPlayerLevelRank1>();
        if (oldRankList == null) {
            for (PlayerBean pb : newRankList) {
                DBPlayerLevelRank1 DBPlayerLevelRank1 = new DBPlayerLevelRank1();
                DBPlayerLevelRank1.setPlayerId(pb.getId());
                DBPlayerLevelRank1.setRankChange(0);
                newRankListCache.add(DBPlayerLevelRank1);
            }
        } else {
            int i = 1;
            for (PlayerBean pb : newRankList) {
                DBPlayerLevelRank1 dbPlayerLevelRank1 = new DBPlayerLevelRank1();
                dbPlayerLevelRank1.setPlayerId(pb.getId());
                DBPlayerLevelRank1 oldRank1 = RankCache.getInstance().getPlayerLevelRankById(pb.getId());
                if (oldRank1 != null) {
                    int oldRankIndex = oldRankList.indexOf(oldRank1);
                    if (oldRankIndex == -1) {
                        dbPlayerLevelRank1.setRankChange(RANK_SIZE - i);
                    } else {
                        int rankChange = oldRankIndex + 1 - i;
                        dbPlayerLevelRank1.setRankChange(rankChange);
                    }
                } else {
                    dbPlayerLevelRank1.setRankChange(RANK_SIZE - i);
                }
                i++;
                newRankListCache.add(dbPlayerLevelRank1);
            }
        }
        RankCache.getInstance().setPlayerLevelRanks(newRankListCache);
        Map<Integer, DBPlayerLevelRank1> newRankListMap = new HashMap<Integer, DBPlayerLevelRank1>();
        try {
            BeanTool.addOrUpdate(newRankListMap, newRankListCache, "playerId");
        } catch (Exception e) {
            logger.error("", e);
        }
        RankCache.getInstance().setPlayerLevelRankMap(newRankListMap);
        // update offline cache
        for (PlayerBean pb : newRankList) {
            if (!PlayerOfflineManager.getInstance().checkExist(pb.getId())) {
                PlayerOfflineManager.getInstance().loadFromDb(pb.getId());
            }
        }
        // my rank
        List<Integer> myNewRankList = PlayerDao.getInstance().rankPlayerLevelMy();
        List<DBRankMyItem> dbSaveList = new ArrayList<DBRankMyItem>();
        for (int i = 0; i < myNewRankList.size(); i++) {
            int currentRank = i + 1;
            int playerId = myNewRankList.get(i);
            int rankChange = 0;
            RankMyItem rankMyItem = RankCache.getInstance().getPlayerLevelRankMy(playerId);
            if (rankMyItem != null) {
                rankChange = rankMyItem.getRank() - currentRank;
                rankMyItem.setRankChange(rankChange);
                rankMyItem.setRank(currentRank);
            } else {
                rankChange = myNewRankList.size() - currentRank;
                rankMyItem = new RankMyItem(currentRank, rankChange);
                RankCache.getInstance().setPlayerLevelRankMy(playerId, rankMyItem);
            }
            DBRankMyItem dbRankBattleForceMy = new DBRankMyItem(playerId, rankChange);
            dbSaveList.add(dbRankBattleForceMy);
        }
        RankCache.getInstance().setPlayerLevelMy(dbSaveList);
    }

    private void rankHeroStar() {
//		List<RankHeroCountBean> newRankList = CardEntityDao.getInstance().rankHeroStars();
//		List<DBRankHeroStars1> oldRankList = RankCache.getInstance().getDbRankHeroStars();
//		List<DBRankHeroStars1> newRankListCache = new ArrayList<DBRankHeroStars1>();
//		if (oldRankList == null) {
//			for (RankHeroCountBean heroCountBean : newRankList) {
//				DBRankHeroStars1 dbRankHeroStars1 = new DBRankHeroStars1();
//				dbRankHeroStars1.setPlayerId(heroCountBean.getPlayerId());
//				dbRankHeroStars1.setRankChange(0);
//				dbRankHeroStars1.setStarSum(heroCountBean.getCount());
//				newRankListCache.add(dbRankHeroStars1);
//			}
//		} else {
//			int i = 1;
//			for (RankHeroCountBean heroCountBean : newRankList) {
//				DBRankHeroStars1 dbRankHeroStars1 = new DBRankHeroStars1();
//				dbRankHeroStars1.setPlayerId(heroCountBean.getPlayerId());
//				dbRankHeroStars1.setStarSum(heroCountBean.getCount());
//				DBRankHeroStars1 oldRank1 = RankCache.getInstance().getDBRankHeroStarsById(heroCountBean.getPlayerId());
//				if (oldRank1 != null) {
//					int oldRankIndex = oldRankList.indexOf(oldRank1);
//					if (oldRankIndex == -1) {
//						dbRankHeroStars1.setRankChange(RANK_SIZE - i);
//					} else {
//						int rankChange = oldRankIndex + 1 - i;
//						dbRankHeroStars1.setRankChange(rankChange);
//					}
//				} else {
//					dbRankHeroStars1.setRankChange(RANK_SIZE - i);
//				}
//				i++;
//				newRankListCache.add(dbRankHeroStars1);
//			}
//		}
//		RankCache.getInstance().setDbRankHeroStars(newRankListCache);
//		Map<Integer, DBRankHeroStars1> newRankListMap = new HashMap<Integer, DBRankHeroStars1>();
//		try {
//			BeanTool.addOrUpdate(newRankListMap, newRankListCache, "playerId");
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//		RankCache.getInstance().setHeroStarsRankMap(newRankListMap);
//		// update offline cache
//		for (RankHeroCountBean pb : newRankList) {
//			if (!PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId())) {
//				PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
//			}
//		}
//		// load hero list
//		for (DBRankHeroStars1 dbRankHeroStars1 : newRankListCache) {
//			int playerId = dbRankHeroStars1.getPlayerId();
//			List<CardEntity> heroList = CardEntityDao.getInstance().getSimpleHeroAll(playerId);
//			List<DBStageHero> dbHeroList = new ArrayList<DBStageHero>();
//			for (CardEntity heroEntity : heroList) {
//				DBStageHero dbStageHero = new DBStageHero(heroEntity.getTemplateId(), heroEntity.getLevel(),
//						heroEntity.getGrade(), 0);
//				dbHeroList.add(dbStageHero);
//			}
//			dbRankHeroStars1.setHerosList(dbHeroList);
//		}
//		// my rank
//		List<Integer> myNewRankList = CardEntityDao.getInstance().rankHeroStarsMy();
//		List<DBRankMyItem> dbSaveList = new ArrayList<DBRankMyItem>();
//		for (int i = 0; i < myNewRankList.size(); i++) {
//			int currentRank = i + 1;
//			int playerId = myNewRankList.get(i);
//			int rankChange = 0;
//			RankMyItem rankMyItem = RankCache.getInstance().getHeroStarsRankMy(playerId);
//			if (rankMyItem != null) {
//				rankChange = rankMyItem.getRank() - currentRank;
//				rankMyItem.setRankChange(rankChange);
//				rankMyItem.setRank(currentRank);
//			} else {
//				rankChange = myNewRankList.size() - currentRank;
//				rankMyItem = new RankMyItem(currentRank, rankChange);
//				RankCache.getInstance().setHeroStarsRankMy(playerId, rankMyItem);
//			}
//			DBRankMyItem dbRankBattleForceMy = new DBRankMyItem(playerId, rankChange);
//			dbSaveList.add(dbRankBattleForceMy);
//		}
//		RankCache.getInstance().setHeroStarsMy(dbSaveList);
    }

    /**
     * 战队战力排行
     */
    private void rankTeamBattleForce() {
        List<PlayerBean> newRankList = PlayerDao.getInstance().rankPlayerPower();
        List<DBRankBattleForce1> oldRankList = RankCache.getInstance().getDbRankTeamBattleForce();
        List<DBRankBattleForce1> newRankListCache = new ArrayList<DBRankBattleForce1>();
        if (oldRankList == null) {
            for (PlayerBean pb : newRankList) {
                DBRankBattleForce1 dbRankBattleForce1 = new DBRankBattleForce1();
                dbRankBattleForce1.setPlayerId(pb.getId());
                dbRankBattleForce1.setRankChange(0);
                dbRankBattleForce1.setBattleForce(pb.getPower());
                newRankListCache.add(dbRankBattleForce1);
            }
        } else {
            int i = 1;
            for (PlayerBean pb : newRankList) {
                DBRankBattleForce1 dbRankBattleForce1 = new DBRankBattleForce1();
                dbRankBattleForce1.setPlayerId(pb.getId());
                dbRankBattleForce1.setBattleForce(pb.getPower());
                DBRankBattleForce1 oldRank1 = RankCache.getInstance().getDBRankTeamBattleForceById(pb.getId());
                if (oldRank1 != null) {
                    int oldRankIndex = oldRankList.indexOf(oldRank1);
                    if (oldRankIndex == -1) {
                        dbRankBattleForce1.setRankChange(RANK_SIZE - i);
                    } else {
                        int rankChange = oldRankIndex + 1 - i;
                        dbRankBattleForce1.setRankChange(rankChange);
                    }
                } else {
                    dbRankBattleForce1.setRankChange(RANK_SIZE - i);
                }
                i++;
                newRankListCache.add(dbRankBattleForce1);
            }
        }
        RankCache.getInstance().setDbRankTeamBattleForce(newRankListCache);
        Map<Integer, DBRankBattleForce1> newRankListMap = new HashMap<Integer, DBRankBattleForce1>();
        try {
            BeanTool.addOrUpdate(newRankListMap, newRankListCache, "playerId");
        } catch (Exception e) {
            logger.error("", e);
        }
        RankCache.getInstance().setTeamBfRankMap(newRankListMap);
        // update offline cache
        for (PlayerBean pb : newRankList) {
            if (!PlayerOfflineManager.getInstance().checkExist(pb.getId())) {
                PlayerOfflineManager.getInstance().loadFromDb(pb.getId());
            }
        }
        // my rank
        List<Integer> myNewRankList = PlayerDao.getInstance().rankPlayerPowerMy();
        List<DBRankMyItem> dbSaveList = new ArrayList<DBRankMyItem>();
        for (int i = 0; i < myNewRankList.size(); i++) {
            int currentRank = i + 1;
            int playerId = myNewRankList.get(i);
            int rankChange = 0;
            RankMyItem randTeamBattleForceMyItem = RankCache.getInstance().getTeamBattleForceRankMy(playerId);
            if (randTeamBattleForceMyItem != null) {
                rankChange = randTeamBattleForceMyItem.getRank() - currentRank;
                randTeamBattleForceMyItem.setRankChange(rankChange);
                randTeamBattleForceMyItem.setRank(currentRank);
            } else {
                rankChange = myNewRankList.size() - currentRank;
                randTeamBattleForceMyItem = new RankMyItem(currentRank, rankChange);
                RankCache.getInstance().setTeamBattleForceRankMy(playerId, randTeamBattleForceMyItem);
            }
            DBRankMyItem dbRankBattleForceMy = new DBRankMyItem(playerId, rankChange);
            dbSaveList.add(dbRankBattleForceMy);
        }
        RankCache.getInstance().setDBRankBattleForceMy(dbSaveList);
    }

    private void rankTower() {
        List<PlayerProp> newRankList = PlayerServerPropDao.getInstance().rankTowerLevel(ItemConstants.TOWER_ID);
        List<DbRankTower.DbRankTower1> oldRankList = RankCache.getInstance().getTowerRankList();
        List<DbRankTower.DbRankTower1> newRankListCache = new ArrayList<>();
        if (oldRankList == null) {
            for (PlayerProp pb : newRankList) {
                DbRankTower.DbRankTower1 dbRankTower1 = new DbRankTower.DbRankTower1();
                dbRankTower1.setPlayerId(pb.getPlayerId());
                dbRankTower1.setRankChange(0);
                dbRankTower1.setLevel(pb.getCount());
                newRankListCache.add(dbRankTower1);
            }
        } else {
            int i = 1;
            for (PlayerProp pb : newRankList) {
                DbRankTower.DbRankTower1 dbRankTower1 = new DbRankTower.DbRankTower1();
                dbRankTower1.setPlayerId(pb.getPlayerId());
                dbRankTower1.setLevel(pb.getCount());
                DbRankTower.DbRankTower1 oldRank1 = RankCache.getInstance().getRankTowerById(pb.getPlayerId());
                if (oldRank1 != null) {
                    int oldRankIndex = oldRankList.indexOf(oldRank1);
                    if (oldRankIndex == -1) {
                        dbRankTower1.setRankChange(RANK_SIZE - i);
                    } else {
                        int rankChange = oldRankIndex + 1 - i;
                        dbRankTower1.setRankChange(rankChange);
                    }
                } else {
                    dbRankTower1.setRankChange(RANK_SIZE - i);
                }
                i++;
                newRankListCache.add(dbRankTower1);
            }
        }
        RankCache.getInstance().setTowerRankList(newRankListCache);
        Map<Integer, DbRankTower.DbRankTower1> newRankListMap = new HashMap<>();
        try {
            BeanTool.addOrUpdate(newRankListMap, newRankListCache, "playerId");
        } catch (Exception e) {
            logger.error("", e);
        }
        RankCache.getInstance().setTowerRankMap(newRankListMap);
        // update offline cache
        for (PlayerProp pb : newRankList) {
            if (!PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId())) {
                PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
            }
        }
        // my rank
        List<Integer> myNewRankList = PlayerServerPropDao.getInstance().rankTowerLevelMy(ItemConstants.TOWER_ID);
        List<DBRankMyItem> dbSaveList = new ArrayList<>();
        for (int i = 0; i < myNewRankList.size(); i++) {
            int currentRank = i + 1;
            int playerId = myNewRankList.get(i);
            int rankChange = 0;
            RankMyItem rankTowerMyItem = RankCache.getInstance().getTowerRankMy(playerId);
            if (rankTowerMyItem != null) {
                rankChange = rankTowerMyItem.getRank() - currentRank;
                rankTowerMyItem.setRankChange(rankChange);
                rankTowerMyItem.setRank(currentRank);
            } else {
                rankChange = myNewRankList.size() - currentRank;
                rankTowerMyItem = new RankMyItem(currentRank, rankChange);
                RankCache.getInstance().setTowerRankMy(playerId, rankTowerMyItem);
            }
            DBRankMyItem dbRankTowerMy = new DBRankMyItem(playerId, rankChange);
            dbSaveList.add(dbRankTowerMy);
        }
        RankCache.getInstance().setToSaveListTower(dbSaveList);
    }

    private void rankDungeon() {
        List<DungeonBean> newRankList = DungeonDao.getInstance().randDungeon();
        List<DbRankDungeon.DbRankDungeon1> oldRankList = RankCache.getInstance().getDungeonRankList();
        List<DbRankDungeon.DbRankDungeon1> newRankListCache = new ArrayList<>();
        if (oldRankList == null) {
            for (DungeonBean pb : newRankList) {
                DbRankDungeon.DbRankDungeon1 dbRankDungeon1 = new DbRankDungeon.DbRankDungeon1();
                dbRankDungeon1.setPlayerId(pb.getPlayerId());
                dbRankDungeon1.setRankChange(0);
                dbRankDungeon1.setChapter(pb.getChapterIndex());
                dbRankDungeon1.setNode(pb.getNode());
                newRankListCache.add(dbRankDungeon1);
            }
        } else {
            int i = 1;
            for (DungeonBean pb : newRankList) {
                DbRankDungeon.DbRankDungeon1 dbRankDungeon1 = new DbRankDungeon.DbRankDungeon1();
                dbRankDungeon1.setPlayerId(pb.getPlayerId());
                dbRankDungeon1.setChapter(pb.getChapterIndex());
                dbRankDungeon1.setNode(pb.getNode());
                DbRankDungeon.DbRankDungeon1 oldRank1 = RankCache.getInstance().getRankDungeonById(pb.getPlayerId());
                if (oldRank1 != null) {
                    int oldRankIndex = oldRankList.indexOf(oldRank1);
                    if (oldRankIndex == -1) {
                        dbRankDungeon1.setRankChange(RANK_SIZE - i);
                    } else {
                        int rankChange = oldRankIndex + 1 - i;
                        dbRankDungeon1.setRankChange(rankChange);
                    }
                } else {
                    dbRankDungeon1.setRankChange(RANK_SIZE - i);
                }
                i++;
                newRankListCache.add(dbRankDungeon1);
            }
        }
        RankCache.getInstance().setDungeonRankList(newRankListCache);
        Map<Integer, DbRankDungeon.DbRankDungeon1> newRankListMap = new HashMap<>();
        try {
            BeanTool.addOrUpdate(newRankListMap, newRankListCache, "playerId");
        } catch (Exception e) {
            logger.error("", e);
        }
        RankCache.getInstance().setDungeonRankMap(newRankListMap);
        // update offline cache
        for (DungeonBean pb : newRankList) {
            if (!PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId())) {
                PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
            }
        }
        // my rank
        List<Integer> myNewRankList = DungeonDao.getInstance().rankDungeonLevelMy();
        List<DBRankMyItem> dbSaveList = new ArrayList<>();
        for (int i = 0; i < myNewRankList.size(); i++) {
            int currentRank = i + 1;
            int playerId = myNewRankList.get(i);
            int rankChange = 0;
            RankMyItem rankDungeonMyItem = RankCache.getInstance().getDungeonRankMy(playerId);
            if (rankDungeonMyItem != null) {
                rankChange = rankDungeonMyItem.getRank() - currentRank;
                rankDungeonMyItem.setRankChange(rankChange);
                rankDungeonMyItem.setRank(currentRank);
            } else {
                rankChange = myNewRankList.size() - currentRank;
                rankDungeonMyItem = new RankMyItem(currentRank, rankChange);
                RankCache.getInstance().setDungeonRankMy(playerId, rankDungeonMyItem);
            }
            DBRankMyItem dbRankDungeonMy = new DBRankMyItem(playerId, rankChange);
            dbSaveList.add(dbRankDungeonMy);
        }
        RankCache.getInstance().setToSaveListDungeon(dbSaveList);
    }
    
    private void rankKingPvp() {
        List<KingPvp> newRankList = KingPvpDao.getInstance().randKingPvp(RANK_SIZE);
        List<DbRankKingPvp.DbRankKingPvp1> oldRankList = RankCache.getInstance().getKingPvpRankList();
        List<DbRankKingPvp.DbRankKingPvp1> newRankListCache = new ArrayList<>();
        if (oldRankList == null) {
            for (KingPvp pb : newRankList) {
                DbRankKingPvp.DbRankKingPvp1 dbRankKingPvp1 = new DbRankKingPvp.DbRankKingPvp1();
                dbRankKingPvp1.setPlayerId(pb.getPlayerId());
                dbRankKingPvp1.setRankChange(0);
                dbRankKingPvp1.setStage(pb.getStage());
                dbRankKingPvp1.setStar(pb.getStar());
                newRankListCache.add(dbRankKingPvp1);
            }
        } else {
            int i = 1;
            for (KingPvp pb : newRankList) {
                DbRankKingPvp.DbRankKingPvp1 dbRankKingPvp1 = new DbRankKingPvp.DbRankKingPvp1();
                dbRankKingPvp1.setPlayerId(pb.getPlayerId());
                dbRankKingPvp1.setStage(pb.getStage());
                dbRankKingPvp1.setStar(pb.getStar());
                DbRankKingPvp.DbRankKingPvp1 oldRank1 = RankCache.getInstance().getRankKingPvpById(pb.getPlayerId());
                if (oldRank1 != null) {
                    int oldRankIndex = oldRankList.indexOf(oldRank1);
                    if (oldRankIndex == -1) {
                        dbRankKingPvp1.setRankChange(RANK_SIZE - i);
                    } else {
                        int rankChange = oldRankIndex + 1 - i;
                        dbRankKingPvp1.setRankChange(rankChange);
                    }
                } else {
                    dbRankKingPvp1.setRankChange(RANK_SIZE - i);
                }
                i++;
                newRankListCache.add(dbRankKingPvp1);
            }
        }
        RankCache.getInstance().setKingPvpRankList(newRankListCache);
        Map<Integer, DbRankKingPvp.DbRankKingPvp1> newRankListMap = new HashMap<>();
        try {
            BeanTool.addOrUpdate(newRankListMap, newRankListCache, "playerId");
        } catch (Exception e) {
            logger.error("", e);
        }
        RankCache.getInstance().setKingPvpRankMap(newRankListMap);
        // update offline cache
        for (KingPvp pb : newRankList) {
            if (!PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId())) {
                PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
            }
        }
        // my rank
        List<Integer> myNewRankList = KingPvpDao.getInstance().rankKingPvpLevelMy();
        List<DBRankMyItem> dbSaveList = new ArrayList<>();
        for (int i = 0; i < myNewRankList.size(); i++) {
            int currentRank = i + 1;
            int playerId = myNewRankList.get(i);
            int rankChange = 0;
            RankMyItem rankKingPvpMyItem = RankCache.getInstance().getKingPvpRankMy(playerId);
            if (rankKingPvpMyItem != null) {
                rankChange = rankKingPvpMyItem.getRank() - currentRank;
                rankKingPvpMyItem.setRankChange(rankChange);
                rankKingPvpMyItem.setRank(currentRank);
            } else {
                rankChange = myNewRankList.size() - currentRank;
                rankKingPvpMyItem = new RankMyItem(currentRank, rankChange);
                RankCache.getInstance().setKingPvpRankMy(playerId, rankKingPvpMyItem);
            }
            DBRankMyItem dbRankKingPvpMy = new DBRankMyItem(playerId, rankChange);
            dbSaveList.add(dbRankKingPvpMy);
        }
        RankCache.getInstance().setToSaveListkingPvp(dbSaveList);
    }

    private void rankPvp() {
        PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
        List<Integer> newRankList = pvpBean.getPlayers();
        Map<Integer, Integer> pvpScore = pvpBean.getPvpScore();
        List<DbRankArena.DbRankArena1> oldRankList = RankCache.getInstance().getArenaRankList();
        List<DbRankArena.DbRankArena1> newRankListCache = new ArrayList<>();
        if (oldRankList == null) {
            for (Integer pb : newRankList) {
                DbRankArena.DbRankArena1 dbRankArena1 = new DbRankArena.DbRankArena1();
                dbRankArena1.setPlayerId(pb);
                dbRankArena1.setRankChange(0);
                dbRankArena1.setScore(pvpScore.get(pb));
                newRankListCache.add(dbRankArena1);
            }
        } else {
            int i = 1;
            for (Integer pb : newRankList) {
                DbRankArena.DbRankArena1 dbRankArena1 = new DbRankArena.DbRankArena1();
                dbRankArena1.setPlayerId(pb);
                dbRankArena1.setScore(pvpScore.get(pb));
                DbRankArena.DbRankArena1 oldRank1 = RankCache.getInstance().getDBRankArenaById(pb);
                if (oldRank1 != null) {
                    int oldRankIndex = oldRankList.indexOf(oldRank1);
                    if (oldRankIndex == -1) {
                        dbRankArena1.setRankChange(RANK_SIZE - i);
                    } else {
                        int rankChange = oldRankIndex + 1 - i;
                        dbRankArena1.setRankChange(rankChange);
                    }
                } else {
                    dbRankArena1.setRankChange(RANK_SIZE - i);
                }
                i++;
                newRankListCache.add(dbRankArena1);
            }
        }
        RankCache.getInstance().setArenaRankList(newRankListCache);
        Map<Integer, DbRankArena.DbRankArena1> newRankListMap = new HashMap<>();
        try {
            BeanTool.addOrUpdate(newRankListMap, newRankListCache, "playerId");
        } catch (Exception e) {
            logger.error("", e);
        }
        RankCache.getInstance().setArenaRankMap(newRankListMap);
        // update offline cache
        for (Integer pb : newRankList) {
            if (!PlayerOfflineManager.getInstance().checkExist(pb)) {
                PlayerOfflineManager.getInstance().loadFromDb(pb);
            }
        }
        // my rank
        List<Integer> myNewRankList = new ArrayList<>(pvpScore.keySet());
        myNewRankList.sort((p1, p2) -> {
            Integer score1 = pvpScore.get(p1);
            Integer score2 = pvpScore.get(p2);
            return score1 - score2;
        });
        List<DBRankMyItem> dbSaveList = new ArrayList<>();
        for (int i = 0; i < myNewRankList.size(); i++) {
            int currentRank = i + 1;
            int playerId = myNewRankList.get(i);
            int rankChange = 0;
            RankMyItem rankArenaMyItem = RankCache.getInstance().getArenaRankMy(playerId);
            if (rankArenaMyItem != null) {
                rankChange = rankArenaMyItem.getRank() - currentRank;
                rankArenaMyItem.setRankChange(rankChange);
                rankArenaMyItem.setRank(currentRank);
            } else {
                rankChange = myNewRankList.size() - currentRank;
                rankArenaMyItem = new RankMyItem(currentRank, rankChange);
                RankCache.getInstance().setArenaRankMy(playerId, rankArenaMyItem);
            }
            DBRankMyItem dbRankArenaMy = new DBRankMyItem(playerId, rankChange);
            dbSaveList.add(dbRankArenaMy);
        }
        RankCache.getInstance().setToSaveListDbArena(dbSaveList);
    }

    public static class RankMyItem {
        private int rank;
        private int rankChange;

        public RankMyItem(int rank, int rankChange) {
            super();
            this.rank = rank;
            this.rankChange = rankChange;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getRankChange() {
            return rankChange;
        }

        public void setRankChange(int rankChange) {
            this.rankChange = rankChange;
        }
    }

    public static class RankDbBean {
        private int playerId;
        private int statValue;

        public RankDbBean(int playerId, int battleForce) {
            super();
            this.playerId = playerId;
            this.statValue = battleForce;
        }

        public int getPlayerId() {
            return playerId;
        }

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }

        public int getStatValue() {
            return statValue;
        }

        public void setStatValue(int statValue) {
            this.statValue = statValue;
        }
    }

}
