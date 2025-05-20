package cross.boss.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import game.module.craft.bean.CraftRoom.CraftPlayerInfo;
import game.module.cross.bean.CrossBossBegin;
import game.module.pvp.bean.PvpPlayer.PVP_BATTLE_STATUS;
import lion.netty4.message.GamePlayer;

public class CrossBossPlayer {

	private int uuid;

	private CrossPlayerId crossPlayerId;

	private CrossBossBegin crossPlayerInfo;

	private float[] pos_pair;
	
	private float[] target_pos_pair;

	private int hp;

	private int maxHp;

	private int sp;

	private float speedUpRate;

	private float[] born_position;

	private PVP_BATTLE_STATUS rawStatus;
	
	private GamePlayer netty4Session;
	
	private Long lastUpdateTime = 0L;
	
	private boolean isMove;
	
	private boolean isFirstCreate = true;

	public CrossPlayerId getCrossPlayerId() {
		return crossPlayerId;
	}

	public void setCrossPlayerId(CrossPlayerId crossPlayerId) {
		this.crossPlayerId = crossPlayerId;
	}

	public CrossBossBegin getCrossPlayerInfo() {
		return crossPlayerInfo;
	}

	public void setCrossPlayerInfo(CrossBossBegin crossPlayerInfo) {
		this.crossPlayerInfo = crossPlayerInfo;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getSp() {
		return sp;
	}

	public void setSp(int sp) {
		this.sp = sp;
	}

	public float[] getPos_pair() {
		return pos_pair;
	}

	public void setPos_pair(float[] pos_pair) {
		this.pos_pair = pos_pair;
	}

	public float[] getBorn_position() {
		return born_position;
	}

	public void setBorn_position(float[] born_position) {
		this.born_position = born_position;
	}

	public int getUuid() {
		return crossPlayerId.hashCode();
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public GamePlayer getNetty4Session() {
		return netty4Session;
	}

	public void setNetty4Session(GamePlayer netty4Session) {
		this.netty4Session = netty4Session;
	}

	public float getSpeedUpRate() {
		return speedUpRate;
	}

	public void setSpeedUpRate(float speedUpRate) {
		this.speedUpRate = speedUpRate;
	}

	public static final class CrossPlayerId {

		public int serverId;
		public int playerId;

		public CrossPlayerId(int serverId, int playerId) {
			this.serverId = serverId;
			this.playerId = playerId;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof CrossPlayerId)) {
				return false;
			}
			CrossPlayerId another = (CrossPlayerId) obj;
			if (this.serverId == another.serverId && this.playerId == another.playerId) {
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			int rethash = (serverId << 17) + playerId;
			return rethash;
		}

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this);
		}

	}

	public static void main(String[] args) {
		Map<CrossPlayerId, Integer> aMap = new HashMap<>();
		aMap.put(new CrossPlayerId(10, 5), 1);
		Integer ret = aMap.get(new CrossPlayerId(10, 5));
		System.out.println(ret);
	}

	public PVP_BATTLE_STATUS getRawStatus() {
		return rawStatus;
	}

	public void setRawStatus(PVP_BATTLE_STATUS rawStatus) {
		this.rawStatus = rawStatus;
	}

	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public boolean isMove() {
		return isMove;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public float[] getTarget_pos_pair() {
		return target_pos_pair;
	}

	public void setTarget_pos_pair(float[] target_pos_pair) {
		this.target_pos_pair = target_pos_pair;
	}

	public boolean isFirstCreate() {
		return isFirstCreate;
	}

	public void setFirstCreate(boolean isFirstCreate) {
		this.isFirstCreate = isFirstCreate;
	}

}
