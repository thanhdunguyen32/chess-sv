package game.module.user.bean;

import game.module.offline.bean.PlayerBaseBean;

import java.util.Date;

public class PlayerBean extends PlayerBaseBean {


    private Integer gold;

    private Integer money;

    private Integer levelExp;

    private Integer vipExp;

    private Date createTime;

    private Date upgradeTime;

    private Integer guideProgress;

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getLevelExp() {
        return levelExp;
    }

    public void setLevelExp(Integer levelExp) {
        this.levelExp = levelExp;
    }

    public Integer getVipExp() {
        return vipExp;
    }

    public void setVipExp(Integer vipExp) {
        this.vipExp = vipExp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(Date upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    @Override
    public String toString() {
        return "PlayerBean{" +
                "gold=" + gold +
                ", money=" + money +
                ", levelExp=" + levelExp +
                ", vipExp=" + vipExp +
                ", createTime=" + createTime +
                ", upgradeTime=" + upgradeTime +
                ", guideProgress=" + guideProgress +
                "} " + super.toString();
    }

    public String toJsonString() {
        return "{" +
                "id:" + getId() +
                ", lv:" + getLevel() +
                ", accountId:" + getAccountId() +
                ", serverId:" + getServerId() +
                ", name:" + getName() +
                ", gold:" + getGold() +
                ", money:" + getMoney() +
                ", levelExp:" + getLevelExp() +
                ", vipExp:" + getVipExp() +
                ", createTime:" + getCreateTime() +
                ", upgradeTime:" + getUpgradeTime() +
                ", guideProgress:" + guideProgress +
                "} ";
    }

    public Integer getGuideProgress() {
        return guideProgress;
    }

    public void setGuideProgress(Integer guideProgress) {
        this.guideProgress = guideProgress;
    }

}
