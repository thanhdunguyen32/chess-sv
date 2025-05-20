package game.module.secret.bean;

import java.util.Arrays;

public class SecretTemplate {

	/**
	 * 关卡ID
	 */
	private Integer id;

	private Integer lv;

	private int[] win_strength;
	
	private int[] loose_strength;
	
	private int[] award_chest;
	
	private int[] copper_award;
	
	private int super_award;
	
	private int[] stage_list;

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

	public int[] getWin_strength() {
		return win_strength;
	}

	public void setWin_strength(int[] win_strength) {
		this.win_strength = win_strength;
	}

	public int[] getLoose_strength() {
		return loose_strength;
	}

	public void setLoose_strength(int[] loose_strength) {
		this.loose_strength = loose_strength;
	}

	public int[] getAward_chest() {
		return award_chest;
	}

	public void setAward_chest(int[] award_chest) {
		this.award_chest = award_chest;
	}

	@Override
	public String toString() {
		return "SecretTemplate [id=" + id + ", lv=" + lv + ", win_strength=" + Arrays.toString(win_strength)
				+ ", loose_strength=" + Arrays.toString(loose_strength) + ", award_chest="
				+ Arrays.toString(award_chest) + ", copper_award=" + Arrays.toString(copper_award) + ", super_award="
				+ super_award + ", stage_list=" + Arrays.toString(stage_list) + "]";
	}

	public int[] getStage_list() {
		return stage_list;
	}

	public void setStage_list(int[] stage_list) {
		this.stage_list = stage_list;
	}

	public int[] getCopper_award() {
		return copper_award;
	}

	public void setCopper_award(int[] copper_award) {
		this.copper_award = copper_award;
	}

	public int getSuper_award() {
		return super_award;
	}

	public void setSuper_award(int super_award) {
		this.super_award = super_award;
	}
	
}
