package game.module.pay.bean;

import java.util.Date;

public class BuyCostBean {

	private Integer id;

	private Integer playerId;

	private Integer skillPointCount;

	private Date skillPointTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public Integer getSkillPointCount() {
		return skillPointCount;
	}

	public void setSkillPointCount(Integer skillPointCount) {
		this.skillPointCount = skillPointCount;
	}

	public Date getSkillPointTime() {
		return skillPointTime;
	}

	public void setSkillPointTime(Date skillPointTime) {
		this.skillPointTime = skillPointTime;
	}

}
