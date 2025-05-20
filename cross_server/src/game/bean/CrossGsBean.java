package game.bean;

public class CrossGsBean {

	private int id;
	
	private String name;

	private String ip;

	private int port;

	private int status;
	
	private int lanPort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLanPort() {
		return lanPort;
	}

	public void setLanPort(int lanPort) {
		this.lanPort = lanPort;
	}

}
