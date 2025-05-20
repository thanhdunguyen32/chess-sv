package cross.boss.processor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossPlayer;
import cross.boss.bean.CrossBossPlayer.CrossPlayerId;
import cross.boss.bean.CrossBossPlayerJoinInfo;
import cross.boss.bean.CrossBossRoom;
import cross.boss.logic.BossBattleManager;
import cross.boss.logic.BossPlayerManager;
import cross.boss.logic.BossRequestManager;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.BeanCrossBossPlayer;
import game.module.battle.ProtoMessageBattle.C2SJoinCrossBoss1;
import game.module.battle.ProtoMessageBattle.S2CJoinCrossBoss1;
import game.module.cross.bean.CrossBossBegin;
import game.module.question.ProtoMessageQuestion.IOHeroSimpleInfo;
import game.module.user.bean.PlayerBean;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 37005, accessLimit = 200)
public class CrossBossJoinProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CrossBossJoinProcessor.class);

	@Override
	public void process(GamePlayer session, RequestMessage request) throws Exception {

	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
		C2SJoinCrossBoss1 reqMsg = ProtoUtil.getProtoObj(C2SJoinCrossBoss1.PARSER, request);
		long sessionId = reqMsg.getSessionId();
		logger.info("cross boss join,sessionId={}", sessionId);
		CrossBossBegin crossCraftBegin = BossRequestManager.getInstance().checkExist(sessionId);
		if (crossCraftBegin == null) {
			player.writeAndFlush(37006, RetCode.CRAFT_BATTLE_NOT_EXIST);
			return;
		}
		// 添加玩家
		int playerId = crossCraftBegin.getPlayerId();
		int serverId = crossCraftBegin.getServerId();
		int roomTypeId = crossCraftBegin.getRoomId();
		PlayingRole playingRole = null;
		if (player.getSessionId() != null) {
			playingRole = SessionManager.getInstance().visit(player.getSessionId());
		} else {
			PlayerBean pb = new PlayerBean();
			pb.setId(playerId);
			pb.setServerId(serverId);
			pb.setName(crossCraftBegin.getPlayerName());
			pb.setIcon(crossCraftBegin.getPlayerIcon());
			pb.setHeadFrame(crossCraftBegin.getHeadFrame());
			pb.setLevel(crossCraftBegin.getLevel());
			playingRole = new PlayingRole(player, pb);
			Long nettySessionId = SessionManager.getInstance().create(playingRole);
			player.saveSessionId(nettySessionId);
		}
		//
		playingRole.getPlayerCacheStatus().setPosition(PlayerPosition.PLAYER_POSITION_CROSS_BOSS_LOADING);
		// 添加匹配
		Integer existRoomId = null;
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId,
				serverId);
		if (crossBossPlayerJoinInfo != null) {
			Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
			if (roomIdMap != null) {
				existRoomId = roomIdMap.get(roomTypeId);
			}
			crossBossPlayerJoinInfo.setRoomTypeId(roomTypeId);
		} else {
			crossBossPlayerJoinInfo = new CrossBossPlayerJoinInfo();
			crossBossPlayerJoinInfo.setPlayerId(new CrossPlayerId(serverId, playerId));
			crossBossPlayerJoinInfo.setRoomTypeId(roomTypeId);
			BossPlayerManager.getInstance().addPlayerJoinInfo(playerId, serverId, crossBossPlayerJoinInfo);
		}
		// 加入房间
		CrossBossRoom crossBossRoom = null;
		CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
		if (existRoomId != null) {
			crossBossRoom = BossBattleManager.getInstance().getRoom(existRoomId);
			if (crossBossRoom.getPlayerMap().containsKey(crossPlayerId.hashCode())) {
				CrossBossPlayer crossBossPlayer = crossBossRoom.getPlayerMap().get(crossPlayerId.hashCode());
				crossBossRoom.getLoadingPlayerMap().put(crossPlayerId.hashCode(), crossBossPlayer);
				crossBossRoom.getPlayerMap().remove(crossPlayerId.hashCode());
				crossBossPlayer.setNetty4Session(player);
			} else {
				crossBossRoom.getLoadingPlayerMap().get(crossPlayerId.hashCode()).setNetty4Session(player);
			}
		} else {
			crossBossRoom = BossBattleManager.getInstance().joinRoom(roomTypeId, playerId, serverId, crossCraftBegin,
					player);
		}
		CrossBossPlayer myCrossPlayer = crossBossRoom.getLoadingPlayerMap().get(crossPlayerId.hashCode());
		// ret
		S2CJoinCrossBoss1.Builder retBuilder = S2CJoinCrossBoss1.newBuilder()
				.setMyHeroId(myCrossPlayer.getCrossPlayerInfo().getHeroEntity().getTemplateId());
		// 返回数据
		for (CrossBossPlayer crossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			if (crossBossPlayer.getNetty4Session() != null && crossBossPlayer.getNetty4Session().isChannelActive()) {
				BeanCrossBossPlayer.Builder playerBuilder = retBuilder.addMembersBuilder();
				IOHeroSimpleInfo.Builder heroBuilder = playerBuilder.setId(crossBossPlayer.getCrossPlayerId().playerId)
						.setServerId(crossBossPlayer.getCrossPlayerId().serverId)
						.setName(crossBossPlayer.getCrossPlayerInfo().getPlayerName())
						.setLevel(crossBossPlayer.getCrossPlayerInfo().getLevel())
						.setUuid(crossBossPlayer.getCrossPlayerId().hashCode()).getHeroBuilder();
				BossBattleManager.buildIOHeroSimpleInfo(heroBuilder,
						crossBossPlayer.getCrossPlayerInfo().getHeroEntity());
			}
		}
		// BOSS信息
		retBuilder.getBossInfoBuilder().setMonsterId(crossBossRoom.getCrossBossEntity().getTemplate_id())
				.setUuid(crossBossRoom.getCrossBossEntity().getUuid());
		playingRole.getGamePlayer().writeAndFlush(37006, retBuilder);
		// 移除缓存
		BossRequestManager.getInstance().remove(sessionId);
	}

	@Override
	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
