package game.module.mission.bean;

public class TaskEntity {

	private Integer id;
	
	private Integer playerId;

	private Integer templateId;

	private Integer progress;

	private Boolean awardGet;

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

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Boolean getAwardGet() {
		return awardGet;
	}

	public void setAwardGet(Boolean awardGet) {
		this.awardGet = awardGet;
	}

	@Override
	public String toString() {
		return "TaskEntity [id=" + id + ", playerId=" + playerId + ", templateId=" + templateId + ", progress="
				+ progress + ", awardGet=" + awardGet + "]";
	}
	
}
