package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "AcrossBossReward")
public class CrossBossRewardTemplate {

	private Integer id;

	private Integer room_type_id;
	
	private Integer rank;
	
	private String reward_item;
	
	private Integer reward_item_count;
	
	private Integer reward_coins;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Integer getRoom_type_id() {
		return room_type_id;
	}

	public void setRoom_type_id(Integer room_type_id) {
		this.room_type_id = room_type_id;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getReward_item() {
		return reward_item;
	}

	public void setReward_item(String reward_item) {
		this.reward_item = reward_item;
	}

	public Integer getReward_item_count() {
		return reward_item_count;
	}

	public void setReward_item_count(Integer reward_item_count) {
		this.reward_item_count = reward_item_count;
	}

	public Integer getReward_coins() {
		return reward_coins;
	}

	public void setReward_coins(Integer reward_coins) {
		this.reward_coins = reward_coins;
	}

}
