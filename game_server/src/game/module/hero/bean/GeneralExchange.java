package game.module.hero.bean;

public class GeneralExchange {

    private Integer id;

    private Integer playerId;

    private Long oldGeneralUuid;

    private Integer newGeneralTemplateId;

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

    public Long getOldGeneralUuid() {
        return oldGeneralUuid;
    }

    public void setOldGeneralUuid(Long oldGeneralUuid) {
        this.oldGeneralUuid = oldGeneralUuid;
    }

    public Integer getNewGeneralTemplateId() {
        return newGeneralTemplateId;
    }

    public void setNewGeneralTemplateId(Integer newGeneralTemplateId) {
        this.newGeneralTemplateId = newGeneralTemplateId;
    }

    @Override
    public String toString() {
        return "GeneralExchange{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", oldGeneralUuid=" + oldGeneralUuid +
                ", newGeneralTemplateId=" + newGeneralTemplateId +
                '}';
    }
}
