package game;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class ServerConfig {

	// Game DB Constants
	public static final String KEY_MYSQL_GAME_CLASSNAME = "mysql.game.classname";
	public static final String KEY_MYSQL_GAME_URL = "mysql.game.url";
	public static final String KEY_MYSQL_GAME_USERNAME = "mysql.game.username";
	public static final String KEY_MYSQL_GAME_PASSWORD = "mysql.game.password";

	// Login DB Constants
	public static final String KEY_MYSQL_LOGIN_CLASSNAME = "mysql.login.classname";
	public static final String KEY_MYSQL_LOGIN_URL = "mysql.login.url";
	public static final String KEY_MYSQL_LOGIN_USERNAME = "mysql.login.username";
	public static final String KEY_MYSQL_LOGIN_PASSWORD = "mysql.login.password";

	public static final String KEY_SOCKET_PORT = "socket.port";

	public static final String KEY_HTTP_PORT = "http.port";

	public static final String KEY_GS_LAN_PORT = "lan.port";

	public static final String CONFIG_FILE_NAME = "game.properties";

	public static final String KEY_CROSS_HOST = "cross.host";

	public static final String KEY_CROSS_LAN_PORT = "cross.lan.port";

	public static final String KEY_GM_ENABLE = "gm.enable";

	public static final String KEY_LAN_ALLOW_IP = "lan.allow.ip";
	
    public static final String KEY_IOS_IAP_SANDBOX = "ios.iap.sandbox";
    
    
	
	private String mysqlGameClassname;

	private String mysqlGameUrl;

	private String mysqlGameUsername;

	private String mysqlGamePassword;

	private String mysqlLoginClassname;

	private String mysqlLoginUrl;

	private String mysqlLoginUsername;

	private String mysqlLoginPassword;

	private String mysqlClassname;

	private String mysqlUrl;

	private String mysqlUsername;

	private String mysqlPassword;

	private int socketPort;
	
	private int socketPortSsl;

	private int httpPort;

	private int lanPort;

	private String crossHost;

	private int crossLanPort;

	private String loginHost;

	private int loginLanPort;

	private boolean gmEnable;

	private Set<String> lanAllowIps;

	private boolean iosIapIsSandbox;
	
	private String sslCertPath;
	
	private String sslKeyPath;

	private Date openTime;

	private List<String> scrollAnno;
	
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

	public int getLanPort() {
		return lanPort;
	}

	public void setLanPort(int lanPort) {
		this.lanPort = lanPort;
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

	public boolean isGmEnable() {
		return gmEnable;
	}

	public void setGmEnable(boolean gmEnable) {
		this.gmEnable = gmEnable;
	}

	public Set<String> getLanAllowIps() {
		return lanAllowIps;
	}

	public void setLanAllowIps(Set<String> lanAllowIps) {
		this.lanAllowIps = lanAllowIps;
	}

	public boolean isIosIapIsSandbox() {
		return iosIapIsSandbox;
	}

	public void setIosIapIsSandbox(boolean iosIapIsSandbox) {
		this.iosIapIsSandbox = iosIapIsSandbox;
	}

	public String getCrossHost() {
		return crossHost;
	}

	public void setCrossHost(String crossHost) {
		this.crossHost = crossHost;
	}

	public int getCrossLanPort() {
		return crossLanPort;
	}

	public void setCrossLanPort(int crossLanPort) {
		this.crossLanPort = crossLanPort;
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

	public int getSocketPortSsl() {
		return socketPortSsl;
	}

	public void setSocketPortSsl(int socketPortSsl) {
		this.socketPortSsl = socketPortSsl;
	}

	public String getLoginHost() {
		return loginHost;
	}

	public void setLoginHost(String loginHost) {
		this.loginHost = loginHost;
	}

	public int getLoginLanPort() {
		return loginLanPort;
	}

	public void setLoginLanPort(int loginLanPort) {
		this.loginLanPort = loginLanPort;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public List<String> getScrollAnno() {
		return scrollAnno;
	}

	public void setScrollAnno(List<String> scrollAnno) {
		this.scrollAnno = scrollAnno;
	}

	public String getMysqlGameClassname() {
		return mysqlGameClassname;
	}

	public void setMysqlGameClassname(String mysqlGameClassname) {
		this.mysqlGameClassname = mysqlGameClassname;
	}

	public String getMysqlGameUrl() {
		return mysqlGameUrl;
	}

	public void setMysqlGameUrl(String mysqlGameUrl) {
		this.mysqlGameUrl = mysqlGameUrl;
	}

	public String getMysqlGameUsername() {
		return mysqlGameUsername;
	}

	public void setMysqlGameUsername(String mysqlGameUsername) {
		this.mysqlGameUsername = mysqlGameUsername;
	}

	public String getMysqlGamePassword() {
		return mysqlGamePassword;
	}

	public void setMysqlGamePassword(String mysqlGamePassword) {
		this.mysqlGamePassword = mysqlGamePassword;
	}

	public String getMysqlLoginClassname() {
		return mysqlLoginClassname;
	}

	public void setMysqlLoginClassname(String mysqlLoginClassname) {
		this.mysqlLoginClassname = mysqlLoginClassname;
	}

	public String getMysqlLoginUrl() {
		return mysqlLoginUrl;
	}

	public void setMysqlLoginUrl(String mysqlLoginUrl) {
		this.mysqlLoginUrl = mysqlLoginUrl;
	}

	public String getMysqlLoginUsername() {
		return mysqlLoginUsername;
	}

	public void setMysqlLoginUsername(String mysqlLoginUsername) {
		this.mysqlLoginUsername = mysqlLoginUsername;
	}

	public String getMysqlLoginPassword() {
		return mysqlLoginPassword;
	}

	public void setMysqlLoginPassword(String mysqlLoginPassword) {
		this.mysqlLoginPassword = mysqlLoginPassword;
	}
}
