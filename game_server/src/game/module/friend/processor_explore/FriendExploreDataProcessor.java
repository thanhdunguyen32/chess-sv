package game.module.friend.processor_explore;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendBoss;
import game.module.friend.bean.FriendExplore;
import game.module.friend.dao.FchapterTemplateCache;
import game.module.friend.dao.FriendBossCache;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendExploreCache;
import game.module.friend.logic.FriendConstants;
import game.module.friend.logic.FriendManager;
import game.module.manor.bean.DbBattleGeneral;
import game.module.template.FchapterTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageFriend;

import java.util.*;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendExploreData.id, accessLimit = 200)
public class FriendExploreDataProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendExploreDataProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        logger.info("friend explore data!");
        int playerId = playingRole.getId();
        //friend boss end
        FriendManager.getInstance().checkBossEnd(playerId);
        //好友探索界面数据
        WsMessageFriend.S2CFriendExploreData respmsg = buildResponse(playerId,0);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    public static WsMessageFriend.S2CFriendExploreData buildResponse(int playerId, int isKill) {
        WsMessageFriend.S2CFriendExploreData respmsg = new WsMessageFriend.S2CFriendExploreData();
        respmsg.kill = isKill;
        respmsg.maxbphys = FriendConstants.FRIEND_BOSS_ATTACK_COUNT;
        respmsg.maxqphys = FriendConstants.FRIEND_EXPLORE_CHAPTER_COUNT;
        FriendExplore friendExplore = FriendExploreCache.getInstance().getFriendExplore(playerId);
        //chapter
        List<FchapterTemplate> chapterList = FchapterTemplateCache.getInstance().getChapterList();
        respmsg.chapters = new ArrayList<>(chapterList.size());
        for (FchapterTemplate fchapterTemplate : chapterList) {
            WsMessageBase.IOFriendChapter ioFriendChapter = new WsMessageBase.IOFriendChapter();
            respmsg.chapters.add(ioFriendChapter);
            int chapterid = fchapterTemplate.getID();
            ioFriendChapter.chapterid = chapterid;
            ioFriendChapter.exploremin = fchapterTemplate.getMIN();
            ioFriendChapter.power = fchapterTemplate.getPOWER();
            if (friendExplore != null && friendExplore.getDbFriendChapter() != null && friendExplore.getDbFriendChapter().getChapters() != null &&
                    friendExplore.getDbFriendChapter().getChapters().containsKey(chapterid)) {
                FriendExplore.DbFriendChapter1 dbFriendChapter1 = friendExplore.getDbFriendChapter().getChapters().get(chapterid);
                Date etime = dbFriendChapter1.getEtime();
                ioFriendChapter.etime = etime.getTime();
                Date now = new Date();
                if (etime.before(now)) {
                    ioFriendChapter.status = 2;
                } else {
                    ioFriendChapter.status = 1;
                }
                ioFriendChapter.friends = new ArrayList<>(dbFriendChapter1.getFriends().size());
                for (int aFriendId : dbFriendChapter1.getFriends()) {
                    WsMessageBase.IOFriendEntity ioFriendEntity = new WsMessageBase.IOFriendEntity();
                    FriendManager.getInstance().buildIoFriendBase(ioFriendEntity, aFriendId);
                    ioFriendChapter.friends.add(ioFriendEntity);
                }
            } else {
                ioFriendChapter.status = 0;
            }
        }
        //get friend boss
        Date now = new Date();
        FriendBoss friendBoss = FriendBossCache.getInstance().getFriendBoss(playerId);
        boolean findMy = false;
        if (friendBoss != null && friendBoss.getDbFriendBoss() != null) {
            FriendBoss.DbFriendBoss dbFriendBoss = friendBoss.getDbFriendBoss();
            if (dbFriendBoss.getNowhp() > 0 && dbFriendBoss.getEtime().after(now)) {
                respmsg.boss = buildFriendBoss(playerId, dbFriendBoss, playerId);
                findMy = true;
            }
        }
        if (!findMy) {
            Map<Integer, FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
            for (Map.Entry<Integer, FriendBean> afriend : friends.entrySet()) {
                Integer aFriendId = afriend.getKey();
                FriendBoss afriendBoss = FriendBossCache.getInstance().getFriendBoss(aFriendId);
                if (afriendBoss != null && afriendBoss.getDbFriendBoss() != null && afriendBoss.getDbFriendBoss().getNowhp() > 0
                        && afriendBoss.getDbFriendBoss().getEtime().after(now)) {
                    FriendBoss.DbFriendBoss dbFriendBoss = afriendBoss.getDbFriendBoss();
                    respmsg.boss = buildFriendBoss(aFriendId, dbFriendBoss, playerId);
                    break;
                }
            }
        }
        return respmsg;
    }

    private static WsMessageBase.IOFriendBoss buildFriendBoss(int playerId, FriendBoss.DbFriendBoss dbFriendBoss,int myPlayerId) {
        if (dbFriendBoss != null) {
            WsMessageBase.IOFriendBoss ioFriendBoss = new WsMessageBase.IOFriendBoss();
            ioFriendBoss.ownPlayerId = playerId;
            ioFriendBoss.id = dbFriendBoss.getId();
            ioFriendBoss.gsid = dbFriendBoss.getGsid();
            ioFriendBoss.name = dbFriendBoss.getName();
            ioFriendBoss.level = dbFriendBoss.getLevel();
            ioFriendBoss.rewards = new ArrayList<>(dbFriendBoss.getRewards().size());
            for (RewardTemplateSimple rewardTemplateSimple : dbFriendBoss.getRewards()) {
                ioFriendBoss.rewards.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            ioFriendBoss.etime = dbFriendBoss.getEtime().getTime();
            ioFriendBoss.maxhp = dbFriendBoss.getMaxhp();
            ioFriendBoss.nowhp = dbFriendBoss.getNowhp();
            if(dbFriendBoss.getPlayerHurm().containsKey(myPlayerId)) {
                ioFriendBoss.lastdamges = dbFriendBoss.getPlayerHurm().get(myPlayerId).intValue();
            }
            ioFriendBoss.bset = new HashMap<>(dbFriendBoss.getFormationHeros().size());
            for (Map.Entry<Integer, DbBattleGeneral> aEntry : dbFriendBoss.getFormationHeros().entrySet()) {
                WsMessageBase.IOGeneralSimple ioGeneralSimple = new WsMessageBase.IOGeneralSimple();
                ioFriendBoss.bset.put(aEntry.getKey(), ioGeneralSimple);
                DbBattleGeneral dbBattleGeneral = aEntry.getValue();
                ioGeneralSimple.gsid = dbBattleGeneral.getChapterBattleTemplate().getGsid();
                ioGeneralSimple.level = dbBattleGeneral.getChapterBattleTemplate().getLevel();
                ioGeneralSimple.hpcover = dbBattleGeneral.getChapterBattleTemplate().getHpcover();
                ioGeneralSimple.pclass = dbBattleGeneral.getChapterBattleTemplate().getPclass();
                ioGeneralSimple.exhp = dbBattleGeneral.getChapterBattleTemplate().getExhp();
                ioGeneralSimple.nowhp = dbBattleGeneral.getNowhp();
                ioGeneralSimple.exatk = dbBattleGeneral.getChapterBattleTemplate().getExatk();
            }
            return ioFriendBoss;
        }
        return null;
    }

}
