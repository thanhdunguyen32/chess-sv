package game.module.user.bean;

public class PlayerProp {

    private Integer id;

    private Integer playerId;

    private Integer gsid;

    private Integer count;

    @Override
    public String toString() {
        return "PlayerHidden{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", gsid=" + gsid +
                ", count=" + count +
                '}';
    }

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

    public Integer getGsid() {
        return gsid;
    }

    public void setGsid(Integer gsid) {
        this.gsid = gsid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
