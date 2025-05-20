package login.bean;

public class ZyLoginSessionBean extends LoginClientSessionBean {

	private String userId;

	public ZyLoginSessionBean(String userId, long sessionId) {
		super(0, sessionId);
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
