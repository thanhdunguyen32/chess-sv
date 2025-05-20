package login.bean;

public class ServerList4Db {

	private int id;
	
	private String name;

	private String ip;

	private int port;
	
	private int portSsl;

	private int status;
	
	private int lanPort;
	
    private String httpUrl;
    
    private String urlSsl;

    private Boolean isSsl;

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

	public int getPortSsl() {
		return portSsl;
	}

	public void setPortSsl(int portSsl) {
		this.portSsl = portSsl;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

    public String getUrlSsl() {
        return urlSsl;
    }

    public void setUrlSsl(String urlSsl) {
        this.urlSsl = urlSsl;
    }

    public Boolean getIsSsl() {
        return isSsl;
    }

    public void setIsSsl(Boolean isSsl) {
        this.isSsl = isSsl;
    }

    public String toString() {
        return "ServerListItem [id="+id+",name="+name+",ip_addr="+ip+",port="+port+",status="+status+",port_ssl="+portSsl+",url_ssl="+urlSsl+",is_ssl="+isSsl+",]";
    }
}
