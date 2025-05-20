package game.module.chapter.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.pvp.logic.PvpManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SBattleFormationSave.id, accessLimit = 200)
public class BattleFormationSaveProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(BattleFormationSaveProcessor.class);

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
        WsMessageBattle.C2SBattleFormationSave reqmsg = WsMessageBattle.C2SBattleFormationSave.parse(request);
        logger.info("battle formation save!player={},req={}", playerId, reqmsg);
        //check formation exist
        String formation_name = reqmsg.formation_name;
        int battleFormationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, formation_name);
        if (battleFormationIndex < 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CBattleFormationSave.msgCode, 1421);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //check
        Map<Long, GeneralBean> generalCache = GeneralCache.getInstance().getHeros(playerId);
        Set<Long> existHeros = new HashSet<>();
        for (WsMessageBase.IOFormationGeneralPos formationGeneralPos : reqmsg.items) {
            long general_uuid = formationGeneralPos.general_uuid;
            if (existHeros.contains(general_uuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CBattleFormationSave.msgCode, 1475);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (!generalCache.containsKey(general_uuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CBattleFormationSave.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            existHeros.add(general_uuid);
        }
        //王者演武阵容
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormationIndex == 9 || battleFormationIndex == 10 || battleFormationIndex == 11) {
            existHeros.clear();
            for (int i = 0; i < 3; i++) {
                String kingpvpFormation = "kingpvp" + (i + 1);
                if (kingpvpFormation.equals(formation_name)) {
                    continue;
                }
                int formationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, kingpvpFormation);
                if (battleFormation == null) {
                    continue;
                }
                Map<Integer, Long> kpFormationSet = BattleFormationManager.getInstance().getFormationByType(formationIndex, battleFormation);
                if (kpFormationSet != null) {
                    existHeros.addAll(kpFormationSet.values());
                }
            }
            //check duplicate
            for (WsMessageBase.IOFormationGeneralPos formationGeneralPos : reqmsg.items) {
                long general_uuid = formationGeneralPos.general_uuid;
                if (existHeros.contains(general_uuid)) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CBattleFormationSave.msgCode, 1476);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            }
        }
        //save battle formation
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int mythic = reqmsg.mythic;
        if (mythic > 0) {
            //check mythic exist
            boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
            if (!containsTemplate) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CBattleFormationSave.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
            if (mythicsMap == null) {
                mythicsMap = new HashMap<>();
                battleFormation.setMythics(mythicsMap);
            }
            mythicsMap.put(battleFormationIndex, mythic);
        }
        //team
        Map<Integer, Long> formationMap = BattleFormationManager.getInstance().getFormationByType(battleFormationIndex, battleFormation);
        if (formationMap == null) {
            formationMap = new HashMap<>();
            BattleFormationManager.getInstance().setFormationByType(battleFormationIndex, battleFormation, formationMap);
        } else {
            formationMap.clear();
        }
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
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
        // init pvp score
        if (formation_name.equals("pvpdef") || formation_name.equals("pvpatt")) {
            PvpManager.getInstance().initMyPvpScore(playerId);
        }
        //ret
        WsMessageBattle.S2CBattleFormationSave respmsg = new WsMessageBattle.S2CBattleFormationSave();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
