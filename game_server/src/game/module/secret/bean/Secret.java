package game.module.secret.bean;

import java.util.Date;

import db.proto.ProtoMessageSecret.DBSecretBoxAward;
import db.proto.ProtoMessageSecret.DBSecretUsedHero;
import ws.WsMessageBase.IOSecretHero;

/**
 * 秘密基地
 * 
 * @author zhangning
 * 
 * @Date 2015年1月26日 下午3:15:47
 */
public class Secret {

	private int Id;
	private int playerId;
	private int mapId;
	private int progress;
	private int reviveCount;
	private DBSecretBoxAward boxAward;
	// 阵型中的英雄+士兵
	private DBSecretUsedHero formationHeros;
	private DBSecretUsedHero myCost;
	private DBSecretUsedHero enemyCost;
	private Date resetTime;

	//
	private int tmpMapId;
	
	private IOSecretHero[] tmpOnlineFormationList;
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public DBSecretBoxAward getBoxAward() {
		return boxAward;
	}

	public void setBoxAward(DBSecretBoxAward boxmAward) {
		this.boxAward = boxmAward;
	}

	public DBSecretUsedHero getFormationHeros() {
		return formationHeros;
	}

	public void setFormationHeros(DBSecretUsedHero formationHeros) {
		this.formationHeros = formationHeros;
	}

	public DBSecretUsedHero getMyCost() {
		return myCost;
	}

	public void setMyCost(DBSecretUsedHero myCost) {
		this.myCost = myCost;
	}

	public DBSecretUsedHero getEnemyCost() {
		return enemyCost;
	}

	public void setEnemyCost(DBSecretUsedHero enemyCost) {
		this.enemyCost = enemyCost;
	}

	public Date getResetTime() {
		return resetTime;
	}

	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
	}

	public int getReviveCount() {
		return reviveCount;
	}

	public void setReviveCount(int reviveCount) {
		this.reviveCount = reviveCount;
	}

	public int getTmpMapId() {
		return tmpMapId;
	}

	public void setTmpMapId(int tmpMapId) {
		this.tmpMapId = tmpMapId;
	}

	public IOSecretHero[] getTmpOnlineFormationList() {
		return tmpOnlineFormationList;
	}

	public void setTmpOnlineFormationList(IOSecretHero[] tmpOnlineFormationList) {
		this.tmpOnlineFormationList = tmpOnlineFormationList;
	}

}
