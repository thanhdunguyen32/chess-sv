package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.friend.logic.FriendManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.season.dao.SeasonCache;
import game.module.user.logic.PlayerInfoManager;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SFriendBattleStart.id, accessLimit = 200)
public class FriendBattleStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendBattleStartProcessor.class);

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
        WsMessageBattle.C2SFriendBattleStart reqmsg = WsMessageBattle.C2SFriendBattleStart.parse(request);
        logger.info("friend pk battle start!player={},req={}", playerId, reqmsg);
        //friend pvpdef formation exist
        int friendid = reqmsg.friendid;
        BattleFormation friendBattleFormation = PlayerOfflineManager.getInstance().getBattleFormation(friendid);
        if (friendBattleFormation == null || friendBattleFormation.getPvpdef() == null) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CFriendBattleStart.msgCode, 1456);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //is general exist
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        for (WsMessageBase.IOFormationGeneralPos ioFormationGeneralPos : items) {
            if (!generalMap.containsKey(ioFormationGeneralPos.general_uuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CFriendBattleStart.msgCode, 1432);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (ioFormationGeneralPos.pos < 0 || ioFormationGeneralPos.pos > 32) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CFriendBattleStart.msgCode, 1433);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //save exped formation
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
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CFriendBattleStart.msgCode, 1420);
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
        Map<Integer, Long> expedMap = BattleFormationManager.getInstance().getFormationByType(normalFormationIndex, battleFormation);
        if (expedMap == null) {
            expedMap = new HashMap<>();
            BattleFormationManager.getInstance().setFormationByType(normalFormationIndex, battleFormation, expedMap);
        } else {
            expedMap.clear();
        }
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            expedMap.put(aitem.pos, aitem.general_uuid);
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
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            long general_uuid = aitem.general_uuid;
            GeneralBean generalBean = generalMap.get(general_uuid);
            if (generalBean != null) {
                sumPower += generalBean.getPower();
            }
        }
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,expedMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //tmp save
        FriendManager.getInstance().putFriendPkMap(playerId,friendid);
        //ret
        WsMessageBattle.S2CFriendBattleStart respmsg = new WsMessageBattle.S2CFriendBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
