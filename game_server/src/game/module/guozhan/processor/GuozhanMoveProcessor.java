package game.module.guozhan.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuozhanDaoHelper;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageGuozhan.C2SGuozhanMove;
import ws.WsMessageGuozhan.S2CGuozhanMove;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SGuozhanMove.id, accessLimit = 500)
public class GuozhanMoveProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanMoveProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SGuozhanMove reqMsg = C2SGuozhanMove.parse(request);
		int playerId = playingRole.getId();
		int city_index = reqMsg.city_index;
		logger.info("guo zhan move!playerId={},city_index={}", playerId, city_index);
		//
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		if (guozhanPlayer == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanMove.msgCode, 130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 能否打
		if (guozhanPlayer != null && guozhanPlayer.getStay_city_index() == city_index) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanMove.msgCode,1004);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 是否已经通关
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& !guozhanPlayer.getDbGuozhanPlayer().containsPassCityIndex(city_index)) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanMove.msgCode,1005);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// do
		guozhanPlayer.setStay_city_index(city_index);
		GuozhanDaoHelper.asyncUpdateGuozhanPlayer(guozhanPlayer);
		// ret
		S2CGuozhanMove respMsg = new S2CGuozhanMove(city_index);
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
