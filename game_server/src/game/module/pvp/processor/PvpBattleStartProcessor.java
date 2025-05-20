package game.module.pvp.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.pvp.bean.PvpBean;
import game.module.pvp.bean.PvpPlayer;
import game.module.pvp.dao.PvpCache;
import game.module.pvp.logic.PvpConstants;
import game.module.season.dao.SeasonCache;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
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
@MsgCodeAnn(msgcode = WsMessageBattle.C2SPvpBattleStart.id, accessLimit = 200)
public class PvpBattleStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(PvpBattleStartProcessor.class);

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
        WsMessageBattle.C2SPvpBattleStart reqmsg = WsMessageBattle.C2SPvpBattleStart.parse(request);
        logger.info("pvp battle start!player={},req={}", playerId, reqmsg);
        int pvp_index = reqmsg.pvp_index;
        //cost item
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        int markCount = 0;
        if (playerOthers.containsKey(PvpConstants.PVP_FREE_ATTACK_COUNT_MARK)) {
            markCount = playerOthers.get(PvpConstants.PVP_FREE_ATTACK_COUNT_MARK).getCount();
        }
        if (markCount >= 3 && !ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.PVP_COIN, 1)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CPvpBattleStart.msgCode, 1426);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is in battle
        PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
        PvpPlayer myPvpInfo = pvpBean.getPvpPlayerInfo().get(playerId);
        if(myPvpInfo.getAgainstPlayers() == null){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CPvpBattleStart.msgCode, 1426);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Integer enemyPlayerId = myPvpInfo.getAgainstPlayers().get(pvp_index);
        PvpPlayer enemyPvpInfo = pvpBean.getPvpPlayerInfo().get(enemyPlayerId);
        if (enemyPvpInfo != null) {
            if (enemyPvpInfo.getInBattle() != null && enemyPvpInfo.getInBattle()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CPvpBattleStart.msgCode, 1520);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //save normal formation
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int pvpAttackFormationIndex = 3;
        int mythic = reqmsg.mythic;
        if (mythic > 0) {
            //check mythic exist
            boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
            if (!containsTemplate) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CPvpBattleStart.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
            if (mythicsMap == null) {
                mythicsMap = new HashMap<>();
                battleFormation.setMythics(mythicsMap);
            }
            mythicsMap.put(pvpAttackFormationIndex, mythic);
        }
        //team
        Map<Integer, Long> pvpattMap = battleFormation.getPvpatt();
        if (pvpattMap == null) {
            pvpattMap = new HashMap<>();
            battleFormation.setPvpatt(pvpattMap);
        } else {
            pvpattMap.clear();
        }
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            pvpattMap.put(aitem.pos, aitem.general_uuid);
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
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,pvpattMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //cost
        if (markCount < 3) {
            AwardUtils.changeRes(playingRole, PvpConstants.PVP_FREE_ATTACK_COUNT_MARK, 1, LogConstants.MODULE_PVP);
        } else {
            AwardUtils.changeRes(playingRole, ItemConstants.PVP_COIN, -1, LogConstants.MODULE_PVP);
        }
        //save in battle info
        myPvpInfo.setInBattle(true);
        myPvpInfo.setEnemyIndex(pvp_index);
        if (enemyPvpInfo == null) {
            enemyPvpInfo = new PvpPlayer(enemyPlayerId);
            pvpBean.getPvpPlayerInfo().put(enemyPlayerId, enemyPvpInfo);
        }
        enemyPvpInfo.setInBattle(true);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.PVP_PMARK, 1, LogConstants.MODULE_PVP);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.PVP_ATTACK, 1, LogConstants.MODULE_PVP);
        //ret
        WsMessageBattle.S2CPvpBattleStart respmsg = new WsMessageBattle.S2CPvpBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
