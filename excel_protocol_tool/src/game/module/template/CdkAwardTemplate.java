package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 邮件：配置文件类
 * 
 * @author zhangning
 *
 */
@ExcelTemplateAnn(file = "CdkAward")
public class CdkAwardTemplate {

	private Integer id;

	private String item;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
