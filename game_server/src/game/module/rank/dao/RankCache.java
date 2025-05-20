package game.module.rank.dao;

import game.module.offline.logic.PlayerOfflineManager;
import game.module.rank.bean.*;
import game.module.rank.logic.RankManager.RankMyItem;
import lion.common.BeanTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankCache {

	private static Logger logger = LoggerFactory.getLogger(RankCache.class);

	static class SingletonHolder {
		static RankCache instance = new RankCache();
	}

	public static RankCache getInstance() {
		return SingletonHolder.instance;
	}

	private List<DBPlayerLevelRank1> playerLevelRankList;

	private List<DBRankBattleForce1> teamBattleForceRankList;

	private List<DBRankHeroStars1> heroStarsRankList;

	private List<DbRankTower.DbRankTower1> towerRankList;

	private List<DbRankDungeon.DbRankDungeon1> dungeonRankList;

	private List<DbRankKingPvp.DbRankKingPvp1> kingPvpRankList;

	private List<DbRankArena.DbRankArena1> arenaRankList;

	private Map<Integer, DBPlayerLevelRank1> playerLevelRankMap;

	private Map<Integer, DBRankBattleForce1> teamBattleForceRankMap;

	private Map<Integer, DBRankHeroStars1> heroStarsRankMap;

	private Map<Integer, DbRankTower.DbRankTower1> towerRankMap;

	private Map<Integer, DbRankDungeon.DbRankDungeon1> dungeonRankMap;

	private Map<Integer, DbRankKingPvp.DbRankKingPvp1> kingPvpRankMap;

	private Map<Integer, DbRankArena.DbRankArena1> arenaRankMap;

	private Map<Integer, RankMyItem> teamBattleForceRankMyMap;
	private List<DBRankMyItem> toSaveListDBRankBattleForceMy;

	private Map<Integer, RankMyItem> heroStarsMyMap;
	private List<DBRankMyItem> toSaveListHeroStars;

	private Map<Integer, RankMyItem> playerLevelMyMap;
	private List<DBRankMyItem> toSaveListPlayerLevel;

	private Map<Integer, RankMyItem> towerMyMap;
	private List<DBRankMyItem> toSaveListTower;

	private Map<Integer, RankMyItem> dungeonMyMap;
	private List<DBRankMyItem> toSaveListDungeon;

	private Map<Integer, RankMyItem> kingPvpMyMap;
	private List<DBRankMyItem> toSaveListKingPvp;

	private Map<Integer, RankMyItem> arenaRankMyMap;
	private List<DBRankMyItem> toSaveListDbArena;

	public void load() {
		// rank player level
		DBPlayerLevelRank dbPlayerLevelRank = RankDao.getInstance().getDBRankPlayerLevel();
		List<DBPlayerLevelRank1> dbPlayerLevelRank1s = dbPlayerLevelRank.getRankItemList();
		if (dbPlayerLevelRank1s == null) {
			playerLevelRankList = new ArrayList<DBPlayerLevelRank1>();
			dbPlayerLevelRank = new DBPlayerLevelRank();
			dbPlayerLevelRank.setRankItemList(playerLevelRankList);
			playerLevelRankMap = new HashMap<Integer, DBPlayerLevelRank1>();
			if (!RankDao.getInstance().isSystemBlobExist()) {
				RankDao.getInstance().insertDBRankPlayerLevel(dbPlayerLevelRank);
			}
		} else {
			playerLevelRankList = dbPlayerLevelRank1s;
			Map<Integer, DBPlayerLevelRank1> tmpTemplateMap = new HashMap<Integer, DBPlayerLevelRank1>();
			try {
				BeanTool.addOrUpdate(tmpTemplateMap, playerLevelRankList, "playerId");
				playerLevelRankMap = tmpTemplateMap;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		List<DBRankMyItem> playerLevelRankMyItems = dbPlayerLevelRank.getRankMyList();
		playerLevelMyMap = new HashMap<Integer, RankMyItem>();
		if (playerLevelRankMyItems != null) {
			for (int i = 0; i < playerLevelRankMyItems.size(); i++) {
				int rank = i + 1;
				DBRankMyItem dbRankBattleForceMy = playerLevelRankMyItems.get(i);
				playerLevelMyMap.put(dbRankBattleForceMy.getPlayerId(),
						new RankMyItem(rank, dbRankBattleForceMy.getRankChange()));
			}
		}
		// update offline cache
		for (DBPlayerLevelRank1 pb : playerLevelRankList) {
			boolean isExist = PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId());
			if (!isExist) {
				PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
			}
		}
		// rank hero stars
		DBRankHeroStars dbRankHeroStars = RankDao.getInstance().getDBRankHeroStars();
		List<DBRankHeroStars1> dbRankHeroStars1s = dbRankHeroStars.getRankItemList();
		if (dbRankHeroStars1s == null) {
			heroStarsRankList = new ArrayList<DBRankHeroStars1>();
			heroStarsRankMap = new HashMap<Integer, DBRankHeroStars1>();
		} else {
			heroStarsRankList = dbRankHeroStars1s;
			Map<Integer, DBRankHeroStars1> tmpTemplateMap = new HashMap<Integer, DBRankHeroStars1>();
			try {
				BeanTool.addOrUpdate(tmpTemplateMap, dbRankHeroStars1s, "playerId");
				heroStarsRankMap = tmpTemplateMap;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		List<DBRankMyItem> heroStarsRankMyItems = dbRankHeroStars.getRankMyList();
		heroStarsMyMap = new HashMap<Integer, RankMyItem>();
		if (heroStarsRankMyItems != null) {
			for (int i = 0; i < heroStarsRankMyItems.size(); i++) {
				int rank = i + 1;
				DBRankMyItem dbRankBattleForceMy = heroStarsRankMyItems.get(i);
				heroStarsMyMap.put(dbRankBattleForceMy.getPlayerId(),
						new RankMyItem(rank, dbRankBattleForceMy.getRankChange()));
			}
		}
		// update offline cache
		for (DBRankHeroStars1 pb : heroStarsRankList) {
			boolean isExist = PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId());
			if (!isExist) {
				PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
			}
		}
		// rank team bf
		DBRankBattleForce dbRankBattleForce = RankDao.getInstance().getDBRankTeamBf();
		List<DBRankBattleForce1> dbRankBattleForce1s = dbRankBattleForce.getRankItemList();
		if (dbRankBattleForce1s == null) {
			teamBattleForceRankList = new ArrayList<DBRankBattleForce1>();
			teamBattleForceRankMap = new HashMap<Integer, DBRankBattleForce1>();
		} else {
			teamBattleForceRankList = dbRankBattleForce1s;
			Map<Integer, DBRankBattleForce1> tmpTemplateMap = new HashMap<Integer, DBRankBattleForce1>();
			try {
				BeanTool.addOrUpdate(tmpTemplateMap, dbRankBattleForce1s, "playerId");
				teamBattleForceRankMap = tmpTemplateMap;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		List<DBRankMyItem> dbRankBattleForceMies = dbRankBattleForce.getRankMyList();
		teamBattleForceRankMyMap = new HashMap<Integer, RankMyItem>();
		if (dbRankBattleForceMies != null) {
			for (int i = 0; i < dbRankBattleForceMies.size(); i++) {
				int rank = i + 1;
				DBRankMyItem dbRankBattleForceMy = dbRankBattleForceMies.get(i);
				teamBattleForceRankMyMap.put(dbRankBattleForceMy.getPlayerId(),
						new RankMyItem(rank, dbRankBattleForceMy.getRankChange()));
			}
		}
		// update offline cache
		for (DBRankBattleForce1 pb : teamBattleForceRankList) {
			boolean isExist = PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId());
			if (!isExist) {
				PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
			}
		}
		// rank tower
		DbRankTower dbRankTower = RankDao.getInstance().getDbRankTower();
		if (dbRankTower == null || dbRankTower.getRankItem() == null) {
			towerRankList = new ArrayList<DbRankTower.DbRankTower1>();
			towerRankMap = new HashMap<Integer, DbRankTower.DbRankTower1>();
		} else {
			List<DbRankTower.DbRankTower1> dbRankTower1s = dbRankTower.getRankItem();
			towerRankList = dbRankTower1s;
			Map<Integer, DbRankTower.DbRankTower1> tmpTemplateMap = new HashMap<>();
			try {
				BeanTool.addOrUpdate(tmpTemplateMap, dbRankTower1s, "playerId");
				towerRankMap = tmpTemplateMap;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		towerMyMap = new HashMap<Integer, RankMyItem>();
		if (dbRankTower != null && dbRankTower.getRankMy() != null) {
			List<DBRankMyItem> dbRankTowerMies = dbRankTower.getRankMy();
			for (int i = 0; i < dbRankTowerMies.size(); i++) {
				int rank = i + 1;
				DBRankMyItem dbRankTowerMy = dbRankTowerMies.get(i);
				towerMyMap.put(dbRankTowerMy.getPlayerId(),
						new RankMyItem(rank, dbRankTowerMy.getRankChange()));
			}
		}
		// update offline cache
		for (DbRankTower.DbRankTower1 pb : towerRankList) {
			boolean isExist = PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId());
			if (!isExist) {
				PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
			}
		}
		// rank dungeon
		DbRankDungeon dbRankDungeon = RankDao.getInstance().getDbRankDungeon();
		if (dbRankDungeon == null || dbRankDungeon.getRankItem() == null) {
			dungeonRankList = new ArrayList<>();
			dungeonRankMap = new HashMap<>();
		} else {
			List<DbRankDungeon.DbRankDungeon1> dbRankDungeon1s = dbRankDungeon.getRankItem();
			dungeonRankList = dbRankDungeon1s;
			Map<Integer, DbRankDungeon.DbRankDungeon1> tmpTemplateMap = new HashMap<>();
			try {
				BeanTool.addOrUpdate(tmpTemplateMap, dbRankDungeon1s, "playerId");
				dungeonRankMap = tmpTemplateMap;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		dungeonMyMap = new HashMap<>();
		if (dbRankDungeon != null && dbRankDungeon.getRankMy() != null) {
			List<DBRankMyItem> dbRankDungeonMies = dbRankDungeon.getRankMy();
			for (int i = 0; i < dbRankDungeonMies.size(); i++) {
				int rank = i + 1;
				DBRankMyItem dbRankDungeonMy = dbRankDungeonMies.get(i);
				dungeonMyMap.put(dbRankDungeonMy.getPlayerId(),
						new RankMyItem(rank, dbRankDungeonMy.getRankChange()));
			}
		}
		// update offline cache
		for (DbRankDungeon.DbRankDungeon1 pb : dungeonRankList) {
			boolean isExist = PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId());
			if (!isExist) {
				PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
			}
		}
		// rank kingPvp
		DbRankKingPvp dbRankKingPvp = RankDao.getInstance().getDbRankKingPvp();
		if (dbRankKingPvp == null || dbRankKingPvp.getRankItem() == null) {
			kingPvpRankList = new ArrayList<>();
			kingPvpRankMap = new HashMap<>();
		} else {
			List<DbRankKingPvp.DbRankKingPvp1> dbRankKingPvp1s = dbRankKingPvp.getRankItem();
			kingPvpRankList = dbRankKingPvp1s;
			Map<Integer, DbRankKingPvp.DbRankKingPvp1> tmpTemplateMap = new HashMap<>();
			try {
				BeanTool.addOrUpdate(tmpTemplateMap, dbRankKingPvp1s, "playerId");
				kingPvpRankMap = tmpTemplateMap;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		kingPvpMyMap = new HashMap<>();
		if (dbRankKingPvp != null && dbRankKingPvp.getRankMy() != null) {
			List<DBRankMyItem> dbRankKingPvpMies = dbRankKingPvp.getRankMy();
			for (int i = 0; i < dbRankKingPvpMies.size(); i++) {
				int rank = i + 1;
				DBRankMyItem dbRankKingPvpMy = dbRankKingPvpMies.get(i);
				kingPvpMyMap.put(dbRankKingPvpMy.getPlayerId(),
						new RankMyItem(rank, dbRankKingPvpMy.getRankChange()));
			}
		}
		// update offline cache
		for (DbRankKingPvp.DbRankKingPvp1 pb : kingPvpRankList) {
			boolean isExist = PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId());
			if (!isExist) {
				PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
			}
		}
		// rank arena
		DbRankArena dbRankArena = RankDao.getInstance().getDbRankArena();
		if (dbRankArena == null || dbRankArena.getRankItem() == null) {
			arenaRankList = new ArrayList<>();
			arenaRankMap = new HashMap<>();
		} else {
			List<DbRankArena.DbRankArena1> dbRankArena1s = dbRankArena.getRankItem();
			arenaRankList = dbRankArena1s;
			Map<Integer, DbRankArena.DbRankArena1> tmpTemplateMap = new HashMap<>();
			try {
				BeanTool.addOrUpdate(tmpTemplateMap, dbRankArena1s, "playerId");
				arenaRankMap = tmpTemplateMap;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		arenaRankMyMap = new HashMap<Integer, RankMyItem>();
		if (dbRankArena != null && dbRankArena.getRankMy() != null) {
			List<DBRankMyItem> dbRankArenaMies = dbRankArena.getRankMy();
			for (int i = 0; i < dbRankArenaMies.size(); i++) {
				int rank = i + 1;
				DBRankMyItem dbRankArenaMy = dbRankArenaMies.get(i);
				arenaRankMyMap.put(dbRankArenaMy.getPlayerId(),
						new RankMyItem(rank, dbRankArenaMy.getRankChange()));
			}
		}
		// update offline cache
		for (DbRankArena.DbRankArena1 pb : arenaRankList) {
			boolean isExist = PlayerOfflineManager.getInstance().checkExist(pb.getPlayerId());
			if (!isExist) {
				PlayerOfflineManager.getInstance().loadFromDb(pb.getPlayerId());
			}
		}
	}

	public void save() {
		// player level rank
		DBPlayerLevelRank dbPlayerLevelRank = new DBPlayerLevelRank();
		dbPlayerLevelRank.setRankItemList(playerLevelRankList);
		dbPlayerLevelRank.setRankMyList(toSaveListPlayerLevel);
		RankDao.getInstance().updateDBRankPlayerLevel(dbPlayerLevelRank);
		// hero stars rank
		DBRankHeroStars dbRankHeroStars = new DBRankHeroStars();
		dbRankHeroStars.setRankItemList(heroStarsRankList);
		dbRankHeroStars.setRankMyList(toSaveListHeroStars);
		RankDao.getInstance().updateDBRankHeroStars(dbRankHeroStars);
		// team bf rank
		DBRankBattleForce dbRankBattleForce = new DBRankBattleForce();
		dbRankBattleForce.setRankItemList(teamBattleForceRankList);
		dbRankBattleForce.setRankMyList(toSaveListDBRankBattleForceMy);
		RankDao.getInstance().updateDBRankTeamBf(dbRankBattleForce);
		// tower rank
		DbRankTower dbRankTower = new DbRankTower();
		dbRankTower.setRankItem(towerRankList);
		dbRankTower.setRankMy(toSaveListTower);
        RankDao.getInstance().updateDbRankTower(dbRankTower);
        // tower 1 rank
        DbRankTower.DbRankTower1 tower1Rank = new DbRankTower.DbRankTower1();

		// dungeon rank
		DbRankDungeon dbRankDungeon = new DbRankDungeon();
		dbRankDungeon.setRankItem(dungeonRankList);
		dbRankDungeon.setRankMy(toSaveListDungeon);
		RankDao.getInstance().updateDbRankDungeon(dbRankDungeon);
		// kingPvp rank
		DbRankKingPvp dbRankKingPvp = new DbRankKingPvp();
		dbRankKingPvp.setRankItem(kingPvpRankList);
		dbRankKingPvp.setRankMy(toSaveListKingPvp);
		RankDao.getInstance().updateDbRankKingPvp(dbRankKingPvp);
		// arena rank
		DbRankArena dbRankArena = new DbRankArena();
		dbRankArena.setRankItem(arenaRankList);
		dbRankArena.setRankMy(toSaveListDbArena);
		RankDao.getInstance().updateDbRankArena(dbRankArena);
	}

	public List<DBPlayerLevelRank1> getPlayerLevelRanks() {
		return playerLevelRankList;
	}

	public void setPlayerLevelRanks(List<DBPlayerLevelRank1> param) {
		playerLevelRankList = param;
	}

	public DBPlayerLevelRank1 getPlayerLevelRankById(int playerId) {
		return playerLevelRankMap.get(playerId);
	}

	public List<DBRankBattleForce1> getDbRankTeamBattleForce() {
		return teamBattleForceRankList;
	}

	public void setDbRankTeamBattleForce(List<DBRankBattleForce1> param) {
		teamBattleForceRankList = param;
	}

	public DBRankBattleForce1 getDBRankTeamBattleForceById(int playerId) {
		return teamBattleForceRankMap.get(playerId);
	}

	public DbRankArena.DbRankArena1 getDBRankArenaById(int playerId) {
		return arenaRankMap.get(playerId);
	}

	public DbRankTower.DbRankTower1 getRankTowerById(int playerId){
		return towerRankMap.get(playerId);
	}
	
	public DbRankDungeon.DbRankDungeon1 getRankDungeonById(int playerId){
		return dungeonRankMap.get(playerId);
	}
	
	public DbRankKingPvp.DbRankKingPvp1 getRankKingPvpById(int playerId){
		return kingPvpRankMap.get(playerId);
	}

	public List<DBRankHeroStars1> getDbRankHeroStars() {
		return heroStarsRankList;
	}

	public void setDbRankHeroStars(List<DBRankHeroStars1> newRankListCache) {
		this.heroStarsRankList = newRankListCache;
	}

	public void setHeroStarsRankMap(Map<Integer, DBRankHeroStars1> map) {
		heroStarsRankMap = map;
	}

	public DBRankHeroStars1 getDBRankHeroStarsById(int playerId) {
		return heroStarsRankMap.get(playerId);
	}

	public RankMyItem getTeamBattleForceRankMy(int playerId) {
		return teamBattleForceRankMyMap.get(playerId);
	}

	public RankMyItem getTowerRankMy(int playerId) {
		return towerMyMap.get(playerId);
	}

	public RankMyItem getDungeonRankMy(int playerId) {
		return dungeonMyMap.get(playerId);
	}
	
	public RankMyItem getKingPvpRankMy(int playerId) {
		return kingPvpMyMap.get(playerId);
	}

	public RankMyItem getArenaRankMy(int playerId) {
		return arenaRankMyMap.get(playerId);
	}

	public void setTeamBattleForceRankMy(int playerId, RankMyItem randTeamBattleForceMyItem) {
		teamBattleForceRankMyMap.put(playerId, randTeamBattleForceMyItem);
	}

	public void setTowerRankMy(int playerId, RankMyItem randTeamBattleForceMyItem) {
		towerMyMap.put(playerId, randTeamBattleForceMyItem);
	}

	public void setDungeonRankMy(int playerId, RankMyItem randTeamBattleForceMyItem) {
		dungeonMyMap.put(playerId, randTeamBattleForceMyItem);
	}
	
	public void setKingPvpRankMy(int playerId, RankMyItem randTeamBattleForceMyItem) {
		kingPvpMyMap.put(playerId, randTeamBattleForceMyItem);
	}

	public void setArenaRankMy(int playerId, RankMyItem randTeamBattleForceMyItem) {
		arenaRankMyMap.put(playerId, randTeamBattleForceMyItem);
	}

	public void setDBRankBattleForceMy(List<DBRankMyItem> dbSaveList) {
		toSaveListDBRankBattleForceMy = dbSaveList;
	}

	public RankMyItem getHeroStarsRankMy(int playerId) {
		return heroStarsMyMap.get(playerId);
	}

	public void setHeroStarsRankMy(int playerId, RankMyItem randTeamBattleForceMyItem) {
		heroStarsMyMap.put(playerId, randTeamBattleForceMyItem);
	}

	public void setHeroStarsMy(List<DBRankMyItem> dbSaveList) {
		toSaveListHeroStars = dbSaveList;
	}

	public RankMyItem getPlayerLevelRankMy(int playerId) {
		return playerLevelMyMap.get(playerId);
	}

	public void setPlayerLevelRankMy(int playerId, RankMyItem randTeamBattleForceMyItem) {
		playerLevelMyMap.put(playerId, randTeamBattleForceMyItem);
	}

	public void setPlayerLevelMy(List<DBRankMyItem> dbSaveList) {
		toSaveListPlayerLevel = dbSaveList;
	}

	public void setPlayerLevelRankMap(Map<Integer, DBPlayerLevelRank1> newRankListMap) {
		playerLevelRankMap = newRankListMap;
	}

	public void setTeamBfRankMap(Map<Integer, DBRankBattleForce1> newRankListMap) {
		teamBattleForceRankMap = newRankListMap;
	}

	public void setArenaRankMap(Map<Integer, DbRankArena.DbRankArena1> newRankListMap) {
		arenaRankMap = newRankListMap;
	}

	public List<DbRankTower.DbRankTower1> getTowerRankList() {
		return towerRankList;
	}

	public void setTowerRankList(List<DbRankTower.DbRankTower1> towerRankList) {
		this.towerRankList = towerRankList;
	}

	public Map<Integer, DbRankTower.DbRankTower1> getTowerRankMap() {
		return towerRankMap;
	}

	public void setTowerRankMap(Map<Integer, DbRankTower.DbRankTower1> towerRankMap) {
		this.towerRankMap = towerRankMap;
	}
	
	public List<DbRankDungeon.DbRankDungeon1> getDungeonRankList() {
		return dungeonRankList;
	}

	public void setDungeonRankList(List<DbRankDungeon.DbRankDungeon1> dungeonRankList) {
		this.dungeonRankList = dungeonRankList;
	}

	public Map<Integer, DbRankDungeon.DbRankDungeon1> getDungeonRankMap() {
		return dungeonRankMap;
	}

	public void setDungeonRankMap(Map<Integer, DbRankDungeon.DbRankDungeon1> dungeonRankMap) {
		this.dungeonRankMap = dungeonRankMap;
	}
	
	public List<DbRankKingPvp.DbRankKingPvp1> getKingPvpRankList() {
		return kingPvpRankList;
	}

	public void setKingPvpRankList(List<DbRankKingPvp.DbRankKingPvp1> kingPvpRankList) {
		this.kingPvpRankList = kingPvpRankList;
	}

	public Map<Integer, DbRankKingPvp.DbRankKingPvp1> getKingPvpRankMap() {
		return kingPvpRankMap;
	}

	public void setKingPvpRankMap(Map<Integer, DbRankKingPvp.DbRankKingPvp1> kingPvpRankMap) {
		this.kingPvpRankMap = kingPvpRankMap;
	}

	public void setToSaveListTower(List<DBRankMyItem> toSaveListTower) {
		this.toSaveListTower = toSaveListTower;
	}
	
	public void setToSaveListDungeon(List<DBRankMyItem> toSaveListDungeon) {
		this.toSaveListDungeon = toSaveListDungeon;
	}
	
	public void setToSaveListkingPvp(List<DBRankMyItem> toSaveListkingPvp) {
		this.toSaveListKingPvp = toSaveListkingPvp;
	}

	public void setToSaveListDbArena(List<DBRankMyItem> toSaveListDbArena) {
		this.toSaveListDbArena = toSaveListDbArena;
	}

	public List<DbRankArena.DbRankArena1> getArenaRankList() {
		return arenaRankList;
	}

	public void setArenaRankList(List<DbRankArena.DbRankArena1> arenaRankList) {
		this.arenaRankList = arenaRankList;
	}

}
