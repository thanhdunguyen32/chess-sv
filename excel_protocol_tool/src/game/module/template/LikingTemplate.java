package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 邮件：配置文件类
 * 
 * @author zhangning
 *
 */
@ExcelTemplateAnn(file = "Liking")
public class LikingTemplate {

	private Integer id;

	private Integer exp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
