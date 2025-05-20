package game.module.sign.bean;

import java.util.Date;

/**
 * 签到Entity
 * 
 * @author zhangning
 * 
 * @Date 2015年1月12日 下午5:04:10
 */
public class SignIn {

	private int id;
	private int playerId;
	private Date lastSignTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public Date getLastSignTime() {
		return lastSignTime;
	}

	public void setLastSignTime(Date lastSignTime) {
		this.lastSignTime = lastSignTime;
	}

	@Override
	public String toString() {
		return "SignIn{" +
				"id=" + id +
				", playerId=" + playerId +
				", lastSignTime=" + lastSignTime +
				'}';
	}

}
