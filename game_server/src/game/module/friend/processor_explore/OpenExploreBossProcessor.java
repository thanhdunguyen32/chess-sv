package game.module.friend.processor_explore;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendBoss;
import game.module.friend.dao.FriendBossCache;
import game.module.friend.dao.FriendCache;
import game.module.friend.logic.FriendConstants;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.item.logic.ItemConstants;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.user.logic.PlayerInfoManager;
import game.module.user.logic.PlayerServerPropManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SOpenExploreBoss.id, accessLimit = 200)
public class OpenExploreBossProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(OpenExploreBossProcessor.class);

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
        WsMessageBattle.C2SOpenExploreBoss reqmsg = WsMessageBattle.C2SOpenExploreBoss.parse(request);
        logger.info("friend boss battle start!player={},req={}", playerId, reqmsg);
        //can attack
        int boss_owner_id = reqmsg.boss_owner_id;
        FriendBoss friendBoss = FriendBossCache.getInstance().getFriendBoss(boss_owner_id);
        if (friendBoss == null || friendBoss.getDbFriendBoss() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2COpenExploreBoss.msgCode, 1451);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is dead
        FriendBoss.DbFriendBoss dbFriendBoss = friendBoss.getDbFriendBoss();
        Date now = new Date();
        if (dbFriendBoss.getEtime().before(now) || dbFriendBoss.getNowhp() <= 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2COpenExploreBoss.msgCode, 1452);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is not friend
        Map<Integer, FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
        if (boss_owner_id != playerId && !friends.containsKey(boss_owner_id)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2COpenExploreBoss.msgCode, 1453);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count max
        int friendBossAttackCount = PlayerServerPropManager.getInstance().getServerPropCount(playerId, ItemConstants.FRIEND_BOSS_ATTACK_COUNT_MARK);
        if (friendBossAttackCount >= FriendConstants.FRIEND_BOSS_ATTACK_COUNT) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2COpenExploreBoss.msgCode, 1454);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //add mark
        PlayerServerPropManager.getInstance().changeServerProp(playingRole, ItemConstants.FRIEND_BOSS_ATTACK_COUNT_MARK, 1);
        //save normal formation
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int normalFormationIndex = 0;
        int mythic = reqmsg.mythic;
        if (mythic > 0) {
            //check mythic exist
            boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
            if (!containsTemplate) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2COpenExploreBoss.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
            if (mythicsMap == null) {
                mythicsMap = new HashMap<>();
                battleFormation.setMythics(mythicsMap);
            }
            mythicsMap.put(normalFormationIndex, mythic);
        }
        //team
        Map<Integer, Long> normalMap = battleFormation.getNormal();
        if (normalMap == null) {
            normalMap = new HashMap<>();
            battleFormation.setNormal(normalMap);
        } else {
            normalMap.clear();
        }
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            normalMap.put(aitem.pos, aitem.general_uuid);
        }
        //save formation
        if (battleFormation.getId() != null) {
            BattleFormationDaoHelper.asyncUpdateBattleFormation(battleFormation);
        } else {
            BattleFormationCache.getInstance().addBattleFormation(battleFormation);
            BattleFormationDaoHelper.asyncInsertBattleFormation(battleFormation);
        }
        //update player power
        int sumPower = 0;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            long general_uuid = aitem.general_uuid;
            GeneralBean generalBean = generalMap.get(general_uuid);
            if (generalBean != null) {
                sumPower += generalBean.getPower();
            }
        }
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,normalMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //ret
        WsMessageBattle.S2COpenExploreBoss respmsg = new WsMessageBattle.S2COpenExploreBoss();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
