package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan.DBGuoZhanCity;
import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import db.proto.ProtoMessageGuozhan.DBGuoZhanRole;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanFightManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageGuozhan;
import ws.WsMessageGuozhan.C2SGuoZhanCityMove;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SGuoZhanCityMove.id, accessLimit = 500)
public class GuozhanCityMoveProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanCityMoveProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SGuoZhanCityMove reqMsg = C2SGuoZhanCityMove.parse(request);
		int playerId = playingRole.getId();
		int city_index = reqMsg.city_index;
		logger.info("guo zhan city move!playerId={},city_index={}", playerId, city_index);
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		// 还没有国家
		if (guozhanPlayer == null || guozhanPlayer.getNation() == 0) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode, 130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 是否已经通关
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() < citySize) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode,1006);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//还未生成
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if(guoZhanFight == null) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode,130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		DBGuoZhanCity guoZhanCity = guoZhanFight.getCitys(city_index);
		if(guoZhanCity == null) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode,130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//我的信息
		DBGuoZhanRole guoZhanRole = guoZhanFight.getPlayersOrDefault(playerId, null);
		if(guoZhanRole == null) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode,130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//
		int oldCityIndex = guoZhanRole.getCityIndex();
		if(oldCityIndex == city_index) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode,1011);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//能否移动到
		int moveSteps = GuoZhanFightManager.getInstance().checkMove2City(oldCityIndex,city_index,guozhanPlayer.getNation());
		if(moveSteps <= 0) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode,1012);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//步数是否够
		int targetNationId = guoZhanCity.getOwnNationId();
		int costSteps = moveSteps;
		if(targetNationId != guozhanPlayer.getNation()) {
			costSteps += 2;
		}
		logger.info("move cost step:{}",costSteps);
		int mySteps = GuoZhanFightManager.getInstance().getMyMoveStep(guoZhanRole);
		if(mySteps < costSteps) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode,1013);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//是否有敌方驻守
		if (targetNationId != guozhanPlayer.getNation() && guoZhanCity.getOccupyPlayerCount() > 0) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuoZhanCityMove.msgCode, 1014);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 每日任务进度
//		DailyMissionManager.getInstance().gzChangeProgress(playingRole);
		// do
		GuoZhanFightManager.getInstance().processorCityMove(playingRole, playerId, city_index, targetNationId, mySteps,
				costSteps, oldCityIndex, guoZhanCity, guoZhanRole, guozhanPlayer);
	}

}
