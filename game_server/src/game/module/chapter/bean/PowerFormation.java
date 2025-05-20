package game.module.chapter.bean;

public class PowerFormation {
    private Integer id;
    private Integer playerId;
    private String name;
    private Integer level;
    private Integer iconId;
    private Integer headId;
    private Integer frameId;
    private Integer power;
    private DbBattleset dbBattleset;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public Integer getHeadId() {
        return headId;
    }

    public void setHeadId(Integer headId) {
        this.headId = headId;
    }

    public Integer getFrameId() {
        return frameId;
    }

    public void setFrameId(Integer frameId) {
        this.frameId = frameId;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public DbBattleset getDbBattleset() {
        return dbBattleset;
    }

    public void setDbBattleset(DbBattleset dbBattleset) {
        this.dbBattleset = dbBattleset;
    }

    @Override
    public String toString() {
        return "PowerFormation{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", iconId=" + iconId +
                ", headId=" + headId +
                ", frameId=" + frameId +
                ", power=" + power +
                ", dbBattleset=" + dbBattleset +
                '}';
    }
}
