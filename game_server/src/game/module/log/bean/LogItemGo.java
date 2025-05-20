package game.module.log.bean;

import java.util.Date;

/**
 * 玩家消耗日志
 * 
 * @author zhangning
 * 
 * @Date 2015年5月26日 下午2:47:51
 */
public class LogItemGo {

	private int playerId;

	/**
	 * 模块类型: 0其他模块, 1装备合成, 2商店, 3购买体力, 4过往之球, 5现在之球, 6未来之球, 7精英副本刷新
	 */
	private int moduleType;

	/**
	 * 货币类型: 金币1000, 钻石币1001, 天空币1002, 勇气币1003, 正义币1004
	 */
	private int itemId;

	/**
	 * 值
	 */
	private int changeValue;
	
	private String playerName;
	
	private Date createTime;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getModuleType() {
		return moduleType;
	}

	public void setModuleType(int moduleType) {
		this.moduleType = moduleType;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(int changeValue) {
		this.changeValue = changeValue;
	}

}
