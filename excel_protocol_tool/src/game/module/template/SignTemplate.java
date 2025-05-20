package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 签到：配置文件类
 * 
 * @author zhangning
 *
 */
@ExcelTemplateAnn(file = "Sign")
public class SignTemplate {

	private Integer id;

	private Integer month;

	private String item;

	private String hero;

	private Integer viplv;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getHero() {
		return hero;
	}

	public void setHero(String hero) {
		this.hero = hero;
	}

	public Integer getViplv() {
		return viplv;
	}

	public void setViplv(Integer viplv) {
		this.viplv = viplv;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
