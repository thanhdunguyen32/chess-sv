package game.module.guozhan.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import game.module.user.logic.PlayerInfoManager;
import lion.netty4.message.RequestByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageGuozhan.DBGuoZhanNation;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOfficePoint;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.bean.TimerTaskOfficeReset;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanManager;
import game.module.guozhan.logic.GuozhanDaoHelper;
import game.module.user.dao.CommonTemplateCache;
import game.session.SessionManager;
import io.netty.util.Timeout;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.session.GlobalTimer;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageBattle.C2SGuozhanOfficeStart;
import ws.WsMessageBattle.S2CGuozhanOfficeStart;
import ws.WsMessageHall;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SGuozhanOfficeStart.id, accessLimit = 500)
public class GuozhanOfficeStartProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanOfficeStartProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {

	}

	@Override
	public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		C2SGuozhanOfficeStart reqMsg = C2SGuozhanOfficeStart.parse(request);
		int office_index = reqMsg.office_index;
		logger.info("guozhan office battle start!playerId={},office_id={}", playerId, office_index);
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		// 还没有国家
		if (guozhanPlayer == null || guozhanPlayer.getNation() == 0) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanOfficeStart.msgCode, 130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 是否已经通关
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() < citySize) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanOfficeStart.msgCode,1006);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 对手是否战斗中
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		int myNationId = guozhanPlayer.getNation();
		if (guoZhanOffice != null) {
			DBGuoZhanNation guoZhanNation = guoZhanOffice.getNations(myNationId - 1);
			if (guoZhanNation.getPlayerOfficesCount() > 0) {
				DBGuoZhanOfficePoint guoZhanOfficePoint = guoZhanNation.getPlayerOffices(office_index);
				if (guoZhanOfficePoint.getIsFighting()) {
					S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanOfficeStart.msgCode,1009);
					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
					return;
				}
			}
		}
        //cost 演武令
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.PVP_COIN, 1)) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CGuozhanOfficeStart.msgCode, 2105);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
		//关卡id是否正确
		int myOfficeIndex = -1;
		if (guoZhanOffice != null) {
			myOfficeIndex = guoZhanOffice.getPlayerPointOrDefault(playerId, -1);
		}
		if(myOfficeIndex >-1) {
			//已经最高级
			if(myOfficeIndex /10 ==0) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanOfficeStart.msgCode,1010);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
			//大一级
			if(myOfficeIndex/10 - 1 != office_index/10) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanOfficeStart.msgCode,1010);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}else {
			if(office_index /10 != 9) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanOfficeStart.msgCode,1010);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
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
		if (guoZhanOffice == null) {
			DBGuoZhanOffice.Builder builder = GuoZhanManager.getInstance().createGuoZhanOffice();
			GuoZhanManager.getInstance().createGuozhanPoints(builder, myNationId);
			builder.getNationsBuilder(myNationId - 1).getPlayerOfficesBuilder(office_index).setIsFighting(true);
			guoZhanOffice = builder.build();
            GuozhanCache.getInstance().setGuozhanOffice(guoZhanOffice);
			GuozhanDaoHelper.asyncUpdateGuozhanOffice(guoZhanOffice);
		} else {
			DBGuoZhanOffice.Builder builder = guoZhanOffice.toBuilder();
			DBGuoZhanNation nation1 = builder.getNations(myNationId - 1);
			if (nation1.getPlayerOfficesCount() == 0) {
				GuoZhanManager.getInstance().createGuozhanPoints(builder, myNationId);
			}
			builder.getNationsBuilder(myNationId - 1).getPlayerOfficesBuilder(office_index).setIsFighting(true);
			guoZhanOffice = builder.build();
			GuozhanCache.getInstance().setGuozhanOffice(guoZhanOffice);
		}
		//schedule
		int pointVal = (myNationId-1)*100+office_index;
		Timeout timeout = GuoZhanManager.getInstance().getScheduleTimeout(pointVal);
		if(timeout != null) {
			timeout.cancel();
		}
		Timeout newTimeout = GlobalTimer.getInstance().newTimeout(new TimerTaskOfficeReset(pointVal),
				5*60);
		GuoZhanManager.getInstance().addScheduleTimeout(pointVal, newTimeout);
		//
        //save mine formation
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
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,mineMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //cost
        AwardUtils.changeRes(playingRole, ItemConstants.PVP_COIN, -1, LogConstants.MODULE_MINE);
        //ret
        WsMessageBattle.S2CGuozhanOfficeStart respmsg = new WsMessageBattle.S2CGuozhanOfficeStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        BattleManager.getInstance().tmpSaveGuozhanCache(playerId,office_index);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
	}

}
