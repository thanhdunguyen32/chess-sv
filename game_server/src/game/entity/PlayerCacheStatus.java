package game.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlayerCacheStatus {

	public static enum STATUS_LIST {
		LAST_CHAT_TIME_MM
	};

	private PlayerPosition currentPosition = PlayerPosition.PLAYER_POSITION_HALL;

	private Map<STATUS_LIST, Object> statusMap;

	private Date enterGameTime;

	private Integer pvpBattleId;

	private Integer pvpRoomId;

	private Integer pvpTeamId;

	private Integer pvpMatchType;
	
	private Integer craftRoomId;
	
	private Long randBattleId;

	public PlayerCacheStatus() {
		statusMap = new HashMap<PlayerCacheStatus.STATUS_LIST, Object>();
	}

	public Long getLastChatTime() {
		return (Long) statusMap.get(STATUS_LIST.LAST_CHAT_TIME_MM);
	}

	public void setLastChatTime(Long lastChatTime) {
		statusMap.put(STATUS_LIST.LAST_CHAT_TIME_MM, lastChatTime);
	}

	public void setPosition(PlayerPosition newPosition) {
		currentPosition = newPosition;
	}

	public PlayerPosition getPosition() {
		return currentPosition;
	}

	public Date getEnterGameTime() {
		return enterGameTime;
	}

	public void setEnterGameTime(Date enterGameTime) {
		this.enterGameTime = enterGameTime;
	}

	public Integer getPvpBattleId() {
		return pvpBattleId;
	}

	public void setPvpBattleId(Integer pvpBattleId) {
		this.pvpBattleId = pvpBattleId;
	}

	public Integer getPvpRoomId() {
		return pvpRoomId;
	}

	public void setPvpRoomId(Integer pvpRoomId) {
		this.pvpRoomId = pvpRoomId;
	}

	public Integer getPvpTeamId() {
		return pvpTeamId;
	}

	public void setPvpTeamId(Integer pvpTeamId) {
		this.pvpTeamId = pvpTeamId;
	}

	public Integer getPvpMatchType() {
		return pvpMatchType;
	}

	public void setPvpMatchType(Integer pvpMatchType) {
		this.pvpMatchType = pvpMatchType;
	}

	public Integer getCraftRoomId() {
		return craftRoomId;
	}

	public void setCraftRoomId(Integer craftRoomId) {
		this.craftRoomId = craftRoomId;
	}

	public enum PlayerPosition {
		PLAYER_POSITION_HALL, PLAYER_POSITION_STAGE, PLAYER_POSITION_TOWER, PLAYER_POSITION_SECRET, PLAYER_POSITION_BOSS, PLAYER_POSITION_PVP_BATTLE, PLAYER_POSITION_PVP_PENDING, PLAYER_POSITION_PVP_ROOM, PLAYER_POSITION_PVP_LOADING, PLAYER_POSITION_PVP_TEAM, PLAYER_POSITION_PVP_MATCHING,
		PLAYER_POSITION_CRAFT_MATCHING,PLAYER_POSITION_CRAFT_ROOM,PLAYER_POSITION_CRAFT_LOADING,PLAYER_POSITION_CRAFT_BATTLE,PLAYER_POSITION_CROSS_BOSS_LOADING,PLAYER_POSITION_CROSS_BOSS_BATTLE
	}

	public Long getRandBattleId() {
		return randBattleId;
	}

	public void setRandBattleId(Long randBattleId) {
		this.randBattleId = randBattleId;
	}

}
