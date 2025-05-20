package login.bean;

public class GarenaLoginSessionBean extends LoginClientSessionBean {

	private String accessToken;
	
	private String openId;

	public GarenaLoginSessionBean(String pOpenId, long sessionId, String pAccessToken) {
		super(0, sessionId);
		accessToken = pAccessToken;
		setOpenId(pOpenId);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
