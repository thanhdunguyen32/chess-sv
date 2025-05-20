package game.module.template;


@ExcelTemplateAnn(file = "EnchantQuality")
public class FuMoTemplate {

	private Integer id;

	private String exp;

	private Integer lv;

	private Float money;

	private Float diamond;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Float getDiamond() {
		return diamond;
	}

	public void setDiamond(Float diamond) {
		this.diamond = diamond;
	}

}
