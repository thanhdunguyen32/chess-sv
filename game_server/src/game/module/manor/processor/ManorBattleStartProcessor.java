package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.friend.bean.FriendBean;
import game.module.friend.dao.FriendCache;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.logic.ManorConstants;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.user.logic.PlayerInfoManager;
import game.module.user.logic.PlayerManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageFriend;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SManorBattleStart.id, accessLimit = 200)
public class ManorBattleStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorBattleStartProcessor.class);

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
        WsMessageBattle.C2SManorBattleStart reqmsg = WsMessageBattle.C2SManorBattleStart.parse(request);
        logger.info("manor battle start!player={},req={}", playerId, reqmsg);
        int enemy_index = reqmsg.index;
        //好友是否存在
        if (enemy_index == -1 && reqmsg.friendid > 0) {
            Map<Integer, FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
            if (!friends.containsKey(reqmsg.friendid)) {
                WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendDel.msgCode, 1404);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
        }
        //boss 邀请好友挑战次数限制2次
        int manorTeamBossCount = PlayerManager.getInstance().getOtherCount(playerId, ManorConstants.MANOR_BOSS_TEAM_FIGHT_COUNT_MARK);
        if (enemy_index == -1 && reqmsg.friendid > 0 && manorTeamBossCount >= ManorConstants.MANOR_BOSS_TEAM_FIGHT_COUNT_MAX) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleStart.msgCode, 1389);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is general exist
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        for (WsMessageBase.IOFormationGeneralPos ioFormationGeneralPos : items) {
            if (!generalMap.containsKey(ioFormationGeneralPos.general_uuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleStart.msgCode, 1432);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (ioFormationGeneralPos.pos < 0 || ioFormationGeneralPos.pos > 32) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleStart.msgCode, 1433);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //粮草是否够
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.FOOD, 1)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleStart.msgCode, 1359);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (items.length > 0) {
            //save manor formation
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            if (battleFormation == null) {
                battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
            }
            int manorFormationIndex = 0;
            int mythic = reqmsg.mythic;
            if (mythic > 0) {
                //check mythic exist
                boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
                if (!containsTemplate) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleStart.msgCode, 1420);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
                if (mythicsMap == null) {
                    mythicsMap = new HashMap<>();
                    battleFormation.setMythics(mythicsMap);
                }
                mythicsMap.put(manorFormationIndex, mythic);
            }
            //team
            Map<Integer, Long> formationMap = BattleFormationManager.getInstance().getFormationByType(manorFormationIndex, battleFormation);
            if (formationMap == null) {
                formationMap = new HashMap<>();
                BattleFormationManager.getInstance().setFormationByType(manorFormationIndex, battleFormation, formationMap);
            } else {
                formationMap.clear();
            }
            for (WsMessageBase.IOFormationGeneralPos aitem : items) {
                formationMap.put(aitem.pos, aitem.general_uuid);
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
            PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,formationMap,playingRole);
            PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        }
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.FOOD, -1, LogConstants.MODULE_MANOR);
        //save count
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        if (enemy_index == -1 && reqmsg.friendid > 0) {
            AwardUtils.changeRes(playingRole, ManorConstants.MANOR_BOSS_TEAM_FIGHT_COUNT_MARK, 1, LogConstants.MODULE_MANOR);
            manorBean.setTmpFriendId(reqmsg.friendid);
        } else {
            manorBean.setTmpFriendId(null);
        }
        //ret
        WsMessageBattle.S2CManorBattleStart respmsg = new WsMessageBattle.S2CManorBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
