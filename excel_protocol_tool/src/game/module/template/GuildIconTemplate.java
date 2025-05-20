package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "GuildIcon")
public class GuildIconTemplate {

	private Integer id;

	private Integer type;

	private Integer obtain;

	private Integer money;

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

	public Integer getObtain() {
		return obtain;
	}

	public void setObtain(Integer obtain) {
		this.obtain = obtain;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
