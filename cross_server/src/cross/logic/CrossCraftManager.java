package cross.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.CrossServer;
import game.bean.CrossGsBean;
import game.entity.PlayingRole;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.logic.CrossGsListManager;
import ws.WsMessageBattle.C2SCraftPropChange;
import ws.WsMessageBattle.PVP_PROPERTIES;
import ws.WsMessageBattle.PushCraftMatchStatus;
import game.module.battle.bean.DBCraftRecordHero;
import game.module.battle.bean.DBCraftRecordItem;
import game.module.battle.bean.DBCraftRecordSide;
import game.module.craft.bean.CraftBean;
import game.module.craft.bean.CraftRoom;
import game.module.craft.bean.CraftRoom.BATTLE_STAUS;
import game.module.craft.bean.CraftRoom.CraftPlayerInfo;
import game.module.craft.bean.CraftRoomPlayer;
import game.module.craft.bean.CraftRoomPlayer.CraftHeroBean;
import game.module.craft.dao.CraftBeanCache;
import game.module.craft.logic.CraftConstants;
import game.module.craft.logic.CraftLogic;
import game.module.craft.logic.CraftManager;
import game.module.craft.logic.ICraftTimeout;
import game.module.hero.bean.HeroEntity;
import game.module.hero.dao.HeroCache;
import game.module.other.OtherConfigCache;
import game.session.SessionManager;

public class CrossCraftManager implements ICraftTimeout {

	private static Logger logger = LoggerFactory.getLogger(CrossCraftManager.class);

	static class SingletonHolder {
		static CrossCraftManager instance = new CrossCraftManager();
	}

	public static CrossCraftManager getInstance() {
		return SingletonHolder.instance;
	}

	public void propChange(CraftRoom craftRoom, C2SCraftPropChange reqMsg) {
		if (reqMsg.getPropType() == PVP_PROPERTIES.PVP_PROP_HP) {
			int playerId = reqMsg.getTargetPlayerId();
			int serverId = reqMsg.getTargetServerId();
			int heroId = reqMsg.getTargetHeroId();
			CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers().get(new CraftPlayerInfo(serverId, playerId));
			if (craftRoomPlayer == null) {
				logger.warn("pvp player not exist,playerId={},heroId={}", playerId, heroId);
				return;
			}
			CraftHeroBean craftHeroBean = craftRoomPlayer.getHeroInfoMap().get(heroId);
			int finalHp = reqMsg.getVal();
			// int finalHp = craftHeroBean.getHp() + reqMsg.getVal();
			if (finalHp > craftHeroBean.getMaxHp()) {
				finalHp = craftHeroBean.getMaxHp();
			}
			craftHeroBean.setHp(finalHp);
			logger.info("hero hp change!playerId={},heroId={},currentHp={},maxHp={}", playerId, heroId, finalHp,
					craftHeroBean.getMaxHp());
			if (finalHp <= 0 && !craftHeroBean.isDie()) {
				craftHeroBean.setDie(true);
				// 保存死亡记录
				int[] dieCount = craftRoom.getDieCount();
				if (dieCount == null) {
					dieCount = new int[] { 0, 0 };
					craftRoom.setDieCount(dieCount);
				}
				int playerIndex = 0;
				if (playerId == craftRoom.getPlayerIdPair().get(1).playerId
						&& serverId == craftRoom.getPlayerIdPair().get(1).serverId) {
					playerIndex = 1;
				}
				int currentDieCount = ++dieCount[playerIndex];
				logger.info("currentDieCount={}", currentDieCount);
				if (currentDieCount >= 5) {
					// 战斗结束
					finishBattle(craftRoom, playerIndex);
				}
			}
		}
	}

	public void finishBattle(CraftRoom craftRoom, int loosePlayerIndex) {
		logger.info("craft finish battle!roomId={}", craftRoom.getUuid());
		if (craftRoom.getBattleStatus() != BATTLE_STAUS.BATTLE_STAUS_PROGRESS) {
			return;
		}
		// 创建记录
		DBCraftRecordItem craftRecordItem = new DBCraftRecordItem(true, (int) (System.currentTimeMillis() / 1000));
		// 组织数据
		List<DBCraftRecordSide> sideList = new ArrayList<>();
		craftRecordItem.setOneSideList(sideList);
		for (CraftPlayerInfo craftPlayerInfo : craftRoom.getPlayerIdPair()) {
			int onePlayerId = craftPlayerInfo.playerId;
			int oneServerId = craftPlayerInfo.serverId;
			CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers()
					.get(new CraftPlayerInfo(oneServerId, onePlayerId));
			Map<Integer, HeroEntity> heroAll = HeroCache.getInstance()
					.getHeroAll(new CraftPlayerInfo(oneServerId, onePlayerId));
			// data
			CraftBean oneCb = CraftBeanCache.getInstance().getCraftBean(onePlayerId);
			int craftlevel = 0;
			if (oneCb != null) {
				craftlevel = oneCb.getLevel();
			}
			DBCraftRecordSide craftRecordSide = new DBCraftRecordSide(onePlayerId, oneServerId,
					craftRoomPlayer.getPlayingRole().getPlayerBean().getName(), craftlevel);
			sideList.add(craftRecordSide);
			List<DBCraftRecordHero> heroInfoList = new ArrayList<>();
			craftRecordSide.setHeroInfoList(heroInfoList);
			for (int aHeroId : craftRoomPlayer.getSelectedHeros()) {
				// 是否周免
				List<Integer> weekFreeHeros = CraftConstants.getWeekFreeHeros();
				HeroEntity myHe = heroAll.get(aHeroId);
				int heroStar = 0;
				if (weekFreeHeros.contains(aHeroId)) {
					heroStar = OtherConfigCache.getInstance().getIntConfig(1380);
				} else {
					heroStar = myHe.getEvolutionGrade();
				}
				DBCraftRecordHero craftRecordHero = new DBCraftRecordHero(aHeroId, heroStar);
				heroInfoList.add(craftRecordHero);
			}
		}
		// 发送到game server
		int winPlayerId = craftRoom.getPlayerIdPair().get(loosePlayerIndex == 0 ? 1 : 0).playerId;
		int winServerId = craftRoom.getPlayerIdPair().get(loosePlayerIndex == 0 ? 1 : 0).serverId;
		int loosePlayerId = craftRoom.getPlayerIdPair().get(loosePlayerIndex).playerId;
		int looseServerId = craftRoom.getPlayerIdPair().get(loosePlayerIndex).serverId;
		send2Gs(winPlayerId, winServerId, true, craftRecordItem);
		send2Gs(loosePlayerId, looseServerId, false, craftRecordItem);
		// 移除缓存
		CraftLogic.getInstance().removeCastSkillTimeInfo(winPlayerId);
		CraftLogic.getInstance().removeCastSkillTimeInfo(loosePlayerId);
		int craftRoomId = craftRoom.getUuid();
		craftRoom.getOnlinePlayers().clear();
		craftRoom.setPlayerIdPair(null);
		CraftManager.getInstance().remove(craftRoomId);
		CraftManager.getInstance().removeSchedule(craftRoomId);
		//
		for (CraftRoomPlayer craftRoomPlayer : craftRoom.getOnlinePlayers().values()) {
			if (craftRoomPlayer.getPlayingRole() != null && craftRoomPlayer.getPlayingRole().getPlayerBean() != null) {
//				SessionManager.getInstance().remove(craftRoomPlayer.getPlayingRole().getGamePlayer().getSessionId());
				int aServerId = craftRoomPlayer.getPlayingRole().getPlayerBean().getServerId();
				int aPlayerId = craftRoomPlayer.getPlayingRole().getPlayerBean().getId();
				removePlayerDataCache(aServerId, aPlayerId);
			}
		}
	}

	private void send2Gs(int playerId, int serverId, boolean isWin, DBCraftRecordItem craftRecordItem) {
		CrossGsBean gsInfo = CrossGsListManager.getInstance().getServer(serverId);
		String gsHost = gsInfo.getIp();
		int gsPort = gsInfo.getLanPort();
		if (CrossServer.getInstance().getLanClientManager().connect(gsHost, gsPort)) {
			CrossServer.getInstance().getLanClientManager().sendCraftResult2Gs(gsHost, gsPort, playerId, isWin,
					craftRecordItem);
		}
	}

	public void downlineRoom(PlayingRole playingRole) {
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null) {
			return;
		}
		CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		if (craftRoom == null) {
			return;
		}
		int myPlayerId = playingRole.getId();
		int myServerId = playingRole.getPlayerBean().getServerId();
		if (!craftRoom.getOnlinePlayers().containsKey(new CraftPlayerInfo(myServerId, myPlayerId))) {
			return;
		}
		// 发送解散消息
		PushCraftMatchStatus.Builder retBuilder = PushCraftMatchStatus.newBuilder().setOpType(2);
		for (CraftRoomPlayer aPlayer : craftRoom.getOnlinePlayers().values()) {
			PlayingRole pr = aPlayer.getPlayingRole();
			if(pr != null && pr.getGamePlayer() != null && pr.getGamePlayer().isChannelActive()){
				pr.getGamePlayer().writeAndFlush(36034, retBuilder);
				pr.getPlayerCacheStatus().setPosition(PlayerPosition.PLAYER_POSITION_HALL);
			}
		}
		// 移除玩家缓存
		for (CraftRoomPlayer craftRoomPlayer : craftRoom.getOnlinePlayers().values()) {
			if (craftRoomPlayer.getPlayingRole() != null && craftRoomPlayer.getPlayingRole().getPlayerBean() != null) {
//				SessionManager.getInstance().remove(craftRoomPlayer.getPlayingRole().getGamePlayer().getSessionId());
				removePlayerDataCache(craftRoomPlayer.getPlayingRole().getPlayerBean().getServerId(), craftRoomPlayer.getPlayingRole().getPlayerBean().getId());
			}
		}
		// 发送到gs
		for (CraftRoomPlayer aPlayer : craftRoom.getOnlinePlayers().values()) {
			int aPlayerId = aPlayer.getPlayerId();
			int aServerId = aPlayer.getServerId();
			boolean isPunish = (aPlayerId == myPlayerId && aServerId == myServerId) ? true : false;
			CrossGsBean gsInfo = CrossGsListManager.getInstance().getServer(aServerId);
			String gsHost = gsInfo.getIp();
			int gsPort = gsInfo.getLanPort();
			if (CrossServer.getInstance().getLanClientManager().connect(gsHost, gsPort)) {
				CrossServer.getInstance().getLanClientManager().sendCraftCancel2Gs(gsHost, gsPort, aPlayerId, isPunish);
			}
		}
		// 移除缓存
		CraftManager.getInstance().removeCraftRoom(craftRoomId);
		CraftManager.getInstance().removeSchedule(craftRoomId);
	}

	public void downlineLoading(PlayingRole playingRole) {
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null) {
			return;
		}
		CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		if (craftRoom == null) {
			return;
		}
		int playerId = playingRole.getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		int loosePlayerIndex = 0;
		if (craftRoom.getPlayerIdPair().get(1).playerId == playerId
				&& craftRoom.getPlayerIdPair().get(1).serverId == serverId) {
			loosePlayerIndex = 1;
		}
		craftRoom.setBattleStatus(BATTLE_STAUS.BATTLE_STAUS_PROGRESS);
		finishBattle(craftRoom, loosePlayerIndex);
	}

	public void downlineBattle(PlayingRole playingRole) {
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null) {
			return;
		}
		CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		if (craftRoom == null) {
			return;
		}
		int playerId = playingRole.getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers().get(new CraftPlayerInfo(serverId, playerId));
		craftRoomPlayer.setDownline(true);
		// 判断是否都掉线
		boolean isAllDownline = true;
		for (CraftRoomPlayer aCraftRoomPlayer : craftRoom.getOnlinePlayers().values()) {
			if (!aCraftRoomPlayer.isDownline()) {
				isAllDownline = false;
				break;
			}
		}
		if (!isAllDownline) {
			return;
		}
		// 发送到gs
		for (CraftRoomPlayer aPlayer : craftRoom.getOnlinePlayers().values()) {
			int aPlayerId = aPlayer.getPlayerId();
			int aServerId = aPlayer.getServerId();
			boolean isPunish = true;
			CrossGsBean gsInfo = CrossGsListManager.getInstance().getServer(aServerId);
			String gsHost = gsInfo.getIp();
			int gsPort = gsInfo.getLanPort();
			if (CrossServer.getInstance().getLanClientManager().connect(gsHost, gsPort)) {
				CrossServer.getInstance().getLanClientManager().sendCraftCancel2Gs(gsHost, gsPort, aPlayerId, isPunish);
			}
			//
//			if (aPlayer.getPlayingRole() != null && aPlayer.getPlayingRole().getGamePlayer() != null) {
//				SessionManager.getInstance().remove(aPlayer.getPlayingRole().getGamePlayer().getSessionId());
//			}
			removePlayerDataCache(aServerId, aPlayerId);
		}
		// 移除缓存
		CraftManager.getInstance().removeCraftRoom(craftRoomId);
		CraftManager.getInstance().removeSchedule(craftRoomId);
	}

	@Override
	public void roomTimeout(CraftRoom craftRoom) {
		// 判断是谁超时
		int selectHeroIndex = craftRoom.getSelectHeroCount();
		logger.info("craft select hero timeout,selectHeroIndex={}", selectHeroIndex);
		// 发送解散消息
		int timeoutPlayerId = craftRoom.getPlayerIdPair().get(selectHeroIndex % 2).playerId;
		int timeoutServerId = craftRoom.getPlayerIdPair().get(selectHeroIndex % 2).serverId;
		PushCraftMatchStatus.Builder retBuilder = PushCraftMatchStatus.newBuilder().setOpType(1);
		for (CraftRoomPlayer aPlayer : craftRoom.getOnlinePlayers().values()) {
			PlayingRole pr = aPlayer.getPlayingRole();
			if (pr != null && pr.getGamePlayer() != null) {
				pr.getGamePlayer().writeAndFlush(36034, retBuilder);
			}
		}
		// 移除玩家缓存
		for (CraftRoomPlayer craftRoomPlayer : craftRoom.getOnlinePlayers().values()) {
			if (craftRoomPlayer.getPlayingRole() != null && craftRoomPlayer.getPlayingRole().getPlayerBean() != null) {
//				SessionManager.getInstance().remove(craftRoomPlayer.getPlayingRole().getGamePlayer().getSessionId());
				removePlayerDataCache(craftRoomPlayer.getPlayingRole().getPlayerBean().getServerId(), craftRoomPlayer.getPlayingRole().getPlayerBean().getId());
			}
		}
		// 发送到gs
		for (CraftRoomPlayer aPlayer : craftRoom.getOnlinePlayers().values()) {
			int aPlayerId = aPlayer.getPlayerId();
			int aServerId = aPlayer.getServerId();
			boolean isPunish = (aPlayerId == timeoutPlayerId && aServerId == timeoutServerId) ? true : false;
			CrossGsBean gsInfo = CrossGsListManager.getInstance().getServer(aServerId);
			String gsHost = gsInfo.getIp();
			int gsPort = gsInfo.getLanPort();
			if (CrossServer.getInstance().getLanClientManager().connect(gsHost, gsPort)) {
				CrossServer.getInstance().getLanClientManager().sendCraftCancel2Gs(gsHost, gsPort, aPlayerId, isPunish);
			}
		}
		//移除缓存
		int craftRoomId = craftRoom.getUuid();
		CraftManager.getInstance().removeCraftRoom(craftRoomId);
		CraftManager.getInstance().removeSchedule(craftRoomId);
	}

	@Override
	public void loadingTimeout(CraftRoom craftRoom) {
		// TODO Auto-generated method stub
	}

	public void removePlayerDataCache(int serverId,int playerId){
		HeroCache.getInstance().removeCraftHeros(serverId, playerId);
		CraftBeanCache.getInstance().remove(serverId, playerId);
	}
	
}
