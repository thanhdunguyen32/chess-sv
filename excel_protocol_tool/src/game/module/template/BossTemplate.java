package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 秘密基地配置表
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午4:33:55
 */
@ExcelTemplateAnn(file = "laboratoryStage")
public class BossTemplate {

	/**
	 * 关卡ID
	 */
	private Integer id;

	private Integer lv;

	private Integer sweep;

	private String serveraward;

	private Integer award2;

	private Integer award_money;

	private Integer heroexp;

	private Integer winenergy;

	private Integer loseenergy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public Integer getSweep() {
		return sweep;
	}

	public void setSweep(Integer sweep) {
		this.sweep = sweep;
	}

	public String getServeraward() {
		return serveraward;
	}

	public void setServeraward(String serveraward) {
		this.serveraward = serveraward;
	}

	public Integer getAward2() {
		return award2;
	}

	public void setAward2(Integer award2) {
		this.award2 = award2;
	}

	public Integer getAward_money() {
		return award_money;
	}

	public void setAward_money(Integer award_money) {
		this.award_money = award_money;
	}

	public Integer getHeroexp() {
		return heroexp;
	}

	public void setHeroexp(Integer heroexp) {
		this.heroexp = heroexp;
	}

	public Integer getWinenergy() {
		return winenergy;
	}

	public void setWinenergy(Integer winenergy) {
		this.winenergy = winenergy;
	}

	public Integer getLoseenergy() {
		return loseenergy;
	}

	public void setLoseenergy(Integer loseenergy) {
		this.loseenergy = loseenergy;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
