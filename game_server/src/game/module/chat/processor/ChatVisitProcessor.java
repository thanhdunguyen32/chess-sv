package game.module.chat.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chat.logic.ChatManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageChat.C2SChatVisit;
import ws.WsMessageChat.S2CChatVisit;

@MsgCodeAnn(msgcode = C2SChatVisit.id, accessLimit = 400)
public class ChatVisitProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ChatVisitProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		logger.info("chat visit,playerId={}", playerId);
		ChatManager.getInstance().updatePlayerChatVisit(playerId, System.currentTimeMillis());
		S2CChatVisit respMsg = new S2CChatVisit();
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
