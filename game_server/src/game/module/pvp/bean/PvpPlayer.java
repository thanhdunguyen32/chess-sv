package game.module.pvp.bean;

import java.util.List;

public class PvpPlayer {

    private Integer playerId;
    private Boolean inBattle;
    private List<Integer> againstPlayers;
    private Integer enemyIndex;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Boolean getInBattle() {
        return inBattle;
    }

    public void setInBattle(Boolean inBattle) {
        this.inBattle = inBattle;
    }

    public PvpPlayer(Integer playerId) {
        this.playerId = playerId;
    }

    public List<Integer> getAgainstPlayers() {
        return againstPlayers;
    }

    public void setAgainstPlayers(List<Integer> againstPlayers) {
        this.againstPlayers = againstPlayers;
    }

    public Integer getEnemyIndex() {
        return enemyIndex;
    }

    public void setEnemyIndex(Integer enemyIndex) {
        this.enemyIndex = enemyIndex;
    }

    @Override
    public String toString() {
        return "PvpPlayer{" +
                "playerId=" + playerId +
                ", inBattle=" + inBattle +
                ", againstPlayers=" + againstPlayers +
                ", enemyIndex=" + enemyIndex +
                '}';
    }
}
