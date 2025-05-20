package game.module.user.bean;

import java.util.Set;

public class PlayerHead {

    private Integer id;

    private Integer playerId;

    private Set<Integer> headIcons;

    private Set<Integer> headFrames;

    private Set<Integer> headImages;

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

    public Set<Integer> getHeadIcons() {
        return headIcons;
    }

    public void setHeadIcons(Set<Integer> headIcons) {
        this.headIcons = headIcons;
    }

    public Set<Integer> getHeadFrames() {
        return headFrames;
    }

    public void setHeadFrames(Set<Integer> headFrames) {
        this.headFrames = headFrames;
    }

    public Set<Integer> getHeadImages() {
        return headImages;
    }

    public void setHeadImages(Set<Integer> headImages) {
        this.headImages = headImages;
    }

    @Override
    public String toString() {
        return "PlayerHead{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", headIcons=" + headIcons +
                ", headFrames=" + headFrames +
                ", headImages=" + headImages +
                '}';
    }
}
