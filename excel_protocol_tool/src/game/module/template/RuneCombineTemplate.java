package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "RuneCombine")
public class RuneCombineTemplate {

	private Integer id;

	private Integer quality;

	private Integer base_success_rate;

	private Integer number;

	private Integer coins;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getBase_success_rate() {
		return base_success_rate;
	}

	public void setBase_success_rate(Integer base_success_rate) {
		this.base_success_rate = base_success_rate;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getCoins() {
		return coins;
	}

	public void setCoins(Integer coins) {
		this.coins = coins;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
