package game.module.guozhan.bean;

import java.util.Date;

import db.proto.ProtoMessageGuozhan.DBGuozhanPlayer;

public class GuozhanPlayer {
	
	private int id;

	private int playerId;
	
	private DBGuozhanPlayer dbGuozhanPlayer;
	
	private int stay_city_index;

	private long r;
	
	private int nation;
	
	private Date nationChangeTime;
	
	private int officeId;
	
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public DBGuozhanPlayer getDbGuozhanPlayer() {
		return dbGuozhanPlayer;
	}

	public void setDbGuozhanPlayer(DBGuozhanPlayer dbGuozhanPlayer) {
		this.dbGuozhanPlayer = dbGuozhanPlayer;
	}

	public int getStay_city_index() {
		return stay_city_index;
	}

	public void setStay_city_index(int stay_city_index) {
		this.stay_city_index = stay_city_index;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getR() {
		return r;
	}

	public void setR(long r) {
		this.r = r;
	}

	public int getNation() {
		return nation;
	}

	public void setNation(int nation) {
		this.nation = nation;
	}

	public Date getNationChangeTime() {
		return nationChangeTime;
	}

	public void setNationChangeTime(Date nationChangeTime) {
		this.nationChangeTime = nationChangeTime;
	}

	public int getOfficeId() {
		return officeId;
	}

	public void setOfficeId(int officeId) {
		this.officeId = officeId;
	}
	
}
