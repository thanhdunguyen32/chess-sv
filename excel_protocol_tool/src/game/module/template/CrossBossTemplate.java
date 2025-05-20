package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "AcrossBoss")
public class CrossBossTemplate {

	private Integer id;

	private Integer boss_id;
	
	private String lv_limit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Integer getBoss_id() {
		return boss_id;
	}

	public void setBoss_id(Integer boss_id) {
		this.boss_id = boss_id;
	}

	public String getLv_limit() {
		return lv_limit;
	}

	public void setLv_limit(String lv_limit) {
		this.lv_limit = lv_limit;
	}

}
