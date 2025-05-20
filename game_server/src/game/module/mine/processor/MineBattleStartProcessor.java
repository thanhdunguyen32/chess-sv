package game.module.mine.processor;

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
import game.module.mine.bean.DBMine;
import game.module.mine.bean.DBMinePlayer;
import game.module.mine.dao.MineCache;
import game.module.mine.logic.MineConstants;
import game.module.mine.logic.MineManager;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.user.dao.CommonTemplateCache;
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
import ws.WsMessageMine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SMineBattleStart.id, accessLimit = 200)
public class MineBattleStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MineBattleStartProcessor.class);

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
        WsMessageBattle.C2SMineBattleStart reqmsg = WsMessageBattle.C2SMineBattleStart.parse(request);
        logger.info("mine battle start!player={},req={}", playerId, reqmsg);
        //is general exist
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        for (WsMessageBase.IOFormationGeneralPos ioFormationGeneralPos : items) {
            if (!generalMap.containsKey(ioFormationGeneralPos.general_uuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 1432);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (ioFormationGeneralPos.pos < 0 || ioFormationGeneralPos.pos > 32) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 1433);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        int level_index = reqmsg.level_index;
        int point_index = reqmsg.point_index;
        List<Integer> mineLevelConfig = (List<Integer>) CommonTemplateCache.getInstance().getConfig("mine_levels");
        if (level_index < 0 || level_index >= mineLevelConfig.size() - 1) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        int min_level = mineLevelConfig.get(level_index);
        if (playingRole.getPlayerBean().getLevel() < min_level) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode,2101);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        if (point_index < 0 || point_index >= MineConstants.PAGE_SIZE * MineConstants.POINT_SIZE_1_PAGE) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode,130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        DBMine mineEntity = MineCache.getInstance().getDBMine();
        if (mineEntity == null) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode,130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // 占领超过上限
        DBMinePlayer minePlayerEntity = null;
        if(mineEntity.getPlayers() != null) {
            minePlayerEntity = mineEntity.getPlayers().get(playerId);
            if (minePlayerEntity != null && minePlayerEntity.getOwnMinePoint() != null && minePlayerEntity.getOwnMinePoint().size() >= MineConstants.MAX_MINE_COUNT) {
                WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 2102);
                playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                return;
            }
        }
        // 是否已经占领
        int minePointVal = MineManager.getInstance().genereteMintPoint(level_index, point_index);
        if (minePlayerEntity != null && minePlayerEntity.getOwnMinePoint() != null) {
            List<Integer> ownPointList = minePlayerEntity.getOwnMinePoint();
            for (Integer aPointVal : ownPointList) {
                if (minePointVal == aPointVal) {
                    WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode,2103);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
            }
        }
        //cost 演武令
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.PVP_COIN, 1)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //save mine formation
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int mineFormationIndex = 3;
        int mythic = reqmsg.mythic;
        if (mythic > 0) {
            //check mythic exist
            boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
            if (!containsTemplate) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
            if (mythicsMap == null) {
                mythicsMap = new HashMap<>();
                battleFormation.setMythics(mythicsMap);
            }
            mythicsMap.put(mineFormationIndex, mythic);
        }
        //team
        Map<Integer, Long> mineMap = BattleFormationManager.getInstance().getFormationByType(mineFormationIndex, battleFormation);
        if (mineMap == null) {
            mineMap = new HashMap<>();
            BattleFormationManager.getInstance().setFormationByType(mineFormationIndex, battleFormation, mineMap);
        } else {
            mineMap.clear();
        }
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            mineMap.put(aitem.pos, aitem.general_uuid);
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
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,mineMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //cost
        AwardUtils.changeRes(playingRole, ItemConstants.PVP_COIN, -1, LogConstants.MODULE_MINE);
        //ret
        WsMessageBattle.S2CMineBattleStart respmsg = new WsMessageBattle.S2CMineBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        BattleManager.getInstance().tmpSaveMineCache(playerId,level_index,point_index);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
