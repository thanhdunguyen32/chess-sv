package game.module.friend.bean;

public class FriendBean {
    private Integer id;
    private Integer playerId;
    private Integer friendId;

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

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    @Override
    public String toString() {
        return "FriendBean{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", friendId=" + friendId +
                '}';
    }
}
