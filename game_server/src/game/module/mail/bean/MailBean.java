package game.module.mail.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 邮件Entity
 * 
 * @author zhangning
 * @Date 2014年12月29日 上午11:53:38
 *
 */
public class MailBean {

	private int id;
	private int playerId;
	private byte type;
	private byte state;
	private String fromType;
	private Integer fromFid;
	private Integer fromLegionId;
	private String title;
	private String content;
	private Map<Integer,Integer> attachs;
	private Date createTime;
	private Date endTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public Integer getFromFid() {
		return fromFid;
	}

	public void setFromFid(Integer fromFid) {
		this.fromFid = fromFid;
	}

	public Integer getFromLegionId() {
		return fromLegionId;
	}

	public void setFromLegionId(Integer fromLegionId) {
		this.fromLegionId = fromLegionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<Integer, Integer> getAttachs() {
		return attachs;
	}

	public void setAttachs(Map<Integer, Integer> attachs) {
		this.attachs = attachs;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "MailBean{" +
				"id=" + id +
				", playerId=" + playerId +
				", type=" + type +
				", state=" + state +
				", fromType='" + fromType + '\'' +
				", fromFid=" + fromFid +
				", fromLegionId=" + fromLegionId +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", attachs=" + attachs +
				", createTime=" + createTime +
				", endTime=" + endTime +
				'}';
	}

}
