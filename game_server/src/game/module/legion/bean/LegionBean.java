package game.module.legion.bean;

import java.util.Map;

public class LegionBean {

    private Integer id;

    private Long uuid;

    private String name;

    private String notice;

    private int minLevel;

    private Boolean isPass;

    private LegionPlayer.DbLegionPlayers dbLegionPlayers;

    private Integer ceoId;

    private Integer power;

    private Integer exp;

    private Integer fexp;

    private Integer flevel;

    private Boolean kceo;

    private Integer level;

    private Integer pos;

    private Map<Integer, Long> applyPlayers;

    private DbLegionBoss legionBoss;

    private DbLegionLog legionLog;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    public LegionPlayer.DbLegionPlayers getDbLegionPlayers() {
        return dbLegionPlayers;
    }

    public void setDbLegionPlayers(LegionPlayer.DbLegionPlayers dbLegionPlayers) {
        this.dbLegionPlayers = dbLegionPlayers;
    }

    public Integer getCeoId() {
        return ceoId;
    }

    public void setCeoId(Integer ceoId) {
        this.ceoId = ceoId;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getFexp() {
        return fexp;
    }

    public void setFexp(Integer fexp) {
        this.fexp = fexp;
    }

    public Integer getFlevel() {
        return flevel;
    }

    public void setFlevel(Integer flevel) {
        this.flevel = flevel;
    }

    public Boolean getKceo() {
        return kceo;
    }

    public void setKceo(Boolean kceo) {
        this.kceo = kceo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Map<Integer, Long> getApplyPlayers() {
        return applyPlayers;
    }

    public void setApplyPlayers(Map<Integer, Long> applyPlayers) {
        this.applyPlayers = applyPlayers;
    }

    public DbLegionBoss getLegionBoss() {
        return legionBoss;
    }

    public void setLegionBoss(DbLegionBoss legionBoss) {
        this.legionBoss = legionBoss;
    }

    public DbLegionLog getLegionLog() {
        return legionLog;
    }

    public void setLegionLog(DbLegionLog legionLog) {
        this.legionLog = legionLog;
    }

    @Override
    public String toString() {
        return "LegionBean{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                ", notice='" + notice + '\'' +
                ", minLevel=" + minLevel +
                ", isPass=" + isPass +
                ", dbLegionPlayers=" + dbLegionPlayers +
                ", ceoId=" + ceoId +
                ", power=" + power +
                ", exp=" + exp +
                ", fexp=" + fexp +
                ", flevel=" + flevel +
                ", kceo=" + kceo +
                ", level=" + level +
                ", pos=" + pos +
                ", applyPlayers=" + applyPlayers +
                ", legionBoss=" + legionBoss +
                ", legionLog=" + legionLog +
                '}';
    }
}
