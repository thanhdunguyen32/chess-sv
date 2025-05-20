package game.module.kingpvp.bean;

import game.module.exped.bean.ExpedPlayer;

import java.util.Date;
import java.util.List;

public class KingPvp {

    private Integer id;

    private Integer playerId;

    private Integer stage;

    private Integer star;

    private Integer hstage;

    private List<Integer> promotion;

    private List<Integer> locate;

    private Date lastUpdateTime;

    private KingPvpPlayer tmpTargetPlayer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getHstage() {
        return hstage;
    }

    public void setHstage(Integer hstage) {
        this.hstage = hstage;
    }

    public List<Integer> getPromotion() {
        return promotion;
    }

    public void setPromotion(List<Integer> promotion) {
        this.promotion = promotion;
    }

    public List<Integer> getLocate() {
        return locate;
    }

    public void setLocate(List<Integer> locate) {
        this.locate = locate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public KingPvpPlayer getTmpTargetPlayer() {
        return tmpTargetPlayer;
    }

    public void setTmpTargetPlayer(KingPvpPlayer tmpTargetPlayer) {
        this.tmpTargetPlayer = tmpTargetPlayer;
    }

    @Override
    public String toString() {
        return "KingPvp [id=" + id + ", playerId=" + playerId + ", stage=" + stage + ", star=" + star + ", hstage=" + hstage
                + ", promotion=" + promotion + ", locate=" + locate + ", lastUpdateTime=" + lastUpdateTime + "]";
    }
}
