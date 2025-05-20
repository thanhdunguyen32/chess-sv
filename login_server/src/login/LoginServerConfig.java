package login;

import java.util.Set;

public class LoginServerConfig {

	public static final String KEY_MYSQL_CLASSNAME = "mysql.classname";
	
	public static final String KEY_MYSQL_URL = "mysql.url";
	
	public static final String KEY_MYSQL_USERNAME = "mysql.username";
	
	public static final String KEY_MYSQL_PASSWORD = "mysql.password";
	
	public static final String KEY_SOCKET_PORT = "socket_port";

	public static final String KEY_HTTP_PORT = "http_port";

	public static final String CONFIG_FILE_NAME = "login.properties";
	
	public static final String KEY_announcement = "announcement";
	
	public static final String KEY_LAN_PORT = "lan_port";
	
	public static final String KEY_CODE_VERSION = "code.version";
	
	public static final String KEY_LAN_ALLOW_IP = "ls.lan.allow.ip";
	
	public static final String KEY_CODE_REVIEW = "code.review";
	
	public static final String KEY_LOGIN_SERVER_HOST = "login.server.host";
	
	public static final String KEY_LOGIN_WHITE_LIST = "login.white_list";
	
	private String mysqlClassname;
	
	private String mysqlUrl;
	
	private String mysqlUsername;
	
	private String mysqlPassword;

	private int socketPort;
	
	private int socketPortSsl;
	
	private int lanPort;

	private int httpPort;
	
	private String announcement;
	
	private Integer codeVersion;

	private Set<String> lanAllowIps;
	
	private Integer codeReview;
	
	private String loginServerHost;
	
	private Integer loginServerPort;
	
	private Set<String> loginWhiteList;
	
	private String sslCertPath;
	
	private String sslKeyPath;
	
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

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	public int getLanPort() {
		return lanPort;
	}

	public void setLanPort(int lanPort) {
		this.lanPort = lanPort;
	}

	public Integer getCodeVersion() {
		return codeVersion;
	}

	public void setCodeVersion(Integer codeVersion) {
		this.codeVersion = codeVersion;
	}

	public Set<String> getLanAllowIps() {
		return lanAllowIps;
	}

	public void setLanAllowIps(Set<String> lanAllowIps) {
		this.lanAllowIps = lanAllowIps;
	}

	public Integer getCodeReview() {
		return codeReview;
	}

	public void setCodeReview(Integer codeReview) {
		this.codeReview = codeReview;
	}

	public String getLoginServerHost() {
		return loginServerHost;
	}

	public void setLoginServerHost(String loginServerHost) {
		this.loginServerHost = loginServerHost;
	}

	public Set<String> getLoginWhiteList() {
		return loginWhiteList;
	}

	public void setLoginWhiteList(Set<String> loginWhiteList) {
		this.loginWhiteList = loginWhiteList;
	}

	public String getSslCertPath() {
		return sslCertPath;
	}

	public void setSslCertPath(String sslPemPath) {
		this.sslCertPath = sslPemPath;
	}

	public String getSslKeyPath() {
		return sslKeyPath;
	}

	public void setSslKeyPath(String sslKeyPath) {
		this.sslKeyPath = sslKeyPath;
	}

	public Integer getLoginServerPort() {
		return loginServerPort;
	}

	public void setLoginServerPort(Integer loginServerPort) {
		this.loginServerPort = loginServerPort;
	}

	public int getSocketPortSsl() {
		return socketPortSsl;
	}

	public void setSocketPortSsl(int socketPortSsl) {
		this.socketPortSsl = socketPortSsl;
	}

}
