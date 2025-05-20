package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 在线奖励：配置文件类
 * 
 * @author zhangning
 *
 */
@ExcelTemplateAnn(file = "OnlineAward")
public class OnlineAwardTemplate {

	private Integer id;

	private Integer time;

	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
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
