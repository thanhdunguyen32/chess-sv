package login.bean;

public class QqLoginSessionBean extends LoginClientSessionBean {

	private String openId;

	public QqLoginSessionBean(String openId, long sessionId) {
		super(0, sessionId);
		this.openId = openId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
