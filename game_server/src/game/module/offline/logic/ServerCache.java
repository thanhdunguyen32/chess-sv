package game.module.offline.logic;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import game.module.chat.bean.DbChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.EvictingQueue;

import ws.WsMessageChat.S2CChatPush;
import ws.WsMessageChat.S2CChatView;

public class ServerCache {

	private static Logger logger = LoggerFactory.getLogger(ServerCache.class);

	private EvictingQueue<S2CChatPush> chatPublic = EvictingQueue.create(50);
	
	private S2CChatPush chatPublicNewest;

	private Map<Integer, EvictingQueue<S2CChatPush>> chatPrivate = new ConcurrentHashMap<Integer, EvictingQueue<S2CChatPush>>();
	
	private Map<Integer, S2CChatPush> chatPrivateLatest = new ConcurrentHashMap<Integer, S2CChatPush>();

	private Map<Integer, EvictingQueue<S2CChatPush>> chatNation = new ConcurrentHashMap<Integer, EvictingQueue<S2CChatPush>>();
	
	private Map<Integer, S2CChatPush> chatNationLatest = new ConcurrentHashMap<Integer, S2CChatPush>();

	final ReadWriteLock publicLock = new ReentrantReadWriteLock();

	private Map<Integer, Date> activityLastUpdateTimeMap = new HashMap<Integer, Date>();

	private Map<Integer, Long> fengHaoPlayers = new ConcurrentHashMap<>();

	private Map<Integer, Long> jinYanPlayers = new ConcurrentHashMap<>();
	// Thêm vào ServerCache
	private Map<Long, EvictingQueue<S2CChatPush>> chatLegion = new ConcurrentHashMap<>();
	private Map<Long, S2CChatPush> chatLegionLatest = new ConcurrentHashMap<>();
	static class SingletonHolder {
		static ServerCache instance = new ServerCache();
	}

	public static ServerCache getInstance() {
		return SingletonHolder.instance;
	}

	public void addChatPublic(S2CChatPush msg) {
		publicLock.writeLock().lock();
		try {
			chatPublic.add(msg);
			chatPublicNewest = msg;
		} finally {
			publicLock.writeLock().unlock();
		}
	}
	
	public void removeChatPublic(int playerId) {
		publicLock.writeLock().lock();
		try {
			chatPublic.removeIf(s -> s.rid == playerId);
		} finally {
			publicLock.writeLock().unlock();
		}
	}

	public void addChatPrivate(int playerId, S2CChatPush msg) {
		EvictingQueue<S2CChatPush> chatQueue = chatPrivate.get(playerId);
		if (chatQueue == null) {
			chatQueue = EvictingQueue.create(50);
			chatPrivate.put(playerId, chatQueue);
		}
		chatQueue.add(msg);
		chatPrivateLatest.put(playerId, msg);
	}

	public void addChatNation(int nationId, S2CChatPush msg) {
		EvictingQueue<S2CChatPush> chatQueue = chatNation.get(nationId);
		if (chatQueue == null) {
			chatQueue = EvictingQueue.create(50);
			chatNation.put(nationId, chatQueue);
		}
		chatQueue.add(msg);
		chatNationLatest.put(nationId, msg);
	}

	public Collection<S2CChatPush> getChatNation(int guildId) {
		EvictingQueue<S2CChatPush> chatQueue = chatNation.get(guildId);
		return chatQueue;
	}

	public Collection<S2CChatPush> getChatPrivate(int playerId) {
		EvictingQueue<S2CChatPush> chatQueue = chatPrivate.get(playerId);
		return chatQueue;
	}
	
	public S2CChatView buildChatPublic() throws IOException {
		publicLock.readLock().lock();
		try {
			S2CChatView retMsg = new S2CChatView();
			retMsg.chat_content = new ArrayList<>(chatPublic);
			return retMsg;
		} finally {
			publicLock.readLock().unlock();
		}
	}

	public Date getActivityLastUpdateTime(Integer activityId) {
		return activityLastUpdateTimeMap.get(activityId);
	}

	public void setActivityLastUpdateTime(Integer activityId, Date activityLastUpdateTime) {
		activityLastUpdateTimeMap.put(activityId, activityLastUpdateTime);
	}

	public void addFengHao(Integer playerId, Long endTime) {
		fengHaoPlayers.put(playerId, endTime);
	}

	public void removeFengHao(Integer playerId) {
		fengHaoPlayers.remove(playerId);
	}

	public boolean isFengHao(Integer playerId) {
		boolean ret = false;
		Long endTime = fengHaoPlayers.get(playerId);
		if (endTime != null) {
			ret = endTime > System.currentTimeMillis();
		}
		return ret;
	}

	public void addJinYan(int playerId, long endTime) {
		jinYanPlayers.put(playerId, endTime);
	}

	public void cancelJinYan(int playerId) {
		jinYanPlayers.remove(playerId);
	}

	public boolean isJinYan(int playerId) {
		boolean ret = false;
		Long endTime = jinYanPlayers.get(playerId);
		if (endTime != null) {
			ret = endTime > System.currentTimeMillis();
		}
		return ret;
	}

	public Map<Integer, Long> getFenghaoAll() {
		return fengHaoPlayers;
	}

	public long getChatPublicLatest() {
		if(chatPublicNewest != null) {
			return chatPublicNewest.send_time;
		}else {
			return 0;
		}
	}

	public void initPublicChat(List<DbChat.DbChatItem> list) {
		for (DbChat.DbChatItem dbChatItem : list) {
			S2CChatPush msg = new S2CChatPush(dbChatItem.getMsgtype(), dbChatItem.getSenderid(), dbChatItem.getRid(), dbChatItem.getRname(),
					dbChatItem.getIconid(), dbChatItem.getHeadid(), dbChatItem.getFrameid(),
					dbChatItem.getLevel(), dbChatItem.getVip(),0,dbChatItem.getServerid(), dbChatItem.getContent(),
					dbChatItem.getVideoId(), dbChatItem.getSend_time(), dbChatItem.getId());
			chatPublic.add(msg);
			chatPublicNewest = msg;
		}
	}
	
	public Collection<S2CChatPush> getChatPublicAll() {
		return chatPublic;
	}

	public Map<Integer, EvictingQueue<S2CChatPush>> getChatNationAll() {
		return chatNation;
	}
	
	public Map<Integer, EvictingQueue<S2CChatPush>> getChatPrivateAll() {
		return chatPrivate;
	}

	public Long getChatPrivateLatest(int playerId) {
		S2CChatPush chatPrivateMsg = chatPrivateLatest.get(playerId);
		if (chatPrivateMsg != null) {
			return chatPrivateMsg.send_time;
		}
		return null;
	}

	public void clearNationChat() {
		for (EvictingQueue<S2CChatPush> chatlist : chatNation.values()) {
			chatlist.clear();
		}
	}

	public Long getChatNationLatest(int nationId) {
		S2CChatPush chatPrivateMsg = chatNationLatest.get(nationId);
		if (chatPrivateMsg != null) {
			return chatPrivateMsg.send_time;
		}
		return null;
	}
	
	// Nếu cần khởi tạo chatQuânĐoàn từ DB (y như initLegionChat cũ):
	public void initLegionChat(Map<Long, DbChat.DbChatLegion> legionChatMap) {
		// Thay vì map<Integer, DbChat.DbChatLegion> ta dùng map<Long,...>
		for(Map.Entry<Long, DbChat.DbChatLegion> e : legionChatMap.entrySet()) {
			long legionId = e.getKey();
			EvictingQueue<S2CChatPush> chatQueue = EvictingQueue.create(50);
			chatLegion.put(legionId, chatQueue);
			for (DbChat.DbChatItem dbItem : e.getValue().getChatItem()) {
				S2CChatPush msg = new S2CChatPush(
						dbItem.getMsgtype(),
						dbItem.getSenderid(), dbItem.getRid(), dbItem.getRname(),
						dbItem.getIconid(), dbItem.getHeadid(), dbItem.getFrameid(),
						dbItem.getLevel(), dbItem.getVip(), 0, dbItem.getServerid(),
						dbItem.getContent(), dbItem.getVideoId(), dbItem.getSend_time(),
						dbItem.getId()
				);
				chatQueue.add(msg);
				chatLegionLatest.put(legionId, msg);
			}
		}
	}
	public void addChatLegion(long legionId, S2CChatPush msg) {
		EvictingQueue<S2CChatPush> chatQueue = chatLegion.get(legionId);
		if (chatQueue == null) {
			chatQueue = EvictingQueue.create(50); // hoặc 100, tùy bạn
			chatLegion.put(legionId, chatQueue);
		}
		chatQueue.add(msg);
		chatLegionLatest.put(legionId, msg);
	}
	
	// Lấy danh sách tin nhắn của 1 quân đoàn
	public Collection<S2CChatPush> getChatLegion(long legionId) {
		EvictingQueue<S2CChatPush> chatQueue = chatLegion.get(legionId);
		return chatQueue; // có thể null => bạn return null
	}
	
	// Trả về map chatLegionAll nếu muốn
	public Map<Long, EvictingQueue<S2CChatPush>> getChatLegionAll() {
		return chatLegion;
	}
	
	// Tương tự, nếu muốn lấy tin nhắn mới nhất
	public Long getChatLegionLatest(long legionId) {
		S2CChatPush lastMsg = chatLegionLatest.get(legionId);
		return (lastMsg != null) ? lastMsg.send_time : null;
	}
}
