package test.excel;

import game.common.excel.ExcelTemplateAnn;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "Stage.xlsx")
public class StageTemplate {

	private Integer id;

	private String name;

	private String battles;

	private String scene;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBattles() {
		return battles;
	}

	public void setBattles(String battles) {
		this.battles = battles;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
