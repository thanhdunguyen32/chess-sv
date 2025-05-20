package game.module.chat.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chat.logic.ChatManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageChat;

@MsgCodeAnn(msgcode = WsMessageChat.C2SChatNewmsg.id, accessLimit = 200)
public class ChatNewmsgProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ChatNewmsgProcessor.class);

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
		logger.info("chat newmsg check,playerId={}", playerId);
		WsMessageChat.S2CChatNewmsg respmsg = new WsMessageChat.S2CChatNewmsg(false);
		boolean chatHasNewMsg = ChatManager.getInstance().checkHasNewMsg(playerId);
		if (chatHasNewMsg) {
			respmsg.has_newmsg = true;
		}
		playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
	}

}
