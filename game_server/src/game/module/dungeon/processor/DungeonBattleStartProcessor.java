package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.bean.DungeonNode;
import game.module.dungeon.dao.DungeonCache;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.mythical.dao.MythicalTemplateCache;
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
import ws.WsMessageDungeon;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SDungeonBattleStart.id, accessLimit = 200)
public class DungeonBattleStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DungeonBattleStartProcessor.class);

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
        WsMessageBattle.C2SDungeonBattleStart reqmsg = WsMessageBattle.C2SDungeonBattleStart.parse(request);
        logger.info("dungeon battle start!player={},req={}", playerId, reqmsg);
        //check
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean == null || dungeonBean.getDbDungeonNode() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CDungeonBattleStart.msgCode, 1442);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        DungeonNode dungeonNode = dungeonBean.getDbDungeonNode().getNodelist().get(1);
        for (DungeonNode.DungeonNodePos dungeonNodePos : dungeonNode.getPoslist()) {
            if (dungeonNodePos.getChoose() != null && dungeonNodePos.getChoose().equals(1)) {
                if (!dungeonNodePos.getType().equals(1) && !dungeonNodePos.getType().equals(2) && !dungeonNodePos.getType().equals(3)) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CDungeonBattleStart.msgCode, 1447);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                break;
            }
        }
        //save dungeon formation
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int dungeonFormationIndex = 7;
        int mythic = reqmsg.mythic;
        if (mythic > 0) {
            //check mythic exist
            boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
            if (!containsTemplate) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CDungeonBattleStart.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
            if (mythicsMap == null) {
                mythicsMap = new HashMap<>();
                battleFormation.setMythics(mythicsMap);
            }
            mythicsMap.put(dungeonFormationIndex, mythic);
        }
        //team
        Map<Integer, Long> formationMap = BattleFormationManager.getInstance().getFormationByType(dungeonFormationIndex, battleFormation);
        if (formationMap == null) {
            formationMap = new HashMap<>();
            BattleFormationManager.getInstance().setFormationByType(dungeonFormationIndex, battleFormation, formationMap);
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
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,formationMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //ret
        WsMessageBattle.S2CDungeonBattleStart respmsg = new WsMessageBattle.S2CDungeonBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
