package game.module.chat.processor;

import game.common.CommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus;
import game.entity.PlayingRole;
import game.module.badword.logic.BadWordFilter;
import game.module.chat.logic.ChatManager;
import game.module.guozhan.logic.GuoZhanManager;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionCache;
import game.module.legion.logic.LegionManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.offline.logic.ServerCache;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageChat;
import ws.WsMessageChat.S2CChatPush;
import ws.WsMessageHall.S2CErrorCode;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
@MsgCodeAnn(msgcode = WsMessageChat.C2SChat.id, accessLimit = 400)
public class ChatProcessor extends PlayingRoleMsgProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(ChatProcessor.class);
	
	public static final int MAX_CHAT_CONTENT_LENGTH = 100;
	
	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	
	}
	
	private void chatPrivate(final PlayingRole playingRole, final int targetUid, final String content)
			throws IOException {
		final PlayingRole targetPlayingRole = SessionManager.getInstance().getPlayer(targetUid);
		// 聊天长度
		if (CommonUtils.getStrLength(content) > MAX_CHAT_CONTENT_LENGTH) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 904);
			playingRole.getGamePlayer().writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// bad word filter
		boolean hasBadWord = BadWordFilter.getInstance().hasBadWords(content);
		if (hasBadWord) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 903);
			playingRole.getGamePlayer().writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// send msg
		PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetUid);
		if (poc == null) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 906);
			playingRole.getGamePlayer().writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		String tatgetPlayerName = poc.getName();
		int officeIndex = 0;
		//		S2CChatPush chatPush = new S2CChatPush(playingRole.getId(),playingRole.getPlayerBean().getName(),1, officeIndex,
		//				playingRole.getPlayerBean().getLevel(), playingRole.getPlayerBean().getImage(), tatgetPlayerName,
		//				  playingRole.getPlayerBean().getVipLevel(),
		//				playingRole.getPlayerBean().getImage(), content, (int) (System.currentTimeMillis() / 1000));
		S2CChatPush chatPush = new S2CChatPush();
		if (targetPlayingRole != null) {
			targetPlayingRole.getGamePlayer().writeAndFlush(chatPush.build(targetPlayingRole.alloc()));
		}
		playingRole.getGamePlayer().writeAndFlush(chatPush.build(playingRole.alloc()));
		ServerCache.getInstance().addChatPrivate(targetUid, chatPush);
		ServerCache.getInstance().addChatPrivate(playingRole.getId(), chatPush);
		//更新visit time
		ChatManager.getInstance().updatePlayerChatPrivateVisit(playingRole.getId(), System.currentTimeMillis());
		// ret
		WsMessageChat.S2CChat respmsg = new WsMessageChat.S2CChat();
		playingRole.getGamePlayer().writeAndFlush(respmsg.build(playingRole.alloc()));
	}
	
	private void chatPublic(int msgtype, final PlayingRole playingRole, String content) {
		PlayerCacheStatus playerCacheStatus = playingRole.getPlayerCacheStatus();
		Long lastChatTime = playerCacheStatus.getLastChatTime();
		long now = System.currentTimeMillis();
		if (lastChatTime != null) {
			if (now - lastChatTime < 1000) {
				S2CErrorCode respmsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 901);
				playingRole.getGamePlayer().writeAndFlush(respmsg.build(playingRole.alloc()));
				return;
			}
		}
		// 聊天长度
		if (CommonUtils.getStrLength(content) > MAX_CHAT_CONTENT_LENGTH) {
			S2CErrorCode respmsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 904);
			playingRole.getGamePlayer().writeAndFlush(respmsg.build(playingRole.alloc()));
			return;
		}
		// bad word filter
		content = BadWordFilter.getInstance().filterBadWords(content);
		int officeIndex = GuoZhanManager.getInstance().getOfficeIndex(playingRole.getId());
		// 发送聊天信息
		S2CChatPush chatPush = new S2CChatPush(msgtype, "", playingRole.getPlayerBean().getId(), playingRole.getPlayerBean().getName(),
				playingRole.getPlayerBean().getIconid(), playingRole.getPlayerBean().getHeadid(), playingRole.getPlayerBean().getFrameid(),
				playingRole.getPlayerBean().getLevel(), playingRole.getPlayerBean().getVipLevel(), officeIndex, playingRole.getPlayerBean().getServerId(),
				content, 0, System.currentTimeMillis(), 0);
		for (PlayingRole aPlayingRole : SessionManager.getInstance().getAllPlayers()) {
			if(aPlayingRole.isChannelActive()) {
				aPlayingRole.writeAndFlush(chatPush.build(aPlayingRole.alloc()));
			}
		}
		ServerCache.getInstance().addChatPublic(chatPush);
		// msg
		WsMessageChat.S2CChat respmsg = new WsMessageChat.S2CChat();
		playingRole.getGamePlayer().writeAndFlush(respmsg.build(playingRole.alloc()));
		// cache
		playerCacheStatus.setLastChatTime(now);
	}
	
	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		WsMessageChat.C2SChat reqmsg = WsMessageChat.C2SChat.parse(request);
		int msgtype = reqmsg.msgtype;
		String content = reqmsg.content;
		int playerId = playingRole.getId();
		logger.info("chat msg,player={},type={},content={}", playerId, msgtype, content);
		// 判断是否禁言
		boolean isJinYan = ServerCache.getInstance().isJinYan(playerId);
		if (isJinYan) {
			logger.warn("玩家被禁言,id={}", playerId);
			S2CErrorCode respmsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 903);
			playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
			return;
		}
		// 等级太低
		if (playingRole.getPlayerBean().getLevel() < 20) {
			S2CErrorCode retMsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 139);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		//VIP等级太低
		//		if(playingRole.getPlayerBean().getVipLevel() < 8) {
		//			S2CErrorCode retMsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 149);
		//			playingRole.getGamePlayer().writeAndFlush(retMsg.build(playingRole.alloc()));
		//			return;
		//		}
		switch (msgtype) {
			case 1:
				chatPublic(msgtype, playingRole, content);
				break;
			case 2:
				//			chatPrivate(playingRole, targetUid, content);
				break;
			case 3:
				chatLegion(playingRole, content);
				break;
			default:
				break;
		}
		// 更新
		ChatManager.getInstance().updatePlayerChatVisit(playingRole.getId(), System.currentTimeMillis());
	}
	
	private void chatLegion(PlayingRole playingRole, String content) {
		// 1) Kiểm tra người chơi có quân đoàn chưa
		long legionId = LegionManager.getInstance().getLegionId(playingRole.getId());
		if (legionId <= 0) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 148);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 2) Kiểm tra độ dài, bad word filter
		if (CommonUtils.getStrLength(content) > MAX_CHAT_CONTENT_LENGTH) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageChat.S2CChat.msgCode, 904);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		content = BadWordFilter.getInstance().filterBadWords(content);
		// 3) Tạo chatPush
		int playerId = playingRole.getId();
		int officeIndex = GuoZhanManager.getInstance().getOfficeIndex(playerId);
		S2CChatPush chatPush = new S2CChatPush(
				3,            // msgtype=3
				"",
				playerId,     // rid
				playingRole.getPlayerBean().getName(), // rname
				playingRole.getPlayerBean().getIconid(),
				playingRole.getPlayerBean().getHeadid(),
				playingRole.getPlayerBean().getFrameid(),
				playingRole.getPlayerBean().getLevel(),
				playingRole.getPlayerBean().getVipLevel(),
				officeIndex,
				playingRole.getPlayerBean().getServerId(),
				content,
				0,
				System.currentTimeMillis(),
				0
		);
		// 4) Gửi tin nhắn đến tất cả thành viên trong quân đoàn
		//    => Lấy danh sách member: legionBean.getDbLegionPlayers().getMembers().keySet()
		LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
		if (legionBean == null) {
			return;
		}
		Set<Integer> members = legionBean.getDbLegionPlayers().getMembers().keySet();
		for (Integer memberId : members) {
			PlayingRole memberPr = SessionManager.getInstance().getPlayer(memberId);
			if (memberPr != null && memberPr.isChannelActive()) {
				memberPr.writeAndFlush(chatPush.build(memberPr.alloc()));
			}
		}
		ServerCache.getInstance().addChatLegion(legionId, chatPush);
		WsMessageChat.S2CChat respmsg = new WsMessageChat.S2CChat();
		playingRole.getGamePlayer().writeAndFlush(respmsg.build(playingRole.alloc()));
	}
	
}
