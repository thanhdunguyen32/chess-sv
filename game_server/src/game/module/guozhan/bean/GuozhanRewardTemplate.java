package game.module.guozhan.bean;

import java.util.List;

public class GuozhanRewardTemplate {

	private int money;

	private int gamemoney;

	private List<List<Integer>> items;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getGamemoney() {
		return gamemoney;
	}

	public void setGamemoney(int gamemoney) {
		this.gamemoney = gamemoney;
	}

	public List<List<Integer>> getItems() {
		return items;
	}

	public void setItems(List<List<Integer>> items) {
		this.items = items;
	}

}
