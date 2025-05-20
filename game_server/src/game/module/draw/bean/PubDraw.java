package game.module.draw.bean;

import java.util.Date;

/**
 * 酒馆抽取英雄
 */
public class PubDraw {

    private Integer id;

    private Integer playerId;

    private Date lastNormalTime;

    private Date lastAdvanceTime;

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

    public Date getLastNormalTime() {
        return lastNormalTime;
    }

    public void setLastNormalTime(Date lastNormalTime) {
        this.lastNormalTime = lastNormalTime;
    }

    public Date getLastAdvanceTime() {
        return lastAdvanceTime;
    }

    public void setLastAdvanceTime(Date lastAdvanceTime) {
        this.lastAdvanceTime = lastAdvanceTime;
    }

    @Override
    public String toString() {
        return "PubDraw{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", lastNormalTime=" + lastNormalTime +
                ", lastAdvanceTime=" + lastAdvanceTime +
                '}';
    }
}
