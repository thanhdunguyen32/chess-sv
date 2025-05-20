package game.module.rank.bean;

import game.module.rank.logic.RankManager.RankMyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRankData {
    private List<DBPlayerLevelRank1> playerLevelRankList = new ArrayList<>();
    private List<DBRankBattleForce1> teamBattleForceRankList = new ArrayList<>();
    private List<DbRankTower.DbRankTower1> towerRankList = new ArrayList<>();
    private List<DbRankDungeon.DbRankDungeon1> dungeonRankList = new ArrayList<>();
    private List<DbRankKingPvp.DbRankKingPvp1> kingPvpRankList = new ArrayList<>();
    private List<DbRankArena.DbRankArena1> arenaRankList = new ArrayList<>();
    private List<DBRankHeroStars1> heroStarsRankList = new ArrayList<>();

    private Map<Integer, RankMyItem> playerLevelMyMap = new ConcurrentHashMap<>();
    private Map<Integer, RankMyItem> teamBattleForceRankMyMap = new ConcurrentHashMap<>();
    private Map<Integer, RankMyItem> towerMyMap = new ConcurrentHashMap<>();
    private Map<Integer, RankMyItem> dungeonMyMap = new ConcurrentHashMap<>();
    private Map<Integer, RankMyItem> kingPvpMyMap = new ConcurrentHashMap<>();
    private Map<Integer, RankMyItem> arenaRankMyMap = new ConcurrentHashMap<>();
    private Map<Integer, RankMyItem> heroStarsMyMap = new ConcurrentHashMap<>();

    private List<DBRankMyItem> toSaveListDbArena = new ArrayList<>();
    private List<DBRankMyItem> toSaveListDBRankBattleForceMy = new ArrayList<>();
    private List<DBRankMyItem> toSaveListTower = new ArrayList<>();
    private List<DBRankMyItem> toSaveListDungeon = new ArrayList<>();
    private List<DBRankMyItem> toSaveListKingPvp = new ArrayList<>();

    public ServerRankData() {
    }

    // Getters and setters for all fields
    public List<DBPlayerLevelRank1> getPlayerLevelRankList() {
        return playerLevelRankList;
    }

    public void setPlayerLevelRankList(List<DBPlayerLevelRank1> playerLevelRankList) {
        this.playerLevelRankList = playerLevelRankList;
    }

    public List<DBRankBattleForce1> getTeamBattleForceRankList() {
        return teamBattleForceRankList;
    }

    public void setTeamBattleForceRankList(List<DBRankBattleForce1> teamBattleForceRankList) {
        this.teamBattleForceRankList = teamBattleForceRankList;
    }

    public List<DbRankTower.DbRankTower1> getTowerRankList() {
        return towerRankList;
    }

    public void setTowerRankList(List<DbRankTower.DbRankTower1> towerRankList) {
        this.towerRankList = towerRankList;
    }

    public List<DbRankDungeon.DbRankDungeon1> getDungeonRankList() {
        return dungeonRankList;
    }

    public void setDungeonRankList(List<DbRankDungeon.DbRankDungeon1> dungeonRankList) {
        this.dungeonRankList = dungeonRankList;
    }

    public List<DbRankKingPvp.DbRankKingPvp1> getKingPvpRankList() {
        return kingPvpRankList;
    }

    public void setKingPvpRankList(List<DbRankKingPvp.DbRankKingPvp1> kingPvpRankList) {
        this.kingPvpRankList = kingPvpRankList;
    }

    public List<DbRankArena.DbRankArena1> getArenaRankList() {
        return arenaRankList;
    }

    public void setArenaRankList(List<DbRankArena.DbRankArena1> arenaRankList) {
        this.arenaRankList = arenaRankList;
    }

    public List<DBRankHeroStars1> getHeroStarsRankList() {
        return heroStarsRankList;
    }

    public void setHeroStarsRankList(List<DBRankHeroStars1> heroStarsRankList) {
        this.heroStarsRankList = heroStarsRankList;
    }

    public Map<Integer, RankMyItem> getPlayerLevelMyMap() {
        return playerLevelMyMap;
    }

    public Map<Integer, RankMyItem> getTeamBattleForceRankMyMap() {
        return teamBattleForceRankMyMap;
    }

    public Map<Integer, RankMyItem> getTowerMyMap() {
        return towerMyMap;
    }

    public Map<Integer, RankMyItem> getDungeonMyMap() {
        return dungeonMyMap;
    }

    public Map<Integer, RankMyItem> getKingPvpMyMap() {
        return kingPvpMyMap;
    }

    public Map<Integer, RankMyItem> getArenaRankMyMap() {
        return arenaRankMyMap;
    }

    public Map<Integer, RankMyItem> getHeroStarsMyMap() {
        return heroStarsMyMap;
    }

    public void setToSaveListDbArena(List<DBRankMyItem> toSaveListDbArena) {
        this.toSaveListDbArena = toSaveListDbArena;
    }

    public void setToSaveListDBRankBattleForceMy(List<DBRankMyItem> toSaveListDBRankBattleForceMy) {
        this.toSaveListDBRankBattleForceMy = toSaveListDBRankBattleForceMy;
    }

    public void setToSaveListTower(List<DBRankMyItem> toSaveListTower) {
        this.toSaveListTower = toSaveListTower;
    }

    public void setToSaveListDungeon(List<DBRankMyItem> toSaveListDungeon) {
        this.toSaveListDungeon = toSaveListDungeon;
    }

    public void setToSaveListKingPvp(List<DBRankMyItem> toSaveListKingPvp) {
        this.toSaveListKingPvp = toSaveListKingPvp;
    }
} 