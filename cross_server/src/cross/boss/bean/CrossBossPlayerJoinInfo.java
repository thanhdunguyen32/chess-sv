package cross.boss.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cross.boss.bean.CrossBossPlayer.CrossPlayerId;

public class CrossBossPlayerJoinInfo {

	private CrossPlayerId playerId;

	private Set<Integer> finishRoomIds;

	private int roomTypeId;

	private Map<Integer, Integer> roomIdMap = new HashMap<>();

	public CrossPlayerId getPlayerId() {
		return playerId;
	}

	public void setPlayerId(CrossPlayerId playerId) {
		this.playerId = playerId;
	}

	public Set<Integer> getFinishRoomIds() {
		return finishRoomIds;
	}

	public void setFinishRoomIds(Set<Integer> finishRoomIds) {
		this.finishRoomIds = finishRoomIds;
	}

	public Map<Integer, Integer> getRoomIdMap() {
		return roomIdMap;
	}

	public void setRoomIdMap(Map<Integer, Integer> roomIdMap) {
		this.roomIdMap = roomIdMap;
	}

	public int getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(int roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

}
