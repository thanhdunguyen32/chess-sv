package cc.mrbird.febs.system.entity;

import lombok.Data;

@Data
public class PlayerBaseInfoEntity {

	private int id;

	private String name;

	private int level;

	private int viplevel;

	private int fight;

	private int gold;

	private int cooper;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getViplevel() {
		return viplevel;
	}

	public void setViplevel(int viplevel) {
		this.viplevel = viplevel;
	}

	public int getFight() {
		return fight;
	}

	public void setFight(int fight) {
		this.fight = fight;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getCooper() {
		return cooper;
	}

	public void setCooper(int cooper) {
		this.cooper = cooper;
	}

}
