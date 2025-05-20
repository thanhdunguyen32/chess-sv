package game.module.cdkey.bean;

/**
 * 激活码奖励Entity
 * 
 * @author zhangning
 * 
 * @Date 2015年2月13日 上午7:09:11
 */
public class Cdkey {

	private int Id;
	private int platform;
	private int area;
	private String cdkey;
	private int playerId;
	private int cdkId;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public String getCdkey() {
		return cdkey;
	}

	public void setCdkey(String cdkey) {
		this.cdkey = cdkey;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getCdkId() {
		return cdkId;
	}

	public void setCdkId(int cdkId) {
		this.cdkId = cdkId;
	}

	@Override
	public String toString() {
		return "Cdkey [Id=" + Id + ", platform=" + platform + ", area=" + area + ", cdkey=" + cdkey + ", playerId="
				+ playerId + ", cdkId=" + cdkId + "]";
	}

}
