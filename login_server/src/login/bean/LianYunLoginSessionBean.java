package login.bean;

public class LianYunLoginSessionBean extends LoginClientSessionBean {

	private String userId;

	public LianYunLoginSessionBean(String userId, long sessionId) {
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
