package game.module.dungeon.bean;

import game.module.template.ChapterBattleTemplate;
import ws.WsMessageBase;

import java.util.List;
import java.util.Map;

public class DungeonNode {

    private List<DungeonNodePos> poslist;

    public List<DungeonNodePos> getPoslist() {
        return poslist;
    }

    public void setPoslist(List<DungeonNodePos> poslist) {
        this.poslist = poslist;
    }

    @Override
    public String toString() {
        return "DungeonNode{" +
                "poslist=" + poslist +
                '}';
    }

    public static final class DungeonNodePos {
        private Integer type;
        private Integer choose;
        private Long finish;
        private DungeonNodeDetail dungeonNodeDetail;

        public DungeonNodePos(Integer type, Integer choose, Long finish) {
            this.type = type;
            this.choose = choose;
            this.finish = finish;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getChoose() {
            return choose;
        }

        public void setChoose(Integer choose) {
            this.choose = choose;
        }

        public Long getFinish() {
            return finish;
        }

        public void setFinish(Long finish) {
            this.finish = finish;
        }

        public DungeonNodeDetail getDungeonNodeDetail() {
            return dungeonNodeDetail;
        }

        public void setDungeonNodeDetail(DungeonNodeDetail dungeonNodeDetail) {
            this.dungeonNodeDetail = dungeonNodeDetail;
        }

        @Override
        public String toString() {
            return "DungeonNodePos{" +
                    "type=" + type +
                    ", choose=" + choose +
                    ", finish=" + finish +
                    ", dungeonNodeDetail=" + dungeonNodeDetail +
                    '}';
        }
    }

    public static final class DbDungeonNode {
        private List<DungeonNode> nodelist;

        public List<DungeonNode> getNodelist() {
            return nodelist;
        }

        public void setNodelist(List<DungeonNode> nodelist) {
            this.nodelist = nodelist;
        }

        @Override
        public String toString() {
            return "DbDungeonNode{" +
                    "nodelist=" + nodelist +
                    '}';
        }
    }

    public static final class DungeonNodeDetail {
        private Integer id;
        private String name;
        private Integer gsid;
        private Map<Integer, ChapterBattleTemplate> battleset;
        private Map<Integer,Integer> enemyHpPercent;
        private List<Integer> buffs;
        private Integer disc;
        private List<WsMessageBase.IORewardItem> item;
        private List<WsMessageBase.IORewardItem> consume;
        private Integer quality;
        private Integer refnum;
        private List<WsMessageBase.IORewardItem> goods;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getGsid() {
            return gsid;
        }

        public void setGsid(Integer gsid) {
            this.gsid = gsid;
        }

        public Map<Integer, ChapterBattleTemplate> getBattleset() {
            return battleset;
        }

        public void setBattleset(Map<Integer, ChapterBattleTemplate> battleset) {
            this.battleset = battleset;
        }

        public List<Integer> getBuffs() {
            return buffs;
        }

        public void setBuffs(List<Integer> buffs) {
            this.buffs = buffs;
        }

        public Integer getDisc() {
            return disc;
        }

        public void setDisc(Integer disc) {
            this.disc = disc;
        }

        public List<WsMessageBase.IORewardItem> getItem() {
            return item;
        }

        public void setItem(List<WsMessageBase.IORewardItem> item) {
            this.item = item;
        }

        public List<WsMessageBase.IORewardItem> getConsume() {
            return consume;
        }

        public void setConsume(List<WsMessageBase.IORewardItem> consume) {
            this.consume = consume;
        }

        public Integer getQuality() {
            return quality;
        }

        public void setQuality(Integer quality) {
            this.quality = quality;
        }

        public Integer getRefnum() {
            return refnum;
        }

        public void setRefnum(Integer refnum) {
            this.refnum = refnum;
        }

        public List<WsMessageBase.IORewardItem> getGoods() {
            return goods;
        }

        public void setGoods(List<WsMessageBase.IORewardItem> goods) {
            this.goods = goods;
        }

        public Map<Integer, Integer> getEnemyHpPercent() {
            return enemyHpPercent;
        }

        public void setEnemyHpPercent(Map<Integer, Integer> enemyHpPercent) {
            this.enemyHpPercent = enemyHpPercent;
        }

        @Override
        public String toString() {
            return "DungeonNodeDetail{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", gsid=" + gsid +
                    ", battleset=" + battleset +
                    ", enemyHpPercent=" + enemyHpPercent +
                    ", buffs=" + buffs +
                    ", disc=" + disc +
                    ", item=" + item +
                    ", consume=" + consume +
                    ", quality=" + quality +
                    ", refnum=" + refnum +
                    ", goods=" + goods +
                    '}';
        }
    }

}
