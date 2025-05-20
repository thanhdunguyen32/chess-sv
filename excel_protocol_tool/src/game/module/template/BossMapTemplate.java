package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 秘密基地关卡配置表
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午4:34:09
 */
@ExcelTemplateAnn(file = "laboratoryTotal")
public class BossMapTemplate {

	/**
	 * 关卡ID
	 */
	private Integer id;

	private String time;

	// 挑战次数
	private Integer num;

	// 关卡组
	private String stage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
