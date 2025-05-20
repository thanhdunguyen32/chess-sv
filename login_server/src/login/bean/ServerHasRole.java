package login.bean;

import java.util.Map;

public class ServerHasRole {

    private Integer id;

    private String accountId;

    private Integer lastLoginServerId;

    private Map<Integer, Integer> serverLevels;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getLastLoginServerId() {
        return lastLoginServerId;
    }

    public void setLastLoginServerId(Integer lastLoginServerId) {
        this.lastLoginServerId = lastLoginServerId;
    }

    public Map<Integer, Integer> getServerLevels() {
        return serverLevels;
    }

    public void setServerLevels(Map<Integer, Integer> serverLevels) {
        this.serverLevels = serverLevels;
    }

}
