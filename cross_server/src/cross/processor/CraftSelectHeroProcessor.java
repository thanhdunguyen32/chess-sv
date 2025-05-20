package cross.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.C2SCraftSelectHero;
import game.module.battle.ProtoMessageBattle.PushCraftSelectHero;
import game.module.battle.ProtoMessageBattle.S2CCraftSelectHero;
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

@MsgCodeAnn(msgcode = 36029, accessLimit = 100)
public class CraftSelectHeroProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CraftSelectHeroProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		C2SCraftSelectHero reqMsg = ProtoUtil.getProtoObj(C2SCraftSelectHero.PARSER, request);
		logger.info("craft select hero!,playerId={},msg={}", playingRole.getId(), reqMsg);
		// 状态是否正确
		PlayerPosition playerPosition = playingRole.getPlayerCacheStatus().getPosition();
		if (playerPosition != PlayerPosition.PLAYER_POSITION_CRAFT_ROOM) {
			playingRole.getGamePlayer().writeAndFlush(36030, RetCode.CRAFT_PLAYER_STATUS_ERROR);
			return;
		}
		// 房间是否存在
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null || CraftManager.getInstance().getCraftRoom(craftRoomId) == null) {
			playingRole.getGamePlayer().writeAndFlush(36030, RetCode.CRAFT_BATTLE_NOT_EXIST);
			return;
		}
		// 玩家信息是否存在
		final CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		int playerId = playingRole.getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		final CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers()
				.get(new CraftPlayerInfo(serverId, playerId));
		if (craftRoomPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(36030, RetCode.CRAFT_PLAYER_NOT_EXIST);
			return;
		}
		// 是否轮到自己
		int selectHeroCount = craftRoom.getSelectHeroCount();
		if (craftRoom.getPlayerIdPair().get(0).playerId == playerId
				&& craftRoom.getPlayerIdPair().get(0).serverId == serverId && selectHeroCount % 2 == 1) {
			playingRole.getGamePlayer().writeAndFlush(36030, RetCode.CRAFT_SELECT_HERO_NOT_ALLOW);
			return;
		}
		if (craftRoom.getPlayerIdPair().get(1).playerId == playerId
				&& craftRoom.getPlayerIdPair().get(1).serverId == serverId && selectHeroCount % 2 == 0) {
			playingRole.getGamePlayer().writeAndFlush(36030, RetCode.CRAFT_SELECT_HERO_NOT_ALLOW);
			return;
		}
		// 是否已经选择完毕
		if (selectHeroCount >= 6) {
			playingRole.getGamePlayer().writeAndFlush(36030, RetCode.CRAFT_SELECT_HERO_FINISH);
			return;
		}
		// 英雄是否存在
		int aHeroId = reqMsg.getHeroId();
		Map<Integer, HeroEntity> heroAll = HeroCache.getInstance().getHeroAll(new CraftPlayerInfo(serverId, playerId));
		List<Integer> freeHeros = CraftConstants.getWeekFreeHeros();
		if (!freeHeros.contains(aHeroId) && !heroAll.containsKey(aHeroId)) {
			playingRole.getGamePlayer().writeAndFlush(36030, RetCode.CRAFT_SELECT_HERO_INVALID);
			return;
		}
		// 是否已经被选择
		List<Integer> selectHeros = craftRoomPlayer.getSelectedHeros();
		// 对方的craft player
		List<Integer> otherSelectHeros = null;
		if (craftRoom.getPlayerIdPair().get(0).playerId == playerId
				&& craftRoom.getPlayerIdPair().get(0).serverId == serverId) {
			otherSelectHeros = craftRoom.getOnlinePlayers().get(craftRoom.getPlayerIdPair().get(1)).getSelectedHeros();
		} else {
			otherSelectHeros = craftRoom.getOnlinePlayers().get(craftRoom.getPlayerIdPair().get(0)).getSelectedHeros();
		}
		if ((selectHeros != null && selectHeros.contains(aHeroId))
				|| (otherSelectHeros != null && otherSelectHeros.contains(aHeroId))) {
			logger.error("英雄已经被选择！");
			playingRole.getGamePlayer().writeAndFlush(36030, RetCode.CRAFT_SELECT_HERO_INVALID);
			return;
		}
		// do
		// push
		PushCraftSelectHero.Builder pushBuider = PushCraftSelectHero.newBuilder().setSelectIndex(selectHeroCount)
				.setPositionIndex(reqMsg.getPositionIndex()).setHeroId(aHeroId);
		for (CraftRoomPlayer aPvpPlayer : craftRoom.getOnlinePlayers().values()) {
			aPvpPlayer.getPlayingRole().getGamePlayer().writeAndFlush(36032, pushBuider);
		}
		// ret
		playingRole.getGamePlayer().writeAndFlush(36030, S2CCraftSelectHero.newBuilder());
	}

	public static void main(String[] args) {
		CraftRoom cr = new CraftRoom();
		Map<CraftPlayerInfo, CraftRoomPlayer> onlinePlayers = new HashMap<>();
		onlinePlayers.put(new CraftPlayerInfo(0, 1951), new CraftRoomPlayer(1951, 0, null));
		onlinePlayers.put(new CraftPlayerInfo(0, 1952), new CraftRoomPlayer(1952, 0, null));
		cr.setOnlinePlayers(onlinePlayers);
		CraftRoomPlayer player1 = cr.getOnlinePlayers().get(new CraftPlayerInfo(0, 1951));
		logger.info("player1={}", player1);
	}

	@Override
	public void processWebsocket(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
