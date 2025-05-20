package game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.logic.BossBattleManager;
import cross.logic.CrossCraftManager;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.craft.logic.CraftLogic;
import game.module.craft.logic.CraftManager;
import game.module.craft.logic.CraftMatchManager;
import game.module.onlineAward.dao.OnlineAwardCache;
import game.module.onlineAward.logic.OnlineAwardManager;
import game.session.SessionManager;
import lion.netty4.core.BaseIoExecutor;
import lion.netty4.core.BaseProtoIoExecutor;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.INetty4EventHandler;

public class CrossSessionManager implements INetty4EventHandler {

	public static final Logger logger = LoggerFactory.getLogger(CrossSessionManager.class);

	static class SingletonHolder {
		static CrossSessionManager instance = new CrossSessionManager();
	}

	public static CrossSessionManager getInstance() {
		return SingletonHolder.instance;
	}

	private CrossSessionManager() {
		BaseIoExecutor.handlerSet.add(this);
		BaseProtoIoExecutor.handlerSet.add(this);
	}

	@Override
	public void channelInactive(GamePlayer gamePlayer) {
		logger.info("craft channel inactive,addr={}", gamePlayer.getAddress());
		Long sessionId = gamePlayer.getSessionId();
		if (sessionId == null) {
			return;
		}
		// removeLogicHero(sessionId);
		PlayingRole playingRole = SessionManager.getInstance().getPlayer(sessionId);
		if (playingRole != null) {
			// add to offline cache
			logger.info("channel inactive,sessionId={}", sessionId);
			// 下线,记录玩家在线时间(在线奖励模块)
			OnlineAwardManager.getInstance()
					.calcOnlineTime(OnlineAwardCache.getInstance().getOnlineAward(playingRole.getId()));
			// 掉线处理
			if (playingRole.getPlayerCacheStatus().getPosition() == PlayerPosition.PLAYER_POSITION_PVP_TEAM) {
				// PvpTeamManager.getInstance().downline(playingRole);
			} else if (playingRole.getPlayerCacheStatus()
					.getPosition() == PlayerPosition.PLAYER_POSITION_PVP_MATCHING) {
				// PvpMatchManager.getInstance().downline(playingRole);
			} else if (playingRole.getPlayerCacheStatus().getPosition() == PlayerPosition.PLAYER_POSITION_PVP_ROOM) {
				// PvpRoomManager.getInstance().downline(playingRole);
			} else if (playingRole.getPlayerCacheStatus()
					.getPosition() == PlayerPosition.PLAYER_POSITION_CRAFT_MATCHING) {
				CraftMatchManager.getInstance().downline(playingRole);
//				SessionManager.getInstance().remove(sessionId);
			} else if (playingRole.getPlayerCacheStatus().getPosition() == PlayerPosition.PLAYER_POSITION_CRAFT_ROOM) {
				//CraftManager.getInstance().downline(playingRole);
				// 发送下线消息
				CrossCraftManager.getInstance().downlineRoom(playingRole);
			} else if (playingRole.getPlayerCacheStatus()
					.getPosition() == PlayerPosition.PLAYER_POSITION_CRAFT_LOADING) {
				CrossCraftManager.getInstance().downlineLoading(playingRole);
			} else if (playingRole.getPlayerCacheStatus()
					.getPosition() == PlayerPosition.PLAYER_POSITION_CRAFT_BATTLE) {
				CraftManager.getInstance().downlineBattle(playingRole);
			} else if (playingRole.getPlayerCacheStatus()
					.getPosition() == PlayerPosition.PLAYER_POSITION_CROSS_BOSS_LOADING) {
//				SessionManager.getInstance().remove(sessionId);
			} else if (playingRole.getPlayerCacheStatus()
					.getPosition() == PlayerPosition.PLAYER_POSITION_CROSS_BOSS_BATTLE) {
				BossBattleManager.getInstance().downlineBattle(playingRole);
//				SessionManager.getInstance().remove(sessionId);
			}
			CraftLogic.getInstance().downline(playingRole.getId());
			// data
			playingRole.setGamePlayer(null);
		}
	}

	@Override
	public void readTimeout(GamePlayer gamePlayer) {
		logger.info("read timeout,close channel!addr={}", gamePlayer.getAddress());
		gamePlayer.close();
	}

}
