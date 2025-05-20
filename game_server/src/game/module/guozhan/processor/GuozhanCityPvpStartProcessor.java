package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.bean.TimerTaskGuozhanCityReset;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanFightManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.user.logic.PlayerInfoManager;
import game.session.SessionManager;
import io.netty.util.Timeout;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.session.GlobalTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageBattle.C2SGuozhanCityStart;
import ws.WsMessageHall;
import ws.WsMessageHall.S2CErrorCode;

import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBattle.C2SGuozhanCityStart.id, accessLimit = 500)
public class GuozhanCityPvpStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuozhanCityPvpStartProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        C2SGuozhanCityStart reqMsg = C2SGuozhanCityStart.parse(request);
        int city_index = reqMsg.city_index;
        logger.info("guozhan city pvp battle start!playerId={},city_index={}", playerId, city_index);
        GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
        // 还没有国家
        if (guozhanPlayer == null || guozhanPlayer.getNation() == 0) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 130);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // 是否已经通关
        int citySize = CityJoinTemplateCache.getInstance().getSize();
        if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
                && guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() < citySize) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 1006);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //还未生成
        ProtoMessageGuozhan.DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
        if (guoZhanFight == null) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 130);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        ProtoMessageGuozhan.DBGuoZhanCity guoZhanCity = guoZhanFight.getCitys(city_index);
        if (guoZhanCity == null) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 130);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //cost 演武令
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.PVP_COIN, 1)) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 2105);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //是否在战斗中
        boolean inBattle = guoZhanCity.getInBattle();
        if (inBattle) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 1015);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //我的信息
        ProtoMessageGuozhan.DBGuoZhanRole guoZhanRole = guoZhanFight.getPlayersOrDefault(playerId, null);
        if (guoZhanRole == null) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 130);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //同一个city
        int oldCityIndex = guoZhanRole.getCityIndex();
        if (oldCityIndex == city_index) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 1011);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //能否移动到
        int moveSteps = GuoZhanFightManager.getInstance().checkMove2City(oldCityIndex, city_index, guozhanPlayer.getNation());
        if (moveSteps <= 0) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 1012);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //步数是否够
        int targetNationId = guoZhanCity.getOwnNationId();
        int costSteps = moveSteps;
        if (targetNationId != guozhanPlayer.getNation()) {
            costSteps += 2;
        }
        logger.info("move cost step:{}", costSteps);
        int mySteps = GuoZhanFightManager.getInstance().getMyMoveStep(guoZhanRole);
        if (mySteps < costSteps) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 1013);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //是否有敌方驻守
        if (targetNationId == guozhanPlayer.getNation() || guoZhanCity.getOccupyPlayerCount() == 0) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanCityStart.msgCode, 1014);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //is general exist
        WsMessageBase.IOFormationGeneralPos[] items = reqMsg.items;
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
        // do
        //更新移动步数
        ProtoMessageGuozhan.DBGuoZhanFight.Builder guozhanFightBuilder = guoZhanFight.toBuilder();
        ProtoMessageGuozhan.DBGuoZhanRole.Builder guoZhanRoleBuilder = guoZhanRole.toBuilder();
        int curMoveStep = mySteps - costSteps;
        guoZhanRoleBuilder.setMoveStep(curMoveStep).setLastAddStepTime(System.currentTimeMillis());
        guozhanFightBuilder.putPlayers(playerId, guoZhanRoleBuilder.build());
        //设置战斗状态
        guozhanFightBuilder.getCitysBuilder(city_index).setInBattle(true);
        guoZhanFight = guozhanFightBuilder.build();
        GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
        //schedule
        Timeout newTimeout = GuoZhanFightManager.getInstance().getScheduleTimeout(city_index);
        if (newTimeout == null) {
            newTimeout = GlobalTimer.getInstance().newTimeout(new TimerTaskGuozhanCityReset(city_index),
                    5 * 60);
            GuoZhanFightManager.getInstance().addScheduleTimeout(city_index, newTimeout);
        } else {
            TimerTaskGuozhanCityReset myTask = (TimerTaskGuozhanCityReset) newTimeout.task();
            myTask.increaseTaskSize();
        }
        //
        //save pvp att formation
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int mineFormationIndex = 3;
        int mythic = reqMsg.mythic;
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
        for (WsMessageBase.IOFormationGeneralPos aitem : reqMsg.items) {
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
        for (WsMessageBase.IOFormationGeneralPos aitem : reqMsg.items) {
            long general_uuid = aitem.general_uuid;
            GeneralBean generalBean = generalMap.get(general_uuid);
            if (generalBean != null) {
                sumPower += generalBean.getPower();
            }
        }
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(), sumPower, mythic, mineMap, playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //cost
        AwardUtils.changeRes(playingRole, ItemConstants.PVP_COIN, -1, LogConstants.MODULE_MINE);
        //ret
        WsMessageBattle.S2CGuozhanCityStart respmsg = new WsMessageBattle.S2CGuozhanCityStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        BattleManager.getInstance().tmpSaveGuozhanCache(playerId, city_index);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));

    }

}
