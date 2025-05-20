package game.session;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.activity.dao.*;
import game.module.affair.dao.AffairCache;
import game.module.bigbattle.dao.MonthBossCache;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.ChapterCache;
import game.module.draw.dao.PubDrawCache;
import game.module.dungeon.dao.DungeonCache;
import game.module.exped.dao.ExpedCache;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendExploreCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralExchangeCache;
import game.module.item.dao.ItemCache;
import game.module.log.dao.LogItemGoCache;
import game.module.log.logic.LogItemGoManager;
import game.module.mail.dao.MailCache;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.SurrenderPersuadeCache;
import game.module.mapevent.dao.MapEventCache;
import game.module.mission.dao.MissionDailyCache;
import game.module.mythical.dao.MythicalAnimalCache;
import game.module.occtask.dao.OccTaskCache;
import game.module.pay.dao.ChargeCache;
import game.module.pay.dao.LibaoBuyCache;
import game.module.pay.logic.BuyCostCache;
import game.module.pvp.dao.PvpRecordCache;
import game.module.shop.dao.ShopCache;
import game.module.sign.dao.SignInCache;
import game.module.spin.dao.SpinCache;
import game.module.user.dao.*;
import game.module.vip.dao.VipCountCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

public class PlayerDownlineManager {

	public static final Logger logger = LoggerFactory.getLogger(PlayerDownlineManager.class);

	static class SingletonHolder {
		static PlayerDownlineManager instance = new PlayerDownlineManager();
	}

	public static PlayerDownlineManager getInstance() {
		return SingletonHolder.instance;
	}

	public void removeCache(int playerId) {
		logger.info("stage status reset!,id={}", playerId);
		// 关卡中处理
//		if (playingRole.getPlayerCacheStatus().getPosition() == PlayerPosition.PLAYER_POSITION_STAGE
//				|| playingRole.getPlayerCacheStatus().getPosition() == PlayerPosition.PLAYER_POSITION_TOWER) {
//			StagePveManager.getInstance().downlineStat(playingRole);
//		} else if (playingRole.getPlayerCacheStatus().getPosition() == PlayerPosition.PLAYER_POSITION_BOSS) {
//			BossLogManager.getInstance().downlineBoss(playingRole);
//		}
		// playingRole.getPlayerCacheStatus().setPosition(PlayerPosition.PLAYER_POSITION_HALL);

		logger.info("remove player cache!,id={}", playerId);
		// 逻辑移除
		ActivityPlayerCache.getInstance().remove(playerId);
		ItemCache.getInstance().remove(playerId);
		MissionDailyCache.getInstance().remove(playerId);
		MailCache.getInstance().remove(playerId);
		SignInCache.getInstance().remove(playerId);
		GeneralCache.getInstance().remove(playerId);
		VipCountCache.getInstance().remove(playerId);
		// 删除缓存数据
		ChargeCache.getInstance().remove(playerId);
		BuyCostCache.getInstance().remove(playerId);
		// 删除日志缓存
		LogItemGoCache.getInstance().remove(playerId);
		// 删除日志监听器
		LogItemGoManager.getInstance().cancleLogScheduled(playerId);
		ActivityPlayerCache.getInstance().remove(playerId);
		FriendCache.getInstance().removeFriends(playerId);
		FriendCache.getInstance().removeFriendRequests(playerId);
		AffairCache.getInstance().removeAffairs(playerId);
		MythicalAnimalCache.getInstance().remove(playerId);
		ShopCache.getInstance().remove(playerId);
		ChapterCache.getInstance().remove(playerId);
		GoldBuyCache.getInstance().removeGoldBuy(playerId);
		PubDrawCache.getInstance().removePubDraw(playerId);
		SpinCache.getInstance().removeSpinBean(playerId);
		GeneralExchangeCache.getInstance().removeGeneralExchange(playerId);
		PlayerHeadCache.getInstance().removePlayerHead(playerId);
		PlayerHideCache.getInstance().removePlayerHidden(playerId);
		PlayerOtherCache.getInstance().removePlayerOther(playerId);
		BattleFormationCache.getInstance().removeBattleFormation(playerId);
		MonthBossCache.getInstance().removeMonthBoss(playerId);
		PlayerServerPropCache.getInstance().removePlayerServerProp(playerId);
		ExpedCache.getInstance().removeExped(playerId);
		ManorCache.getInstance().removeManor(playerId);
		FriendExploreCache.getInstance().removeFriendExplore(playerId);
		MapEventCache.getInstance().removeMapEvent(playerId);
		PvpRecordCache.getInstance().removePvpRecord(playerId);
		DungeonCache.getInstance().removeDungeon(playerId);
		SurrenderPersuadeCache.getInstance().removeSurrenderPersuade(playerId);
		ChargeCache.getInstance().remove(playerId);
		OccTaskCache.getInstance().removeOccTask(playerId);
		ActivityXiangouCache.getInstance().remove(playerId);
		LibaoBuyCache.getInstance().removeLibaoBuy(playerId);
		ActMjbgCache.getInstance().removeActMjbg(playerId);
		ActCxryCache.getInstance().removeActCxry(playerId);
		ActTnqwCache.getInstance().removeActTnqw(playerId);
		GuozhanCache.getInstance().remove(playerId);
	}

	public void savePlayerData(final PlayingRole playingRole) {
		logger.info("persist player data!,id={}", playingRole.getId());
		// saveHeroData(playingRole.getPlayerBean());
		if (playingRole.getLevelGoTimeout() != null) {
			playingRole.getLevelGoTimeout().cancel();
		}
		if (playingRole.getLoginTimeout() != null) {
			playingRole.getLoginTimeout().cancel();
		}
		// update player offline cache
//		PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
//				.getPlayerOfflineCache(playingRole.getId());
//		if (playerOfflineCache != null) {
//			PlayerOfflineManager.getInstance().updatePlayerOffline(playingRole, playerOfflineCache);
//		}
		Date now = new Date();
		playingRole.getPlayerBean().setDownlineTime(now);
		// 保存玩家数据
		if (GameServer.executorService != null) {
			GameServer.executorService.execute(new Runnable() {
				@Override
				public void run() {
					PlayerDao.getInstance().updatePlayerBean(playingRole.getPlayerBean());
				}
			});
		}
		// 发送到login server
		String openId = playingRole.getPlayerBean().getAccountId();
		String loginHost = GameServer.getInstance().getServerConfig().getLoginHost();
		int loginPort = GameServer.getInstance().getServerConfig().getLoginLanPort();
		if (GameServer.getInstance().getLanClientManager().connect(loginHost, loginPort)) {
			int serverId = playingRole.getPlayerBean().getServerId();
			GameServer.getInstance().getLanClientManager().sendPlayerLevel2Ls(loginHost, loginPort, openId, serverId, playingRole.getPlayerBean().getLevel());
		}
	}

	/**
	 * 服务器停机保存数据
	 */
	public void serverShutdown() {
		Collection<PlayingRole> playingRoles = SessionManager.getInstance().getAllPlayers();
		Date now = new Date();
		for (PlayingRole playingRole : playingRoles) {
			playingRole.getPlayerBean().setDownlineTime(now);
			PlayerDao.getInstance().updatePlayerBean(playingRole.getPlayerBean());
//			updateOnlineDuration(playingRole);
			// 发送到login server
			String openId = playingRole.getPlayerBean().getAccountId();
			String loginHost = GameServer.getInstance().getServerConfig().getLoginHost();
			int loginPort = GameServer.getInstance().getServerConfig().getLoginLanPort();
			if (GameServer.getInstance().getLanClientManager().connect(loginHost, loginPort)) {
				int serverId = playingRole.getPlayerBean().getServerId();
				GameServer.getInstance().getLanClientManager().sendPlayerLevel2Ls(loginHost, loginPort, openId, serverId, playingRole.getPlayerBean().getLevel());
			}
		}
	}

}
