package game.module.cdkey.bean;

import java.util.Date;

/**
 * 激活码奖励Entity
 * 
 * @author zhangning
 * 
 * @Date 2015年2月13日 上午7:09:11
 */
public class Cdk {

	private int Id;
	private int platform;
	private int area;
	private String cdk;
	private String cdkName;
	private int awardId;
	private Integer moneyYuan;
	private int isReuse;
	private Date startTime;
	private Date endTime;
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

	public String getCdk() {
		return cdk;
	}

	public void setCdk(String cdk) {
		this.cdk = cdk;
	}

	public String getCdkName() {
		return cdkName;
	}

	public void setCdkName(String cdkName) {
		this.cdkName = cdkName;
	}

	public int getAwardId() {
		return awardId;
	}

	public void setAwardId(int awardId) {
		this.awardId = awardId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Cdk{" +
				"Id=" + Id +
				", platform=" + platform +
				", area=" + area +
				", cdk='" + cdk + '\'' +
				", cdkName='" + cdkName + '\'' +
				", awardId=" + awardId +
				", moneyYuan=" + moneyYuan +
				", isReuse=" + isReuse +
				", startTime=" + startTime +
				", endTime=" + endTime +
				'}';
	}

	public int getIsReuse() {
		return isReuse;
	}

	public void setIsReuse(int isReuse) {
		this.isReuse = isReuse;
	}

	public Integer getMoneyYuan() {
		return moneyYuan;
	}

	public void setMoneyYuan(Integer moneyYuan) {
		this.moneyYuan = moneyYuan;
	}
}
