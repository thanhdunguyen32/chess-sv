package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 邮件：配置文件类
 * 
 * @author zhangning
 *
 */
@ExcelTemplateAnn(file = "SecretSoldier")
public class SecretSoldierTemplate {

	private Integer id;

	private String soldier;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSoldier() {
		return soldier;
	}

	public void setSoldier(String soldier) {
		this.soldier = soldier;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
