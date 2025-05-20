package game.module.rank.bean;

public class RankArenaItem {

	private int robotId;

	private int playerId;

	private String robotName;
	
	private int rankChange;

	public int getRobotId() {
		return robotId;
	}

	public void setRobotId(int robotId) {
		this.robotId = robotId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getRobotName() {
		return robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public int getRankChange() {
		return rankChange;
	}

	public void setRankChange(int rankChange) {
		this.rankChange = rankChange;
	}

}
