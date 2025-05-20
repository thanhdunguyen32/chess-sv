package game.module.friend.logic;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendBoss;
import game.module.friend.bean.FriendExplore;
import game.module.friend.bean.FriendHeartSend;
import game.module.friend.dao.FriendBossCache;
import game.module.friend.dao.FriendBossDaoHelper;
import game.module.friend.dao.FriendshipSendCache;
import game.module.friend.dao.MyFriendExploreTemplateCache;
import game.module.manor.bean.DbBattleGeneral;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.template.ChapterBattleTemplate;
import game.module.template.MyFriendExploreTemplate;
import game.session.PlayerOnlineCacheMng;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author HeXuhui
 */
public class FriendManager {

    private static Logger logger = LoggerFactory.getLogger(FriendManager.class);

    private Cache<Integer, Integer> friendPkMap = CacheBuilder.newBuilder().concurrencyLevel(8)
            .expireAfterAccess(30, TimeUnit.MINUTES).build();

    public FriendExplore createFriendExplore(int playerId) {
        FriendExplore friendExplore = new FriendExplore();
        friendExplore.setPlayerId(playerId);
        return friendExplore;
    }

    public void generateFriendExploreBoss(int playerId) {
        FriendBoss.DbFriendBoss dbFriendBoss = new FriendBoss.DbFriendBoss();
        MyFriendExploreTemplate friendExploreConfig = MyFriendExploreTemplateCache.getInstance().randFriendBoss();
        Date now = new Date();
        dbFriendBoss.setEtime(DateUtils.addHours(now, 24));
        dbFriendBoss.setId(friendExploreConfig.getId());
        dbFriendBoss.setGsid(friendExploreConfig.getGsid());
        dbFriendBoss.setName(friendExploreConfig.getName());
        dbFriendBoss.setLevel(friendExploreConfig.getLevel());
        dbFriendBoss.setMaxhp(friendExploreConfig.getMaxhp());
        dbFriendBoss.setNowhp(friendExploreConfig.getMaxhp());
        dbFriendBoss.setRewards(friendExploreConfig.getRewards());
        //formation heros
        Map<Integer, DbBattleGeneral> generalMap = new HashMap<>(friendExploreConfig.getBset().size());
        dbFriendBoss.setFormationHeros(generalMap);
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : friendExploreConfig.getBset().entrySet()) {
            DbBattleGeneral dbBattleGeneral = new DbBattleGeneral();
            generalMap.put(aEntry.getKey(), dbBattleGeneral);
            ChapterBattleTemplate beastSet = aEntry.getValue();
            dbBattleGeneral.setChapterBattleTemplate(beastSet);
            dbBattleGeneral.setMaxhp(beastSet.getExhp().intValue());
            dbBattleGeneral.setNowhp(beastSet.getExhp().intValue());
        }
        //join players
        Map<Integer, Long> joinPlayers = new HashMap<>();
        joinPlayers.put(playerId, 0L);
        dbFriendBoss.setPlayerHurm(joinPlayers);
        //add cache
        FriendBoss friendBoss = FriendBossCache.getInstance().getFriendBoss(playerId);
        if (friendBoss == null) {
            friendBoss = new FriendBoss();
            friendBoss.setPlayerId(playerId);
            FriendBossCache.getInstance().addFriendBoss(friendBoss);
        }
        friendBoss.setDbFriendBoss(dbFriendBoss);
        if (friendBoss.getId() == null) {
            FriendBossDaoHelper.asyncInsertFriendBoss(friendBoss);
        } else {
            FriendBossDaoHelper.asyncUpdateFriendBoss(friendBoss);
        }
    }

    public void checkBossEnd(int playerId) {
        FriendBoss friendBoss = FriendBossCache.getInstance().getFriendBoss(playerId);
        if (friendBoss != null) {
            FriendBoss.DbFriendBoss dbFriendBoss = friendBoss.getDbFriendBoss();
            Date now = new Date();
            if (dbFriendBoss != null && dbFriendBoss.getEtime().before(now)) {
                friendBoss.setDbFriendBoss(null);
                FriendBossDaoHelper.asyncUpdateFriendBoss(friendBoss);
            }
        }
    }

    static class SingletonHolder {

        static FriendManager instance = new FriendManager();


    }

    public static FriendManager getInstance() {
        return SingletonHolder.instance;
    }

    public FriendBean createFriendBean(int playerId, int friendId) {
        FriendBean friendBean = new FriendBean();
        friendBean.setPlayerId(playerId);
        friendBean.setFriendId(friendId);
        return friendBean;
    }

    public void buildIoFriend(WsMessageBase.IOFriendEntity ioFriendEntity, int playerId, int friendId) {
        buildIoFriendBase(ioFriendEntity, friendId);
        //能否送红星
        Date now = new Date();
        ioFriendEntity.pstatus = new WsMessageBase.IOpstatus(1, 0);
        FriendHeartSend.HeartSendItem heartSendItem = FriendshipSendCache.getInstance().getFriendHeartSend(playerId, friendId);
        if (heartSendItem != null) {
            Date sendTime = heartSendItem.getSendTime();
            if (DateUtils.isSameDay(sendTime, now)) {
                ioFriendEntity.pstatus.send = 0;
            }
        }
        //can receive
        heartSendItem = FriendshipSendCache.getInstance().getFriendHeartSend(friendId, playerId);
        if (heartSendItem != null) {
            Date sendTime = heartSendItem.getSendTime();
            if (DateUtils.isSameDay(sendTime, now) && !heartSendItem.getGet()) {
                ioFriendEntity.pstatus.receive = 1;
            }
        }
    }

    public void buildIoFriendBase(WsMessageBase.IOFriendEntity ioFriendEntity, int friendId) {
        PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(friendId);
        ioFriendEntity.rname = poc.getName();
        ioFriendEntity.iconid = poc.getIconid();
        ioFriendEntity.headid = poc.getHeadid();
        ioFriendEntity.frameid = poc.getFrameid();
        ioFriendEntity.level = poc.getLevel();
        ioFriendEntity.vipLevel = poc.getVipLevel();
        ioFriendEntity.power = poc.getPower();
        //最后离线时间
        ioFriendEntity.lasttime = getPlayerLastestTime(friendId);
    }

    public long getPlayerLastestTime(int playerId) {
        Date now = new Date();
        long lasttime = now.getTime();
        if (PlayerOnlineCacheMng.getInstance().getOnlinePlayerById(playerId) == null) {
            PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(playerId);
            Date downlineTime = poc.getDownlineTime();
            if (downlineTime != null) {
                lasttime = downlineTime.getTime();
            }
        }
        return lasttime;
    }

    public void putFriendPkMap(int playerId, int enemyId) {
        friendPkMap.put(playerId, enemyId);
    }

    public Integer getFriendPk(int playerId) {
        return friendPkMap.getIfPresent(playerId);
    }

}
