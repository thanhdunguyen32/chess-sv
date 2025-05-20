package game;

import java.util.Set;

public class CrossServerConfig {

	public static final String KEY_MYSQL_CLASSNAME = "mysql.classname";
	
	public static final String KEY_MYSQL_URL = "mysql.url";
	
	public static final String KEY_MYSQL_USERNAME = "mysql.username";
	
	public static final String KEY_MYSQL_PASSWORD = "mysql.password";
	
	public static final String KEY_SOCKET_PORT = "socket_port";

	public static final String KEY_HTTP_PORT = "http_port";
	
	public static final String KEY_LAN_PORT = "lan_port";
	
	public static final String KEY_LAN_ALLOW_IP = "cs.lan.allow.ip";
	
	private String mysqlClassname;
	
	private String mysqlUrl;
	
	private String mysqlUsername;
	
	private String mysqlPassword;
	
	private int socketPort;

	private int httpPort;
	
	private int lanPort;
	
	private Set<String> lanAllowIps;
	
	public int getSocketPort() {
		return socketPort;
	}

	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public String getMysqlClassname() {
		return mysqlClassname;
	}

	public void setMysqlClassname(String mysqlClassname) {
		this.mysqlClassname = mysqlClassname;
	}

	public String getMysqlUrl() {
		return mysqlUrl;
	}

	public void setMysqlUrl(String mysqlUrl) {
		this.mysqlUrl = mysqlUrl;
	}

	public String getMysqlUsername() {
		return mysqlUsername;
	}

	public void setMysqlUsername(String mysqlUsername) {
		this.mysqlUsername = mysqlUsername;
	}

	public String getMysqlPassword() {
		return mysqlPassword;
	}

	public void setMysqlPassword(String mysqlPassword) {
		this.mysqlPassword = mysqlPassword;
	}

	public int getLanPort() {
		return lanPort;
	}

	public void setLanPort(int lanPort) {
		this.lanPort = lanPort;
	}

	public Set<String> getLanAllowIps() {
		return lanAllowIps;
	}

	public void setLanAllowIps(Set<String> lanAllowIps) {
		this.lanAllowIps = lanAllowIps;
	}

}
