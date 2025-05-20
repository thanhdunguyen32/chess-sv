package login.http.bean;

import java.util.List;

public class O185syServerRet {

	private int state;

	private List<O185syServerItem> data;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<O185syServerItem> getData() {
		return data;
	}

	public void setData(List<O185syServerItem> data) {
		this.data = data;
	}

	public static final class O185syServerItem {
		private Integer id;
		private Integer serverID;
		private String serverName;

		public O185syServerItem(Integer id, Integer serverID, String serverName) {
			super();
			this.id = id;
			this.serverID = serverID;
			this.serverName = serverName;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getServerID() {
			return serverID;
		}

		public void setServerID(Integer serverID) {
			this.serverID = serverID;
		}

		public String getServerName() {
			return serverName;
		}

		public void setServerName(String serverName) {
			this.serverName = serverName;
		}
	}

}
