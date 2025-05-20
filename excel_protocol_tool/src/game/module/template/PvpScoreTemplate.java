package game.module.template;

@ExcelTemplateAnn(file = "PvpScore")
public class PvpScoreTemplate {

	private Integer id;

	private Integer score;

	private Integer reward_hammer;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getReward_hammer() {
		return reward_hammer;
	}

	public void setReward_hammer(Integer reward_hammer) {
		this.reward_hammer = reward_hammer;
	}

}
