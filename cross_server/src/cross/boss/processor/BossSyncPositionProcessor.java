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
import game.module.battle.ProtoMessageBattle.C2SBossSyncPosition;
import game.module.battle.ProtoMessageBattle.S2CBossSyncPosition;
import game.module.battle.ProtoMessageBattle.S2CSyncPosition;
import game.module.pvp.bean.PvpPlayer.PVP_BATTLE_STATUS;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 37011, accessLimit = 100)
public class BossSyncPositionProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(BossSyncPositionProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		C2SBossSyncPosition syncPositionMsg = ProtoUtil.getProtoObj(C2SBossSyncPosition.PARSER, request);
		logger.info("cross boss sync position,player={},msg={}", playingRole.getId(), syncPositionMsg);
		final boolean isMove = syncPositionMsg.getIsMove();
		final float newPosX = syncPositionMsg.getNewPosX();
		final float newPosZ = syncPositionMsg.getNewPosZ();
		final float targetX = syncPositionMsg.getTargetX();
		final float targetZ = syncPositionMsg.getTargetZ();
		// 玩家状态不正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CROSS_BOSS_BATTLE) {
			logger.error("CROSS_BOSS_PLAYER_STATUS_ERROR,#1");
			playingRole.getGamePlayer().writeAndFlush(37012, S2CBossSyncPosition.newBuilder());
			return;
		}
		// 获取房间id
		int playerId = playingRole.getPlayerBean().getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId,
				serverId);
		if (crossBossPlayerJoinInfo == null) {
			playingRole.getGamePlayer().writeAndFlush(37012, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomTypeId = crossBossPlayerJoinInfo.getRoomTypeId();
		Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
		if (roomIdMap == null || roomIdMap.get(roomTypeId) == null) {
			playingRole.getGamePlayer().writeAndFlush(37012, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomId = roomIdMap.get(roomTypeId);
		final CrossBossRoom crossBossRoom = BossBattleManager.getInstance().getRoom(roomId);
		if (crossBossRoom == null) {
			playingRole.getGamePlayer().writeAndFlush(37012, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		// 玩家信息
		final CrossBossPlayer crossBossPlayer = crossBossRoom.getPlayerMap().get(crossPlayerId.hashCode());
		if (crossBossPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(37012, RetCode.PVP_PLAYER_NOT_EXIST);
			return;
		}
		// 玩家状态是否为死亡
		if (crossBossPlayer.getRawStatus() == PVP_BATTLE_STATUS.DIE) {
			logger.error("player={},is die!", playingRole.getId());
			playingRole.getGamePlayer().writeAndFlush(37012, S2CBossSyncPosition.newBuilder());
			return;
		}
		BossBattleManager.getInstance().execute(new OrderedEventRunnable() {
			@Override
			public void run() {
				// 更新玩家信息
				RetCode retCode = BossBattleManager.getInstance().updatePosition(crossBossRoom, crossBossPlayer, newPosX, newPosZ,
						targetX, targetZ, isMove);
				// ret
				if (retCode == RetCode.RET_OK) {
					crossBossPlayer.setRawStatus(PVP_BATTLE_STATUS.CLICK_MOVE);
				} else {
					logger.error("player sync position error!player={},ret={}", playingRole.getId(), retCode);
				}
				playingRole.getGamePlayer().writeAndFlush(37012, S2CSyncPosition.newBuilder().build());
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