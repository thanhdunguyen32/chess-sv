package game.module.template;

import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "s_bad_words")
public class BadWordTemplate {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BadWordTemplate [name=" + name + "]";
	}

}
