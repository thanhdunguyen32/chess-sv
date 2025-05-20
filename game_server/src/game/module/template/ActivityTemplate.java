package game.module.template;

import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "s_activity_my")
public class ActivityTemplate {

	private Integer id;
	
	private String name;

	private Integer type;
	
	private Integer priority;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "ActivityTemplate [id=" + id + ", name=" + name + ", type=" + type + ", priority=" + priority + "]";
	}

}
