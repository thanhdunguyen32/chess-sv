package cross.boss.bean;

import java.util.HashMap;
import java.util.Map;

import cross.boss.bean.CrossBossPlayer.CrossPlayerId;

public class CrossBossRoom {

	private int id;
	
	/**
	 * 
	 * 1,2,3一共3档
	 * 
	 */
	private int roomType;
	
	private Map<Integer, CrossBossPlayer> playerMap = new HashMap<>();
	
	private Map<Integer, CrossBossPlayer> loadingPlayerMap = new HashMap<>();
	
	private Map<Integer, Integer> damageSum = new HashMap<>();
	
	private Long startTime;

	private CrossBossEntity crossBossEntity;
	
	private boolean isFinish = false;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Integer, CrossBossPlayer> getPlayerMap() {
		return playerMap;
	}

	public void setPlayerMap(Map<Integer, CrossBossPlayer> playerMap) {
		this.playerMap = playerMap;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}

	public Map<Integer, CrossBossPlayer> getLoadingPlayerMap() {
		return loadingPlayerMap;
	}

	public void setLoadingPlayerMap(Map<Integer, CrossBossPlayer> loadingPlayerMap) {
		this.loadingPlayerMap = loadingPlayerMap;
	}

	public CrossBossEntity getCrossBossEntity() {
		return crossBossEntity;
	}

	public void setCrossBossEntity(CrossBossEntity crossBossEntity) {
		this.crossBossEntity = crossBossEntity;
	}

	public Map<Integer, Integer> getDamageSum() {
		return damageSum;
	}

	public void setDamageSum(Map<Integer, Integer> damageSum) {
		this.damageSum = damageSum;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	
}
