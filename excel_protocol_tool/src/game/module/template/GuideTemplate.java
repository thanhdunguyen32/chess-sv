package game.module.template;


@ExcelTemplateAnn(file = "Guide")
public class GuideTemplate {

	private Integer id;

	private Integer unlcok_level;

	private Integer battle_times;

	private String hero_reward;

	private Integer stage_clearup;
	
	private String open_item_reward;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUnlcok_level() {
		return unlcok_level;
	}

	public void setUnlcok_level(Integer unlcok_level) {
		this.unlcok_level = unlcok_level;
	}

	public Integer getBattle_times() {
		return battle_times;
	}

	public void setBattle_times(Integer battle_times) {
		this.battle_times = battle_times;
	}

	public String getHero_reward() {
		return hero_reward;
	}

	public void setHero_reward(String hero_reward) {
		this.hero_reward = hero_reward;
	}

	public Integer getStage_clearup() {
		return stage_clearup;
	}

	public void setStage_clearup(Integer stage_clearup) {
		this.stage_clearup = stage_clearup;
	}

	public String getOpen_item_reward() {
		return open_item_reward;
	}

	public void setOpen_item_reward(String open_item_reward) {
		this.open_item_reward = open_item_reward;
	}

}
