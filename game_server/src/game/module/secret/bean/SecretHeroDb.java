package game.module.secret.bean;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class SecretHeroDb {

	private Integer heroType;
	private Integer heroId;
	private Integer npcNum;
	private Integer hp;
	private Integer anger;

	public Integer getHeroType() {
		return heroType;
	}

	public void setHeroType(Integer heroType) {
		this.heroType = heroType;
	}

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public Integer getNpcNum() {
		return npcNum;
	}

	public void setNpcNum(Integer npcNum) {
		this.npcNum = npcNum;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	public Integer getAnger() {
		return anger;
	}

	public void setAnger(Integer anger) {
		this.anger = anger;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
