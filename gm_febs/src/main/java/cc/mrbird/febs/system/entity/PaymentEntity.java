package cc.mrbird.febs.system.entity;

public class PaymentEntity {
    private String userId;
    private String orderId;
    private int money;
    private int time;
    private String pid;
    private int serverId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "PaymentEntity [userId=" + userId + ", orderId=" + orderId + ", money=" + money + ", time=" + time
                + ", pid=" + pid + ", serverId=" + serverId + "]";
    }
}
