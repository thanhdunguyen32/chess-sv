package game.module.lan.bean;

import io.netty.util.Timeout;


public class LoginSessionBean implements Comparable<Long> {

	private long sessionId;

	private String openId;

	private long createMiliSeconds;
	
	private Timeout timeout;
	
	private int serverId;
	
	public LoginSessionBean(long sessionId, String pOpenId, long createMiliSeconds,int serverId) {
		this.sessionId = sessionId;
		this.setOpenId(pOpenId);
		this.createMiliSeconds = createMiliSeconds;
		this.setServerId(serverId);
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public int compareTo(Long otherDate) {
		int ret = 0;
		if (getCreateMiliSeconds() > otherDate) {
			ret = 1;
		} else if (getCreateMiliSeconds() < otherDate) {
			ret = -1;
		}
		return ret;
	}

	public long getCreateMiliSeconds() {
		return createMiliSeconds;
	}

	public void setCreateMiliSeconds(long createMiliSeconds) {
		this.createMiliSeconds = createMiliSeconds;
	}

	public Timeout getTimeout() {
		return timeout;
	}

	public void setTimeout(Timeout timeout) {
		this.timeout = timeout;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
