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
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.C2SBossChase;
import game.module.battle.ProtoMessageBattle.S2CBossChase;
import game.module.battle.ProtoMessageBattle.S2CPvpChase;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 37015, accessLimit = 100)
public class BossChaseProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(BossChaseProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		final C2SBossChase pvpChaseMsg = ProtoUtil.getProtoObj(C2SBossChase.PARSER, request);
		logger.info("boss chase target,msg={}", pvpChaseMsg);
		final int targetPlayerId = pvpChaseMsg.getTargetPlayerUuid();
		// 玩家状态不正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CROSS_BOSS_BATTLE) {
			logger.error("playerId={},error={}", playingRole.getId(), RetCode.PVP_PLAYER_STATUS_ERROR);
			playingRole.getGamePlayer().writeAndFlush(37016, S2CBossChase.newBuilder());
			return;
		}
		// 获取房间id
		int playerId = playingRole.getPlayerBean().getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId,
				serverId);
		if (crossBossPlayerJoinInfo == null) {
			playingRole.getGamePlayer().writeAndFlush(37016, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomTypeId = crossBossPlayerJoinInfo.getRoomTypeId();
		Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
		if (roomIdMap == null || roomIdMap.get(roomTypeId) == null) {
			playingRole.getGamePlayer().writeAndFlush(37016, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomId = roomIdMap.get(roomTypeId);
		final CrossBossRoom crossBossRoom = BossBattleManager.getInstance().getRoom(roomId);
		if (crossBossRoom == null) {
			playingRole.getGamePlayer().writeAndFlush(37016, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		// 玩家信息
		final CrossBossPlayer crossBossPlayer = crossBossRoom.getPlayerMap().get(crossPlayerId.hashCode());
		if (crossBossPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(37016, RetCode.PVP_PLAYER_NOT_EXIST);
			return;
		}
		// 对方玩家是否存在
		if (!crossBossRoom.getPlayerMap().containsKey(targetPlayerId) && crossBossRoom.getCrossBossEntity().getUuid() != targetPlayerId) {
			playingRole.getGamePlayer().writeAndFlush(37016, RetCode.PVP_PLAYER_NOT_EXIST);
			return;
		}
		BossBattleManager.getInstance().execute(new OrderedEventRunnable() {
			@Override
			public void run() {
				RetCode retCode = BossBattleManager.getInstance().chaseTarget(crossBossRoom, crossBossPlayer, targetPlayerId,
						pvpChaseMsg.getNewPosX(), pvpChaseMsg.getNewPosZ(), pvpChaseMsg.getTargetX(),
						pvpChaseMsg.getTargetZ());
				// ret
				if (retCode == RetCode.RET_OK) {
					playingRole.getGamePlayer().writeAndFlush(37016, S2CBossChase.newBuilder().build());
				} else {
					playingRole.getGamePlayer().writeAndFlush(37016, retCode);
				}
			}

			@Override
			public Object getIdentifyer() {
				return crossBossRoom.getId();
			}

			@Override
			public byte getEventType() {
				return 0;
			}
		});

	}

	@Override
	public void processWebsocket(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}