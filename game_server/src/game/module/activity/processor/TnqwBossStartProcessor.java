package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActTnqw;
import game.module.activity.dao.ActTnqwCache;
import game.module.activity.dao.ActivityWeekTemplateCache;
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
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.template.ZhdTnqwTemplate;
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
@MsgCodeAnn(msgcode = WsMessageBattle.C2STnqwBossStart.id, accessLimit = 200)
public class TnqwBossStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TnqwBossStartProcessor.class);

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
        WsMessageBattle.C2STnqwBossStart reqmsg = WsMessageBattle.C2STnqwBossStart.parse(request);
        logger.info("tnqw boss battle start!player={},req={}", playerId, reqmsg);
        int boss_index = reqmsg.boss_index;
        ZhdTnqwTemplate tnqwTemplate = ActivityWeekTemplateCache.getInstance().getTnqwTemplate();
        if (boss_index < 0 || boss_index >= tnqwTemplate.getBosslist().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 130);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        int scoreSum = 0;
        for (ZhdTnqwTemplate.ZhdTnqwEvent zhdTnqwEvent : tnqwTemplate.getEvent()) {
            int scoreMark = zhdTnqwEvent.getMark();
            scoreSum += ItemManager.getInstance().getCount(playingRole.getPlayerBean(), scoreMark);
        }
        ActTnqw actTnqw = ActTnqwCache.getInstance().getActTnqw(playerId);
        ZhdTnqwTemplate.ZhdTnqwBosslist zhdTnqwBosslist = tnqwTemplate.getBosslist().get(boss_index);
        if (zhdTnqwBosslist.getActscore() != null) {
            //积分未到
            if (scoreSum < zhdTnqwBosslist.getActscore()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 1551);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        } else {//大 boss
            if (actTnqw == null || actTnqw.getNowHp() == null) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 1553);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //其余boss没有全部死亡
            for (int i = 0; i < tnqwTemplate.getBosslist().size() - 1; i++) {
                if (!actTnqw.getNowHp().containsKey(i) || actTnqw.getNowHp().get(i) > 0) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 1553);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            }
        }
        //已经死亡
        if (actTnqw != null && actTnqw.getNowHp() != null && actTnqw.getNowHp().containsKey(boss_index) && actTnqw.getNowHp().get(boss_index)<=0) {//已经死亡
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 1552);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost兵符
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.BING_FU, 1)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 1554);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is general exist
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        for (WsMessageBase.IOFormationGeneralPos ioFormationGeneralPos : items) {
            if (!generalMap.containsKey(ioFormationGeneralPos.general_uuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 1432);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (ioFormationGeneralPos.pos < 0 || ioFormationGeneralPos.pos > 32) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 1433);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //save formation
        if (items.length > 0) {
            //save world boss formation
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
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossStart.msgCode, 1420);
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
            PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(), sumPower, mythic, formationMap, playingRole);
            PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        }
        //cost兵符
        AwardUtils.changeRes(playingRole, ItemConstants.BING_FU, -1, LogConstants.MODULE_ACTIVITY);
        //ret
        WsMessageBattle.S2CTnqwBossStart respmsg = new WsMessageBattle.S2CTnqwBossStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
