package game.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.entity.PlayingRole;
import game.session.SessionManager;
import lion.common.AuthenticateFailResponse;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import ws.WsMessageHall;

public abstract class PlayingRoleMsgProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(PlayingRoleMsgProcessor.class);

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
		Long sessionId = session.getSessionId();
		if (sessionId == null) {
			return;
		}
		// 更新最新的session
		PlayingRole savedHero = SessionManager.getInstance().visit(sessionId);
		// 检查session是否过期
		if (savedHero == null) {
			session.sendMsg(new AuthenticateFailResponse(session.alloc()));
			return;
		}
		processByte(savedHero, request);
	}

	@Override
	public void process(GamePlayer session, RequestProtoMessage request) throws Exception {
		Long sessionId = session.getSessionId();
		if (sessionId == null) {
			logger.warn("sessionid is null!gamePlayer={}", session);
			session.writeAndFlush(GameConstants.ERROR_CODE_MSG, 1);
			return;
		}
		// 更新最新的session
		PlayingRole savedHero = SessionManager.getInstance().visit(sessionId);
		// 检查session是否过期
		if (savedHero == null) {
			logger.info("playingRole not exist!sessionId={},gamePlayer={}", sessionId, session);
			session.writeAndFlush(GameConstants.ERROR_CODE_MSG, 2);
			return;
		}
		processProto(savedHero, request);
	}

	public abstract void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception;

	public abstract void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception;

	public abstract void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception;

	@Override
	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
		Long sessionId = session.getSessionId();
		if (sessionId == null) {
			logger.warn("sessionid is null!gamePlayer={}", session);
			WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(request.getMsgCode()+1, 106);
			session.writeAndFlush(respMsg.build(session.alloc()));
			return;
		}
		// 更新最新的session
		PlayingRole savedHero = SessionManager.getInstance().visit(sessionId);
		// 检查session是否过期
		if (savedHero == null) {
			logger.info("playingRole not exist!sessionId={},gamePlayer={}", sessionId, session);
			WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(request.getMsgCode()+1, 106);
			session.writeAndFlush(respMsg.build(session.alloc()));
			return;
		}
		processMy(savedHero, request);
	}

}
