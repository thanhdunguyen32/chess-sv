package game.module.dungeon.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DungeonBean {
    private Integer id;
    private Integer playerId;
    private Integer chapterIndex;
    private Integer node;
    private Integer pos;
    private List<Integer> chapterAwardGet;
    private Map<Long, Integer> onlineGenerals;
    private Map<Integer, Integer> potions;
    private DungeonNode.DbDungeonNode dbDungeonNode;
    private DungeonBuff dungeonBuff;
    private DungeonBuff spBuff;
    private Set<Integer> shopBuy;
    private Date updateTime;

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
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

    public Integer getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(Integer chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public List<Integer> getChapterAwardGet() {
        return chapterAwardGet;
    }

    public void setChapterAwardGet(List<Integer> chapterAwardGet) {
        this.chapterAwardGet = chapterAwardGet;
    }

    public Map<Long, Integer> getOnlineGenerals() {
        return onlineGenerals;
    }

    public void setOnlineGenerals(Map<Long, Integer> onlineGenerals) {
        this.onlineGenerals = onlineGenerals;
    }

    public Map<Integer, Integer> getPotions() {
        return potions;
    }

    public void setPotions(Map<Integer, Integer> potions) {
        this.potions = potions;
    }

    public DungeonNode.DbDungeonNode getDbDungeonNode() {
        return dbDungeonNode;
    }

    public void setDbDungeonNode(DungeonNode.DbDungeonNode dbDungeonNode) {
        this.dbDungeonNode = dbDungeonNode;
    }

    public DungeonBuff getDungeonBuff() {
        return dungeonBuff;
    }

    public void setDungeonBuff(DungeonBuff dungeonBuff) {
        this.dungeonBuff = dungeonBuff;
    }

    public Set<Integer> getShopBuy() {
        return shopBuy;
    }

    public void setShopBuy(Set<Integer> shopBuy) {
        this.shopBuy = shopBuy;
    }

    public DungeonBuff getSpBuff() {
        return spBuff;
    }

    public void setSpBuff(DungeonBuff spBuff) {
        this.spBuff = spBuff;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "DungeonBean{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", chapterIndex=" + chapterIndex +
                ", node=" + node +
                ", pos=" + pos +
                ", chapterAwardGet=" + chapterAwardGet +
                ", onlineGenerals=" + onlineGenerals +
                ", potions=" + potions +
                ", dbDungeonNode=" + dbDungeonNode +
                ", dungeonBuff=" + dungeonBuff +
                ", spBuff=" + spBuff +
                ", shopBuy=" + shopBuy +
                ", updateTime=" + updateTime +
                '}';
    }

}
