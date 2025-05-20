package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "AnswerReward")
public class AnswerRewardTemplate {

	private Integer id;

	private Integer type;

	private Integer award;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAward() {
		return award;
	}

	public void setAward(Integer award) {
		this.award = award;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
