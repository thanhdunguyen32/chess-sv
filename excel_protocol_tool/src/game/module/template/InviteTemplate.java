package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "Invite")
public class InviteTemplate {

	private Integer id;

	private Integer postposition;

	private Integer condition;

	private Integer param1;

	private Integer param2;

	private Integer param3;

	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPostposition() {
		return postposition;
	}

	public void setPostposition(Integer postposition) {
		this.postposition = postposition;
	}

	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public Integer getParam1() {
		return param1;
	}

	public void setParam1(Integer param1) {
		this.param1 = param1;
	}

	public Integer getParam2() {
		return param2;
	}

	public void setParam2(Integer param2) {
		this.param2 = param2;
	}

	public Integer getParam3() {
		return param3;
	}

	public void setParam3(Integer param3) {
		this.param3 = param3;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
