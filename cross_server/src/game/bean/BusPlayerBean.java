package game.bean;

public class BusPlayerBean {

	private Integer zoneId;

	private String garenaOpenId;

	private Integer playerId;

	private String name;

	private Integer icon;

	private Integer level;

	public BusPlayerBean() {

	}

	public BusPlayerBean(Integer zoneId, String garenaOpenId, Integer playerId, String name, Integer icon, Integer level) {
		super();
		this.zoneId = zoneId;
		this.garenaOpenId = garenaOpenId;
		this.playerId = playerId;
		this.name = name;
		this.icon = icon;
		this.level = level;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public String getGarenaOpenId() {
		return garenaOpenId;
	}

	public void setGarenaOpenId(String garenaOpenId) {
		this.garenaOpenId = garenaOpenId;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIcon() {
		return icon;
	}

	public void setIcon(Integer icon) {
		this.icon = icon;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

}
