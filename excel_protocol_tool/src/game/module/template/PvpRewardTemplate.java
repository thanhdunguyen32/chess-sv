package game.module.template;

@ExcelTemplateAnn(file = "PvpReward")
public class PvpRewardTemplate {

	private Integer id;

	private String range;

	private String award;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

}
