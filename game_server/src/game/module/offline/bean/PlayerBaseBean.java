package game.module.offline.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlayerBaseBean {

	private Integer id;

	private String accountId;

	private Integer serverId;

	private String name;

	private Integer level;

	private Integer sex;

	private Integer vipLevel;

	private Integer iconid;
	private Integer headid;
	private Integer frameid;
	private Integer imageid;

	private Integer nationId;

	private Date downlineTime;

	/**
	 * 战斗力
	 */
	private Integer power;

	private Map<Integer,Date> activityTipModules = new HashMap<Integer,Date>();

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getIconid() {
		return iconid;
	}

	public void setIconid(Integer iconid) {
		this.iconid = iconid;
	}

	public Integer getHeadid() {
		return headid;
	}

	public void setHeadid(Integer headid) {
		this.headid = headid;
	}

	public Integer getFrameid() {
		return frameid;
	}

	public void setFrameid(Integer frameid) {
		this.frameid = frameid;
	}

	public Integer getImageid() {
		return imageid;
	}

	public void setImageid(Integer imageid) {
		this.imageid = imageid;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Map<Integer, Date> getActivityTipModules() {
		return activityTipModules;
	}

	public void setActivityTipModules(Map<Integer, Date> activityTipModules) {
		this.activityTipModules = activityTipModules;
	}

	@Override
	public String toString() {
		return "PlayerBaseBean{" +
				"id=" + id +
				", accountId='" + accountId + '\'' +
				", serverId=" + serverId +
				", name='" + name + '\'' +
				", level=" + level +
				", sex=" + sex +
				", vipLevel=" + vipLevel +
				", iconid=" + iconid +
				", headid=" + headid +
				", frameid=" + frameid +
				", imageid=" + imageid +
				", nationId=" + nationId +
				", downlineTime=" + downlineTime +
				", power=" + power +
				", activityTipModules=" + activityTipModules +
				'}';
	}

	public Date getDownlineTime() {
		return downlineTime;
	}

	public void setDownlineTime(Date downlineTime) {
		this.downlineTime = downlineTime;
	}

	public Integer getNationId() {
		return nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}
}
