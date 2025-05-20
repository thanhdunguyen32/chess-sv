package game.module.legion.processor;

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
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.item.logic.ItemManager;
import game.module.legion.dao.LegionBossTemplateCache;
import game.module.legion.logic.LegionConstants;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.template.LegionBossTemplate;
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
import ws.WsMessageLegion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SLegionBossBattleStart.id, accessLimit = 200)
public class LegionBossBattleStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionBossBattleStartProcessor.class);

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
        WsMessageBattle.C2SLegionBossBattleStart reqmsg = WsMessageBattle.C2SLegionBossBattleStart.parse(request);
        logger.info("legion boss battle start!player={},req={}", playerId, reqmsg);
        int enemy_index = reqmsg.chapter_id;
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CLegionBossBattleStart.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count max
        List<LegionBossTemplate.LegionBossCost> legionBossCosts = LegionBossTemplateCache.getInstance().getCosts();
        int attackCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), LegionConstants.LEGION_BOSS_ATTACK_MARK);
        if (attackCount >= legionBossCosts.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CLegionBossBattleStart.msgCode, 1543);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        LegionBossTemplate.LegionBossCost legionBossCost = legionBossCosts.get(attackCount);
        if (legionBossCost.getCOST() > 0 && !ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.YB,
                legionBossCost.getCOST())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CLegionBossBattleStart.msgCode, 1544);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is general exist
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        for (WsMessageBase.IOFormationGeneralPos ioFormationGeneralPos : items) {
            if (!generalMap.containsKey(ioFormationGeneralPos.general_uuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CLegionBossBattleStart.msgCode, 1432);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (ioFormationGeneralPos.pos < 0 || ioFormationGeneralPos.pos > 32) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CLegionBossBattleStart.msgCode, 1433);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //save formation
        if (items.length > 0) {
            //save legion boss formation
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
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CLegionBossBattleStart.msgCode, 1420);
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
        //mark
        AwardUtils.changeRes(playingRole, LegionConstants.LEGION_BOSS_ATTACK_MARK, 1, LogConstants.MODULE_LEGION);
        //cost
        if (legionBossCost.getCOST() > 0) {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -legionBossCost.getCOST(), LogConstants.MODULE_LEGION);
        }
        //ret
        WsMessageBattle.S2CLegionBossBattleStart respmsg = new WsMessageBattle.S2CLegionBossBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
