package cross.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.C2SCraftSelectHeroConfirm;
import game.module.battle.ProtoMessageBattle.PushCraftSelectHeroConfirm;
import game.module.battle.ProtoMessageBattle.S2CCraftSelectHeroConfirm;
import game.module.craft.bean.CraftRoom;
import game.module.craft.bean.CraftRoom.CraftPlayerInfo;
import game.module.craft.bean.CraftRoomPlayer;
import game.module.craft.logic.CraftConstants;
import game.module.craft.logic.CraftManager;
import game.module.hero.bean.HeroEntity;
import game.module.hero.dao.HeroCache;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 36037, accessLimit = 500)
public class CraftSelectHeroConfirmProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CraftSelectHeroConfirmProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		C2SCraftSelectHeroConfirm reqMsg = ProtoUtil.getProtoObj(C2SCraftSelectHeroConfirm.PARSER, request);
		logger.info("craft select hero confirm!,playerId={},msg={}", playingRole.getId(), reqMsg);
		// 状态是否正确
		PlayerPosition playerPosition = playingRole.getPlayerCacheStatus().getPosition();
		if (playerPosition != PlayerPosition.PLAYER_POSITION_CRAFT_ROOM) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_PLAYER_STATUS_ERROR);
			return;
		}
		// 房间是否存在
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null || CraftManager.getInstance().getCraftRoom(craftRoomId) == null) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_BATTLE_NOT_EXIST);
			return;
		}
		// 玩家信息是否存在
		final CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		int playerId = playingRole.getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		final CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers()
				.get(new game.module.craft.bean.CraftRoom.CraftPlayerInfo(serverId, playerId));
		if (craftRoomPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_PLAYER_NOT_EXIST);
			return;
		}
		// 是否轮到自己
		int selectHeroCount = craftRoom.getSelectHeroCount();
		if (craftRoom.getPlayerIdPair().get(0).playerId == playerId
				&& craftRoom.getPlayerIdPair().get(0).serverId == serverId && selectHeroCount % 2 == 1) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_NOT_ALLOW);
			return;
		}
		if (craftRoom.getPlayerIdPair().get(1).playerId == playerId
				&& craftRoom.getPlayerIdPair().get(1).serverId == serverId && selectHeroCount % 2 == 0) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_NOT_ALLOW);
			return;
		}
		// 是否已经选择完毕
		if (selectHeroCount >= 6) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_FINISH);
			return;
		}
		// 能否选择
		List<Integer> heroIdList = reqMsg.getHeroIdList();
		if (heroIdList == null || heroIdList.size() == 0) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_NONE);
			return;
		}
		if (selectHeroCount > 0 && selectHeroCount < 5 && heroIdList.size() != 2) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_INVALID);
			return;
		}
		if ((selectHeroCount == 0 || selectHeroCount == 5) && heroIdList.size() != 1) {
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_INVALID);
			return;
		}
		// 选择重复英雄
		if (heroIdList.size() == 2 && heroIdList.get(0) == heroIdList.get(1)) {
			logger.error("选择重复英雄");
			playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_INVALID);
			return;
		}
		// 是否已经被选择
		List<Integer> selectHeros = craftRoomPlayer.getSelectedHeros();
		// 对方的craft player
		List<Integer> otherSelectHeros = null;
		if (craftRoom.getPlayerIdPair().get(0).playerId == playerId && craftRoom.getPlayerIdPair().get(0).serverId == serverId) {
			otherSelectHeros = craftRoom.getOnlinePlayers().get(craftRoom.getPlayerIdPair().get(1)).getSelectedHeros();
		} else {
			otherSelectHeros = craftRoom.getOnlinePlayers().get(craftRoom.getPlayerIdPair().get(0)).getSelectedHeros();
		}
		for (int toSelectHeroId : heroIdList) {
			if ((selectHeros != null && selectHeros.contains(toSelectHeroId))
					|| (otherSelectHeros != null && otherSelectHeros.contains(toSelectHeroId))) {
				logger.error("英雄已经被选择！");
				playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_INVALID);
				return;
			}
		}
		// 英雄是否存在
		Map<Integer, HeroEntity> heroAll = HeroCache.getInstance().getHeroAll(new CraftPlayerInfo(serverId, playerId));
		List<Integer> freeHeros = CraftConstants.getWeekFreeHeros();
		for (Integer aHeroId : heroIdList) {
			if (!freeHeros.contains(aHeroId) && !heroAll.containsKey(aHeroId)) {
				logger.error("还没拥有当前英雄，id={}", aHeroId);
				playingRole.getGamePlayer().writeAndFlush(36038, RetCode.CRAFT_SELECT_HERO_INVALID);
				return;
			}
		}
		// do
		if (selectHeros == null) {
			selectHeros = new ArrayList<>();
			craftRoomPlayer.setSelectedHeros(selectHeros);
		}
		selectHeros.addAll(heroIdList);
		// push
		PushCraftSelectHeroConfirm.Builder pushBuider = PushCraftSelectHeroConfirm.newBuilder()
				.setSelectIndex(selectHeroCount).addAllHeroId(heroIdList);
		for (CraftRoomPlayer aPvpPlayer : craftRoom.getOnlinePlayers().values()) {
			aPvpPlayer.getPlayingRole().getGamePlayer().writeAndFlush(36040, pushBuider);
		}
		// ret
		playingRole.getGamePlayer().writeAndFlush(36038, S2CCraftSelectHeroConfirm.newBuilder());
		// data
		craftRoom.setSelectHeroCount(++selectHeroCount);
		//
		if (selectHeroCount < 6) {
			// 超时定时器
			CraftManager.getInstance().scheduleSelectHeroTimeout(craftRoom);
		}
		// 开始战斗check
		CraftManager.getInstance().startLoadingCheck(craftRoom);
	}

	@Override
	public void processWebsocket(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
