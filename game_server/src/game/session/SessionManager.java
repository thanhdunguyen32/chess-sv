package game.session;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.entity.PlayingRole;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lion.common.IdWorker;
import lion.netty4.core.BaseIoExecutor;
import lion.netty4.core.BaseProtoIoExecutor;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.INetty4EventHandler;
import lion.session.GlobalTimer;

/**
 * 游戏连接session管理器，线程安全
 * 
 * @author hexuhui
 * 
 */
public class SessionManager implements INetty4EventHandler {

	public static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

	private GlobalTimer globalTimer = GlobalTimer.getInstance();

	private Map<Long, MapValueHolder> sessionTimeoutMap = new ConcurrentHashMap<Long, MapValueHolder>();

	private Map<Integer, MapValueHolder> sessionTimeoutMapPlayerId = new ConcurrentHashMap<Integer, MapValueHolder>();

	private volatile Map<Integer, PlayingRole> onlineCharacterMap = new ConcurrentHashMap<Integer, PlayingRole>();

	private IdWorker uuIdGenerator;

	public static final int SESSION_TIMEOUT_DELAY = 1500;// 25分钟之后超时

	static class SingletonHolder {
		static SessionManager instance = new SessionManager();
	}

	public static SessionManager getInstance() {
		return SingletonHolder.instance;
	}

	private SessionManager() {
		uuIdGenerator = new IdWorker(1);
		BaseIoExecutor.handlerSet.add(this);
		BaseProtoIoExecutor.handlerSet.add(this);
	}

	public void create(PlayingRole player,Long sessionId) {
		Timeout curTimeout = globalTimer.newTimeout(timeout -> {
			// session过期，断开连接
			logger.info("create session timeout,remove player:sid={}", sessionId);
			MapValueHolder oldValueHolder = sessionTimeoutMap.get(sessionId);
			PlayingRole player1 = oldValueHolder.getPlayer();
			if (player1.getGamePlayer() != null) {
				// save to db
				PlayerDownlineManager.getInstance().savePlayerData(player1);
				player1.getGamePlayer().saveSessionId(null);
				player1.getGamePlayer().close();
			}
			removeLogicPlayer(sessionId, true);
		}, SESSION_TIMEOUT_DELAY);
		MapValueHolder mapValueHolder = new MapValueHolder(curTimeout, player);
		sessionTimeoutMap.put(sessionId, mapValueHolder);
		sessionTimeoutMapPlayerId.put(player.getId(), mapValueHolder);
		onlineCharacterMap.put(player.getId(), player);
		//
		player.setSessionId(sessionId);
		//
		PlayerOnlineCacheMng.getInstance().online(player);
	}

	public PlayingRole visit(final Long sessionId) {
		// 已经被移除
		if (!sessionTimeoutMap.containsKey(sessionId)) {
			return null;
		}
		MapValueHolder mapValueHolder = sessionTimeoutMap.get(sessionId);
		Timeout curTimeout = mapValueHolder.getWheelTimeout();
		curTimeout.cancel();
		// 重新加入定时器
		curTimeout = globalTimer.newTimeout(timeout -> {
			// session过期，断开连接
			logger.info("visit session timeout,remove player:sid={}", sessionId);
			MapValueHolder oldValueHolder = sessionTimeoutMap.get(sessionId);
			PlayingRole player = oldValueHolder.getPlayer();
			if (player.getGamePlayer() != null) {
				// save to db
				PlayerDownlineManager.getInstance().savePlayerData(player);
				player.getGamePlayer().saveSessionId(null);
				player.getGamePlayer().close();
			}
			removeLogicPlayer(sessionId, true);
		}, SESSION_TIMEOUT_DELAY);
		mapValueHolder.setWheelTimeout(curTimeout);
		return mapValueHolder.getPlayer();
	}

	public void removeSessionTimeout(Long sessionId) {
		if (sessionId == null) {
			return;
		}
		// 已经被移除
		if (!sessionTimeoutMap.containsKey(sessionId)) {
			return;
		}
		MapValueHolder mapValueHolder = sessionTimeoutMap.get(sessionId);
		Timeout curTimeout = mapValueHolder.getWheelTimeout();
		curTimeout.cancel();
		sessionTimeoutMap.remove(sessionId);
		PlayingRole playingRole = mapValueHolder.getPlayer();
		if (playingRole != null && playingRole.getPlayerBean() != null) {
			sessionTimeoutMapPlayerId.remove(playingRole.getPlayerBean().getId());
		}
	}

	public void removeSessionTimeout(int playerId) {
		if (!sessionTimeoutMapPlayerId.containsKey(playerId)) {
			return;
		}
		logger.info("remove old player cache,id={}", playerId);
		MapValueHolder mapValueHolder = sessionTimeoutMapPlayerId.get(playerId);
		Timeout curTimeout = mapValueHolder.getWheelTimeout();
		curTimeout.cancel();
		PlayingRole playingRole = mapValueHolder.getPlayer();
		if (playingRole != null && playingRole.getSessionId() != null) {
			sessionTimeoutMap.remove(playingRole.getSessionId());
			sessionTimeoutMapPlayerId.remove(playerId);
		}
	}

	public PlayingRole getPlayer(Long sessionId) {
		MapValueHolder mapValueHolder = sessionTimeoutMap.get(sessionId);
		if (mapValueHolder == null) {
			return null;
		}
		return mapValueHolder.getPlayer();
	}

	public PlayingRole getPlayer(int playerId) {
		return onlineCharacterMap.get(playerId);
	}

	public PlayingRole getPlayer(String name) {
		for (PlayingRole playingRole : getAllPlayers()) {
			if (playingRole.getPlayerBean().getName().equals(name)) {
				return playingRole;
			}
		}
		return null;
	}

	public int getOnlineCount() {
		return onlineCharacterMap.size();
	}

	public Collection<PlayingRole> getAllPlayers() {
		return onlineCharacterMap.values();
	}

	/**
	 * 检查session是否有效
	 * 
	 * @param sessionId
	 * @return
	 */
	public boolean checkSessionValid(String sessionId) {
		boolean ret = false;
		if (sessionTimeoutMap.containsKey(Long.valueOf(sessionId))) {
			ret = true;
		}
		return ret;
	}

	public int getSessionCount() {
		return sessionTimeoutMap.size();
	}

	/**
	 * 生成session id
	 * 
	 * @return
	 */
	public Long generateSessionId() {
		return uuIdGenerator.nextId();
	}

	@Override
	public void channelInactive(GamePlayer gamePlayer) {
		logger.info("channel inactive,addr={}", gamePlayer.getAddress());
		Long sessionId = gamePlayer.getSessionId();
		if (sessionId == null) {
			return;
		}
		// removeLogicHero(sessionId);
		PlayingRole playingRole = getPlayer(sessionId);
		if (playingRole != null) {
			// add to offline cache
			logger.info("channel inactive,sessionId={}", sessionId);
			// save to db
			PlayerDownlineManager.getInstance().savePlayerData(playingRole);
			// chat cache
//			int guildId = GuildCache.getInstance().getGuildId(playingRole.getId());
//			NationChatCache.getInstance().removePlayer(guildId, playingRole);
			// 下线,记录玩家在线时间(在线奖励模块)
//			OnlineAwardManager.getInstance()
//					.calcOnlineTime(OnlineAwardCache.getInstance().getOnlineAward(playingRole.getId()));
			// 掉线处理
			// data
			playingRole.setGamePlayer(null);
			onlineCharacterMap.remove(playingRole.getId());
		}
	}

	public void online(PlayingRole playingRole) {
		onlineCharacterMap.put(playingRole.getId(), playingRole);
	}

	public void removeLogicPlayer(Long sessionId, boolean removeCacheData) {
		PlayingRole aPlayer = getPlayer(sessionId);
		if (aPlayer != null) {
			// 战斗掉线处理
			logger.info("remove player session,playerId={}", aPlayer.getId());
			onlineCharacterMap.remove(aPlayer.getId());
			// 下线逻辑
			if(removeCacheData) {
				PlayerDownlineManager.getInstance().removeCache(aPlayer.getId());
			}
			//
			PlayerOnlineCacheMng.getInstance().offline(aPlayer.getPlayerBean());
		}
		removeSessionTimeout(sessionId);
	}

	@Override
	public void readTimeout(GamePlayer gamePlayer) {
		logger.info("read timeout,close socket!,addr={}", gamePlayer.getAddress());
		gamePlayer.close();
	}

}

class MapValueHolder {
	private Timeout wheelTimeout;
	private PlayingRole player;

	public MapValueHolder(Timeout wheelTimeout, PlayingRole player) {
		super();
		this.setWheelTimeout(wheelTimeout);
		this.setPlayer(player);
	}

	public Timeout getWheelTimeout() {
		return wheelTimeout;
	}

	public void setWheelTimeout(Timeout wheelTimeout) {
		this.wheelTimeout = wheelTimeout;
	}

	public PlayingRole getPlayer() {
		return player;
	}

	public void setPlayer(PlayingRole player) {
		this.player = player;
	}
}
