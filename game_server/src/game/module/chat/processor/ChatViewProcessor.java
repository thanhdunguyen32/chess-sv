package game.module.chat.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chat.logic.ChatManager;
import game.module.guozhan.logic.GuoZhanManager;
import game.module.legion.logic.LegionManager;
import game.module.offline.logic.ServerCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageChat.C2SChatView;
import ws.WsMessageChat.S2CChatPush;
import ws.WsMessageChat.S2CChatView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@MsgCodeAnn(msgcode = C2SChatView.id, accessLimit = 100)
public class ChatViewProcessor extends PlayingRoleMsgProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(ChatProcessor.class);
	
	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	
	}
	
	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SChatView reqMsg = C2SChatView.parse(request);
		int pChatTypeVal = reqMsg.chat_type;
		int playerId = playingRole.getId();
		logger.info("chat view,playerId={},type={}", playerId, pChatTypeVal);
		ws.WsMessageChat.S2CChatView retMsg = null;
		switch (pChatTypeVal) {
			case 1:
				retMsg = ServerCache.getInstance().buildChatPublic();
				retMsg.chat_type = pChatTypeVal;
				retMsg.new_msg_list = Arrays.asList(false,false);
				retMsg.new_msg_list.set(0, ChatManager.getInstance().checkHasNewMsgPrivate(playerId));
				for(S2CChatPush chatmsg : retMsg.chat_content){
					if(chatmsg.office_index == 0) {
						chatmsg.office_index = GuoZhanManager.getInstance().getOfficeIndex(chatmsg.rid);
					}
				}
				break;
			case 2:
				//更新visit time
				ChatManager.getInstance().updatePlayerChatPrivateVisit(playerId, System.currentTimeMillis());
				//ret list
				Collection<S2CChatPush> chatList = ServerCache.getInstance().getChatPrivate(playerId);
				retMsg = new S2CChatView();
				retMsg.chat_type = pChatTypeVal;
				if (chatList != null) {
					retMsg.chat_content = new ArrayList<>(chatList);
				}
				retMsg.new_msg_list = Arrays.asList(false,false);
				break;
			case 3://军团
				long legionId = LegionManager.getInstance().getLegionId(playerId);
				Collection<S2CChatPush> chatListLegion = ServerCache.getInstance().getChatLegion(legionId);
				
				retMsg = new S2CChatView();
				retMsg.chat_type = pChatTypeVal;
				retMsg.new_msg_list = Arrays.asList(false,false);
				retMsg.new_msg_list.set(0, ChatManager.getInstance().checkHasNewMsgPrivate(playerId));
				if (chatListLegion != null) {
					retMsg.chat_content = new ArrayList<>(chatListLegion);
					for(S2CChatPush chatmsg : retMsg.chat_content){
						if(chatmsg.office_index == 0) {
							chatmsg.office_index = GuoZhanManager.getInstance().getOfficeIndex(chatmsg.rid);
						}
					}
					logger.info("chat view (2), playerId={}, legionId={}, size={}", playerId, legionId, chatListLegion.size());
				} else {
					retMsg.chat_content = new ArrayList<>();
				}
				break;
			default:
				break;
		}
		// update visit time
		ChatManager.getInstance().updatePlayerChatVisit(playerId, System.currentTimeMillis());
		if (retMsg == null) {
			retMsg = new S2CChatView();
			retMsg.chat_type = pChatTypeVal;
			retMsg.chat_content = new ArrayList<>();
		}
		
		playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
		
	}
	
}
