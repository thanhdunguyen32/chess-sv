package game.module.friend.bean;

public class FriendRequest {

    private Integer id;
    private Integer playerId;
    private Integer requestPlayerId;

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

    public Integer getRequestPlayerId() {
        return requestPlayerId;
    }

    public void setRequestPlayerId(Integer requestPlayerId) {
        this.requestPlayerId = requestPlayerId;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", requestPlayerId=" + requestPlayerId +
                '}';
    }
}
