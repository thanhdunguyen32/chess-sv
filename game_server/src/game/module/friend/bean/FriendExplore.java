package game.module.friend.bean;

import game.module.manor.bean.DbBattleGeneral;
import game.module.template.RewardTemplateSimple;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FriendExplore {

    private Integer id;

    private Integer playerId;

    private DbFriendChapter dbFriendChapter;

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

    public DbFriendChapter getDbFriendChapter() {
        return dbFriendChapter;
    }

    public void setDbFriendChapter(DbFriendChapter dbFriendChapter) {
        this.dbFriendChapter = dbFriendChapter;
    }

    @Override
    public String toString() {
        return "FriendExplore{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", dbFriendChapter=" + dbFriendChapter +
                '}';
    }

    public static final class DbFriendChapter{
        private Map<Integer,DbFriendChapter1> chapters;

        public Map<Integer, DbFriendChapter1> getChapters() {
            return chapters;
        }

        public void setChapters(Map<Integer, DbFriendChapter1> chapters) {
            this.chapters = chapters;
        }

        @Override
        public String toString() {
            return "DbFriendChapter{" +
                    "chapters=" + chapters +
                    '}';
        }
    }

    public static final class DbFriendChapter1{
        private Date etime;
        private List<Integer> friends;

        public Date getEtime() {
            return etime;
        }

        public void setEtime(Date etime) {
            this.etime = etime;
        }

        public List<Integer> getFriends() {
            return friends;
        }

        public void setFriends(List<Integer> friends) {
            this.friends = friends;
        }

        @Override
        public String toString() {
            return "DbFriendChapter{" +
                    "etime=" + etime +
                    ", friends=" + friends +
                    '}';
        }
    }

}
