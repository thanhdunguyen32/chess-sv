package game.module.chat.logic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import game.module.chat.bean.DbChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.EvictingQueue;

import game.module.chat.dao.ChatDao;
import game.module.offline.logic.ServerCache;
import game.module.rank.dao.RankDao;
import ws.WsMessageChat.S2CChatPush;

public class ChatManager {

	private static Logger logger = LoggerFactory.getLogger(ChatManager.class);

	static class SingletonHolder {
		static ChatManager instance = new ChatManager();
	}

	public static ChatManager getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Integer, Long> playerChatVisitCache = new ConcurrentHashMap<>();
	
	private Map<Integer, Long> playerChatPrivateVisitCache = new ConcurrentHashMap<>();
	
	private Map<Integer, Long> playerChatNationVisitCache = new ConcurrentHashMap<>();

	public void loadFromDb() {
		logger.info("chat visit info loadFromDb!");
		DbChat dbChatVisit = ChatDao.getInstance().getDBChatVisit();
		if (dbChatVisit == null) {
			dbChatVisit = new DbChat();
			if (!RankDao.getInstance().isSystemBlobExist()) {
				ChatDao.getInstance().insertDBChatVisit(dbChatVisit);
			}
		} else {
			playerChatVisitCache.putAll(dbChatVisit.getPublicVisit());
			playerChatNationVisitCache.putAll(dbChatVisit.getLegionVisit());
			// 初始化公众聊天内容
			List<DbChat.DbChatItem> list = dbChatVisit.getChatPublic();
			if(list != null) {
				ServerCache.getInstance().initPublicChat(list);
			}
			//国家聊天
			Map<Long, DbChat.DbChatLegion> chatNationList = dbChatVisit.getChatLegion();
			ServerCache.getInstance().initLegionChat(chatNationList);
		}
	}

	public void saveToDb() {
		DbChat dbChat = new DbChat();
		dbChat.setPublicVisit(new HashMap<>());
		dbChat.getPublicVisit().putAll(playerChatVisitCache);
		dbChat.setLegionVisit(new HashMap<>());
		dbChat.getLegionVisit().putAll(playerChatNationVisitCache);
		//public
		Collection<S2CChatPush> chatPublicAll = ServerCache.getInstance().getChatPublicAll();
		for (S2CChatPush s2cChatPush : chatPublicAll) {
			DbChat.DbChatItem chatPublic1 = new DbChat.DbChatItem();
			chatPublic1.setMsgtype(s2cChatPush.msgtype);
			chatPublic1.setSenderid(s2cChatPush.senderid);
			chatPublic1.setRid(s2cChatPush.rid);
			chatPublic1.setRname(s2cChatPush.rname);
			chatPublic1.setIconid(s2cChatPush.iconid);
			chatPublic1.setHeadid(s2cChatPush.headid);
			chatPublic1.setFrameid(s2cChatPush.frameid);
			chatPublic1.setLevel(s2cChatPush.level);
			chatPublic1.setVip(s2cChatPush.vip);
			chatPublic1.setServerid(s2cChatPush.serverid);
			chatPublic1.setContent(s2cChatPush.content);
			chatPublic1.setVideoId(s2cChatPush.videoId);
			chatPublic1.setSend_time(s2cChatPush.send_time);
			chatPublic1.setId(s2cChatPush.id);
			if(dbChat.getChatPublic() == null){
				List<DbChat.DbChatItem> chatPublics = new ArrayList<>();
				dbChat.setChatPublic(chatPublics);
			}
			dbChat.getChatPublic().add(chatPublic1);
		}
		// Tạo map legionChat
		Map<Long, DbChat.DbChatLegion> chatLegions = new HashMap<>();
		dbChat.setChatLegion(chatLegions);
		
		// Lấy hết chatLegionAll
		Map<Long, EvictingQueue<S2CChatPush>> chatLegionAll = ServerCache.getInstance().getChatLegionAll();
		
		
		// Duyệt từng legionId, EvictingQueue
		for (Map.Entry<Long, EvictingQueue<S2CChatPush>> entry : chatLegionAll.entrySet()) {
			long legionId = entry.getKey();
			EvictingQueue<S2CChatPush> queue = entry.getValue();
			
			// Tạo 1 DbChatLegion
			DbChat.DbChatLegion dbLegion = new DbChat.DbChatLegion();
			dbLegion.setChatItem(new ArrayList<>());
			
			for (S2CChatPush pushMsg : queue) {
				// Convert S2CChatPush -> DbChatItem
				DbChat.DbChatItem dbChatItem = new DbChat.DbChatItem();
				dbChatItem.setMsgtype(pushMsg.msgtype);
				dbChatItem.setSenderid(pushMsg.senderid);
				dbChatItem.setRid(pushMsg.rid);
				dbChatItem.setRname(pushMsg.rname);
				dbChatItem.setIconid(pushMsg.iconid);
				dbChatItem.setHeadid(pushMsg.headid);
				dbChatItem.setFrameid(pushMsg.frameid);
				dbChatItem.setLevel(pushMsg.level);
				dbChatItem.setVip(pushMsg.vip);
				dbChatItem.setServerid(pushMsg.serverid);
				dbChatItem.setContent(pushMsg.content);
				dbChatItem.setVideoId(pushMsg.videoId);
				dbChatItem.setSend_time(pushMsg.send_time);
				dbChatItem.setId(pushMsg.id);
				
				dbLegion.getChatItem().add(dbChatItem);
			}
			
			// Đưa vào map
			chatLegions.put(legionId, dbLegion);
		}
		
		// Cuối cùng update DB
		ChatDao.getInstance().updateDBChatVisit(dbChat);
	}
	
	public Long getPlayerChatVisit(int playerId) {
		return playerChatVisitCache.get(playerId);
	}
	
	public void updatePlayerChatVisit(int playerId, long currentTimeMili) {
		playerChatVisitCache.put(playerId, currentTimeMili);
	}
	
	public Long getPlayerChatPrivateVisit(int playerId) {
		return playerChatPrivateVisitCache.get(playerId);
	}
	
	public void updatePlayerChatPrivateVisit(int playerId, long currentTimeMili) {
		playerChatPrivateVisitCache.put(playerId, currentTimeMili);
	}

	public Long getPlayerChatNationVisit(int playerId) {
		return playerChatNationVisitCache.get(playerId);
	}

	public void updatePlayerChatNationVisit(int playerId, long currentTimeMili) {
		playerChatNationVisitCache.put(playerId, currentTimeMili);
	}

	public boolean checkHasNewMsg(int playerId) {
		Long lastChatVisitTimeMili = playerChatVisitCache.get(playerId);
		if (lastChatVisitTimeMili == null) {
			return true;
		} else {
			long lastestMsgTimeMili = ServerCache.getInstance().getChatPublicLatest();
			if (lastChatVisitTimeMili < lastestMsgTimeMili) {
				return true;
			}
		}
		//私聊
		Long chatPrivateLastVisit = playerChatPrivateVisitCache.get(playerId);
		if(chatPrivateLastVisit != null) {
			Long lastestMsgTimeMili = ServerCache.getInstance().getChatPrivateLatest(playerId);
			if (lastestMsgTimeMili != null && lastestMsgTimeMili > chatPrivateLastVisit) {
				return true;
			}
		}else {
			Long lastestMsgTimeMili = ServerCache.getInstance().getChatPrivateLatest(playerId);
			if (lastestMsgTimeMili != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkHasNewMsgPrivate(int playerId) {
		//私聊
		Long chatPrivateLastVisit = playerChatPrivateVisitCache.get(playerId);
		if(chatPrivateLastVisit != null) {
			Long lastestMsgTimeMili = ServerCache.getInstance().getChatPrivateLatest(playerId);
			if (lastestMsgTimeMili != null && lastestMsgTimeMili > chatPrivateLastVisit) {
				return true;
			}
		}else {
			Long lastestMsgTimeMili = ServerCache.getInstance().getChatPrivateLatest(playerId);
			if (lastestMsgTimeMili != null) {
				return true;
			}
		}
		return false;
	}

}
