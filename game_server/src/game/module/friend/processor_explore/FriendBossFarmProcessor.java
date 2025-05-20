package game.module.friend.processor_explore;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendBoss;
import game.module.friend.dao.FriendBossCache;
import game.module.friend.dao.FriendBossDaoHelper;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.MyFriendExploreTemplateCache;
import game.module.friend.logic.FriendConstants;
import game.module.item.logic.ItemConstants;
import game.module.mail.logic.MailManager;
import game.module.manor.bean.DbBattleGeneral;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.template.MyFriendExploreTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.PlayerServerPropManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBattle;
import ws.WsMessageFriend;
import ws.WsMessageHall;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendBossFarm.id, accessLimit = 200)
public class FriendBossFarmProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendBossFarmProcessor.class);

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
        int playerId = playingRole.getId();
        WsMessageFriend.C2SFriendBossFarm reqmsg = WsMessageFriend.C2SFriendBossFarm.parse(request);
        logger.info("friend boss farm!player={}", playerId);
        int boss_owner_id = reqmsg.boss_owner_id;
        FriendBoss friendBoss = FriendBossCache.getInstance().getFriendBoss(boss_owner_id);
        if (friendBoss == null || friendBoss.getDbFriendBoss() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendBossFarm.msgCode, 1451);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is dead
        FriendBoss.DbFriendBoss dbFriendBoss = friendBoss.getDbFriendBoss();
        Date now = new Date();
        if (dbFriendBoss.getEtime().before(now) || dbFriendBoss.getNowhp() <= 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendBossFarm.msgCode, 1452);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is not friend
        Map<Integer, FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
        if (boss_owner_id != playerId && !friends.containsKey(boss_owner_id)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendBossFarm.msgCode, 1453);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count max
        int friendBossAttackCount = PlayerServerPropManager.getInstance().getServerPropCount(playerId, ItemConstants.FRIEND_BOSS_ATTACK_COUNT_MARK);
        if (friendBossAttackCount >= FriendConstants.FRIEND_BOSS_ATTACK_COUNT) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendBossFarm.msgCode, 1454);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //no hurm
        if (!dbFriendBoss.getPlayerHurm().containsKey(playerId)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendBossFarm.msgCode, 1455);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //add mark
        PlayerServerPropManager.getInstance().changeServerProp(playingRole, ItemConstants.FRIEND_BOSS_ATTACK_COUNT_MARK, 1);
        //do
        long lastHurm = dbFriendBoss.getPlayerHurm().get(playerId);
        Long nowhp = dbFriendBoss.getNowhp();
        nowhp -= lastHurm;
        float hurmPercent = nowhp * 1f / dbFriendBoss.getMaxhp();
        if (nowhp > 0) {
            for (DbBattleGeneral dbBattleGeneral : dbFriendBoss.getFormationHeros().values()) {
                dbBattleGeneral.setNowhp((int) (dbBattleGeneral.getMaxhp() * hurmPercent));
            }
            dbFriendBoss.setNowhp(nowhp);
            //save bean
            FriendBossDaoHelper.asyncUpdateFriendBoss(friendBoss);
        } else {
            //send award
            String mailTitle = "Phần thưởng hoạt động BOSS bạn bè"; //"好友BOSS击杀奖励"
            String mailContent = "Người chơi【%1$s】BOSS của bạn bè đã bị bạn bè của bạn giết chết, phần thưởng như sau:"; //"玩家【%1$s】的BOSS被他的好友击杀，以下是奖励：";
            for (int aPlayerId : dbFriendBoss.getPlayerHurm().keySet()) {
                MyFriendExploreTemplate friendExploreConfig = MyFriendExploreTemplateCache.getInstance().getFriendExploreConfig(dbFriendBoss.getId());
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : friendExploreConfig.getRewards()) {
                    int gsid = rewardTemplateSimple.getGSID();
                    int itemCount = rewardTemplateSimple.getCOUNT();
                    mailAtt.put(gsid, itemCount);
                }
                //send mail
                PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance().getPlayerOfflineCache(boss_owner_id);
                String formatMailContent = String.format(mailContent, playerOfflineCache.getName());
                MailManager.getInstance().sendSysMailToSingle(aPlayerId, mailTitle, formatMailContent, mailAtt);
            }
            //remove
            FriendBossCache.getInstance().removeFriendBoss(boss_owner_id);
            FriendBossDaoHelper.asyncRemoveFriendBoss(friendBoss.getId());
        }
        //ret
        WsMessageFriend.S2CFriendBossFarm respmsg = new WsMessageFriend.S2CFriendBossFarm();
        respmsg.ret = FriendExploreDataProcessor.buildResponse(playerId,nowhp <= 0 ? 1 : 0);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
