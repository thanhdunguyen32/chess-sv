package game.module.item.bean;

import java.util.Date;

public class Item {

	private Integer id;

	private Integer playerId;
	
	private Integer templateId;

	private Integer count;

	private Date gainTime;
	
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

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getGainTime() {
		return gainTime;
	}

	public void setGainTime(Date gainTime) {
		this.gainTime = gainTime;
	}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", playerId=" + playerId +
				", templateId=" + templateId +
				", count=" + count +
				", gainTime=" + gainTime +
				'}';
	}

}
