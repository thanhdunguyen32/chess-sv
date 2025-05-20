package login.bean;

public class LoginClientSessionBean {

	private int uid;

	private long sessionId;

	public LoginClientSessionBean(int uid, long sessionId) {
		this.uid = uid;
		this.sessionId = sessionId;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

}
