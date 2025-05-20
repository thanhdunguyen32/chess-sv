package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class ServerInfoBean {
    private Integer id;
    private String name;
    private String ip;
    private Integer port;
    private Integer portSsl;
    private Integer status;
    private Integer lanPort;
    private String httpUrl;
    private Date time_open; // có thể dùng LocalDateTime tuỳ nhu cầu

    private int totalUser;

    private int userOnline;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
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
    
    public Integer getPort() {
        return port;
    }
    
    public void setPort(Integer port) {
        this.port = port;
    }
    
    public Integer getPortSsl() {
        return portSsl;
    }
    
    public void setPortSsl(Integer portSsl) {
        this.portSsl = portSsl;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getLanPort() {
        return lanPort;
    }
    
    public void setLanPort(Integer lanPort) {
        this.lanPort = lanPort;
    }
    
    public String getHttpUrl() {
        return httpUrl;
    }
    
    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }
    
    public Date getTime_open() {
        return time_open;
    }
    
    public void setTime_open(Date time_open) {
        this.time_open = time_open;
    }
    
    public int getTotalUser() {
        return totalUser;
    }
    
    public void setTotalUser(int totalUser) {
        this.totalUser = totalUser;
    }
    
    public int getUserOnline() {
        return userOnline;
    }
    
    public void setUserOnline(int userOnline) {
        this.userOnline = userOnline;
    }
    
    @Override
    public String toString() {
        return "ServerInfoBean{" + "id=" + id + ", name='" + name + '\'' + ", ip='" + ip + '\'' + ", port=" + port + ", portSsl=" + portSsl + ", status=" + status + ", lanPort=" + lanPort + ", httpUrl='" + httpUrl + '\'' + ", time_open=" + time_open + ", totalUser=" + totalUser + ", userOnline=" + userOnline + '}';
    }
}
