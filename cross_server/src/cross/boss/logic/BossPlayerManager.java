package cross.boss.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossPlayer.CrossPlayerId;
import cross.boss.bean.CrossBossPlayerJoinInfo;

public class BossPlayerManager {

	private static Logger logger = LoggerFactory.getLogger(BossPlayerManager.class);

	static class SingletonHolder {
		static BossPlayerManager instance = new BossPlayerManager();
	}

	public static BossPlayerManager getInstance() {
		return SingletonHolder.instance;
	}

	private Map<CrossPlayerId, CrossBossPlayerJoinInfo> playerInfoMap = new ConcurrentHashMap<>();

	public CrossBossPlayerJoinInfo getPlayerJoinInfo(int playerId, int serverId) {
		return playerInfoMap.get(new CrossPlayerId(serverId, playerId));
	}

	public void addPlayerJoinInfo(int playerId, int serverId, CrossBossPlayerJoinInfo crossBossPlayerJoinInfo) {
		playerInfoMap.put(new CrossPlayerId(serverId, playerId), crossBossPlayerJoinInfo);
	}

	public Integer getRoomId(int playerId, int serverId, int roomTypeId) {
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = playerInfoMap.get(new CrossPlayerId(serverId, playerId));
		if (crossBossPlayerJoinInfo != null) {
			Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
			if (roomIdMap != null) {
				return roomIdMap.get(roomTypeId);
			}
		}
		return null;
	}

	public void putRoomId(CrossPlayerId crossPlayerId, int roomTypeId, int roomId) {
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = playerInfoMap.get(crossPlayerId);
		if (crossBossPlayerJoinInfo == null) {
			crossBossPlayerJoinInfo = new CrossBossPlayerJoinInfo();
			crossBossPlayerJoinInfo.setPlayerId(crossPlayerId);
			playerInfoMap.put(crossPlayerId, crossBossPlayerJoinInfo);
		}
		Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
		if (roomIdMap == null) {
			roomIdMap = new HashMap<>();
			crossBossPlayerJoinInfo.setRoomIdMap(roomIdMap);
		}
		roomIdMap.put(roomTypeId, roomId);
	}
	
	public Map<CrossPlayerId, CrossBossPlayerJoinInfo> getPlayerJoins(){
		return playerInfoMap;
	}

}
